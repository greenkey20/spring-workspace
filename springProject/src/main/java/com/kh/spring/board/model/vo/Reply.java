package com.kh.spring.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 2022.2.23(수) 16h50 REPLY 테이블 참고해서 vo 객체 만들기
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Reply {
	
	private int replyNo; // REPLY_NO	NUMBER	No		1	댓글번호
	private String replyContent; // REPLY_CONTENT	VARCHAR2(400 BYTE)	No		2	댓글내용
	private int refBoardNo; // REF_BNO	NUMBER	No		3	참조게시글번호
	private String replyWriter; // REPLY_WRITER	VARCHAR2(30 BYTE)	No		4	댓글작성자아이디
	private String createDate; // CREATE_DATE	DATE	No	SYSDATE 	5	댓글작성날짜
	private String status; // STATUS	VARCHAR2(1 BYTE)	Yes	'Y' 	6	댓글상태값

}
