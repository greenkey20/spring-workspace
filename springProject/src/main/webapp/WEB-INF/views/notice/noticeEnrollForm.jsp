<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 등록</title>
</head>
<body>
    <!--2022.3.24(목) 9h20-->
    <h1 align="center">공지사항 등록</h1>

    <form action="ninsert.do" method="post">
        <table id="noticeInsertTable" border="1">
            <tr>
                <th>제목</th>
                <td><input type="text" size="80" name="title"></td>
            </tr>
            <tr>
                <th>작성자</th>
                <td><input type="text" name="writer" readonly value="${ loginUser.userId }"></td>
            </tr>
            <tr>
                <th>내용</th>
                <td><textarea name="content" cols="80" rows="10" style="resize: none;"></textarea></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="등록하기">
                    <button onclick="location.href='nList.do'">목록으로 돌아가기</button>
                </td>
            </tr>
        </table>
    </form>

</body>
</html>