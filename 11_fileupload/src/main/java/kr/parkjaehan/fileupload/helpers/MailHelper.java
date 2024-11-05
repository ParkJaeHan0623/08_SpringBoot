package kr.parkjaehan.fileupload.helpers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailHelper {
    // -> import 
    @Autowired
    private final JavaMailSender javaMailSender = null;

    // 환경 설정 파일에 설정된 값을 읽어들이기 위한 변수
    // -> import org.springframework.beans.factory.annotation.Value; 
    @Value("${mailhelper.sender.name}")
    private final String senderName = null;

    @Value("${mailhelper.sender.email}")
    private final String senderEmail = null;

    /**
     * 메일을 발송한다
     * 
     * @param receiverName - 수신자 이름
     * @param receiverEmail - 수신자 이메일 주소
     * @param subject - 제목
     * @param content - 내용
     * 
     * @throws MessagingException - 메일 발송 예외
     */
    // -> import jakarta.mail.MessagingException; ????
    public void sendMail(String receiverName, String receiverEmail, String subject, String content) throws Exception { 
        
        log.debug("----------------------------------------");
        log.debug(String.format("ReceiverName: %s", receiverName));     // Recv 대신 그냥 풀로 씀
        log.debug(String.format("ReceiverEmail: %s", receiverEmail));
        log.debug(String.format("Subject: %s", subject));
        // 본문 내용은 기니까 주석처리
        // log.debug(String.format("Content: %s", content));
        log.debug("----------------------------------------");

        // --> import javax.mail.internet.MimeMessage; ==???? 아닌데유? ㅇㅇㅇ. 대체된 내용. 예전 예제.
        MimeMessage message = javaMailSender.createMimeMessage();
        // --> import org.springframework.mail.javamail.MimeMessageHelper;
        MimeMessageHelper helper = new MimeMessageHelper(message); 

        // 제목, 내용, 수신자 설정
        try {
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setTo(new InternetAddress(receiverEmail, receiverName, "UTF-8"));

            // 보내는 사람의 주소와 이름을 환경 설정 파일에서 읽어온 값으로 지정
            helper.setFrom(new InternetAddress(senderEmail, senderName, "UTF-8"));
            
            // 메일 발송
            javaMailSender.send(message);   
        } catch (MessagingException e) {
            log.error("메일 발송 정보 설정 실패", e);
            throw e;
        } catch(UnsupportedEncodingException e){
            log.error("지원하지 않는 인코딩", e);
            throw e;
        } catch (Exception e){
            log.error("알 수 없는 에러 발생", e);
            throw e;
        }    
    }

    /**
     * 메일을 발송한다. (수신자 이름 없음)
     * 
     * @param receiverEmail - 수신자 이메일 주소
     * @param subject - 제목
     * @param content - 내용
     * @throws Messaging Exception - 메일 발송 예외....이라는데 여기도 그냥 throws Exception? 모르게따~
     */
    public void sendMail(String receiverEmail, String subject, String content) throws Exception{
        this.sendMail(null, receiverEmail, subject, content);
    }
}
