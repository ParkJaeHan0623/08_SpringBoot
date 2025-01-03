package kr.parkjaehan.myshop.controllers;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.parkjaehan.myshop.helpers.FileHelper;
import kr.parkjaehan.myshop.helpers.MailHelper;
import kr.parkjaehan.myshop.helpers.RestHelper;
import kr.parkjaehan.myshop.helpers.UtilHelper;
import kr.parkjaehan.myshop.models.Members;
import kr.parkjaehan.myshop.models.UploadItem;
import kr.parkjaehan.myshop.services.MembersService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class AccountRestController {

    @Autowired // 객체 의존성 주입. 기존에는 NEW 나 Singleton패턴의 getInstance()로 객체를 생성했지만, 스프링에서는 이렇게 사용
    private RestHelper restHelper;

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private MembersService membersService;

    @Autowired
    private UtilHelper utilHelper;

    @Autowired
    private MailHelper mailHelper;

    @GetMapping("/api/account/id_unique_check")
    public Map<String, Object> idUniqueCheck(@RequestParam("user_id") String userId) {

        try {
            membersService.isUniqueUserId(userId);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }
        return restHelper.sendJson();
    }

    @GetMapping("/api/account/email_unique_check")
    public Map<String, Object> emailUniqueCheck(@RequestParam("email") String email) {

        try {
            membersService.isUniqueEmail(email);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }
        return restHelper.sendJson();
    }

    @PostMapping("/api/account/find_id")
    public Map<String, Object> findId(
            @RequestParam("user_name") String userName,
            @RequestParam("email") String email) {
        Members input = new Members();
        input.setUser_name(userName);
        input.setEmail(email);

        Members output = null;

        try {
            output = membersService.findId(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output.getUser_id());
        return restHelper.sendJson(data);

    }

    @PostMapping("/api/account/join")
    public Map<String, Object> join(
            @RequestParam("user_id") String userId,
            @RequestParam("user_pw") String userPw,
            @RequestParam("user_name") String userName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("birthday") String birthday,
            @RequestParam("gender") String gender,
            @RequestParam("postcode") String postcode,
            @RequestParam("addr1") String addr1,
            @RequestParam("addr2") String addr2,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        /** 1) 입력값에 대한 유효성 검사 */

        // 여기서는 생략

        /** 2) 아이디 중복검사 */
        try {
            membersService.isUniqueUserId(userId);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }

        /** 3) 이메일 중복검사 */
        try {
            membersService.isUniqueEmail(email);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }

        /** 4) 업로드 받기 */
        UploadItem uploadItem = null;

        try {
            uploadItem = fileHelper.saveMultipartFile(photo);
        } catch (NullPointerException e) {
            // 업로드 된 항목이 있는 경우는 에러가 아니므로 계속 진행
        } catch (Exception e) {
            // 업로드 된 항목이 있으나, 이를 처리하다가 에러가 발생한 경우
            return restHelper.serverError(e);
        }

        /** 5) 정보를 Service에 전달하기 위한 객체 구성 */
        Members members = new Members();
        members.setUser_id(userId);
        members.setUser_pw(userPw);
        members.setUser_name(userName);
        members.setEmail(email);
        members.setPhone(phone);
        members.setBirthday(birthday);
        members.setGender(gender);
        members.setPostcode(postcode);
        members.setAddr1(addr1);
        members.setAddr2(addr2);

        // 업로드 된 이미지의 이름을 표시할 필요가 없다면 저장된 경로만 DB에 저장하면 됨

        if (uploadItem != null) {
            members.setPhoto(uploadItem.getFilePath());
        }

        /** DB에 저장 */
        try {
            membersService.addItem(members);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }
        return restHelper.sendJson();
    }

    @PutMapping("/api/account/reset_pw")
    public Map<String, Object> resetPw(
            @RequestParam("user_id") String userId,
            @RequestParam("email") String email) {

        /** 1) 임시 비밀번호를 DB에 갱신하기 */
        String newPassword = utilHelper.randomPassword(8);
        Members input = new Members();
        input.setUser_id(userId);
        input.setEmail(email);
        input.setUser_pw(newPassword);

        try {
            membersService.resetPw(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        /** 2) 이메일 발송을 위한 템플릿 처리 */
        // 메일 템플릿 파일 경로
        ClassPathResource resource = new ClassPathResource("mail_templates/reset_pw.html");
        String mailTemplatePath = null;

        try {
            mailTemplatePath = resource.getFile().getAbsolutePath();
        } catch (IOException e) {
            return restHelper.serverError("메일 템플릿을 찾을 수 없습니다.");
        }

        // 메일 템플릿 파일 가져오기
        String template = null;

        try {
            template = fileHelper.readString(mailTemplatePath);
        } catch (Exception e) {
            return restHelper.serverError("메일 템플릿을 읽을 수 없습니다.");
        }

        // 메일 템플릿 안의 치환자 처리
        template = template.replace("{{user_id}}", userId);
        template = template.replace("{{password}}", newPassword);

        /** 3) 메일 발송 */
        String subject = userId + "님의 비밀번호가 재설정 되었습니다.";

        try {
            mailHelper.sendMail(email, subject, template);
        } catch (Exception e) {
            return restHelper.serverError("메일 발송에 실패했습니다.");
        }

        return restHelper.sendJson();
    }

    @PostMapping("/api/account/login")
    public Map<String,Object>login(
        // 세션을 사용해야 하므로 request 객체가 필요하다
        HttpServletRequest request,
        @RequestParam("user_id") String userId,
        @RequestParam("user_pw") String userPw){

            /** 1) 입력값에 대한 유효성 검사 */
            // 여기서는 생략

            /** 2) 입력값을 Beans 객체에 저장 */

            Members input = new Members();
            input.setUser_id(userId);
            input.setUser_pw(userPw);

            /** 3) 로그인 시도 */

            Members output = null;

            try{
                output = membersService.login(input);
            }catch(Exception e){
                return restHelper.serverError(e);
            }

            // 프로필 사진의 경로를 URL로 변환
            output.setPhoto(fileHelper.getUrl(output.getPhoto()));

            /** 4) 로그인에 성공했다면 회원 정보를 세션에 저장한다 */
            HttpSession session = request.getSession();
            session.setAttribute("memberInfo", output);

            /** 5) 로그인이 처리되었음을 응답한다 */
            return restHelper.sendJson();
        }

        @GetMapping("/api/account/logout")
        public Map<String,Object> logout(HttpServletRequest request){
            HttpSession session = request.getSession();
            session.invalidate();
            return restHelper.sendJson();
        }

        @DeleteMapping("/api/account/out")
        public Map<String,Object> out(
            HttpServletRequest request,
            @SessionAttribute("memberInfo") Members memberInfo,
            @RequestParam("password") String password){
                // 세션으로부터 추출한 Member 객체에 입력받은 비밀번호를 넣어준다
                memberInfo.setUser_pw(password);

                try {
                    membersService.out(memberInfo);
                } catch (Exception e) {
                    return restHelper.serverError(e);
                }

                // 로그아웃을 위해 세션을 삭제한다
                HttpSession session = request.getSession();
                session.invalidate();

                return restHelper.sendJson();
            }
    
}