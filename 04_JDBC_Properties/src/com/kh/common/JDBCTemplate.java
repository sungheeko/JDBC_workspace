package com.kh.common;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	
	// 이 클래스에서의 모든 메소드들 다 static으로 작업할 것
	// SingleTon Pattern : 메모리영역에 단 한번 올라간거 재사용하는 개념

	
	
	// Connection 객체 생성해서 반환시켜주는 메소드
	// 즉, DB와 연결 시켜주는 메소드
	public static Connection getConnection() {
		
		Properties prop = new Properties();
		
		try {
			
			// resources파일에 있는 key값 value값
			// 매번호출하기보단 기본생성자로 바로 호출하기
			prop.load(new FileReader("resources/driver.properties"));
		
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Connection conn = null;
		
		try {
			Class.forName(prop.getProperty("driver"));
			conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	
	
	// 트랜잭션 처리해주는 메소드
	// commit
	public static void commit(Connection conn) { // 전달받은 Connection 객체 가지고 Commit 시켜주는 메소드
		
		try {
			if(conn != null && !conn.isClosed()) {
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// rollback
	public static void rollback(Connection conn) { // 전달받은 Connection 객체를 가지고 rollback 시켜주느 메소드
		
		try {
			if(conn != null && !conn.isClosed()) {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// 전달받은 JDBC용 객체 자원반납시켜주는 메소드들
	public static void close(Connection conn) { // 전달받은 Connection 객체 반납시켜주는 메소드
		
		try {
			if(conn != null & !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	//  Statement, PreparedStatement 처리해주는 메소드들 (PreparedStaement는 Statement의 자식클래스)
	public static void close(Statement stmt) { // 전달받은 Statement, PreparedStatement 객체 자원반납시켜주는 메소드
		
		try {
			if(stmt != null && !stmt.isClosed()) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	// ResultSet 처리해주는 메소드
	public static void close(ResultSet rset) { // ResultSet 객체 전달받아서 지원반납시켜주는 메소드
		
		try {
			if(rset != null && !rset.isClosed()) {
				rset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
