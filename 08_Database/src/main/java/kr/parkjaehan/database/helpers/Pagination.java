package kr.parkjaehan.database.helpers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 페이지 네비게이션 처리를 위한 유틸리티 클래스
 * 이 클래스는 싱글톤으로 생성되어서는 안된다
 * --> new 키워드로 생성할 수 있도록 기본 생성자를 만들어야 한다
 */
@Data
@Slf4j
public class Pagination {
    // GET 파라미터로 처리할 값
    private int nowPage = 1; // 현재 페이지 번호

    // DB에서 조회한 결과 값
    private int totalCount = 0; // 전체 글 개수 조회

    // 개발자가 정의해야 하는 값
    private int listCount = 10; // 한 페이지에 보여질 글의 목록 개수
    private int groupCount = 5; // 한 그룹에 표시할 페이지 번호 개수

    // 연산처리가 필요한 값
    private int totalPage = 0; // 전체 페이지 수
    private int startPage = 1; // 현재 그룹의 시작 페이지
    private int endPage = 1; // 현재 그룹의 마지막 페이지
    private int prevPage = 0; // 이전 그룹의 마지막 페이지
    private int nextPage = 0; // 다음 그룹의 시작 페이지
    private int offset = 0; // LIMIT 절에서 사용할 offset

    public Pagination(int nowPage, int totalCount, int listCount, int groupCount){
        this.nowPage = nowPage;
        this.totalCount = totalCount;
        this.listCount = listCount;
        this.groupCount = groupCount;

        // 전체 페이지 수 계산
        this.totalPage = ((totalCount - 1) / listCount) + 1;

        // 현재 페이지에 대한 오차 조절
        if (nowPage < 0){
            nowPage = 1;
        }
        if (nowPage > totalPage){
            nowPage = totalPage;
        }

        // 현재 페이징 그룹의 시작 페이지 번호
        startPage = ((nowPage - 1) / groupCount) * groupCount + 1;

        // 현재 그룹의 끝 페이지 번호
        endPage = (((startPage - 1) + groupCount) / groupCount) * groupCount;

        // 끝 페이지 번호가 전체 페이지 수를 초과하면 오차 범위 조절
        if (endPage > totalPage){
            endPage = totalPage;
        }

        // 이전 그룹의 마지막 페이지
        if (startPage > groupCount){
            prevPage = startPage - 1;
        } else {
            prevPage = 0;
        }

        // 다음 그룹의 시작 페이지
        if (endPage < totalPage){
            nextPage = endPage + 1;
        } else {
            nextPage = 0;
        }

        // 검색 범위의 시작 위치
        offset = (nowPage - 1) * listCount;

        // 계산 결과를 로그로 기록한다
        log.debug(this.toString());
    }
}
