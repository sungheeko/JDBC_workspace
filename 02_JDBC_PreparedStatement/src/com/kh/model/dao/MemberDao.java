package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;

public class MemberDao {
	
	/*
	 * 	* Statement와 PreParedStatement의 차이점
	 * 	- Statement 같은 경우 완성된 sql문을 바로 실행하는 객체 (sql문을 완성형태로 만들어줘야됨!)
	 * 	- PreparedStatement 같은 경우 미완성된 sql문을 잠시 보관해둘 수 있는 객체
	 * 	   해당 sql문 실행하기 전 완성형태로 만든 후 실행하면됨
	 * 
	 * 	* 기존의 Statement 방식
	 *  Connection 객체를 통해 Statement 객체 생성 	: stmt = conn.createStatement();
	 *  Statement 객체를 통해 완성된 sql문 실행 및 결과 	:  결과 = stmt.executeXXX(완성된 sql);
	 *  
	 *  * PreparedStatement 방식
	 *  Connection 객체를 통해 PreparedStatement 객체 생성
	 *  			(단, 미완성된 sql문을 담은채로 생성)		 : pstmt = conn.prepareStatement(미완성된 sql)
	 *  PreparedStatement 에서 제공하는 메소드를 통해 완성형태로 : pstmt.setXXX(1, "대체할값"); ...
	 *  그리고 나서 해당 완성된 sql문 실행 및 결과				 :  결과 = pstmt.executeXXX();
	 * 
	 */
	
	public int insertMember(Member m) { // insert문		--> 처리된 행의 갯수(int)
		
		int result = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		// 실행하고자 하는 sql문 미완성된 형태로!!	 --> 미리 사용자가 입력한 값들이 들어갈 수 있게끔 공간('?')만 확보
		String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver"); // --> 새로운 자바프로젝트를 만들때마다 ojdbc6 클래스를 추가 해줘야됨!!
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql); //--> prepareStatement 객체 생성시 미완성된 sql문을 담은채로 생성!!
			
			// 현재 담긴 sql문 미완성된 상태이기 때문에 바로 실행은 불가함 --> 각각의 '?'공간에 실제 값을 대체한 후 실행!
			pstmt.setString(1, m.getUserId()); // --> setString으로 대체 시 'user01' (양옆에 홑따옴표 붙어서 들어감)
			// --> setString : 문자열을 담을때
			
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4,  m.getGender());
			pstmt.setInt(5, m.getAge()); // --> setInt 대체시 20 (홑따옴표 안붙어서 들어감)
			// --> setInt() : 숫자값을 담을때 
			
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			
			result = pstmt.executeUpdate(); //--> 미완성된 sql문을 담으면 안됨

			if(result > 0) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
		
	}

	
	public ArrayList<Member> selectList() { // select 전체조회
		
		// 변수셋팅
		ArrayList<Member> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// 미완성된 sql문
		String sql = "SELECT * FROM MEMBER";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
		
			pstmt = conn.prepareStatement(sql);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				list.add(new Member(rset.getInt("userno"),
									rset.getString("userid"),
									rset.getString("userpwd"),
									rset.getString("username"),
									rset.getString("GENDER"),
									rset.getInt("age"),
									rset.getString("email"),
									rset.getString("phone"),
									rset.getString("address"),
									rset.getString("hobby"),
									rset.getDate("enrolldate")));
				
			}
		
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	
	public Member selectUserId(String userId) { // select조회 --> 1행조회
		
		Member m = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM MEMBER WHERE USERID = ?";
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				
				m = new Member(rset.getInt("USERNO"),
							   rset.getString("userid"),
							   rset.getString("userpwd"),
							   rset.getString("username"),
							   rset.getString("GENDER"),
							   rset.getInt("age"),
							   rset.getString("email"),
							   rset.getString("phone"),
							   rset.getString("address"),
							   rset.getString("hobby"),
							   rset.getDate("enrolldate"));
				
			}
			
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return m;
		
	}
	
	public ArrayList<Member> selectUserName(String keyword) {
		
		ArrayList<Member> list = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		//해결방법 1.
		//String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		
		// 해결방법2.
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%' || ? ||  '%'";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			
			// 해결방법1의 sql문일 경우
			// pstmt.setString(1, "%" + keyword + "%");
			
			// 해결방법2의 sql문일 경우
			pstmt.setString(1, keyword);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				
				list.add(new Member(rset.getInt("userno"),
									rset.getString("userid"),
									rset.getString("userpwd"),
									rset.getString("username"),
									rset.getString("GENDER"),
									rset.getInt("age"),
									rset.getString("email"),
									rset.getString("phone"),
									rset.getString("address"),
									rset.getString("hobby"),
									rset.getDate("enrolldate")));
			}
			
			
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return list;
	}
	
	
	public int updateMember(Member m) {
		
		int result = 0;;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "UPDATE MEMBER SET USERPWD = ?, EMAIL = ?, PHONE = ?, ADDRESS = ? WHERE USERID = ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());
			
			result = pstmt.executeUpdate();
			
			if(result > 0) {
				conn.commit();
			}else
				conn.rollback();
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				
		return result;
	}
	
	public int deleteMember(String userId) {
		
		int  result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			
			if(result > 0) {
				conn.commit();
			}else{
				conn.rollback();
			}
			
			result = pstmt.executeUpdate();
					
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			
			try {
				pstmt.close();
				conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public Member loginMember(String userId, String userPwd) { // select조회 --> 1행조회
		
		Member m = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = "SELECT * FROM MEMBER WHERE USERID = ? AND USERPWD = ?";
	
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				m = new Member(rset.getInt("userno"),
							   rset.getString("userid"),
							   rset.getString("userpwd"),
							   rset.getString("username"),
							   rset.getString("gender"),
							   rset.getInt("age"),
							   rset.getString("email"),
							   rset.getString("phone"),
							   rset.getString("address"),
							   rset.getString("hobby"),
							   rset.getDate("enrolldate"));
				
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return m; //--> 조회가 안되었다면 m = null;
	}
}










