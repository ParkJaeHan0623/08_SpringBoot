package kr.parkjaehan.myshop.models;

import lombok.Data;

@Data
public class Members {
    private int id;
    private String user_id;
    private String user_pw;
    private String user_name;
    private String email;
    private String phone;
    private String birthday;
    private char gender;
    private String postcode;
    private String addr1;
    private String addr2;
    private Byte photo;
    private char is_out;
    private char is_admin;
    private String login_date;
    private String reg_date;
    private String edit_date;
}
