package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.service.MemberService;
import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;

public class MemberController {
	
	public void insertMember(Member m) {
		
		int result = new MemberService().insertMember(m);
		
		if(result > 0) { // 성공
			new MemberMenu().displaySuccess("성공적으로 회원가입 되었습니다.");
		}else { // 실패
			new MemberMenu().displayFail("회원가입에 실패했습니다.");
		}
		
	}

	public void selectList() {
		
		ArrayList<Member> list = new MemberService().selectList();
		
		if(list.isEmpty()) { // 조회결과가 없을경우
			new MemberMenu().displayNoData("조회된 결과가 없습니다.");
		}else { // 조회결과가 댬겨있을 경우
			new MemberMenu().displayMemberList(list);
		}
	}
	
	public void selectByUserId(String userId) {
		
		Member m = new MemberService().selectByUserId(userId);
		
		if(m == null) { // 조회가 안되었을 경우(일치파는 회원을 찾지 못했을 경우)
			new MemberMenu().displayNoData(userId + "에 해당되는 조회 결과 없습니다.");
		}else { // 조회가 되었을 경우
			new MemberMenu().displayMember(m);
		}
		
	}
	
	public void selectByUserName(String keyword) {
		
		ArrayList<Member> list = new MemberService().selectByUserName(keyword);
		
		if(list.isEmpty()) {
			new MemberMenu().displayNoData(keyword + "에 해당되는 조회 결과가 없습니다.");
		}else {
			new MemberMenu().displayMemberList(list);
		}
		
	}
	
	public void updateMember(Member m) {
		
		int result = new MemberService().updateMember(m);
		
		if(result > 0) {
			new MemberMenu().displaySuccess("성공적으로 회원정보가 변경되었습니다.");
		}else {
			new MemberMenu().displayFail("회원정보 변경에 실패하였습니다.");
		}
	}
	
	public void deleteMember(String userName) {
		
		int result = new MemberService().deleteMember(userName);
		
		if(result > 0) {
			new MemberMenu().displaySuccess("성공적으로 회원 탈퇴되었습니다.");
		}else {
			new MemberMenu().displayFail("회원탈퇴에 실패하였습니다.");
		}
	}
	
	public void loginMember(String userId, String userPwd) {
		
		Member m = new MemberService().loginMember(userId, userPwd);
		
		if(m == null) { // 조회가 안되었을 경우(일치파는 회원을 찾지 못했을 경우)
			new MemberMenu().displayNoData("검색된 에 조회 결과 없습니다.");
		}else { // 조회가 되었을 경우
			new MemberMenu().displayMember(m);
		}
		
	}
}
