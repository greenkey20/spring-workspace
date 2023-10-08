package main.java.com.kh.spring.board.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import main.java.com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import main.java.com.kh.spring.common.model.vo.PageInfo;

//2022.2.21(월) 10h35

@Repository
public class BoardDao {
	
	// 2022.2.21(월) 11h25
	public int selectListCount(SqlSessionTemplate sqlSession) {
		return sqlSession.selectOne("boardMapper.selectListCount"); // 조회 결과가 1행인 + 이미 완성된 select문 실행 <- 내가 실행할  mapper 파일의 namespace + 실행할 태그의 id
	}
	
	// 2022.2.21(월) 11h35
	public ArrayList<Board> selectList(SqlSessionTemplate sqlSession, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();
		
		RowBounds rowBounds = new RowBounds(offset, limit); // 12h15 나의 질문 = 강사님, 아까 dao에서 RowBounds는 그냥 저희가 객체 만들어서 쓰는 것으로 하나요, 아니면 Spring에 위임하는 것이 좋나요? -> 필요할 때는 내가 만들어서 쓰면 됨
		
		return (ArrayList)sqlSession.selectList("boardMapper.selectList", null, rowBounds); // 내가 실행할 쿼리문은 완성되어 있기 때문에 dao로부터 mapper에 넘겨줄 값 없음 = 2번째 인자로 null 기재 -> selectList()의 반환형 = list -> 강제 형 변환
	}
	
	// 2022.2.21(월) 16h20
	public int insertBoard(SqlSessionTemplate sqlSession, Board b) {
		return sqlSession.insert("boardMapper.insertBoard", b);
	}
	
	// 2022.2.21(월) 17h25 실습 + 2022.2.22(화) 9h30 강사님 설명
	public int increaseCount(SqlSessionTemplate sqlSession, int boardNo) {
		return sqlSession.update("boardMapper.increaseCount", boardNo);
	}
	
	// 2022.2.21(월) 17h35 실습 + 2022.2.22(화) 9h35 강사님 설명
	public Board selectBoard(SqlSessionTemplate sqlSession, int boardNo) {
		return sqlSession.selectOne("boardMapper.selectBoard", boardNo); // 특정 게시글 1개 조회 -> 조회 결과 = 1행
	}
	
	// 2022.2.22(화) 10h35
	public int deleteBoard(SqlSessionTemplate sqlSession, int boardNo) {
		return sqlSession.update("boardMapper.deleteBoard", boardNo);
	}
	
	// 2022.2.22(화) 12h25 실습 + 15h20 강사님 설명
	public int updateBoard(SqlSessionTemplate sqlSession, Board b) {
		return sqlSession.update("boardMapper.updateBoard", b);
	}
	
	// 2022.2.23(수) 17h15
	public ArrayList<Reply> selectReplyList(SqlSessionTemplate sqlSession, int boardNo) {
		return (ArrayList)sqlSession.selectList("boardMapper.selectReplyList", boardNo);
	}
	
	// 2022.2.24(목) 10h45
	public int insertReply(SqlSessionTemplate sqlSession, Reply r) {
		return sqlSession.insert("boardMapper.insertReply", r);
	}

	// 2022.2.24(목) 11h45
	public ArrayList<Board> selectTopBoardList(SqlSessionTemplate sqlSession) {
		return (ArrayList)sqlSession.selectList("boardMapper.selectTopBoardList");
	}

}
