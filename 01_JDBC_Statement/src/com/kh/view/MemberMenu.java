package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.vo.Member;

// View : 사용자가 보게될 시작적인 요소 담당(입력 및 출력)
public class MemberMenu {
	
	// Scanner 객체 생성 (전역으로 입력받을 수 있게)
	private Scanner sc = new Scanner(System.in);
	// MemberController 객체 생성(전역에서 바로 요청할 수 있게)
	private MemberController mc = new MemberController();
	
	
	/**
	 * 사용자가 보게될 첫 화면
	 */
	public void mainMenu() {
		
		while(true) {
			
			System.out.println("\n===== 회원 관리 프로그램 =====");
			System.out.println("1. 회원 가입");
			System.out.println("2. 회원 전체조회");
			System.out.println("3. 회원 아이디 검색");
			System.out.println("4. 회원 이름으로 검색");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴");
			System.out.println("0. 프로그램 종료");
			System.out.print("번호 선택 : ");
			int menu = sc.nextInt();
			sc.nextLine();
			
			switch(menu) {
			case 1: insertMember();	break; // --> 사용자에게 입력받을거 있음
			case 2:	mc.selectList(); break; // --> 바로 메소드 호출
			
			case 3:	String userId = inputMemberId(); // --> 반환받은 userId를 controller에 호출 
					mc.selectByUserId(userId); 
					break;
			
			case 4: //String userName = inputMemberName(); 
					//mc.selectByUserName(userName);
					mc.selectByUserName(inputMemberName());
					break;
			
			case 5:	updateMember();	break;
			case 6:	mc.deleteMember(inputMemberId()); break;
			case 0: System.out.println("프로그램을 종료합니다."); return; //--> 메소드자체를 빠져나감(Run메소드)
			default: System.out.println("번호를 잘못입력했습니다. 다시입력해주세요."); 
			}
		}
		
	}
	
	/**
	 * 1. 회원가입 창(화면) : 회원의 정보를 입력받는 메소드
	 */
	public void insertMember() {
		
		System.out.println("\n==== 회원 가입 =====");
		
		System.out.print("아이디 : ");
		String userId = sc.nextLine();
		System.out.print("비밀번호 : ");
		String userPwd = sc.nextLine();
		System.out.print("이름 : ");
		String userName = sc.nextLine();
		System.out.print("성별(M/F) : ");
		String gender = sc.nextLine().toUpperCase(); // toUppercase : 모두 다 대문자로 바꿔주는 메소드
		System.out.print("나이 : ");
		int age = sc.nextInt();
		sc.nextLine();
		System.out.print("이메일 : ");
		String email = sc.nextLine();
		System.out.print("전화번호(-빼고 입력) : ");
		String phone = sc.nextLine();
		System.out.print("주소 : ");
		String address = sc.nextLine();
		System.out.print("취미(,로 공백없이 나열) : ");
		String hobby = sc.nextLine();
		
		// Member 객체 생성 후 주섬주섬 담자(사용자가 입력한 값)
		Member m = new Member(userId, userPwd, userName, gender, age, email, phone, address, hobby);
		
		// 회원가입 요청 --> controller 메소드 호출
		mc.insertMember(m);
		
		
	}
	
	/**
	 * 사용자에게 조회할 회원 아이디 입력받은 후 그 아이디 반환하는 메소드
	 * 
	 * @return 	--> 사용자가 입력한 아이디 반환 
	 */
	public String inputMemberId() {
		
		System.out.print("회원 아이디 입력 : ");
		String userId = sc.nextLine();
		
		return userId; //--> switch()로 반환
		
	}
	
	/**
	 * 사용자에게 조회할 회원명(키워드)을 입력받은 후 반환하는 메소드
	 * 
	 * @return	--> 조회할 회원명(키워드)
	 */
	public String inputMemberName() {
		System.out.print("회원 이름(키워드) 입력 : ");
		return sc.nextLine(); 
	}
	
	/**
	 * 사용자에게 변경할 정보들과 해당 회원의 아이디 입력받는 화면
	 */
	public void updateMember() {
		
		Member m = new Member(); // --> 사용자가 입력한 값을 Member객체에 담기
		
		//System.out.print("아이디: ");
		//m.setUserId(sc.nextLine());
		m.setUserId(inputMemberId()); //--> 아이디는 위쪽에 있는 inputMember메소드재사용
		
		System.out.print("변경할 암호 : ");
		m.setUserPwd(sc.nextLine());
		
		System.out.print("변경할 이메일 : ");
		m.setEmail(sc.nextLine());
		
		System.out.print("변경할 전화번호(-빼고 입력) : ");
		m.setPhone(sc.nextLine());
		
		System.out.print("변경할 주소 : ");
		m.setAddress(sc.nextLine());
		
		// m : 변경하고자하는 회원아이디, 변경할암호, 변경할 이메일, 변경할 전화번호, 변경할 주소
		
		// m에 담긴 데이터 변경요청 --> MemberController 호출
		mc.updateMember(m);
		
	}
	
	/**
	 * 서비스 요청 처리후 사용자가 보게될 응답화면
	 */
	public void displaySuccess(String message) { // 요청성공시
		System.out.println("서비스 요청 성공 : " + message);
	}
	
	public void displayFail(String message) { // 요청실패시
		System.out.println("서비스 요청 실패: " + message);
	}
	
	
	public void displayNoData(String message) {
		System.out.println(message);
	}
	
	public void displayMemberList(ArrayList<Member> list) {
		System.out.println("\n조회된 데이터는 다음과 같습니다.\n");
		
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
			//--> SEQUENCE에 CASH설정을 안해서 기본값으로 20바이트로 잡음
			
		}
	}
	
	public void displayMember(Member m) {
		
		System.out.println("\n조회된 데이터는 다음과 같습니다.\n"); //--> 키워드 조회됬을경우
		System.out.println(m);
	}
	
	
}

















