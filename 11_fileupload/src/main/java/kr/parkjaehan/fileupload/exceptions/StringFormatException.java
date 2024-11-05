package kr.parkjaehan.fileupload.exceptions;

/**
 * 정규 표현식에 부합하지 않음을 의미하는 예외상황을 처리
 */

public class StringFormatException extends Exception{
    public StringFormatException(String message){
        super(message);
    }
}
