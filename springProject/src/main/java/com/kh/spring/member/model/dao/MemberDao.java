package com.kh.spring.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
//2022.2.17(목) 10h15
import org.springframework.stereotype.Repository;

import com.kh.spring.entity.CertVo;
import com.kh.spring.member.model.vo.Member;

// repository = 저장소 = db -> db 관련 작업/'영속성' 작업 처리
@Repository
public class MemberDao {
	
	// 2022.2.17(목) 12h_
	public Member loginMember(SqlSessionTemplate sqlSession, Member m) {
		// 1행의 조회 결과를 받고자 함 -> selectOne() 메소드 사용
		return sqlSession.selectOne("memberMapper.loginMember", m); // 조회 결과가 없으면 null로 반환됨
	}
	
	// 2022.2.18(금) 11h25
	public int insertMember(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.insert("memberMapper.insertMember", m);
	}
	
	// 2022.2.18(금) 15h40
	public int updateMember(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.update("memberMapper.updateMember", m); // 결과 값 = 1 또는 0
	}

	// 2022.2.18(금) 17h10 실습 + 17h35 강사님 설명
	public int deleteMember(SqlSessionTemplate sqlSession, String userId) {
		return sqlSession.update("memberMapper.deleteMember", userId);
	}
	
	// 2022.2.23(수) 12h20
	public int idCheck(SqlSessionTemplate sqlSession, String checkId) {
		return sqlSession.selectOne("memberMapper.idCheck", checkId);
	}
	
	// 2022.3.15(화) 12h15 인증 이메일 발송을 위해 추가 -> 인증 요청자, 인증 번호, 인증 요청 시각을 db 관련 테이블에 저장/insert
	public void insertSecret(SqlSessionTemplate sqlSession, CertVo certVo) {
		sqlSession.insert("memberMapper.register", certVo); // db에 data insert만 하고, 반환 결과는 따로 service로 반환하지 않기로 함
	}
	
	// 2022.3.15(화) 12h35
	public boolean validate(SqlSessionTemplate sqlSession, CertVo certVo) {
		CertVo result = sqlSession.selectOne("memberMapper.validate", certVo); // 인증 번호(secret)와 ip 가지고 가면, 조회 결과 1건
		
		// 조회 결과가 null인데 삭제하려고 하면 문제가 될 수 있는 바, 아래와 같이 조건식으로 처리
		if (result != null) { // 인증 성공한 경우, (강사님 설명 제대로 못 들음 - 해당 인증 번호/정보로 다시 인증하지 못하도록?)관련 정보를 db로부터 삭제
			sqlSession.delete("memberMapper.remove", certVo);
		}
		
		return result != null; // 조회 결과 CertVo 객체가 null이 아니면 true vs null이면 false 반환
	}
	
}
