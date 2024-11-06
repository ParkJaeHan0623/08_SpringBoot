package kr.parkjaehan.scheduler.schedulers;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableAsync
public class SchedulerDemo {
    /**
     * 해당 메서드가 끝나는 시간 기준, 지정된 milliscends 간격으로 실행
     * 하나의 인스턴스만 항상 실행되도록 해야 할 상황에서 유용
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 1000)
    public void sample1() throws InterruptedException{
        System.out.println("[sample1] 시작" + LocalDateTime.now());
        Thread.sleep(3000);
        System.out.println("[sample1] 종료" + LocalDateTime.now());
    }

    /**
     * 해당 메서드가 시작하는 시간 기준, 지정된 milliscends 간격으로 실행
     * 모든 실행이 독립적인 경우에 유용
     * 메서드의 수행시간이 지정된 간격보다 긴 경우 메서드가 중복실행(=병렬)될 수 있음
     * 병렬로 Scheduler를 사용할 경우, Class에 @EnableAsync, Method에 @Async 어노테이션을 추가해야 함
     * @throws InterruptedException
     */
    @Async
    @Scheduled(fixedDelay = 1000)
    public void sample2() throws InterruptedException{
        System.out.println("[sample2] 시작" + LocalDateTime.now());
        Thread.sleep(3000);
        System.out.println("[sample2] 종료" + LocalDateTime.now());
    }
    
    /**
     * initialDelay 값 이후 처음 실행되고, fixedDelay 값에 따라 계속 실행
     * @throws InterruptedException
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void sample3() throws InterruptedException{
        System.out.println("[sample3] 시작" + LocalDateTime.now());
        Thread.sleep(3000);
        System.out.println("[sample3] 종료" + LocalDateTime.now());
    }

    /*
     * 지정된 스케쥴에 따라 실행
     * => 초 분 시 일 월 ?
     * 
     * 1초마다 실행되는 작업 : * * * * * ?
     * 매 분 0초에 실행되는 작업 : 0 * * * * ?
     * 매 10초 마다 실행 : 0/10 * * * * ? => 1시 0분 10초, 1시 0분 20초, 1시 0분 30초, ...
     * 매 분 10초마다 실행 : 10 * * * * ? => 1시 0분 10초, 1시 1분 10초, 1시 2분 10초, ...
     * 매 시 정각에 실행되는 작업 : 0 0 * * * ?
     * 매일 자정에 실행되는 작업 : 0 0 0 * * ?
     * 
     * http://www.cronmaker.com/ 에서 cron 표현식을 만들 수 있음
     * 
     */
    @Scheduled(cron = "0 02 * * * ?")// 매일 02분에 실행
    public void sample4() throws InterruptedException{
        System.out.println("[sample4] 시작" + LocalDateTime.now());
        Thread.sleep(3000);
        System.out.println("[sample4] 종료" + LocalDateTime.now());
    }
}
