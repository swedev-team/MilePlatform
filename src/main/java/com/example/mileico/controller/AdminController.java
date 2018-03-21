package com.example.mileico.controller;

import com.example.mileico.model.*;

import com.example.mileico.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ManagementRepository managementRepository;

    @Autowired
    KycRepository kycRepository;

    @Autowired
    WithDrawRepository withDrawRepository;

    @Autowired
    DepositRepository depositRepository;

    @Autowired
    AdminHistoryRepository adminHistoryRepository;

    @Autowired
    SubscriberRepository subscriberRepository;

    @GetMapping("/admin")
    public String admin(Model model, String addAdminFail, HttpSession httpSession, String warning) {
        User user = (User)httpSession.getAttribute("sessionUser");
        if(user==null || !user.isAdmin()){
            return "redirect:/";
        }
        MileManagement mileManagement = managementRepository.findByIsProgress(true);
        if(mileManagement != null) {
            model.addAttribute("adminAddr", mileManagement.getAddress());
            model.addAttribute("exchangeRatio", mileManagement.getRatio());
        }
        model.addAttribute("deposits", depositRepository.findAll());
        model.addAttribute("depositCount", depositRepository.count());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("addAdminFail", addAdminFail);
        model.addAttribute("usersNum", userRepository.count());
        model.addAttribute("adminList", userRepository.findByIsAdmin(true));
        List<MileManagement> mileManagements = managementRepository.findAll();
        model.addAttribute("mileManagement", mileManagements);
        model.addAttribute("isSaveManagement", mileManagements.size() == 0);
        model.addAttribute("progress", mileManagement);
        model.addAttribute("warning", warning);
        model.addAttribute("subscriberList", subscriberRepository.findAll());

        return "/admin";
    }

    @GetMapping("/logoutAdmin")
    public String logoutAdmin(HttpSession httpSession) {
        httpSession.removeAttribute("sessionUser");
        return "redirect:/";
    }

    @GetMapping("/checkKyc/{id}")
    public String checkKyc(@PathVariable Long id, Model model, HttpSession httpSession) {
        User aduser = (User)httpSession.getAttribute("sessionUser");
        if(!aduser.isAdmin()) {
            System.out.println("관리자페이지로의 비정상적인 접근이 확인되었습니다.");
            return "redirect:/";
        }

        User user = userRepository.findOne(id);
        Kyc kyc = user.getKyc();
        model.addAttribute("kyc", kyc);
        return "/kycadmin";
    }


    @RequestMapping("/getPassport/{id}")
    public ResponseEntity<byte[]> getPassport(@PathVariable Long id) {

        User user = userRepository.findOne(id);
        Kyc kyc = user.getKyc();

        byte[] passport = kyc.getPassport();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "image/png");
        //httpHeaders.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(passport, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping("/getSelfPicture/{id}")
    public ResponseEntity<byte[]> getSelfPicture(@PathVariable Long id) {

        User user = userRepository.findOne(id);
        Kyc kyc = user.getKyc();
        byte[] selfPicture = kyc.getSelfPicture();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "image/png");
        return new ResponseEntity<byte[]>(selfPicture, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/processKyc/{id}")
    public String processKyc(String result, String failReason, @PathVariable Long id, HttpSession httpSession) {
        User aduser = (User)httpSession.getAttribute("sessionUser");
        if(!aduser.isAdmin()) {
            System.out.println("관리자페이지로의 비정상적인 접근이 확인되었습니다.");
            return "redirect:/";
        }

        User user = userRepository.findOne(id);
        if(user == null) {
            System.out.println("해당 사용자가 없습니다.");
            return "/admin";
        }
        if(result.equals("ok")) {
            user.setChecked(true);
            user.setKycStatus("승인");
        } else {
            if(!failReason.equals("") || failReason != null) {
                user.setKycStatus("거절");
                user.setFailReason(failReason);
            }
        }
        userRepository.save(user);
        AdminHistory adminHistory = new AdminHistory();
        adminHistory.setEmail(aduser.getEmail());
        adminHistory.setAction( user.getEmail()+"의 kyc 확인, "+ user.getKycStatus()+"처리");
        adminHistory.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        adminHistoryRepository.save(adminHistory);
        return "redirect:/admin";
    }

    @GetMapping("/showAdminHistory")
    public String showAdminHistory(){
        return "/adminHistory";
    }
}
