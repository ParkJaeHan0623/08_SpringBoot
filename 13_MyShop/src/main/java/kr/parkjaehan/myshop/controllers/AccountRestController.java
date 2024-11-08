package kr.parkjaehan.myshop.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.parkjaehan.myshop.helpers.FileHelper;
import kr.parkjaehan.myshop.helpers.RestHelper;
import kr.parkjaehan.myshop.models.Members;
import kr.parkjaehan.myshop.models.UploadItem;
import kr.parkjaehan.myshop.services.MembersService;

@RestController
public class AccountRestController {
    
    @Autowired
    private RestHelper restHelper;

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private MembersService membersService;

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

        Map<String, Object> data = new LinkedHashMap<String,Object>();
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
        @RequestParam("gender") char gender,
        @RequestParam("postcode") String postcode,
        @RequestParam("addr1") String addr1,
        @RequestParam("addr2") String addr2,
        @RequestParam(value = "photo", required=false) MultipartFile photo) {
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
            } catch(NullPointerException e){
                // 업로드 된 항목이 있는 경우는 에러가 아니므로 계속 진행
            }
            catch (Exception e) {
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

            // 업로드 된 이미지의 이름을  표시할 필요가 없다면 저장된 경로만 DB에 저장하면 됨

            if (uploadItem != null) {
                members.setPhoto(uploadItem.getFilePath());
            }
            

        
        
        /**DB에 저장 */
        try {
            membersService.addItem(members);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }
        return restHelper.sendJson();
    }
}
