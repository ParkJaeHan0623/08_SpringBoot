package kr.parkjaehan.crud.mappers;

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
import kr.parkjaehan.crud.models.Student;

@Mapper
public interface StudentMapper {
    @Insert("INSERT INTO student(" + 
            "name, userid, grade, idnum, birthdate, " +
            "tel, height, weight, deptno, profno) " +
            "VALUES (" + 
            "#{name}, #{userid}, #{grade}, #{idnum}, #{birthdate}, " +
            "#{tel}, #{height}, #{weight}, #{deptno}, #{profno})")
    @Options(useGeneratedKeys = true, keyProperty = "studno", keyColumn = "studno")
    int insert(Student input);
    
    @Update("UPDATE student SET " + 
            "name = #{name}, " + 
            "userid = #{userid}, " + 
            "grade = #{grade}, " + 
            "idnum = #{idnum}, " + 
            "birthdate = #{birthdate}, " + 
            "tel = #{tel}, " + 
            "height = #{height}, " + 
            "weight = #{weight}, " + 
            "deptno = #{deptno}, " + 
            "profno = #{profno} " + 
            "WHERE studno = #{studno}")
    int update(Student input);

    @Delete("DELETE FROM student WHERE studno = #{studno}")
    int delete(Student input);

    // 학과를 삭제하기 전에 소속된 학생 데이터를 삭제
    @Delete("DELETE FROM student WHERE deptno = #{deptno}")
    int deleteByDeptno(Student input);

    // 교수를 삭제하기 전에 교수에게 소속된 학생들과의 연결읠 해제
    // --> profno 컬럼이 null 허용으로 설정되어야 함

    @Update("UPDATE student SET profno = NULL WHERE profno = #{profno}")
    int updateByProfno(Student input);


    @Select("SELECT " + 
            "studno, s.name, s.userid, grade, s.idnum, " +
            "DATE_FORMAT(birthdate, '%Y-%m-%d') AS birthdate, " +
            "tel, height, weight, s.deptno, s.profno ,dname, p.name AS pname " +
            "FROM student s " +
            "INNER JOIN department d ON s.deptno = d.deptno " +
            "INNER JOIN professor p ON s.profno = p.profno " +
            "WHERE studno = #{studno}")
    @Results(id = "studentMap", value = {
            @Result(property = "studno", column = "studno"),
            @Result(property = "name", column = "name"),
            @Result(property = "userid", column = "userid"),
            @Result(property = "grade", column = "grade"),
            @Result(property = "idnum", column = "idnum"),
            @Result(property = "birthdate", column = "birthdate"),
            @Result(property = "tel", column = "tel"),
            @Result(property = "height", column = "height"),
            @Result(property = "weight", column = "weight"),
            @Result(property = "deptno", column = "deptno"),
            @Result(property = "profno", column = "profno"),
            @Result(property = "dname", column = "dname"),
            @Result(property = "pname", column = "pname")})
    Student selectItem(Student input);

    @Select("<script>" + 
    "SELECT s.studno, s.name, s.userid, s.grade, s.idnum, " +
    "DATE_FORMAT(s.birthdate, '%Y-%m-%d') AS birthdate, " +
    "s.tel, s.height, s.weight, s.deptno, s.profno ,dname, p.name AS pname " +
    "FROM student s " +  
    "INNER JOIN department d ON s.deptno = d.deptno " +
    "INNER JOIN professor p ON s.profno = p.profno " +
    "<where>" +
    "<if test='name != null'>s.name LIKE concat('%', #{name}, '%')</if>" +
    "<if test='userid != null'> OR s.userid LIKE concat('%', #{userid}, '%')</if>" +
    "</where>" +
    "ORDER BY s.studno DESC " +
    "<if test='listCount > 0'> LIMIT #{offset}, #{listCount}</if>" +  
    "</script>")
@ResultMap("studentMap")
List<Student> selectList(Student input);

@Select("<script>" +
    "SELECT COUNT(*) AS cnt FROM student s " +
    "INNER JOIN department d ON s.deptno = d.deptno " +
    "INNER JOIN professor p ON s.profno = p.profno " +
    "<where>" +
    "<if test='name != null'>s.name LIKE concat('%', #{name}, '%')</if>" +
    "<if test='userid != null'> OR s.userid LIKE concat('%', #{userid}, '%')</if>" +
    "</where>" +
    "</script>")
public int selectCount(Student input);
}
