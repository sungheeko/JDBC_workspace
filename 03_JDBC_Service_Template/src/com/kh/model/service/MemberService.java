package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;

import static com.kh.common.JDBCTemplate.*;


// Service : DB와 연결시키는 Connection 객체 생성 후 
//			 Dao 호출 (이때, 생성된 Connectinon객체와 Controller로 부터 전달된 값이 같이 넘겨줄거임)
//			 돌아오는 반환값 받아서 만약에 트랜잭션 처리가 필요할 경우 트랜잭션 처리도 여기서 진행
public class MemberService {
	
	public int insertMember(Member m) {

		// 처리 결과를 받을 변수
		int result = 0;
		
		// DB의 연결정보를 담는 객체
		Connection conn = null;
		
		//단, PreparedStatement은 Dao에서 생성하자!
		
		try {
			// jdbc driver 등록처리
			Class.forName("oracle.jdbc.driver.OracleDriver"); // --> 오타있다거나, ojdbc.jar 파일 추가 안되어있을 경우
			// Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			// Dao클래스 호출 --> conn과 m 객체 전달!
			result = new MemberDao().insertMember(conn, m);
			
			// 트랜잭션 처리 (commit, rollback)
			if(result > 0) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			// nullpointException 오류가 날수있음
			// 조건처리하기!
			// conn이 null이 아니고 conn이 닫혀있지 않을경우
			try {
				if(conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		*/
		
		Connection conn = getConnection(); 
		// JDBCTemplate클래스를 생략하고싶다면 내가 직접 import에 미리 선언해두면 알아서 바로 호출가능!!
		// "import static com.kh.common.JDBCTemplate.*;"
		
		int result = new MemberDao().insertMember(conn, m);
		
		// 트랜잭션(JDBCTemplate 생성한 메소드 전달받기)
		if(result > 0) {
			 commit(conn);
		}else {
			rollback(conn);
		}
		
		// finally 자원반납
		close(conn);
		
		return result;
	}

	
	public ArrayList<Member> selectList() {
		
		// JDBCTemplate에서 만든 Connection객체 전달받음 
		Connection conn = getConnection();
			
		ArrayList<Member> list = new MemberDao().selectList(conn);
		
		// 트랜잭션 처리
		close(conn);
		
		return list;
		
	}
	
	public Member selectByUserId(String userId) {
		
		// 커넥션객체 생성
		Connection conn = getConnection();
		
		Member m = new MemberDao().selectByUserId(conn, userId); // --> PrepareStatement의 conn객체 전달
		
		// finally 자원반납
		close(conn);
		
		return m;
	}
	
	public ArrayList<Member> selectByUserName(String keyword) {
		
		Connection conn = getConnection();
		
		ArrayList<Member> list = new MemberDao().selectByUserName(conn, keyword);
		
		close(conn);
		
		return list;
	}
	
	public int updateMember(Member m) {
		
		Connection conn = getConnection();
		
		int result = new MemberDao().updateMember(conn, m);
		
		if(result > 0) {
			commit(conn);
		}else
			rollback(conn);
		
		close(conn);
		return result;
	}
	
	public int deleteMember(String userName) {
		
		Connection conn = getConnection();
		int result = new MemberDao().deleteMember(conn, userName);
		
		close(conn);
		
		return result;
		
	}
	
	public Member loginMember(String userId, String userPwd) {
		
		Connection conn = getConnection();
		Member m = new MemberDao().loginMember(conn, userId, userPwd);
		
		// finally 자원반납
		close(conn);
		
		return m;
		
	}
}

