package kr.parkjaehan.database.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileHelper {
    /**
     * 파일에 데이터를 쓰는 메서드
     * 
     * @param filepath
     * @param data
     * @throws Exception
     */
    public void write(String filepath, byte[] data) throws Exception {
        OutputStream os = null;
        try {
            os = new FileOutputStream(filepath);
            os.write(data);
        } catch (FileNotFoundException e) {
            log.error("파일을 찾을 수 없습니다.", e);
            throw e;
        } catch (IOException e) {
            log.error("파일을 쓸 수 없습니다.", e);
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("파일 입출력 오류가 발생했습니다.", e);
            e.printStackTrace();
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("파일을 닫는 중 오류가 발생했습니다.", e);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 파일에서 데이터를 읽는 메서드
     * 
     * @param filepath
     * @return
     * @throws Exception
     */
    public byte[] read(String filepath) throws Exception {
        byte[] data = null;

        InputStream is = null;
        try {
            is = new FileInputStream(filepath);
            data = new byte[is.available()];
            is.read(data);
        } catch (FileNotFoundException e) {
            log.error("파일을 찾을 수 없습니다", e);
            throw e;
        } catch (IOException e) {
            log.error("파일을 읽을 수 없습니다", e);
            throw e;
        } catch (Exception e) {
            log.error("파일 입출력 오류가 발생했습니다.", e);
            throw e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("파일을 닫는 중 오류가 발생했습니다.", e);
                    throw e;
                }
            }
        }
        return data;
    }

    /**
     * 파일에 문자열을 쓰는 메서드
     * 
     * @param filepath
     * @param content
     * @throws Exception
     */
    public void writeString(String filepath, String content) throws Exception {
        try {
            this.write(filepath, content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("지원하지 않는 인코딩입니다.", e);
            throw e;
        }
    }

    /**
     * 파일에서 문자열을 읽는 메서드
     * 
     * @param filepath
     * @return
     * @throws Exception
     */
    public String readString(String filepath) throws Exception {
        String content = null;
        try {
            byte[] data = this.read(filepath);
            content = new String(data, "utf-8");
        } catch (Exception e) {
            log.error("지원하지 않는 인코딩입니다.", e);
            throw e;
        }
        return content;
    }
}