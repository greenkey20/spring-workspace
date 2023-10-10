package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import com.kh.spring.board.model.vo.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;
import com.kh.spring.common.template.Pagination;

// 2022.2.21(월) 10h40

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	// 2022.2.21(월) 11h10
	// menubar.jsp의 '게시판' 메뉴 클릭 시, list.bo + 기본적으로 currentPage=1(1번 페이지) 요청
	// vs paging bar 클릭 시, /list.bo?currentPage=요청하는 페이지의 번호 -> 단, 아래 RequestParam으로 받을 때 'cpage'로 현재 페이지 정보를 받기로 함
	/*
	@RequestMapping("list.bo") // list.bo라는 요청이 오면 처리해주는 메소드임을 표시
	public String selectList(@RequestParam(value="cpage", defaultValue="1") int currentPage, Model model) { // @RequestParam = name + key; RequestParam에서 name 속성 값'cpage'로 넘어오는 값을 받을 변수 = int currentPage -> 11h15 이 매개변수 부분 코드 잘 이해 안 되니, 복습하자 ㅠ.ㅠ
//		System.out.println("cpage = " + currentPage); // 11h15 menubar.jsp의 '게시판' 메뉴 클릭 -> Eclipse console에 'cpage = 1' 찍힘
		
		int listCount = boardService.selectListCount();
		
		// pageLimit과 boardLimit은 내가 정하는 것 -> 더 사용할 일 없을 것 같으므로, 변수 선언x, getPageInfo() 인자로 바로 전달
//		int pageLimit = 10;
//		int boardLimit = 5;
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 10, 5);
//		System.out.println(pi);
		
		// 위에서 만든 PageInfo 객체 가지고 db에 가서, 필요한 게시글 목록 조회해서 반환받아옴
		ArrayList<Board> list = boardService.selectList(pi);
		
		model.addAttribute("list", list);
		model.addAttribute("pi", pi);
		
		// WEB-INF/views/board/boardListView.jsp로 forwarding
		return "board/boardListView";
	} // selectList() 종료
	*/
	
	// 2022.2.21(월) 14h25 실습 = selectList() 메소드의 반환형을 ModelAndView로 해서 작성해보기
	@RequestMapping("list.bo")
	public ModelAndView selectList(@RequestParam(value="cpage", defaultValue="1") int currentPage, ModelAndView mv) {
//		int listCount = boardService.selectListCount();
		PageInfo pi = Pagination.getPageInfo(boardService.selectListCount(), currentPage, 10, 5);
		
		ArrayList<Board> list = boardService.selectList(pi);
		
//		mv.addObject("list", list).addObject("pi", pi).setViewName("board/boardListView"); // 나는 이렇게 했었음
		mv.addObject("pi", pi).addObject("list", list).setViewName("board/boardListView"); // 강사님께서는 이 순서대로 나열하심
		
		return mv;
//		return mv.addObject("pi", pi).addObject("list", list).setViewName("board/boardListView"); // 14h40 나의 실험 -> Cannot return a void result
	} // selectList() 종료
	
	// 2022.2.21(월) 14h45
	@RequestMapping("enrollForm.bo")
	public String enrollForm() {
		// 이 메소드의 역할 = 게시글 작성할 수 있는 화면으로 forwarding
		return "board/boardEnrollForm";
	}
	
	// 2022.2.21(월) 15h
	// 파일 여러 개 받을 때는 배열 MultipartFile[] upfiles로 받으면 됨
	@RequestMapping("insert.bo")
	public String insertBoard(Board b, MultipartFile upfile, HttpSession session, Model model) { // 요청 페이지로부터 multipart/form-data로 요청 보낸 경우, Spring에서는 MultipartFile(Spring 내장 객체?)로 받음
//		System.out.println(b); // 2022.2.21(월) 15h5 테스트 시, Board(boardNo=0, boardTitle=null, boardWriter=null, boardContent=null, originName=null, changeName=null, count=0, createDate=null, status=null)
//		System.out.println(upfile); // 2022.2.21(월) 15h5 테스트 시, null
		// multipartFile 사용하려면 별도의 library 사용해야 함 = pom.xml 파일에서 Maven을 통해 library 등록 및 다운로드 -> bean 등록(내가 만든 것이 아니므로 annotation 붙일 수 없는 바, root-context.xml 파일에 등록)
		// web2 project에서처럼 cos.jar 사용해도 되지만, 파일명 변경하는 객체/클래스 별도로 만드는 등 사용이 비교적 불편하기 때문에, 여기서는 다른 방법으로 해봄
		// 15h25 파일 첨부해서 게시글 작성해 봤을 때는
		// Board(boardNo=0, boardTitle=가입인사, boardWriter=user03, boardContent=안녕하세요, originName=null, changeName=null, count=0, createDate=null, status=null)
		// MultipartFile[field="upfile", filename=flower2.PNG, contentType=image/png, size=402963]
		
		// 15h30 파일 첨부를 하지 않아도 null이 들어가있지는 않음 -> 첨부파일이 있는지 없는지 null로 비교/확인할 수는 없음; 파일 첨부를 하지 않은 경우 filename이 빈 문자열 ""
		// Board(boardNo=0, boardTitle=가입인사, boardWriter=user03, boardContent=hello, originName=null, changeName=null, count=0, createDate=null, status=null)
		// MultipartFile[field="upfile", filename=, contentType=application/octet-stream, size=0]
		
		// MultipartFile = 파일 첨부를 했든 안 했든 생성된 객체 -> 차이점 = filename에 원본 파일명이 들어가있는가, 아니면 ""인가
		if (!upfile.getOriginalFilename().equals("")) { // 전달된 첨부 파일이 있는 경우 = 전달된 첨부 파일의 filename이 빈 문자열이 아닌 경우 -> 파일명을 수정한 뒤 서버에 업로드 + 서버 업로드된 경로를 b에 담기; upfile.getOriginalFilename()의 반환형 = String
			
			// 아래와 같이 파일명 변경하고, 물리적 파일 만들어 저장하는 등의 작업은 다른 게시판에서도 사용하는 등 유용할 것 같아서, 별도의 메소드로 만듦; 별도의 클래스로 만들어서 써도 되지만, 여기서는 이 클래스 내 메소드로 만듦
			/*
			// 파일명 수정 -> 2022(연)02(월)21(일)15(시)37(분)40(초)5자리랜덤숫자.원본파일의 확장자
			String originName = upfile.getOriginalFilename(); // 첨부 파일의 원본명
			
			String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 2022(연)02(월)21(일)15(시)37(분)40(초)
			int ranNum = (int)(Math.random() * 99999 + 10000); //  5자리 랜덤 숫자
			String ext = originName.substring(originName.lastIndexOf(".")); // 원본 파일의 확장자 <- 원본 파일명의 맨 마지막에 있는 .을 기준으로/시작 위치로 해서 자름(15h40 String 메소드 다시 공부하기 ㅠ.ㅠ)
			
			String changeName = currentTime + ranNum + ext;
			
			String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/"); // 업로드하고자 하는 폴더의 물리적인 경로 알아내기; 15h45 강사님께서 uploadFiles 뒤에 반드시 / 붙여야 한다고 말씀/강조하셨는데, 왜인지 정확히 못 들음 ㅠ.ㅠ
			// cf. web-workspace2 > JSP project > BoardInsertController에서 String savePath = request.getSession().getServletContext().getRealPath("/resources/board_upfiles/"); // 가장 왼쪽에 나오는 / = WebContent(15h45 왜/어떻게인지 강사님의 설명 놓침); 가장 우측에 /(폴더임을 표시) 꼭 붙여주기
			
			try {
				upfile.transferTo(new File(savePath + changeName)); // savePath라는 경로에 changeName이라는 이름으로 file을 만들어서 upfile을 (서버 컴퓨터의)hard disc에 물리적으로 저장
			} catch (IOException e) { // try/catch 자동 완성했을 때 작성되는 IllegalStateException은 IO exception의 자식인 바, 삭제해도 됨
				e.printStackTrace();
			}
			*/
			String changeName = saveFile(upfile, session); // upfile과 session을 인자로 전달하며 saveFile() 메소드 호출 -> 해당 메소드의 기능 = 파일명을 수정한 뒤 서버에 업로드 ->  saveFile() 메소드 내에서 만들어진 변경 파일명(changeName)은 메소드 영역을 벗어나면 없어짐 vs 여기에서 사용할 수 있도록 변경 파일명 반환받음
			
			b.setOriginName(upfile.getOriginalFilename()); // 원본 파일명 set
			b.setChangeName("resources/uploadFiles/" + changeName); // 변경 파일명 set -> 16h15 나의 질문 = 강사님께서는 파일 저장 경로 맨 앞과 맨 끝에 / 안 쓰셨는데, 안 써도 괜찮나? -> 2022.2.22(화) 9h55 나는 맨 앞에 / 썼다가 첨부했던 파일들을 찾을 수 없어 다운로드할 수 없는 문제 발생했었음 vs 맨 앞의 / 없으니까 첨부 파일 다운로드 잘 됨
		}
		
		// 게시글 작성 요청 시 넘어온 첨부파일이 없을 경우, b에 담긴 내용 = 글 제목, 작성자, 내용 vs 있는 경우, 글 제목, 작성자, 내용, 파일 원래 이름, 파일 변경 이름(저장 경로 포함)
		int result = boardService.insertBoard(b);
		
		if (result > 0) { // 게시글 작성 성공 시 -> list.bo로 재요청해서 게시글 list page(boardListView.jsp) 볼 수 있도록 redirect
			session.setAttribute("alertMsg", "게시글 작성에 성공했습니다 ^^");
			return "redirect:list.bo";
		} else { // 게시글 작성 실패한 경우; 이 경우는 접할 일 없을 것 같긴 하지만, 혹시 몰라서 만들어둠
			model.addAttribute("errorMsg", "게시글 작성에 실패했습니다 ㅠ.ㅠ");
			return "common/errorPage";
		}
		
	} // insertBoard() 종료 -> 16h30 테스트 완료
	
	// 2022.2.21(월) 16h
	// controller에 요청받는 메소드만 있어야 하는 건 아님 + 내가 필요한 메소드 만들어서 쓸 수 있음 -> 아래 saveFile() 메소드는 요청받는 메소드가 아닌 바, RequestMapping annotation 붙일 필요 없음
	// 다른 기능이나 동료들이 함께 사용할 수 있는 메소드들은 별도의 클래스에 모아두고 그 클래스를(? 16h5 강사님 설명 정확히 못 들음) static으로 만들어, 프로그램 내 어디에서나 사용할 수 있도록 함
	public String saveFile(MultipartFile upfile, HttpSession session) {
		// 파일명 수정 -> 2022(연)02(월)21(일)15(시)37(분)40(초)5자리랜덤숫자.원본파일의 확장자
		String originName = upfile.getOriginalFilename(); // 첨부 파일의 원본명
		
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 2022(연)02(월)21(일)15(시)37(분)40(초)
		int ranNum = (int)(Math.random() * 99999 + 10000); //  5자리 랜덤 숫자
		String ext = originName.substring(originName.lastIndexOf(".")); // 원본 파일의 확장자 <- 원본 파일명의 맨 마지막에 있는 .을 기준으로(?) 자름(15h40 String 메소드 다시 공부하기 ㅠ.ㅠ)
		
		String changeName = currentTime + ranNum + ext; // 수정/변경 파일명
		
		String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/"); // 업로드하고자 하는 폴더의 물리적인 경로 알아내기; 15h45 강사님께서 uploadFiles 뒤에 반드시 / 붙여야 한다고 말씀/강조하셨는데, 왜인지 정확히 못 들음 ㅠ.ㅠ
		// 17h25 강사님께서 어떤 학우님 질문에 대답해주신 내용 = 맨 앞에 / 안 붙이면 contextPath에 붙고 , 붙이면 절대경로(그러면 // 되지 않을까 강사님께서 의구심을 가지셨는데, 학우님께서는 /로 잘 된다고 하심 -> 강사님 생각 = Spring이 알아서 잘 해석해서 /로 되는 듯) -> 둘 다 ok
		
		try {
			upfile.transferTo(new File(savePath + changeName)); // savePath라는 경로에 changeName이라는 이름으로 file을 만들어서 upfile을 (서버 컴퓨터의)hard disc에 물리적으로 저장
		} catch (IOException e) { // try/catch 자동 완성했을 때 작성되는 IllegalStateException은 IO exception의 자식인 바, 삭제해도 됨
			e.printStackTrace();
		}
		
		return changeName; // 16h50 나의 생각 = "savePath + changeName"을 반환하면 더 편리하고 적합하지 않나?
	} // saveFile() 종료
	
	// 2022.2.21(월) 17h10 나의 실습 수행
	/*
	@RequestMapping("detail.bo")
	public ModelAndView boardDetail(int bno, ModelAndView mv) { // 나는 처음에 만들 때 변수명 boardNo으로 전달받았었음
//		System.out.println(boardNo); // 2022.2.21(월) 17h45 테스트 시 "18" 찍힘
		int result = boardService.increaseCount(bno);
		
		if (result > 0) {
			Board b = boardService.selectBoard(bno); // 해당 게시글 번호의 글 제목, 작성자, 작성일(to_char), 원본 파일명, 수정 파일명, 내용을 조회해 와야 함
//			System.out.println(b);
			// 첨부 파일 없는 글을 선택 시, Board(boardNo=0, boardTitle=가입인사, boardWriter=user03, boardContent=안녕하세요, originName=null, changeName=null, count=0, createDate=2022-02-21, status=null)
			// 첨부 파일 있는 글을 선택 시, Board(boardNo=0, boardTitle=예쁜 꽃 사진 공유해요, boardWriter=user03, boardContent=잠시나마 마음의 휴식을 위해 꽃을 보내요~, originName=flower2.jpg, changeName=/resources/uploadFiles/2022022116315333270.jpg, count=0, createDate=2022-02-21, status=null)
			// 또는 Board(boardNo=0, boardTitle=첨부파일 다운로드가 안 되요, boardWriter=user03, boardContent=왜죠? ㅠ.ㅠ, originName=flower1.PNG, changeName=/resources/uploadFiles/2022022118262976090.PNG, count=0, createDate=2022-02-21, status=null)
			// 2022.2.21(월) 18h30 나의 관찰 = "boardMapper.selectBoard" 태그에서 select하지 않은 컬럼들(count, status)은 vo 클래스에서 각 필드 자료형(int, String)의 기본 값(각각 0, null)이 들어가 있음
			
			mv.addObject("b", b).setViewName("board/boardDetailView");
		} else {
			mv.addObject("errorMsg", "게시글 상세 조회에 실패했습니다").setViewName("common/errorPage");
		}
		
		return mv;
	} // boardDetail() 종료 -> 테스트 시 '파일 없음'으로 파일 다운로드가 안 되는 문제 해결하다가 2022.2.21(월) 18h25  중단
	*/
	// 강사님의 guide 필기 못 함(화면 캡쳐만 해둠) -> 2023.10.9(월) 15h 아래에 메모 추가
	
	// 2022.2.22(화) 9h25 강사님 설명
	@RequestMapping("detail.bo")
	public ModelAndView selectBoard(ModelAndView mv, int bno) { // 게시글을 식별하는 데에 필요한 값; key 값과 똑같은 이름의 매개변수 + String형이지만 내가 int형으로 쓰면 알아서 parsing됨 -> 게시글 식별하는 데 필요한 값 가지고 DB에 가서 조건문의 어떤 조건으로 쓰여야 함
		// 회원 가입 시에도 정보 모두 입력하고 가입 잘 되었음(이 때도 String으로 넘어오는 자료가 int로 parsing된 것임) + 단, 반드시 숫자 값이 포함되어 있어야 숫자로 parsing됨 -> 그래서 회원 가입 시 선택적 입력 사항인(2023.10.9(월) 16h 나의 질문 = 무엇을 쓰다 만 걸까?)

		// 해당 게시글 조회 수 증가용 service 호출 결과 받기 <- update문
		int result = boardService.increaseCount(bno);
		
		if (result > 0) { // 조회 수가 정상적으로 증가되었다면
			// 게시글 상세 정보 조회용 service 호출 -> boardDetailView.jsp 상에 필요한 데이터를 조회 = 조회 수를 증가시킨 게시글의 정보를 db로부터 받아옴
			Board b = boardService.selectBoard(bno);

			// 조회된 데이터를 담아서 board/boardDetailView로 forwarding
			mv.addObject("b", b).setViewName("board/boardDetailView");
		} else { // 조회 수 증가에 실패했다면
			// error 문구 담아서 error page로 forwarding
			mv.addObject("errorMsg", "게시글 상세 조회에 실패했습니다").setViewName("common/errorPage");
		}
		
		return mv;
	} // boardDetail() 종료 
	
	// 2022.2.22(화) 10h35
	@RequestMapping("delete.bo")
	public String deleteBoard(int bno, String filePath, HttpSession session, Model model) {
		// 게시글 삭제 = status를 'N'으로 update -> 반환형 = 처리된 행의 개수 int
		int result = boardService.deleteBoard(bno);
		
		if (result > 0) { // 게시글 삭제에 성공했다면
			if (!filePath.equals("")) { // 해당 게시글에 첨부 파일이 있었을 경우 -> 기존에 존재하는 첨부 파일을 삭제 <- "resources/xxx/xxx.jpg" 파일을 찾으려면 물리적인 경로(=파일의 changeName으로 저장해둠)가 필요함
				new File(session.getServletContext().getRealPath(filePath)).delete();
				// new File() = Java에서 파일을 만들 수 있는 객체 -> 외부 자원(e.g. 실제 존재하는 파일 등) 다룰 수 있음 -> 이 파일의 정보를 담고 있는(11h5 강사님 설명 제대로 못들음) 파일 객체 만듦 -> 그 파일을 삭제
				// session.getServletContent() = application 객체 생성 -> application 객체 이용 + filePath를 인자로 받아, 파일의 실제 물리적인 위치 얻어냄
			}
			
			session.setAttribute("alertMsg", "성공적으로 게시글이 삭제되었습니다");
			
			return "redirect:list.bo"; // list.bo -> 게시판 list page로 url 재요청
		} else { // 게시글 삭제에 실패했다면
			model.addAttribute("errorMsg", "게시글 삭제에 실패했습니다");
			return "common/errorPage";
		}
		
	} // deleteBoard() -> 11h20 테스트 완료
	
	// 2022.2.22(화) 11h40 실습
	// controller를 거쳐서 boardUpdateForm.jsp에 가면 경로 노출 안 됨 + 수정하고자 하는 게시글의 정보를 가지고, 게시글 수정 page로 가야/이동해야 함
	/*
	@RequestMapping("updateForm.bo")
	public ModelAndView updateForm(int bno, ModelAndView mv) {
		Board b = boardService.selectBoard(bno);
		
		mv.addObject("b", b).setViewName("board/boardUpdateForm");
		return mv;
	}
	*/
	
	// 2022.2.22(화) 14h15 강사님 설명
	@RequestMapping("updateForm.bo")
	public String updateForm(int bno, Model model) {
//		Board b = boardService.selectBoard(bno);
		model.addAttribute("b", boardService.selectBoard(bno)); // 코드 1줄이라도 줄이고자 이렇게 작성함 + forwarding하는 응답 페이지에서만 사용하고자 하므로, model/request 영역에 담는 것이 적절하고, 굳이 session에 담지 않아도 될 것 같음
		
		return "board/boardUpdateForm";
	}
	
	// 2022.2.22(화) 11h45 실습
	/*
	@RequestMapping("update.bo")
	public String updateBoard(Board b, MultipartFile reUpfile, HttpSession session, Model model) {
		System.out.println(b); // Board(boardNo=0, boardTitle=Spring!, boardWriter=user01, boardContent=자바로 웹사이트 구현을 더 수월하게 할 수 있도록 도와주는 프레임워크에요! 다만 규칙을 지켜야 한답니다!
//		+ 봄꽃 사진 구경하세요, originName=, changeName=null, count=0, createDate=null, status=null) -> 문제점 = 게시글 번호, 원본 파일명, 수정 파일명을 요청 페이지로부터 안 받아옴
		System.out.println(reUpfile); // MultipartFile[field="reUpfile", filename=flower.jpg, contentType=image/jpeg, size=41408]
		
		// 2022.2.22(화) 12h50 테스트 시
		// Board(boardNo=15, boardTitle=Spring!, boardWriter=user01, boardContent=자바로 웹사이트 구현을 더 수월하게 할 수 있도록 도와주는 프레임워크에요! 다만 규칙을 지켜야 한답니다!
//		+ spring flowers, originName=flower1.PNG, changeName=resources/uploadFiles/20220222124931105082.PNG, count=0, createDate=null, status=null)
//		MultipartFile[field="reUpfile", filename=flower2.PNG, contentType=image/png, size=402963]
		
		if (!reUpfile.getOriginalFilename().equals("")) { // 금번/게시글 수정 시, 새로 업로드된 파일이 있는 경우
			if (!b.getOriginName().equals("")) { // 기존의 첨부 파일이 있었다면 -> 기존의 첨부 파일을 서버로부터 삭제
				new File(session.getServletContext().getRealPath(b.getChangeName())).delete(); // 2022.2.22(화) 12h35 테스트 시 null pointer exception 발생
			}
			
			// 새로 업로드된 파일의 정보들을 db로 넘길 게시글 객체 b에 담음
			String changeName = saveFile(reUpfile, session);
			b.setOriginName(reUpfile.getOriginalFilename());
			b.setChangeName("resources/uploadFiles/" + changeName);
		} else { // 금번/게시글 수정 시, 새로 업로드된 파일이 없는 경우 -> 12h50 나의 문제 = 요청 페이지에서 기존 파일을 어떻게 삭제하는지 모르겠음 -> 15h35 강사님 의견 = 기존 파일 누르면/클릭하면 null 값 넣기 등 AJAX로 구현할 수 있을 듯
			if (b.getOriginName() != null) { // 기존의 첨부 파일이 있었다면 -> 기존의 첨부 파일을 서버로부터 삭제 + BOARD 테이블의 첨부파일 관련 컬럼 값들을 삭제/null로  set
				new File(session.getServletContext().getRealPath(b.getChangeName())).delete();
				b.setOriginName(null);
				b.setChangeName(null);
			}
			
		}
		
		int result = boardService.updateBoard(b);
		
		if (result > 0) {
			session.setAttribute("alertMsg", "성공적으로 게시글이 수정되었습니다");
			return "redirect:detail.bo?bno=" + b.getBoardNo();
		} else { // 테스트 중 boardNo을 요청 페이지로부터 안 받아와서 처리된 행의 개수가 0이었기 때문에 이 else절 부분에 들어온 적 있음
			model.addAttribute("errorMsg", "게시글 수정에 실패했습니다");
			return "common/errorPage";
		}
		
	} // updateBoard() 종료 -> 13h 테스트 완료
	*/
	
	// 2022.2.22(화) 14h40 강사님 설명
	@RequestMapping("update.bo")
	public String updateBoard(@ModelAttribute/* 이게 어떤 vo인지 보다 명확하게 소통하기/가시성을 위해 기재할 수 있음*/ Board b, MultipartFile reUpfile, HttpSession session, Model model) { // 요청 페이지로부터 ModelAttribute로 담겨진(? 내가 이해한 게 맞나?) Board에 대해 Spring이 Board의 getter에 접근해서 값 가져옴
		if (!reUpfile.getOriginalFilename().equals("")) { // 게시글 수정 시, 새로 첨부 파일이 넘어온 경우
			if (b.getOriginName() != null) { // 기존의 첨부 파일이 있었다면 -> 기존의 첨부 파일을 서버로부터 삭제
				new File(session.getServletContext().getRealPath(b.getChangeName())).delete(); // Java에서 파일을 사용할 수 있도록 File 객체 생성 -> 그 파일 delete()
			}
			
			String changeName = saveFile(reUpfile, session); // saveFile() 메소드의 역할 = 새로 넘어온 첨부 파일을 서버에 업로드시키기 + 변경 파일명 반환
			
			// Board 객체 b에 첨부 파일 관련해서 새로운 정보(원본 파일명, 수정 파일명/저장 경로) 담기/set
			b.setOriginName(reUpfile.getOriginalFilename());
			b.setChangeName("resources/uploadFiles/" + changeName);
		}
		
		/* 이상 Board 객체 b에 담겨있는 내용 = boardNo, boardTitle, boardContent
		 * + case1) 새로 첨부된 파일x + 기존 첨부 파일 x -> originName = null, changeName = null
		 * + case2) 새로 첨부된 파일x + 기존 첨부 파일 o -> originName = 기존 첨부파일의 원본명, changeName = 기존 첨부파일의 수정명 및 경로
		 * + case3) 새로 첨부된 파일o + 기존 첨부 파일 x -> originName = 새로 첨부된 파일의 이름, changeName = 새로 첨부된 파일의 수정명 및 경로
		 * + case4) 새로 첨부된 파일o + 기존 첨부 파일 o -> originName = 새로 첨부된 파일의 이름, changeName = 새로 첨부된 파일의 수정명 및 경로
		 */
		
		int result = boardService.updateBoard(b);
		
		if (result > 0) {
			session.setAttribute("alertMsg", "성공적으로 게시글이 수정되었습니다");
			return "redirect:detail.bo?bno=" + b.getBoardNo();
		} else {
			model.addAttribute("errorMsg", "게시글 수정에 실패했습니다");
			return "common/errorPage";
		}
		
	} // updateBoard() 종료 -> 15h50 테스트 완료
	
	// 2022.2.23(수) 17h10
	@ResponseBody
	@RequestMapping(value="rlist.bo", produces="application/json; charset=utf-8")
	public String ajaxSelectReplyList(int bno) {
//		ArrayList<Reply> list = boardService.selectReplyList(bno);
		return new Gson().toJson(boardService.selectReplyList(bno)); // JavaScript (객체)배열의 요소로 담긴 JavaScript 객체의 key 값은 GSON 내부적으로 Reply 객체의 필드명으로 알아서 잡힘
	} // ajaxSelectReplyList() 종료
	
	// 2022.2.24(목) 10h40
	@ResponseBody
	@RequestMapping(value="rinsert.bo") // 반환해줄 문자열이 영문자니까 encoding 설정 필요 없음
	public String ajaxInsertReply(Reply r) {
//		System.out.println(r); // Reply(replyNo=0, replyContent=                  ㅁㄴㅇㄻㄴㅇ ㅇㄹ, refBoardNo=21, replyWriter=user02, createDate=null, status=null)
		return boardService.insertReply(r) > 0 ? "success" : "fail";
	}
	
	// 2022.2.24(목) 11h40
	@ResponseBody // 데이터를 보내겠다는 annotation -> getWriter() 없이 응답 가능
	@RequestMapping(value="topList.bo", produces="application/json; charset=utf-8")
	public String ajaxTopBoardList() { // 12h15 강사님 보충 설명 = String이 위 produces 속성 값에 의해 json으로 변환해서 응답되는 것임
//		ArrayList<Board> list = boardService.selectTopBoardList();
		return new Gson().toJson(boardService.selectTopBoardList());
	}

}
