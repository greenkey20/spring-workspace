package com.kh.spring.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
//import lombok.*; // * = 패키지 안에 있는 모든 것/전부를 읽어옴 = 시간 더 많이 걸림 -> 이렇게 써도 되지만(특히 Lombok은 기능이 아주 많지는 않음), 방대한 library의 경우 모든 기능을 사용할 일은 별로 없기 때문에, 내가 사용할 것만 가져오는 게 좋음

// 2022.2.14(월) 17h40 vo 클래스 생성

// 2022.2.17(목) 17h45 Lombok 플러그인 설치 + sts 재구동 -> 아래 annotations 추가 -> 이 클래스 상에는 보이지 않지만, package explorer상 src/main/java > com.kh.spring.member.model.vo에 생성자, setter, getter, toString 등 Lombok이 만들어준 것 모두 보임

/* 2022.2.18(금) 9h25 Lombok = 자동 코드 생성 library -> 반복되는 getter, setter, toString 등의 메소드 작성 코드를 줄여주는, 'code diet' library
 * Lombok 설치 방법
 * 1. library 다운로드 후 Maven pom.xml에 적용 -> 나의 질문 = pom.xml 파일을 Maven이 만드는? 거였나? >.<
 * 2. 다운로드된 jar 파일을 찾고, 작업할 ide를 선택해서 설치
 * 3. ide 재실행
 * 
 * Lombok 사용 시 주의 사항 = 필드명을 uName, bTitle 등 소문자 외자로 시작하게 지으면 안 됨 -> 적어도 2글자의 소문자로 시작해야 함 <- 나의 질문 = 현재 userName처럼 쓴 것과 차이가 무엇인가요?
 * el 구문 ${ loginUser.uName }의 동작 원리 = getter getuName()를 통한 호출 vs Lombok에 의해 생성된 getter는 getUName() -> el 구문에서 호출할 때와 vo 클래스에 만들어진 getter 사이에 호환 잘 안 됨 vs Lombok 안 쓰고 내가 g/setter 만드는 경우라면 el 구문에서 잘 읽히도록 내가 만들면 됨
 * 
 * 9h40 내가 필기 다 했는지, 확인 필요..
 * 
 * 변수명, 메소드명 딱 보면 어떤/뭐 하는 것인지 알 수 있도록 잘 짓기 -> clean code -> refactoring
 */

// 나의 질문 = 그럼 이 메소드들은 어디에 존재하는 것인가? >.< -> 나의 생각 = jvm이 자동으로 만드는 기본 생성자가 눈에 안 보이는 것과 같은 걸까?
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Member {
	
	private String userId; // USER_ID	VARCHAR2(30 BYTE)	No		1	회원아이디
	private String userPwd; // USER_PWD	VARCHAR2(100 BYTE)	No		2	회원비밀번호
	private String userName; // USER_NAME	VARCHAR2(15 BYTE)	No		3	회원이름
	private String email; // EMAIL	VARCHAR2(100 BYTE)	Yes		4	회원이메일
	private String gender; // GENDER	VARCHAR2(1 BYTE)	Yes		5	회원성별
	private String age; // AGE	NUMBER	Yes		6	회원나이 -> 2022.2.17(목) 17h20 회원 가입 시 필수가 아닌 사항들은 입력 안 하고 '회원 가입' 버튼 눌렀을 때, 빈 문자열 ""을 int 자료형 setAge()에 넣으려고 해서 400 error 발생한 바, 이를 해결하기 위해 자료형을 String으로 수정
	private String phone; // PHONE	VARCHAR2(13 BYTE)	Yes		7	회원전화번호
	private String address; // ADDRESS	VARCHAR2(100 BYTE)	Yes		8	회원주소
	private Date enrollDate; // ENROLL_DATE	DATE	Yes	SYSDATE	9	회원가입날짜
	private Date modifyDate; // MODIFY_DATE	DATE	Yes	SYSDATE	10	회원수정날짜
	private String status; // STATUS	VARCHAR2(1 BYTE)	Yes	'Y' 	11	회원상태값
	
	// 필드 추가, 자료형 변경 등의 경우에도 Lombok이 관련 메소드들 다 생성/변경해줌
//	private Date birthday;
//	private double height;
	
	// 2022.2.17(목) 17h30 필드만 남기고 생성자 등 모두 삭제 -> 기본 생성자도, setter도 없기 때문에, 로그인도 안 될 것임 -> Lombok 플러그인 설치해서 annotations 추가
	/*
	public Member() {
		super();
	}

	public Member(String userId, String userPwd, String userName, String email, String gender, String age, String phone,
			String address, Date enrollDate, Date modifyDate, String status) {
		super();
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.email = email;
		this.gender = gender;
		this.age = age;
		this.phone = phone;
		this.address = address;
		this.enrollDate = enrollDate;
		this.modifyDate = modifyDate;
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getEnrollDate() {
		return enrollDate;
	}

	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Member [userId=" + userId + ", userPwd=" + userPwd + ", userName=" + userName + ", email=" + email
				+ ", gender=" + gender + ", age=" + age + ", phone=" + phone + ", address=" + address + ", enrollDate="
				+ enrollDate + ", modifyDate=" + modifyDate + ", status=" + status + "]";
	}
	*/

}
