package kr.parkjaehan.database.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.parkjaehan.database.models.Department;

@Mapper
public interface DepartmentMapper {
    /**
     * 부서 정보를 입력한다.
     * PK값은 파라미터로 전달된 input 객체에 참조로 처리된다
     * @param input - 입력할 학과 정보에 대한 모델 객체
     * @return 입력된 데이터 수
     */
    @Insert("INSERT INTO department (dname,loc) VALUES (#{dname}, #{loc})")
    // INSERT문에서 필요한 PK에 대한 옵션 정의
    // --> userGeneratedKeys = true : AUTO_INCREMENT가 적용된 테이블인 경우 사용
    // --> keyProperty = 파라미터로 전달되는 MODEL 객체에서 PK에 대응되는 멤버변수
    // --> keyColumn = 테이블에서 PK에 대응되는 컬럼명
    @Options(useGeneratedKeys = true, keyProperty = "deptno", keyColumn = "deptno")
    public int insert(Department input);

    /**
     * 부서 정보를 수정한다.
     * @param input - 수정할 학과 정보에 대한 모델 객체
     * @return 수정된 데이터 수
     */
    @Update("UPDATE department SET dname = #{dname}, loc = #{loc} WHERE deptno = #{deptno}")
    public int update(Department input);

    /**
     * 부서 정보를 삭제한다.
     * @param input - 삭제할 학과 정보에 대한 모델 객체
     * @return 삭제된 데이터 수
     */
    @Delete("DELETE FROM department WHERE deptno = #{deptno}")
    public int delete(Department input);

    /**
     * 단일행 조회를 위한 기능 정의
     * @param input - 조회할 학과 정보에 대한 모델 객체
     * @return 조회된 데이터 정보
     */
    @Select("SELECT deptno, dname, loc FROM department WHERE deptno = #{deptno}")
    // 조회 결과와 리턴할 MODEL 객체를 연결하기 위한 규칙 정의
    // --> property : MODEL 객체의 멤버변수명
    // --> column : SELECT문에 명시된 필드 이름(AS 옵션을 사용한 경우 별칭으로 명시)
    // import org.apache.ibatis.annotations.Results;
    // import org.apache.ibatis.annotations.Result;

    /** DepartmentMapper.xml 파일에서 resultMap 부분 */
    @Results(id = "departmentMap",value = {
        @Result(property = "deptno", column = "deptno"),
        @Result(property = "dname", column = "dname"),
        @Result(property = "loc", column = "loc")
    })
    public Department selectItem(Department input);

    /**
     * 다중행 조회를 위한 기능 정의
     * @param input - 조회할 학과 정보에 대한 모델 객체
     * @return 조회된 데이터 리스트
     */
    @Select("SELECT deptno, dname, loc FROM department " + 
            "ORDER BY deptno DESC") // + 로 띄워쓰기를 할 경우 첫번째 문자열의 끝에 공백을 추가해야 한다.
    // 조회 결과와 MODEL의 맵핑이 이전 규칙과 동일한 경우 id 값으로 이전 규칙을 재사용
    @ResultMap("departmentMap")
    public List<Department> selectList(Department input);
    

    
}
