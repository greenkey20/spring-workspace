package com.kh.spring.notice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.notice.model.service.NoticeService;
import com.kh.spring.notice.model.vo.Notice;

// 2022.3.24(목) 9h30

@Controller
public class NoticeController {
	
	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping("enrollForm.no")
	public String enrollForm() {
		return "notice/noticeEnrollForm";
	}
	
	@RequestMapping("ninsert.do")
	public String insertNotice(Notice n) {
		int result = noticeService.insertNotice(n);
		
		if (result > 0) { // 공지사항 작성에 성공한 경우
			return "redirect:nList.do";
		} else { // 공지사항 작성에 실패한 경우
			return "redirect:error.do";
		}
		
	}
	
	@RequestMapping("nList.do")
	public String selectNoticeList() {
		return "notice/noticeListView";
	}
	
	@RequestMapping("error.do")
	public String errorPage() {
		return "notice/errorPage";
	}

}
