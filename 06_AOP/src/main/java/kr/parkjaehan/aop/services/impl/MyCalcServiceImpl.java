package kr.parkjaehan.aop.services.impl;

import org.springframework.stereotype.Service;

import kr.parkjaehan.aop.services.MyCalcService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyCalcServiceImpl implements MyCalcService {
    // 이 객체가 생성되었음을 확인하기 위해 생성자를 정의함
    // 보통의 Service 구현체는 생성자를 정의하지 않음
    public MyCalcServiceImpl() {
        log.debug("MyCalcServiceImpl() 생성자 호출됨");
    }
    @Override
    public int plus(int x, int y) {
        return x + y;
    }

    @Override
    public int minus(int x, int y) {
        return x - y;
    }
    
}
