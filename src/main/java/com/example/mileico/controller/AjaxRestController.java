package com.example.mileico.controller;

import com.example.mileico.model.*;
import com.example.mileico.repository.*;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
public class AjaxRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepositRepository depositRepository;

    @Autowired
    WithDrawRepository withDrawRepository;

    @Autowired
    ManagementRepository managementRepository;

    @Autowired
    AdminHistoryRepository adminHistoryRepository;

    @Autowired
    SubscriberRepository subscriberRepository;

    @PostMapping("/admin/deleteUser")
    public String deleteUser(Long userId, HttpSession httpSession) throws Exception {
        User user = (User) httpSession.getAttribute("sessionUser");
        if (!user.isAdmin() || userId == null) {
            return "abnormal";
        }
        User dbUser = userRepository.findOne(userId);
        if (dbUser.isAdmin()) {
            return "관리자는 삭제할 수 없습니다";
        }
        if (dbUser.getDeposits().isEmpty()) {
            //userRepository.delete(userId);
            dbUser.setWithdraw("y");
            dbUser.setWithdrawDate(new Date(Calendar.getInstance().getTime().getTime()));
            userRepository.save(dbUser);
            AdminHistory adminHistory = new AdminHistory();
            adminHistory.setEmail(user.getEmail());
            adminHistory.setAction(dbUser.getEmail() + " 유저 삭제");
            adminHistory.setDate(new Date(Calendar.getInstance().getTime().getTime()));
            adminHistoryRepository.save(adminHistory);
            return "success";
        } else {
            return "거래내역이 있는 회원은 삭제할 수 없습니다.";
        }
    }

    @PostMapping("/admin/addAdmin")
    public String addAdmin(String email, HttpSession httpSession) {
        User sessionUser = (User) httpSession.getAttribute("sessionUser");
        if (email == null || sessionUser == null || !sessionUser.isAdmin()) {
            return "abnormal";
        }
        if (sessionUser.getId() != 1) {
            return "권한이 없습니다";
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "이메일을 다시 확인해주세요";
        }
        user.setAdmin(true);
        userRepository.save(user);
        AdminHistory adminHistory = new AdminHistory();
        adminHistory.setEmail(sessionUser.getEmail());
        adminHistory.setAction(user.getEmail() + " 관리자 추가");
        adminHistory.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        adminHistoryRepository.save(adminHistory);
        return "success";
    }

    @PostMapping("/admin/removeAdmin")
    public String removeAdmin(Long id, HttpSession httpSession) {
        User sessionUser = (User) httpSession.getAttribute("sessionUser");
        if (id == null || sessionUser == null || !sessionUser.isAdmin()) {
            return "abnormal";
        }
        if (sessionUser.getId() != 1) {
            return "권한이 없습니다";
        }
        if (id == 1) {
            return "메인 관리자는 삭제할 수 없습니다";
        }
        User user = userRepository.findOne(id);
        user.setAdmin(false);
        userRepository.save(user);
        AdminHistory adminHistory = new AdminHistory();
        adminHistory.setEmail(sessionUser.getEmail());
        adminHistory.setAction(user.getEmail() + " 관리자 삭제");
        adminHistory.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        adminHistoryRepository.save(adminHistory);

        return "success";
    }

    @PostMapping("/admin/withdraw")
    public String withDraw(Long id, HttpSession httpSession) {
        User aduser = (User) httpSession.getAttribute("sessionUser");
        if (aduser == null || !aduser.isAdmin()) {
            return "abnormal";
        }
        Deposit deposit = depositRepository.findOne(id);
        if (deposit.isReturned()) {
            return "이미 송금이 확인된 거래입니다";
        }

        WithDraw withDraw = new WithDraw();
        withDraw.setUser(deposit.getUser());

        //실제 나간 양 적어야할거임
        withDraw.setAmount(deposit.getExMile());

        withDraw.setMileManagement(deposit.getMileManagement());
        withDraw.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        withDrawRepository.save(withDraw);
        deposit.setReturned(true);
        depositRepository.save(deposit);
        AdminHistory adminHistory = new AdminHistory();
        adminHistory.setEmail(aduser.getEmail());
        adminHistory.setAction(deposit.getUser().getEmail() + "님의 입금내역 / " + withDraw.getMileManagement().getIdx() + "번 프로젝트에서 " + deposit.getId() + "번 거래 " + deposit.getExMile() + "마일 출금처리");
        adminHistory.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        adminHistoryRepository.save(adminHistory);
        return "success";
    }

    @PostMapping("/admin/saveProject")
    public MileReturnModel saveProject(MileManagement mileManagement, String strStartDate, String strEndDate, HttpSession httpSession) throws Exception {
        User user = (User) httpSession.getAttribute("sessionUser");
        MileReturnModel mileReturnModel = new MileReturnModel();
        if (user == null || !user.isAdmin()) {
            mileReturnModel.setMessage("abnormal");
            return mileReturnModel;
        }
        if (!mileManagement.isValidate()) {
            mileReturnModel.setMessage("입력이 유효하지 않습니다");
            return mileReturnModel;
        }
        Date startDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(strStartDate).getTime());
        Date endDate = new Date(new SimpleDateFormat("MM/dd/yyyy").parse(strEndDate).getTime());

        if (startDate.compareTo(endDate) > 0) {
            mileReturnModel.setMessage("날짜 선택을 확인하세요");
            return mileReturnModel;
        }

        if (!mileManagement.getAddress().matches("^0x[a-fA-F0-9]{40}$")) {
            mileReturnModel.setMessage("Eth계좌가 유효하지 않습니다");
            return mileReturnModel;
        }

        mileManagement.setIdx(managementRepository.count() + 1);
        mileManagement.setStartDate(startDate);
        mileManagement.setEndDate(endDate);
        mileManagement.setAddress(mileManagement.getAddress().toLowerCase());
        MileManagement management = managementRepository.save(mileManagement);
        mileReturnModel.setMessage("success");
        mileReturnModel.setManagement(management);

        AdminHistory adminHistory = new AdminHistory();
        adminHistory.setEmail(user.getEmail());
        adminHistory.setAction(management.getIdx() + "번 프로젝트 저장");
        adminHistory.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        adminHistoryRepository.save(adminHistory);

        return mileReturnModel;
    }


    @PostMapping("/admin/removeProject")
    public String removeProject(HttpSession httpSession, Long idx) {
        User user = (User) httpSession.getAttribute("sessionUser");
        if (user == null || !user.isAdmin()) {
            return "abnormal";
        }
        MileManagement target = managementRepository.findByIdx(idx);
        if (target.isProgress()) {
            return "이미 진행중인 프로젝트는 삭제할 수 없습니다";
        }

        if (target.isFinished()) {
            return "이미 마감된 프로젝트는 삭제할 수 없습니다";
        }
        managementRepository.delete(target);

        for (Long i = idx; i <= managementRepository.count(); i++) {
            MileManagement mileManagement = managementRepository.findByIdx(i + 1);
            mileManagement.setIdx(i);
            managementRepository.save(mileManagement);
        }
        AdminHistory adminHistory = new AdminHistory();
        adminHistory.setEmail(user.getEmail());
        adminHistory.setAction(target.getIdx() + "번 프로젝트 삭제");
        adminHistory.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        adminHistoryRepository.save(adminHistory);
        return "success";
    }



   /*@PostMapping("/admin/startProject")
    public String startProject(HttpSession httpSession){
        User user = (User)httpSession.getAttribute("sessionUser");
        if(user == null || !user.isAdmin()) {
            return "abnormal";
        }
        List<MileManagement> mileManagementList = managementRepository.findAll();
        MileManagement mileManagement = null;

        for(int i = 0 ; i<mileManagementList.size() ; i++){
          if(mileManagementList.get(i).isFinished() == false){
              mileManagement = mileManagementList.get(i);
              break;
          }
        }
        if(mileManagement == null) {
            return "시작할 프로젝트가 없습니다";
        }
        if(managementRepository.findByIsProgress(true) != null){
            return "이미 프로젝트가 진행중입니다";
        }
        mileManagement.setProgress(true);
        managementRepository.save(mileManagement);

        AdminHistory adminHistory = new AdminHistory();
        adminHistory.setEmail(user.getEmail());
        adminHistory.setAction("프로젝트 시작");
        adminHistory.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        adminHistoryRepository.save(adminHistory);
        return "success";
    }*/

    @PostMapping("/admin/startProject")
    public MileReturnModel startProject(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("sessionUser");
        MileReturnModel mileReturnModel = new MileReturnModel();
        if (user == null || !user.isAdmin()) {
            mileReturnModel.setMessage("abnormal");
            return mileReturnModel;
        }
        List<MileManagement> mileManagementList = managementRepository.findAll();
        MileManagement mileManagement = null;

        for (int i = 0; i < mileManagementList.size(); i++) {
            if (mileManagementList.get(i).isFinished() == false) {
                mileManagement = mileManagementList.get(i);
                break;
            }
        }
        if (mileManagement == null) {
            mileReturnModel.setMessage("시작할 프로젝트가 없습니다");
            return mileReturnModel;
        }
        if (managementRepository.findByIsProgress(true) != null) {
            mileReturnModel.setMessage("이미 프로젝트가 진행중입니다");
            return mileReturnModel;
        }
        mileManagement.setProgress(true);
        managementRepository.save(mileManagement);

        mileReturnModel.setMessage("success");
        mileReturnModel.setManagement(mileManagement);
        AdminHistory adminHistory = new AdminHistory();
        adminHistory.setEmail(user.getEmail());
        adminHistory.setAction("프로젝트 시작");
        adminHistory.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        adminHistoryRepository.save(adminHistory);
        return mileReturnModel;
    }

    @PostMapping("/saveAddr")
    public String saveAddr(String address, HttpSession httpSession, HttpServletRequest request) {
        String lang = "en";
        Cookie cookie[] = request.getCookies();
        for(Cookie cookie1 : cookie){
            if(cookie1.getName().equals("lang")){
                lang=cookie1.getValue();
                break;
            }
        }
        User user = (User) httpSession.getAttribute("sessionUser");
        if (user == null) {
            if(lang.equals("en"))
                return "please login";
            else
                return "请登录";
        }

        if (!address.matches("^0x[a-fA-F0-9]{40}$")) {
            if(lang.equals("en"))
                return "Invalid address";
            else
                return "无效地址";
        }


        User dbUser = userRepository.findOne(user.getId());
        if (dbUser.isFirstDeposit()) {
            if(lang.equals("en"))
                return "You already have a deposit record at this address";
            else
                return "您的地址已有存款记录";
        }
        List<User> users = userRepository.findAll();

        String saveAddr = address.toLowerCase();
        for (User userAttr : users) {
            if (userAttr.getAddress().equals(saveAddr)) {
                if(lang.equals("en"))
                    return "This address is already in use. Please use a different address";
                else
                    return "该地址已被使用请更换其他地址";
            }
        }
        user.setAddress(saveAddr);
        userRepository.save(user);

        return "success";
    }

    @GetMapping("/adminHistory")
    public List<AdminHistory> adminHistroy() {
        return adminHistoryRepository.findAll();
    }

    @PostMapping("/subscribe")
    public String subscribe(String email , HttpServletRequest request) {
        String lang = "en";
        Cookie cookie[] = request.getCookies();
        for(Cookie cookie1 : cookie){
            if(cookie1.getName().equals("lang")){
                lang=cookie1.getValue();
                break;
            }
        }
        if ("".equals(email) || email == null) {
            if(lang.equals("en"))
                return "Please enter your e-mail";
            else
                return "请输入您的邮箱";
        }
        if (!email.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$")) {
            if(lang.equals("en"))
                return "Email format is invalid";
            else
                return "邮箱格式无效";
        }
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(email.trim());
        subscriberRepository.save(subscriber);
        return "success";
    }

    @PostMapping("/checkProject")
    public String checkProject(HttpSession httpSession, HttpServletRequest request) {
        String lang = "en";
        Cookie cookie[] = request.getCookies();
        for(Cookie cookie1 : cookie){
            if(cookie1.getName().equals("lang")){
                lang=cookie1.getValue();
                break;
            }
        }
        User user = (User) httpSession.getAttribute("sessionUser");
        if (user == null) {
            if(lang.equals("en"))
                return "please login";
            else
                return "请登录";
        }
        MileManagement mileManagement = managementRepository.findByIsProgress(true);
        if (mileManagement != null) {
            return "success";
        }
        else {
            if(lang.equals("en"))
                return "ico closed, Please wait for next ico";
            else
                return "ICO关闭，请等待下一轮ICO";
        }
    }

    class MileReturnModel {
        private String message;
        private MileManagement management;

        public void setMessage(String message) {
            this.message = message;
        }

        public void setManagement(MileManagement management) {
            this.management = management;
        }

        public String getMessage() {
            return message;
        }

        public MileManagement getManagement() {
            return management;
        }
    }
}
