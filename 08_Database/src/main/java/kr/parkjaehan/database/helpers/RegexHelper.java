package kr.parkjaehan.database.helpers;

import java.util.regex.Pattern;

import kr.parkjaehan.database.exceptions.StringFormatException;

public class RegexHelper {
    // 싱글톤 패턴 시작
    private static RegexHelper current;

    public static RegexHelper getInstance() {
        if (current == null) {
            current = new RegexHelper();
        }

        return current;
    }
    // 싱글톤 패턴 끝
    private RegexHelper() {
        super();
    }

    /**
     * 주어진 문자열이 공백이거나 null인지 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isValue(String str, String message) throws StringFormatException {
        if (str == null || str.trim().equals("")) {
            throw new StringFormatException(message);
        }
    }

    /**
     * 주어진 문자열이 숫자인지 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isNum(String str, String message) throws StringFormatException {
        if (!Pattern.matches("^[0-9]*$", str)) {
            throw new StringFormatException(message);
        }
    }

    /**
     * 한글로만 구성되었는지에 대한 형식 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isKor(String str, String message) throws StringFormatException {
        if (!Pattern.matches("^[가-힣]*$", str)) {
            throw new StringFormatException(message);
        }
    }

    /**
     * 영어로만 구성되었는지에 대한 형식 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isEng(String str, String message) throws StringFormatException {
        if (!Pattern.matches("^[a-zA-Z]*$", str)) {
            throw new StringFormatException(message);
        }
    }

    /**
     * 영어와 숫자로만 구성되었는지에 대한 형식 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isEngNum(String str, String message) throws StringFormatException {
        if (!Pattern.matches("^[a-zA-Z0-9]*$", str)) {
            throw new StringFormatException(message);
        }
    }

    /**
     * 한글과 숫자로만 구성되었는지에 대한 형식 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isKorNum(String str, String message) throws StringFormatException {
        if (!Pattern.matches("^[가-힣0-9]*$", str)) {
            throw new StringFormatException(message);
        }
    }

    /**
     * 이메일 형식인지에 대한 형식 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isEmail(String str, String message) throws StringFormatException {
        if (!Pattern.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+){1,2}$", str)) {
            throw new StringFormatException(message);
        }
    }

    /**
     * "-" 없이 핸드폰번호 형식인지에 대한 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isCellPhone(String str, String message) throws StringFormatException {
        if (!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", str)) {
            throw new StringFormatException(message);
        }
    }

    /**
     * "-" 없이 집전화번호 형식인지에 대한 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isTel(String str, String message) throws StringFormatException {
        if (!Pattern.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$", str)) {
            throw new StringFormatException(message);
        }
    }

    /**
     * "-" 없이 집전화번호 혹은 핸드폰 번호 둘 중 하나를 충족하는지 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isPhone(String str, String message) throws StringFormatException {
        boolean cellPhone = Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", str);
        boolean telphone = Pattern.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$", str);

        if (!cellPhone && !telphone) {
            throw new StringFormatException(message);
        }
    }

    /**
     * 카드 번호 형식인지에 대한 검사
     * @param str
     * @param message
     * @throws StringFormatException
     */
    public void isCardNumber(String str, String message) throws StringFormatException {
        if (!Pattern.matches("^(\\d{4}-){3}\\d{4}$", str)) {
            throw new StringFormatException(message);
        }
    }
}