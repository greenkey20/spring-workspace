package main.java.com.kh.spring.member.model.service;

import com.kh.spring.entity.CertVo;
import main.java.com.kh.spring.member.model.vo.Member;

// interface -> 추상 메소드
public interface MemberService {
	
	// 2022.2.17(목) 11h_
	// 로그인 service = SELECT문 -> 로그인된 회원 정보 받아와서 session에 담아서 사용할 것임
	Member loginMember(Member m); // 추상 메소드
	
	// 로그아웃은 요청 받으면 session 정보 삭제하면 되는 바, db 다녀올 필요 없음?(2022.2.17(목) 11h30 강사님 설명 제대로 못 들음)
	
	// 회원 가입 service = INSERT문 -> DML 구문의 결과 값 = 처리된 행의 개수(int 자료형)
	int insertMember(Member m);
	
	// 회원 정보 수정 service = 테이블에 기존에 있던 데이터를 새로운 데이터로 갱신 = UPDATE문 -> DML 구문의 결과 값 = 처리된 행의 개수(int 자료형)
	int updateMember(Member m);
	
	// 회원 탈퇴 service = 회원의 status를 'N'로 변경 = UPDATE문
	int deleteMember(String userId); // 회원 식별 가능한, 중복 없는 유일한 값으로써 이번에는 회원 번호 컬럼은 안 만들어놨고, userId에 unique 제약 조건을 걸어놨기 때문에 userId로 식별
	
	// id 중복 체크(AJAX로 구현할 것임) service = 중복된 id가 있는지 SELECT COUNT -> 해당 id가 존재한다면 1 vs 존재하지 않는 경우 0 반환
	int idCheck(String checkId);
	
	// 2022.3.15(화) 12h5 인증 이메일 발송을 위해 추가
	String sendMail(String ip);
	
	// 2022.3.15(화) 12h30
	boolean validate(CertVo certVo);
	
}
