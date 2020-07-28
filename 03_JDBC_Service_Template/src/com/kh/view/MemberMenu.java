package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.vo.Member;

public class MemberMenu {
	
	private Scanner sc = new Scanner(System.in);
	private MemberController mc = new MemberController();
	
	public void mainMenu() {
		
		int menu; // --> 바로 선택하게끔 제일먼저 선언
		while(true) {
			
			System.out.println("\n==== 회원 관리 프로그램 =====");
			System.out.println("1. 회원가입");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디로 검색");
			System.out.println("4. 회원 이름(키워드)으로 검색");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원탈퇴");
			System.out.println("7. 로그인하기");
			System.out.println("0. 프로그램 종료");
			System.out.print("메뉴 선택 : ");
			menu = sc.nextInt(); //--> 사용자가 입력한 값으로 바로 초기화
			sc.nextLine();
			
			switch(menu) {
			case 1: insertMember(); break;
			case 2: mc.selectList(); break;
			case 3: String userId = inputMemberId();
					mc.selectByUserId(userId);
					break;
			case 4: mc.selectByUserName(inputMemberName()); break;
			case 5: updateMember(); break;
			case 6: mc.deleteMember(inputMemberId()); break;
			case 7: loginMember(); break;
			case 0: System.out.print("정말로 끝내겠습니까? (y/n) : "); 
					if(sc.nextLine().toUpperCase().charAt(0) == 'Y') {
						System.out.println("프로그램을 종료합니다.");
						return; //--> while반복문은 빠져나감
					}else {
						break; //--> switch문을 빠져나감
					}
			default: System.out.println("번호를 잘못입력했습니다. 다시입력해주세요.");
			
			}
			
		}
		
	}
	
	// 사용자에게 값을 입력받는 화면들
	// 1. 회원가입 화면 (회원의 정보들 입력받는 화면)
	public void insertMember() {
		
		System.out.println("\n==== 회원 가입 =====");
		System.out.print("아이디 : ");
		String userId = sc.nextLine();
		System.out.print("비밀번호 : ");
		String userPwd = sc.nextLine();
		System.out.print("이름 : ");
		String userName = sc.nextLine();
		System.out.print("성별(M/F) : ");
		String gender = sc.nextLine().toUpperCase(); // toUppercase() : 모두 다 대문자로 바꿔주는 메소드
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
		
		// 회원가입 요청 (사용자가 입력한 값들 주섬주섬 담은 Member객체 전달하면서!)
		mc.insertMember(new Member(userId, userPwd, userName, gender, age, email, phone, address, hobby)); //--> 매개변수생성과 동시에 전달하겠다
		
		
	}
	
	
	// 2. 회원 아이디 입력받는 화면 (회원 아이디로검색, 회원정보수정, 회원탈퇴)
	public String inputMemberId() {
		System.out.print("\n회원 아이디 입력 : ");
		return sc.nextLine(); // --> 입력받고 바로 리턴
	
	}
	
	// 3. 회원 이름 입력받는 화면
	public String inputMemberName() {
		System.out.print("\n검색할 회원이름(키워드) 입력 : ");
		return sc.nextLine();
		
	}
	
	// 4. 회원 정보 수정하는 화면 (변경하고자하는 회원 아이디, 변경할 비번, 변경할 이메일, 변경할 전화번호, 변경할 주소)
	public void updateMember() {
		
		System.out.println("\n==== 회원 정보 변경 =====");
		
		Member m = new Member(); // m에 사용자가 변경하고자 하는 값을 담을 객체
		m.setUserId(inputMemberId()); //--> 위에 있는 메소드 재사용
		
		System.out.print("변경할 비밀번호 : ");
		m.setUserPwd(sc.nextLine());
		System.out.print("변경할 이메일 : ");
		m.setEmail(sc.nextLine());
		System.out.print("변경할 전화번호(-빼고 입력) : ");
		m.setPhone(sc.nextLine());
		System.out.print("변경할 주소 : ");
		m.setAddress(sc.nextLine());
		
		//회원 정보 변경 요청!!
		mc.updateMember(m);
	}
	
	
	
	// "7번 메뉴 로그인하기" 만든후
	public void loginMember() {
		
		System.out.println("\n==== 로그인하기 ====");
		System.out.print("아이디 : ");
		String userId = sc.nextLine();
		System.out.print("비밀번호 : ");
		String userPwd = sc.nextLine();
		
		mc.loginMember(userId, userPwd);
		
	}
	// 사용자에게 아이디, 비밀번호 입력받아서
	// 해당 아이디와 비밀번호가 일치하는 회원 조회 후
	// 해당 조회된 회원 정보 출력하는 기능 만들어보기!!
	
	
	//-------------------------------------------------------------------------
	
	// 사용자가 서비스 요청 후 보게되는 응답화면들(성공,실패,조회성공,조회실패)
	// 1. 서비스 요청 성공했을 때의 응답화면
	public void displaySuccess(String message) {
		System.out.println("\n서비스 요청 성공 : " + message);
		
	}
	
	// 2. 서비스 요청 실패했을 때의 응답화면
	public void displayFail(String message) {
		System.out.println("\n서비스 요청 실패 : " + message);
	
	}
	
	// 3. 조회 요청시 조회결과가 없을 경우의 응답화면
	public void displayNoData(String message) {
		System.out.println("\n" + message);
	
	}
	
	// 4. 조회 요청시 한행만 조회되었을 때 응답화면
	public void displayMember(Member m) {
		System.out.println("\n조회된 회원 정보는 다음과 같습니다.");
		System.out.println(m);
		
	}
	
	// 5. 조회 요청시 여러행 조회되었을 때 응답화면
	public void displayMemberList(ArrayList<Member> list) {
		System.out.println("\n조회된 회원 정보는 다음과 같습니다.");
		
		for(Member m : list) { // for-each문
			System.out.println(m);
		}
	}
	

	

	
	

}
