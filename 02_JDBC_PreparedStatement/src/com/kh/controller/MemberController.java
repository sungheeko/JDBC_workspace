package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;

public class MemberController {
	
	/**
	 * 사용자가 회원 가입 요청시 실행되는 메소드
	 * @param m --> 회원가입시 입력한 회원의 정보들이 다 담겨있는 객체
	 */
	public void insertMember(Member m) {
		
		int result = new MemberDao().insertMember(m);
		
		if(result > 0) {
			new MemberMenu().displaySuccess("성공적으로 회원 가입 되었습니다.");
			
		}else {
			new MemberMenu().displayFail("회원 가입에 실패했습니다.");
			
		}
		
	}
	
	public void selectMember() {
		
		ArrayList<Member> list = new MemberDao().selectList();
		
		if(list != null) { // 조회결과가 있을경우
			new MemberMenu().displayMemberList(list);
		}else { // 조회결과가 없을경우
			new MemberMenu().displayNoData("조회된 결과가 없습니다.");
		}
		
	}
	
	public void selectUserId(String userId) {
		
		Member m = new MemberDao().selectUserId(userId);
		
		if(m != null) {
			new MemberMenu().displayMember(m);
		}else {
			new MemberMenu().displayNoData("검색 결과가 없습니다.");
		}
		
	}
	
	public void selectUserName(String keyword) {
		
		ArrayList<Member> list = new MemberDao().selectUserName(keyword);
		
		if(list.isEmpty()) {
			new MemberMenu().displayNoData(keyword + "조회된 결과가 없습니다.");
		}else {
			new MemberMenu().displayMemberList(list);
			
		}
		
	}
	
	public void updateMember(Member m) {
		
		int result = new MemberDao().updateMember(m);
		
		if(result > 0) {
			new MemberMenu().displaySuccess("회원정보가 성공적으로 변경되었습니다.");
			
		}else {
			new MemberMenu().displayFail("회원정보 변경에 실패하였습니다.");
			
		
	}
	}
	
	public void deleteMember(String userId) {
		
		int result = new MemberDao().deleteMember(userId);
		
		if(result > 0) {
			new MemberMenu().displaySuccess("성공적으로 회원 탈퇴 되었습니다.");
		}else {
			new MemberMenu().displayFail("회원 탈퇴에 실패하였습니다.");
		}
	}

	public void loginMember(String userId, String userPwd) {
		
		Member m = new MemberDao().loginMember(userId, userPwd);
		
		if(m == null) {
			new MemberMenu().displayNoData("로그인 실패");
		}else {
			new MemberMenu().displayMember(m);
		}
	}
	
}
