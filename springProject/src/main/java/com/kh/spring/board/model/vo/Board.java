package com.kh.spring.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 2022.2.21(월) 10h5

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Board {
	
	private int boardNo; // BOARD_NO	NUMBER	No		1	게시글번호 -> 게시글 제목이나 작성자는 중복 값이 있을 수도 있기 때문에, 게시글의 식별자로 게시글 번호 사용 + 관련해서 시퀀스 SEQ_BNO도 만들어둠
	private String boardTitle; // BOARD_TITLE	VARCHAR2(100 BYTE)	No		2	게시글제목
	private String boardWriter; // BOARD_WRITER	VARCHAR2(4000 BYTE)	No		3	게시글작성자아이디 + 로그인한 회원만 게시글 작성 가능
	private String boardContent; // BOARD_CONTENT	VARCHAR2(4000 BYTE)	No		4	게시글내용
	private String originName; // ORIGIN_NAME	VARCHAR2(100 BYTE)	Yes		5	첨부파일원래이름
	private String changeName; // CHANGE_NAME	VARCHAR2(100 BYTE)	Yes		6	첨부파일변경이름 -> 'resources/uploadFiles/내가 지정한 방식대로 변경한 파일명.jpg'로 저장해두고자 함
	private int count; // COUNT	NUMBER	Yes	0	7	게시글조회수
	private String createDate; // CREATE_DATE	DATE	Yes	SYSDATE	8	게시글작성날짜 -> 왠만하면 String으로 받는 게 편하기 때문에, 이번에는 날짜도 Date 자료형 대신 String으로 받기로 함
	private String status; // STATUS	VARCHAR2(1 BYTE)	Yes	'Y' 	9	게시글상태값

	// package explorer에서 메소드들 잘 만들어졌는지 확인 가능함
	// Spring에게 이 객체 만들어 달라고 하면 Spring은 기본 생성자 및 setter 사용해서 만듦 -> 매개변수 있는 생성자는 필요 없는 바, 안 만듦
	
}
