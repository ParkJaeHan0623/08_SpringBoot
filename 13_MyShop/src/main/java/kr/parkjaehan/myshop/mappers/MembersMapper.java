package kr.parkjaehan.myshop.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.parkjaehan.myshop.models.Members;

@Mapper
public interface MembersMapper {

        // 회원가입
        @Insert("INSERT INTO members (" +
                        "user_id, user_pw, user_name, email, phone, " +
                        "birthday, gender, postcode, addr1, addr2, " +
                        "photo, is_out, is_admin, login_date, reg_date, edit_date) " +
                        "VALUES (" +
                        "#{user_id}, MD5(#{user_pw}), #{user_name}, #{email}, #{phone}, " +
                        "#{birthday}, #{gender} , #{postcode}, #{addr1}, #{addr2}, " +
                        "#{photo}, 'N', 'N', Null, now(), now())")
        @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        public int insert(Members input);

        // 회원정보 수정
        @Update("UPDATE members SET " +
                        "user_id = #{user_id}, user_pw = MD5(#{user_pw}), user_name = #{user_name}, email = #{email}, phone = #{phone}, "
                        +
                        "birthday = #{birthday}, gender = #{gender}, postcode = #{postcode}, addr1 = #{addr1}, addr2 = #{addr2}, "
                        +
                        "photo = #{photo}, is_out = #{is_out}, is_admin = #{is_admin}, login_date = #{login_date}, reg_date = #{reg_date}, edit_date = now() "
                        +
                        "WHERE id = #{id}")
        public int update(Members input);

        @Delete("DELETE FROM members WHERE id = #{id}")
        public int delete(Members input);

        // 회원정보 조회
        @Select("SELECT * FROM members WHERE id = #{id}")
        @Results(id = "membersMap", value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user_id", column = "user_id"),
                        @Result(property = "user_pw", column = "user_pw"),
                        @Result(property = "user_name", column = "user_name"),
                        @Result(property = "email", column = "email"),
                        @Result(property = "phone", column = "phone"),
                        @Result(property = "birthday", column = "birthday"),
                        @Result(property = "gender", column = "gender"),
                        @Result(property = "postcode", column = "postcode"),
                        @Result(property = "addr1", column = "addr1"),
                        @Result(property = "addr2", column = "addr2"),
                        @Result(property = "photo", column = "photo"),
                        @Result(property = "is_out", column = "is_out"),
                        @Result(property = "is_admin", column = "is_admin"),
                        @Result(property = "login_date", column = "login_date"),
                        @Result(property = "reg_date", column = "reg_date"),
                        @Result(property = "edit_date", column = "edit_date")
        })
        public Members selectItem(Members input);

        // 아이디 중복검사
        @Select("SELECT id, user_id, user_pw, user_name FROM members ORDER BY id")
        @ResultMap("membersMap")
        public List<Members> selectList(Members input);

        // 로그인
        @Select("<script>" + //
                "SELECT COUNT(*) FROM members\n" + //
                "<where>\n" + //
                "<if test='user_id != null'>user_id = #{user_id}</if>\n" + //
                "<if test='email != null'>email = #{email}</if>\n" + //
                "</where>\n" + //
                "</script>")
        public int selectCount(Members input);

        // 아이디 찾기
        @Select("SELECT user_id FROM members " + //
                "WHERE user_name = #{user_name} AND email = #{email} AND is_out='N'")
        @ResultMap("membersMap")
        public Members findId(Members input);

        // 비밀번호 찾기
        @Update("UPDATE members SET " + //
                "user_pw = MD5(#{user_pw}), edit_date = now() " + //
                "WHERE user_id = #{user_id} AND email = #{email} AND is_out='N'")
        public int resetPw(Members input);


        // 로그인
        @Select("SELECT \n" + //
                "id, user_id, user_pw, user_name, email, phone, birthday, gender, \n" + //
                "postcode, addr1, addr2, photo, is_out, is_admin, login_date, reg_date, edit_date \n" + //
                "FROM members \n" + //
                "WHERE user_id = #{user_id} AND user_pw = MD5(#{user_pw}) AND is_out='N'")
        @ResultMap("membersMap")
        public Members login(Members input);

        // 로그인 시 로그인 날짜 업데이트
        @Update("UPDATE members SET login_date = now() WHERE id = #{id} AND is_out='N'")
        public int updateLoginDate(Members input);

        // 회원탈퇴
        @Update("UPDATE members \n" +
                "SET is_out = 'Y', edit_date = now() \n" +
                "WHERE id = #{id} AND user_pw = MD5(#{user_pw}) AND is_out = 'N'" )
        public int out(Members input);

        // 탈퇴한 회원 중 탈퇴 후 1분이 지난 회원 사진만 검색
        @Select("SELECT photo FROM members \n" +
                "WHERE is_out='Y' AND \n" +
                "edit_date < DATE_ADD(NOW(), interval -1 minute) AND \n" +
                "photo IS NOT NULL")
        @ResultMap("membersMap")
        public List<Members> selectOutMembersPhoto();

        // 탈퇴한 회원 중 탈퇴 후 1분이 지난 회원 삭제
        @Delete("DELETE FROM members \n" +
                "WHERE is_out='Y' AND \n" +
                "edit_date < DATE_ADD(NOW(), interval -1 minute)")
        public int deleteOutMembers();
}
