package com.kh.spring.notice.model.vo;

import java.sql.Date;

// 2022.3.24(목) 9h35
public class Notice {
	
	private int nId; // NID	NUMBER	Yes
	private String title; // TITLE	VARCHAR2(30 BYTE)	Yes
	private String writer; // WRITER	VARCHAR2(15 BYTE)	Yes
	private String content; // CONTENT	VARCHAR2(300 BYTE)	Yes
	private Date nDate; // NDATE	DATE	Yes
	
	public Notice() {
		super();
	}

	public Notice(int nId, String title, String writer, String content, Date nDate) {
		super();
		this.nId = nId;
		this.title = title;
		this.writer = writer;
		this.content = content;
		this.nDate = nDate;
	}

	public int getnId() {
		return nId;
	}

	public void setnId(int nId) {
		this.nId = nId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getnDate() {
		return nDate;
	}

	public void setnDate(Date nDate) {
		this.nDate = nDate;
	}

	@Override
	public String toString() {
		return "Notice [nId=" + nId + ", title=" + title + ", writer=" + writer + ", content=" + content + ", nDate="
				+ nDate + "]";
	}

}
