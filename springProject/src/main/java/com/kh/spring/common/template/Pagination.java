package main.java.com.kh.spring.common.template;

import main.java.com.kh.spring.common.model.vo.PageInfo;

// 2022.2.14(월) 17h30
public class Pagination {

    public static PageInfo getPageInfo(int listCount, int currentPage, int pageLimit, int boardLimit) {
        int maxPage = (int) Math.ceil((double) listCount / boardLimit);
        int startPage = (currentPage - 1) / pageLimit * pageLimit + 1;
        int endPage = startPage + pageLimit - 1;

        if (endPage > maxPage) {
            endPage = maxPage;
        }

        return new PageInfo(listCount, currentPage, pageLimit, boardLimit, maxPage, startPage, endPage);
    } // getPageInfo() 종료

}
