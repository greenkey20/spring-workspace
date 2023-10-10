package com.kh.spring.board.model.service;

import java.util.ArrayList;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

// 2022.2.21(월) 10h15
// interface = 강제성이 짙음 -> 이 interface를 implements하는 클래스는 아래 메소드들을 반드시 구현해야 함
public interface BoardService {
	
	// 게시판 list 조회 + paging 처리
	int selectListCount(); // PageInfo 객체를 만드려면 listCount(게시글의 총 개수; 실시간으로 변함)가 필요함 -> 게시글의 총 개수 조회
	ArrayList<Board> selectList(PageInfo pi); // paging 처리하기로 했기 때문에, 게시글 목록 조회 시 PageInfo 객체를 매개변수로 넘겨야 함
	
	// 게시글 작성
	int insertBoard(Board b); // 게시글 작성 시 사용자가 입력한 게시글 관련 여러 항목을 넘겨야 하는데, 나는 게시글 형태의 Board 클래스를 가지고 있으므로, Board 객체로 넘길 수 있음
//	int insertAttachmentList(ArrayList<Attachment> aList); // 금번 서비스에서는  게시글당 첨부파일 1개만 업로드할 수 있음 + 첨부파일 테이블 별도로 없고, BOARD 테이블의 컬럼으로
	
	// 게시글 상세 조회 <- 게시글 조회 수 증가
	int increaseCount(int boardNo); // 아래 SELECT문에서 update를 할 수 없기 때문에, 조회 수 값을 변경/update할 별도의 메소드가 필요함
	Board selectBoard(int boardNo);
	
	// 게시글 삭제
	int deleteBoard(int boardNo);
	
	// 게시글 수정
	int updateBoard(Board b);
//	int updateAttachmentList(int boardNo);
	
	// 댓글 list 조회(AJAX)
	ArrayList<Reply> selectReplyList(int boardNo);
	
	// 댓글 작성(AJAX)
	int insertReply(Reply r);
	
	// 2022.2.24(목) 11h45 추가
	// Top-N 분석(AJAX)
	ArrayList<Board> selectTopBoardList();

}
