package kr.parkjaehan.fileupload.controllers;

import java.io.File;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import kr.parkjaehan.fileupload.helpers.WebHelper;
import kr.parkjaehan.fileupload.models.UploadItem;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class SimpleUploadController {
    
    // WebHelper 주입//
    // --> import kr.parkjaehan.fileupload.helpers.WebHelper;
    @Autowired
    private WebHelper webHelper;

    // 업로드 된 파일이 저장될 경로 (application.properties로부터 읽어옴)
    // --> import org.springframework.beans.factory.annotation.Value;
    @Value("${upload.dir}")
    private String uploadDir;

    // 업로드 된 파일이 노출될 URL 경로 (application.properties로부터 읽어옴)
    @Value("${upload.url}")
    private String uploadUrl;

    /** 업로드 폼을 구성하는 페이지 */
    @GetMapping("/simple/upload_single")
    public String uploadSingle() {
        return "simple/upload_single";
    }  

    /**
     * Spring 순정 업로드 기능 구현
     * - 업로드가 되는 과정에서 다루어야 하는 값들을 확인하기 위하여서 액션 페이지를 볼 뷰를 만듦
     */
    @SuppressWarnings("null")
    @PostMapping("/simple/upload_single_ok")
    public String uploadOk(Model model,
        @RequestParam(value="subject", required=false) String subject,
        // <input type="file">의 name값이 photo인 항목을 받는다.
        // --> import org.springframework.web.multipart.MultipartFile;
        @RequestParam(value="photo", required=false) MultipartFile photo) { // MultipartFile -> 업로드된 파일의 클래스 
        
        /** 1) 업로드 된 파일이 존재하는지 확인한다 */
        // 업로드 된 파일이 존재하는지 확인한다.
        if(photo.getOriginalFilename().isEmpty()) {     // getOriginalFilename() -> 사용자가 지정한 이름
            webHelper.badRequest("업로드 된 파일이 없습니다.");
            return null;
        }
        
        // 업로드 된 파일의 정보를 로그로 기록
        log.debug("=============================================");
        log.debug("원본파일이름: "+ photo.getOriginalFilename());
        log.debug("<input type='file'>의 name 속성값: "+photo.getName());
        log.debug("파일형식: "+photo.getContentType());
        log.debug("파일크기: "+photo.getSize());

        // 업로드 된 파일이 저장될 폴더의 이름을 "년/월/일" 형식으로 생성한다.
        Calendar c= Calendar.getInstance();
        // 강사님 pc에서는 %fmf $로 변경해서 실습
        String targetDir = String.format("%s/%04d/%02d/%02d", 
                            uploadDir, c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));

        // 폴더가 존재하지 않는다면 생성한다.
        File f= new File(targetDir);
        if(!f.exists()){
            f.mkdirs();
        }

        // 저장될 파일의 이름을 생성한다.
        // 파일의 원본 이름 추출
        String originName = photo.getOriginalFilename();
        // 파일의 원본 이름에서 확장자만 추출
        String ext = originName.substring(originName.lastIndexOf("."));
        String fileName=null;   // 웹 서버에 저장될 파일이름.
        File targetFile=null;   // 저장된 파일 정보를 담기 위한 File 객체
        int count = 0;          // 중복된 파일 수

        // 무한루프로 파일이름이 중복되지 않을 때까지 반복
        while(true){
            // 저장될 파일 이름 --> 현재시각 + 카운트값 + 확장자
            fileName = String.format("%d%d%s", System.currentTimeMillis(), count, ext);
            // 업로드 파일이 저장될 폴더 + 파일이름으로 파일객체를 생성한다.
            targetFile = new File(targetDir, fileName);

            // 동일한 이름의 파일이 없다면 반복 중단.
            if(!targetFile.exists()){
                break;
            }

            // if문을 빠져나올 경우 중복된 이름의 파일이 존재한다는 의미이므로 count 를 1증가
            count++;
        }
        
        // 업로드 된 파일이 저장될 경로 정보를 생성한다.
        // --> D:/SteveK/08-Spring/upload + photo.getOriginalFilename()
        // --> /D:/SteveK/08-Spring/upload + myPhoto.jpg
        // 윈도우의 경우 운영체제 형식에 따라 슬래시가 역슬래시로 변환되어 객체가 생성된다.
        // --> 상용화 시에는 리눅스 서버를 사용하므로 이 점을 주의해야 한다.
        // File targetFile = new File(uploadDir, photo.getOriginalFilename()); // 자바 io file 임포트 해옴

        // 업로드 된 파일을 지정된 경로로 복사한다.
        // 업로드 실패를 위해 try-catch 구문을 사용한다(자동 추가가 안되어서 직접 추가해야함)
        try{
            photo.transferTo(targetFile);
        }catch(Exception e){
            webHelper.serverError(e);
            return null;
        }

        /** 2) 업로드 정보 처리하기 */
        // 복사된 파일의 절대경로를 추출한다.
        // --> 운영체제 호환(Windows -> Linux)을 위해 역슬래시를 슬래시로 변환한다.
        // --> D:/SteveK/08-Spring/upload/myPhoto.jpg
        String absPath = targetFile.getAbsolutePath().replace("\\", "/");
        log.debug("업로드 된 파일 경로: "+absPath);

        // 업로드 된 파일의 절대경로(absPath)에서 환경설정 파일에 명시된 폴더까지의 위치는 삭제하여
        // 환경 설정 파일에 명시된 upload.dir 이후의 위치만 추출한다(윈도우만 유ㅅ유)
        String filePath= null;
        if(absPath.substring(0,1).equals("/")){
            // Mac, Linux용 경로 처리
            // absPath: /Users/stevek/studyproject/upload/myPhoto.jpg
            // uploadDir: /Users/stevek/studyproject/upload
            filePath = absPath.replace(uploadDir, "");
        } else{
            // window용 경로처리 --> 설정파일에 명시한 첫 글자(/)를 제거해야 함
            filePath = absPath.replace(uploadDir.substring(1), "");
        }

        log.debug("업로드 폴더 내에서의 최종 경로: "+filePath);

        // 3) 업로드 결과를 Beans에 저장 -> 여기에 저장 안하면, 저장한 위치를 모르니 나중에 불러올 수 없엉...
        UploadItem item = new UploadItem();
        item.setContentType(photo.getContentType());
        item.setFieldName(photo.getName());
        item.setFileSize(photo.getSize());
        item.setOriginName(photo.getOriginalFilename());
        item.setFilePath(filePath);

        // 업로드 경로를 웹 상에서 접근 가능한 경로 문자열로 변환하여 Beans에 추가한다.
        String fileUrl = String.format("%s%s", uploadUrl, filePath);
        item.setFileUrl(fileUrl);
        log.debug("파일의 URL: "+fileUrl);
        log.debug("=============================================");

        // 4) View 처리
        // 텍스트 정보를 View로 전달한다.
        model.addAttribute("subject", subject);
        // 업로드 정보를 view로 전달한다.
        model.addAttribute("item", item);

        // view 호출
        return "simple/upload_single_ok";
    }
}
