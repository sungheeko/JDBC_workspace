package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;

// Controller : View를 통해서 요청한 기능 처리
// 				해당 메소드로 전달된 데이터를 가공처리한 후 Dao로 전달 (Dao 메소드 호출)
//				Dao로부터 반환받은 결과에 따라 View(출력할 화면)을 결정
public class MemberController {
	
	
	/**
	 * 사용자의 회원가입 요청을 처리해주는 기능
	 * @param m --> 사용자가 입력한 정보들이 담겨있는 Member 객체
	 */
	public void insertMember(Member m) {
		
		// 사용자가 입력한 값 --> dao 메소드 호출
		int result = new MemberDao().insertMember(m); // --> dao에서 반환된 result결과값 받기
		
		// view에 출력될 메소드 호출
		if(result > 0) { // 성공했을경우 --> 성공화면
			new MemberMenu().displaySuccess("회원가입 성공했습니다."); 
			
		}else { // 실패했을경우 --> 실패화면
			new MemberMenu().displayFail("회원가입 실패했습니다.");
		}
	}
	
	
	/**
	 * 사용자의 회원 전체 조회 요청을 처리해주는 메소드
	 */
	public void selectList() {
		
		ArrayList<Member> list = new MemberDao().selectList();
		
		// 조회한 결과가 있는지 없는지 판단 한 후 사용자에게 출력화면
		if(list.isEmpty()) { // 리스트가 비어있을 경우 --> 조회결과 없음
			new MemberMenu().displayNoData("조회된 데이터가 없습니다.");
			
		}else { // 비어있지 않을경우 --> 조회결과 있음
			new MemberMenu().displayMemberList(list);
			
		}
		
	}
	
	/**
	 * 사용자의 아이디로 회원 검색 요청 처리해주는 메소드
	 * 
	 * @param userId  --> 사용자가 조회하고자 하는 회원 아이디
	 */
	public void selectByUserId(String userId) {
		
		Member m = new MemberDao().selectByUserId(userId);
		
		if(m != null) { // 조회결과가 있을경우
			new MemberMenu().displayMember(m); // --> m의 결과가 1행이기 때문에 새로운 메소드 생성
		}else { // 조회결과가 없을경우
			new MemberMenu().displayNoData("검색 결과가 없습니다."); // --> 기존에 있는 메소드 재사용
		}
		
	}

	
	/**
	 * 사용자가의 회원이름(키워드)으로 검색요청시 처리하는 메소드
	 * 
	 * @param keyword --> 사용자가 조회하고자 하는 회원이름(키워드)
	 */
	public void selectByUserName(String keyword) {
		
		ArrayList<Member> list = new MemberDao().selectByUserName(keyword);
		
		if(list.isEmpty()) { // list에 비어있을경우 --> 조회결과 없을경우
			new MemberMenu().displayNoData(keyword + "에 해당하는 검색결과 없습니다.");
			
		}else { // 그게 아닐경우 --> 조회결과 있을경우
			new MemberMenu().displayMemberList(list);
		}
		
	}
	
	/**
	 * 사용자의 정보 변경 요청을 처리해주는 메소드
	 * 
	 * @param m	--> 변경하고자하는 회원아이디, 변경할암호, 변경할 이메일, 변경할 전화번호, 변경할 주소
	 */
	public void updateMember(Member m) {
		
		int result = new MemberDao().updateMember(m);
		
		if(result > 0) { // 성공 --> displaySuccess
			new MemberMenu().displaySuccess("회원 정보 변경에 성공하였습니다.");
		}else { // 실패 --> displayFail
			new MemberMenu().displayFail("회원 정보 변경에 실패하셨습니다.");
			
		}
	}
		
		/**
		 * 사용자의 회원 탈퇴 요청을 처리해주는 메소드
		 * @param userId  --> 탈퇴요청한 해당 회원의 아이디 담김
		 * 
		 */
		public void deleteMember(String userId) {
			
			int result = new MemberDao().deleteMember(userId);
			
			if(result > 0) { // 성공
				new MemberMenu().displaySuccess("회원 탈퇴 성공");
				
			}else { // 실패
				new MemberMenu().displayFail("회원 탈퇴 실패!");
			}
		}
		
	}



