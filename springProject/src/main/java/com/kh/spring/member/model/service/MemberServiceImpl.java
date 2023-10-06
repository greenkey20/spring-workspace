package com.kh.spring.member.model.service;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Random;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
// 2022.2.17(목) 10h10
import org.springframework.stereotype.Service; // 이 library import하지 않으면, 'Service cannot be resolved to a type' 빨간줄

import com.kh.spring.entity.CertVo;
import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

// @Component = bean으로 등록 vs @Service = component보다 더 구체화시켜서 Service bean으로 등록
@Service
public class MemberServiceImpl implements MemberService { // 2022.2.17(목) 11h40 interface를 implements(o) extends(x)
	
	// 2022.2.17(목) 12h10
//	private MemberDao memberDao = new MemberDao(); // 앞으로 이렇게 new로 객체 생성 내가 직접 안 할 것임  vs Spring 의존성 주입 -> 그러려면 MemberDao 클래스도 bean으로 등록되어 있어야 함 -> dao 클래스 가서 annotation 붙이고 옴
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private SqlSessionTemplate sqlSession; // MyBatis에서 제공하는 SqlSessionTemplate = 기존의 MyBatis의 sqlSession을 대체함(그저께 root-context.xml에서 설명해 주셨다고 함)
	// SqlSession은 내가 만든 것 아님 = 클래스(o) Java(x) 파일로 옴 -> annotation 붙일 수 없는 바, 2022.2.15(화) root-context.xml에 bean 등록해 두었었음
	
	@Override
	public Member loginMember(Member m) {
//		SqlSession sqlSession = new SqlSession(); // 앞으로 이렇게 new로 객체 생성 내가 직접 안 할 것임 vs Spring 의존성 주입
		
//		Member loginUser = memberDao.loginMember(sqlSession, m);
		
		// SqlSessionTemplate 객체 bean 등록 + @Autowired -> Spring이 사용 후 자동으로/알아서 객체를 반납시켜줌 -> close() 메소드로 자원 반납할 필요 없음
		
		return memberDao.loginMember(sqlSession, m);
	}

	// 2022.2.18(금) 11h25
	@Override
	public int insertMember(Member m) {
//		int result = memberDao.insertMember(sqlSession, m);
		
		// 반환 int 값 보고 SqlSessionTemplate 객체가 알아서/자동 commit을 해줌
		
		return memberDao.insertMember(sqlSession, m);
	}

	// 2022.2.18(금) 15h40
	@Override
	public int updateMember(Member m) {
		return memberDao.updateMember(sqlSession, m);
	}

	// 2022.2.18(금) 17h10 실습 + 17h35 강사님 설명
	@Override
	public int deleteMember(String userId) {
		return memberDao.deleteMember(sqlSession, userId);
	}

	// 2022.2.23(수) 12h15
	@Override
	public int idCheck(String checkId) {
		return memberDao.idCheck(sqlSession, checkId);
	}

	// 2022.3.15(화) 12h5 인증 이메일 발송을 위해 추가
	// 인증 번호 생성 -> 2022.3.15(화) 이 메소드는 MemberService interface에 만들지 않고, 여기에 바로 구현한 특별한 이유가 있을까?
	public String generateSecret() {
		Random r = new Random();
		int n = r.nextInt(100000); // 12h10 이 메소드 정확히 이해 안 됨 ㅠ.ㅠ  vs 2022.3.15(화) 21h40 Java 정석 p.501 0~n 범위(n은 미 포함)에 있는 int 값 반환 -> 0~099999  범위의 난수 반환
		Format f = new DecimalFormat("000000"); // 예를 들어 425와 같이 생성된 랜덤 숫자 앞 빈 자리에 0 채워줌 -> 2022.3.15(화) 21h45 Java 정석 p.540 숫자의 형식화(formatting)에 사용되는 형식화 클래스 
		String secret = f.format(n);
		
		return secret;
	}
	
	@Override
	public String sendMail(String ip) {
		String secret = this.generateSecret(); // '나의' generateSecret() 호출 -> 나의 질문 = this 없으면 왜 안 되지? this 없어도 컴파일 오류 빨간줄은 안 뜨는 것 같은데, 아닌가?
		
//		memberDao.insertSecret(sqlSession, ip, secret); // 1개의 객체?로(MyBatis에게? 강사님 설명 제대로 못 들음) 넘겨야 하므로, 이렇게 보낼 수는 없음 vs ip 및 secret과 같은 정보를 담을 수 있는 CertVo 객체에 담아서 보냄
		CertVo certVo = CertVo.builder()
							.who(ip)
							.secret(secret)
							.build();
		memberDao.insertSecret(sqlSession, certVo); // insert문 실행 결과는 특별히 필요 없을 것 같아(? 강사님 설명 제대로 못 들음) 따로 반환받지 않음 vs 처리 결과를 controller에 전달하려면, 내가 알아서 작성해보기
		
		return secret;
	}

	// 2022.3.15(화) 12h35
	@Override
	public boolean validate(CertVo certVo) {
		return memberDao.validate(sqlSession, certVo);
	}
	
}
