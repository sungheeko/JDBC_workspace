package com.kh.model.dao;

import static com.kh.common.JDBCTemplate.close;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.model.vo.Member;

// Dao에서는 오로지 SQL문 실행 업무만 집중적으로 하는곳!
public class MemberDao {
	
	/*
	 * 기존의 방식 : Dao 클래스에 사용자가 요청할때 마다 실행해야되는 sql문을 소스코드 내에 작성함(명시적으로 기술함) => 정적코딩방식
	 * 	    문제점 : SQL구문 수정이 필요한 경우 소스코드를 수정하는셈임 => 수정된 내용을 반용하고자 한다면 프로그램을 재구동 해야된다는 문제!!
	 * 			 => 유지보스에 불편하다.
	 * 
	 * 해결 방식 : SQL문들을 별도로 관리하는 외부파일 (.properties)로 만들어서 실시간으로 동적으로 SQL문을 읽어오게 할꺼임 => 동적코딩방식
	 * 
	 */
	
	
	// Properties객체 생성(어느 전역이든 사용가능)
	private Properties prop = new Properties();
	
	
	
	// 기본생성자
	public MemberDao() {
		
		// 사용자가 어떤 서비스 요청할 때 마다 매번 new MemberDao().xxx(); 호출함
		// 즉, 다음과 같이 기본생성자를 매번 호출함!!
		// 따라서 query.properties파일에 기록된 값을 매번 실시간으로 prop 읽어들일 수 있는 구문을 여기서 기술할거임!!
		try {
			prop.load(new FileReader("resources/query.properties"));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	// Service에서 전달받은 conn과 m 객체
	public int insertMember(Connection conn, Member m) { // insert문 
		
		// 처리된 결과를 받아줄 변수 선언 (처리된 행의 갯수)
		int result = 0;
		
		// SQL문 실행시 필요한 객체 [Prepared]Statement 객체 선언
		PreparedStatement pstmt = null;
		
		// 실행한 sql문(미완성된 형태여도 괜찮다 --> PreparedStatement이니까)
		//String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, ?,?,?,?,?,?,?,?,?, SYSDATE)";
		
		String sql = prop.getProperty("insertMember"); //--> key값을 제시하면 value값이 돌아옴
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			
			// 위에 구문으로 인해 완성된sql문이 되버림
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				
				// nullpointException 오류가 날수있음
				// 먼저 조건처리하기!
				//pstmt가 null이 아니고 pstmt닫혀있지않을경우
				if(pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				close(pstmt);
			}
		}
		
		return result;
	}

	public ArrayList<Member> selectList(Connection conn) { // select문 --> 여러행 조회
		
		ArrayList<Member> list = new ArrayList<>();
		
		Statement stmt = null;
		ResultSet rset = null;
		
		//String sql = "SELECT * FROM MEMBER ORDER BY ENROLLDATE ASC"; --> 최신날짜로 정렬
		String sql = prop.getProperty("selectList");
		
		
		try {
			stmt = conn.createStatement();
			
			rset = stmt.executeQuery(sql);
			
			while(rset.next()) {
				list.add(new Member(rset.getInt("userno"),
									rset.getString("userid"),
									rset.getString("userpwd"),
									rset.getString("username"),
									rset.getString("GENDER"),
									rset.getInt("age"),
									rset.getString("EMAIL"),
									rset.getString("PHONE"),
									rset.getString("address"),
									rset.getString("hobby"),
									rset.getDate("enrolldate")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(stmt);
		}
		
		return list;

	}
	
	public Member selectByUserId(Connection conn, String userId) { // select문 --> 1행조회
		
		Member m = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		//String sql = "SELECT * FROM MEMBER WHERE USERID = ?";
		String sql = prop.getProperty("selectByUserId");
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				m = new Member(rset.getInt("USERNO"),
							   rset.getString("USERID"),
							   rset.getString("USERPWD"),
							   rset.getString("USERNAME"),
							   rset.getString("GENDER"),
							   rset.getInt("AGE"),
							   rset.getString("EMAIL"),
							   rset.getString("PHONE"),
							   rset.getString("ADDRESS"),
							   rset.getString("HOBBY"),
							   rset.getDate("ENROLLDATE"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return m;
		
	}
	
	public ArrayList<Member> selectByUserName(Connection conn, String keyword) {
		
		ArrayList<Member> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		//String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		String sql = prop.getProperty("selectByUserName");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, keyword);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				list.add(new Member(rset.getInt("USERNO"),
								   rset.getString("USERID"),
								   rset.getString("USERPWD"),
								   rset.getString("USERNAME"),
								   rset.getString("GENDER"),
								   rset.getInt("AGE"),
								   rset.getString("EMAIL"),
								   rset.getString("PHONE"),
								   rset.getString("ADDRESS"),
								   rset.getString("HOBBY"),
								   rset.getDate("ENROLLDATE")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}
	
	
	public int updateMember(Connection conn, Member m) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		
		//String sql = "UPDATE MEMBER SET USERPWD = ?, EMAIL = ?, PHONE = ?, ADDRESS = ? WHERE USERID = ?";
		String sql = prop.getProperty("updateMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	public int deleteMember(Connection conn, String userName) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		
		//String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		String sql = prop.getProperty("deleteMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
				
	}
	
	public Member loginMember(Connection conn, String userId, String userPwd) {
		
		Member m = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		//String sql = "SELECT * FROM MEMBER WHERE USERID = ? AND USERPWD = ?";
		String sql = prop.getProperty("loginMember");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				m = new Member(rset.getInt("USERNO"),
							   rset.getString("USERID"),
							   rset.getString("USERPWD"),
							   rset.getString("USERNAME"),
							   rset.getString("GENDER"),
							   rset.getInt("AGE"),
							   rset.getString("EMAIL"),
							   rset.getString("PHONE"),
							   rset.getString("ADDRESS"),
							   rset.getString("HOBBY"),
							   rset.getDate("ENROLLDATE"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return m;
	
	}
	
	
	/*
	 * 회원탈퇴 정책이 바뀌었다.
	 * 회원탈퇴 요청시 Member 테이블로부터 아예 삭제시키는게 아닌
	 * DEL_FLAG 컬럼값을 기존에 'N'에서 'Y'로 변경하기로 한다.
	 * 
	 * 또, 회원 조회 서비스시 탈퇴한 회원들을 제외하고 조회하게
	 * 로그인기능 또한 탈퇴한 회원은 조회가 안되게끔
	 * 
	 */
}
