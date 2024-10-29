package kr.parkjaehan.database.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

import kr.parkjaehan.database.models.Professor;

@Mapper
public interface ProfessorMapper {
    @Insert("INSERT INTO professor(name, userid, position, sal, hiredate, comm, deptno) " +
            "VALUES (#{name}, #{userid}, #{position}, #{sal}, #{hiredate}, #{comm}, #{deptno})")
    @Options(useGeneratedKeys = true, keyProperty = "profno",keyColumn = "profno")
    int insert(Professor input);
    
    @Update("UPDATE professor SET " + 
            "name = #{name}, " + 
            "userid = #{userid}, " + 
            "position = #{position}, " + 
            "sal = #{sal}, " + 
            "hiredate = #{hiredate}, " + 
            "comm = #{comm}, " + 
            "deptno = #{deptno} " + 
            "WHERE profno = #{profno}")
    int update(Professor input);

    @Delete("DELETE FROM professor WHERE profno = #{profno}")
    int delete(Professor input);
    
    @Delete("DELETE FROM professor WHERE deptno = #{deptno}")
    int deleteByDeptno(Professor input);

    @Select("SELECT " + 
            "profno, name, userid, position, sal, " +
            "DATE_FORMAT(hiredate, '%Y-%m-%d') AS hiredate, comm, p.deptno as deptno, dname " +
            "FROM professor p " +
            "INNER JOIN department d ON p.deptno = d.deptno " +
            "WHERE profno = #{profno}")
    @Results(id = "professorMap", value = {
            @Result(property = "profno", column = "profno"),
            @Result(property = "name", column = "name"),
            @Result(property = "userid", column = "userid"),
            @Result(property = "position", column = "position"),
            @Result(property = "sal", column = "sal"),
            @Result(property = "hiredate", column = "hiredate"),
            @Result(property = "comm", column = "comm"),
            @Result(property = "deptno", column = "deptno"),
            @Result(property = "dname", column = "dname")})
    Professor selectItem(Professor input);

    /**
     * 다중행 조회를 위한 메서드 정의
     * @param input - 조회할 학과 정보에 대한 모델 객체
     * @return 조회된 데이터 리스트
     */
    @Select("<script>" + 
        "SELECT profno, name, userid, position, sal, hiredate, comm, p.deptno as deptno, dname " +
        "FROM professor p " + 
        "INNER JOIN department d ON p.deptno = d.deptno " +
        "<where>" +
        "<if test='name != null'>name LIKE concat('%', #{name}, '%')</if>" +
        "<if test='userid != null'> OR userid LIKE concat('%', #{userid}, '%')</if>" +
        "</where>" +
        "ORDER BY profno DESC " +
        "<if test='listCount > 0'>LIMIT #{offset}, #{listCount}</if>" +
        "</script>")
        @ResultMap("professorMap")
    List<Professor> selectList(Professor input);

    @Select("<script>" + 
        "SELECT COUNT(*) AS cnt FROM professor p " + 
        "INNER JOIN department d ON p.deptno = d.deptno " +
        "<where>" +
        "<if test='name != null'>name LIKE concat('%', #{name}, '%')</if>" +
        "<if test='userid != null'> OR userid LIKE concat('%', #{userid}, '%')</if>" +
        "</where>" +
        "</script>")
        public int selectCount(Professor input);
}
