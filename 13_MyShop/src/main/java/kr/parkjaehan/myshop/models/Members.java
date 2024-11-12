package kr.parkjaehan.myshop.models;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Members implements Serializable{
    private int id; // 일련번호
    private String user_id; // 아이디
    private String user_pw; // 비밀번호(암호화)
    private String user_name; // 이름
    private String email; // 이메일
    private String phone; // 전화번호
    private String birthday; // 생년월일
    private char gender; // 성별 (M:남성, F:여성)
    private String postcode; // 우편번호
    private String addr1; // 주소
    private String addr2; // 상세주소
    private String photo; // 프로필 사진 정보{json=UploadItem}
    private char is_out; // 탈퇴여부(Y:탈퇴, N:가입)
    private char is_admin; // 관리자여부(Y:관리자, N:일반회원)
    private String login_date; // 최종로그인일시
    private String reg_date; // 가입일시
    private String edit_date; // 수정일시


    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;
}
