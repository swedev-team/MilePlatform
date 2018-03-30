package com.example.mileico.controller;

import com.example.mileico.model.*;
import com.example.mileico.repository.*;

import com.example.mileico.utils.PasswordEncode;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import com.mysql.jdbc.Blob;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class MainController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    KycRepository kycRepository;

    @Autowired
    ManagementRepository managementRepository;

    @Autowired
    DepositRepository depositRepository;

    @Autowired
    SubscriberRepository subscriberRepository;

    @Autowired
    RecaptchaValidator recaptchaValidator;


    PasswordEncode passwordEncode = new PasswordEncode();

    @GetMapping("/")
    public String home(Model model, HttpSession httpSession){
        User user = (User)httpSession.getAttribute("sessionUser");
        if(user != null){
            model.addAttribute("loginUser", user );
        }
        MileManagement mileManagement = managementRepository.findByIsProgress(true);
        model.addAttribute("progress", mileManagement);
        return "/index";
    }

    @GetMapping("/login/{state}")
    public String login(HttpSession httpSession, Model model, String alreadyAddr, @PathVariable String state, String loginFail) {
        User sessionUser = (User)httpSession.getAttribute("sessionUser");
        if(sessionUser != null) {
            MileManagement mileManagement = managementRepository.findByIsProgress(true);
            if(mileManagement != null) {
                model.addAttribute("policy", mileManagement);
            }
            User dbUser = userRepository.findOne(sessionUser.getId());
            model.addAttribute("alreadyAddr", alreadyAddr);
            model.addAttribute("loginUser", dbUser);
            List<Deposit> deposits = depositRepository.findByUser(dbUser);
            double totalDeposit = 0.0;
            double totalExMile = 0.0;
            for(Deposit deposit: deposits) {
                totalExMile += deposit.getExMile();
                totalDeposit += deposit.getAmount();
            }

            model.addAttribute("isFirstDeposit", dbUser.isFirstDeposit());
            model.addAttribute("history", deposits);
            model.addAttribute("totalDeposit", totalDeposit);
            model.addAttribute("totalExMile", totalExMile);
            model.addAttribute("isAddress", dbUser.getAddress()!="");

            if(dbUser.getFailReason().equals("")||dbUser.getFailReason()!=null)
                model.addAttribute("failReasonModel", dbUser.getFailReason());
            else
                model.addAttribute("failReasonModel", false);

            if(dbUser.getKycStatus().equals("미확인")) {
                if(dbUser.isSubmitted()){
                    model.addAttribute("kycResult", "KYC verification is verifying");
                } else {
                    model.addAttribute("kycResult", "You have not submitted KYC yet");
                }
            } else if (dbUser.getKycStatus().equals("거절")) {
                model.addAttribute("kycResult","KYC verification has been rejected");
            } else {
                model.addAttribute("kycResult", "KYC verification approved");
            }
            return "/dashboard";
        }
        model.addAttribute("loginFail", loginFail);
        if(state != null) {
            if(state.equals("jointab")){
                return "/login2";
            }
        }
        return "/login";
    }


    @GetMapping("/openKyc")
    public String openKyc(HttpSession httpSession) {
        User sessionUser = (User)httpSession.getAttribute("sessionUser");
        if(sessionUser == null) {
            return "redirect:/login/lgtab";
        }
        return "/kyc";
    }

    @PostMapping("/submitkyc")
    public String tempkyc(Kyc kyc, HttpSession httpSession, MultipartFile passportInput, MultipartFile selfPictureInput, Model model) throws Exception{
        User loginUser = (User)httpSession.getAttribute("sessionUser");
        if(loginUser == null) {
            return "redirect:/login/lgtab";
        }
        loginUser.setKyc(kyc);
        loginUser.setKycStatus("미확인");
        loginUser.setSubmitted(true);
        byte[] passport = passportInput.getBytes();
        byte[] selfPicture = selfPictureInput.getBytes();
        kyc.setPassport(passport);
        kyc.setSelfPicture(selfPicture);
        try{
            kycRepository.save(kyc);
            userRepository.save(loginUser);
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("kycResult", "The image you selected is too large or not supported.");
            return "/dashboard";
        }

        return "redirect:/login/lgtab";
    }

    @PostMapping("/signup")
    public String signup(User user, HttpSession httpSession, String nationalCode, String phoneNum ,RedirectAttributes redirectAttributes, HttpServletRequest request) {
        ValidationResult result = recaptchaValidator.validate(request);
        if(result.isSuccess()){
        //if(true){
            if(user.getEmail().equals("")&&user.getEmail() ==null) {
                redirectAttributes.addAttribute("loginFail", "Please enter your e-mail");
                return "redirect:/login/jointab";
            }
            if(user.getPassword().equals("")&&user.getPassword()==null) {
                redirectAttributes.addAttribute("loginFail", "Please enter your password");
                return "redirect:/login/jointab";
            }
            if(user.getName().equals("") && user.getName()==null) {
                redirectAttributes.addAttribute("loginFail", "Please enter your name");
                return "redirect:/login/jointab";
            }
            List<User> users = userRepository.findAll();
            for(User listUser: users) {
                if(listUser.getEmail().equals(user.getEmail())) {
                    redirectAttributes.addAttribute("loginFail","This is an already signed email");
                    return "redirect:/login/jointab";
                }
            }
            user.setPhoneNumber("+"+nationalCode+" "+phoneNum);
            user.setPassword(passwordEncode.encode(user.getPassword()));
            User dbUser = userRepository.save(user);
            httpSession.setAttribute("sessionUser", dbUser);

            return "redirect:/login/lgtab";
        }else{
            return "redirect:/login/jointab";
        }

    }

    @PostMapping("/signin")
    public String signin(User user, HttpSession httpSession,RedirectAttributes redirectAttributes, HttpServletRequest request) {
        ValidationResult result = recaptchaValidator.validate(request);
        if(result.isSuccess()){
        //if(true){
            User dbUser = userRepository.findByEmail(user.getEmail());
            if(dbUser == null) {
                redirectAttributes.addAttribute("loginFail", "This user does not exist");
                return "redirect:/login/lgtab";
            }
            if(dbUser.getWithdraw().equals("y")){
                redirectAttributes.addAttribute("loginFail", "The withdrawal user");
                return "redirect:/login/lgtab";
            }
            else if(passwordEncode.matches(user.getPassword(), dbUser.getPassword())) {
                httpSession.setAttribute("sessionUser", dbUser);
                //model.addAttribute("loginUser", dbUser);

                return "redirect:/login/lgtab";
            } else {
                redirectAttributes.addAttribute("loginFail","The password is incorrect");
                return "redirect:/login/lgtab";
            }
        } else {
            return "redirect:/login/lgtab";
        }

    }
    @GetMapping("/userLogout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("sessionUser");
        return "redirect:/login/lgtab";
    }

    @GetMapping("/findPassword")
    public String findPassword() {
        return "/password";
    }

    @PostMapping("/findPassword")
    public String findPassword(String email, String address, Model model, HttpSession httpSession, HttpServletRequest request) {
        ValidationResult result = recaptchaValidator.validate(request);
        if(result.isSuccess()){
        //if(true){
            if(!address.matches("^0x[a-fA-F0-9]{40}$")) {
                model.addAttribute("findFail","Invalid address");
                return "/password";
            }

            if(email == null || email.equals("")) {
                model.addAttribute("findFail", "Please enter your email address");
                return "/password";
            }

            User user = userRepository.findByEmail(email);
            if(user == null || !address.toLowerCase().equals(user.getAddress().toLowerCase())) {
                model.addAttribute("findFail", "No matches found");
                return "/password";
            }
            httpSession.setAttribute("sessionUser",  user);

            return "/modifyPassword";
        } else {
            return "redirect:/findPassword";
        }

    }

    @PostMapping("/modifyPassword")
    public String modifyPassword(String userpass2, HttpSession httpSession, Model model) {
        if(userpass2 == null || userpass2.equals("")){
            model.addAttribute("modifyFail", "Please enter your password");
            return "/modifyPassword";
        }
        User user = (User)httpSession.getAttribute("sessionUser");
        user.setPassword(passwordEncode.encode(userpass2));
        userRepository.save(user);
        httpSession.removeAttribute("sessionUser");
        return "redirect:/login/lgtab";
    }

}
