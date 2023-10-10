package com.kh.spring.board.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

// 2022.2.21(월) 10h35

@Service // 10h35 왜 이 annotation 다는지 강사님 설명 제대로 못 들음 ㅠ.ㅠ -> bean으로 등록 -> Spring이 관리할 수 있음 e.g. 다른 클래스에서 의존성 주입 가능
public class BoardServiceImpl implements BoardService {
	
	// 변수 선언 + Spring이 사용할 수 있도록 'Autowired' annotation 달기 -> 내가(x) Spring이(o) 객체 생성
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private BoardDao boardDao;

	// 2022.2.21(월) 11h25
	@Override
	public int selectListCount() {
		return boardDao.selectListCount(sqlSession);
	}

	// 2022.2.21(월) 11h35
	@Override
	public ArrayList<Board> selectList(PageInfo pi) {
		return boardDao.selectList(sqlSession, pi);
	}

	// 2022.2.21(월) 16h15
	@Override
	public int insertBoard(Board b) {
		return boardDao.insertBoard(sqlSession, b);
	}

	// 2022.2.21(월) 17h25 실습 + 2022.2.22(화) 9h30 강사님 설명
	@Override
	public int increaseCount(int boardNo) {
		return boardDao.increaseCount(sqlSession, boardNo);
	}

	// 2022.2.21(월) 17h35 실습 + 2022.2.22(화) 9h35 강사님 설명
	@Override
	public Board selectBoard(int boardNo) {
		return boardDao.selectBoard(sqlSession, boardNo);
	}

	// 2022.2.22(화) 10h35
	@Override
	public int deleteBoard(int boardNo) {
		return boardDao.deleteBoard(sqlSession, boardNo);
	}

	// 2022.2.22(화) 12h20 실습 + 15h20 강사님 설명
	@Override
	public int updateBoard(Board b) {
		return boardDao.updateBoard(sqlSession, b);
	}

	// 2022.2.23(수) 17h10
	@Override
	public ArrayList<Reply> selectReplyList(int boardNo) {
		return boardDao.selectReplyList(sqlSession, boardNo);
	}

	// 2022.2.24(목) 10h45
	@Override
	public int insertReply(Reply r) {
		return boardDao.insertReply(sqlSession, r);
	}

	// 2022.2.24(목) 11h45 추가
	@Override
	public ArrayList<Board> selectTopBoardList() {
		return boardDao.selectTopBoardList(sqlSession);
	}

}
