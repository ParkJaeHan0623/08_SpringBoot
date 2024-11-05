package kr.parkjaehan.fileupload.exceptions;
// 서비스에서 결과가 없을때의 예외처리를 따로 구분하기 위해서 제작. 원래는 에러가 아니지만, 우리가 에러로 간주하기 위해서 따로 추가함.
public class ServiceNoResultException extends Exception{
    public ServiceNoResultException(String message){
        super(message);
    }
}
