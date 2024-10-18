package kr.parkjaehan.mailer.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailHelper {
    @Autowired
    private final JavaMailSender javaMailSender = null;

    // 환경설정파일에 설정된 값을 읽어들이기 위한 변수
    @Value("${mailhelper.sender.name}}")
    private final String senderName = null;

    @Value("${mailhelper.sender.email}")
    private final String senderEmail = null;

    /**
     * 메일을 발송한다.
     * 
     * @param recevierName - 수신자 이름
     * @param receiverEmail - 수신자 이메일
     * @param subject - 제목
     * @param content - 내용
     * @throws Exception
     */
    public void sendMail(String recevierName, String receiverEmail, String subject, String content) throws Exception {
        log.debug("=============================");
        log.debug(String.format("receiverName: %s",recevierName));
        log.debug(String.format("receiverEmail: %s",receiverEmail));
        log.debug(String.format("subject: %s",subject));
        log.debug(String.format("content: %s",content));
        log.debug("=============================");

       
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

         // 제목, 내용, 수신자 설정
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setTo(new InternetAddress(receiverEmail, recevierName , "UTF-8"));

        // 보내는 사람의 주소와 이름을 환경설정 파일에서 읽어온 값으로 지정
        helper.setFrom(new InternetAddress(senderEmail, senderName, "UTF-8"));

        // 메일 발송
        javaMailSender.send(message);
    }

    /**
     * 메일을 발송한다(첨부파일 없음)
     * 
     * @param receiverEmail - 수신자 이메일
     * @param subject - 제목
     * @param content - 내용
     * @throws Exception 
     */
    public void sendMail(String receiverEmail, String subject, String content) throws Exception {
        this.sendMail(null, receiverEmail, subject, content);
    }
}
