package kr.parkjaehan.restfulapi.exceptions;

/**
 * 서비스에서 결과가 없을 때 발생하는 예외 상황을 처리
 */
public class ServiceNoResultException extends Exception{
    public ServiceNoResultException(String message){
        super(message);
    }
    
}
