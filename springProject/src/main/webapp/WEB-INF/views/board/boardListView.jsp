<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 게시글 목록</title>
<style>
	#boardList {text-align:center;}
    #boardList>tbody>tr:hover {cursor:pointer;}
    #pagingArea {width:fit-content; margin:auto;}
    #searchForm {
        width:80%;
        margin:auto;
    }
    #searchForm>* {
        float:left;
        margin:5px;
    }
    .select {width:20%;}
    .text {width:53%;}
    .searchBtn {width:20%;}
</style>

</head>
<body>
	<!--2022.2.21(월) 10h45
		window > preferences > web > jsp files > editor > templates > html5 template에 taglib 지시어 추가 -> 추후 새로 추가하는 jsp 파일에는 taglib 지시어 자동으로 입력됨-->
	
	<jsp:include page="../common/header.jsp" />

    <div class="content">
        <br><br>
        <div class="innerOuter" style="padding:5% 10%;">
            <h2>게시판</h2>
            <br>
            
            <!--2022.2.21(월) 12h30 로그인 후 상태일 경우만 보여지는 글쓰기 버튼-->
            <c:if test="${ not empty loginUser }">
	            <a class="btn btn-secondary" style="float:right;" href="enrollForm.bo">글쓰기</a>
            </c:if>
            
            <br>
            <br>
            <table id="boardList" class="table table-hover" align="center">
                <thead>
                    <tr>
                        <th>글번호</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>조회수</th>
                        <th>작성일</th>
                        <th>첨부파일</th> <!--첨부파일이 있는 경우 ★ 표시 vs 없는 경우 아무 것도 표시하지 않음-->
                    </tr>
                </thead>
                <tbody>
                	<c:forEach var="b" items="${ list }"> <!--items = 반복해서 조회할 배열이나 collection-->
                		<tr>
	                        <td class="bno">${ b.boardNo }</td> <!--vo 객체의 getter(메소드)를 통해 접근하므로, getter가 반드시 존재해야 함-->
	                        <td>${ b.boardTitle }</td>
	                        <td>${ b.boardWriter }</td>
	                        <td>${ b.count }</td>
	                        <td>${ b.createDate }</td>
	                        <td>
	                        	<c:if test="${ not empty b.originName }">
	                        		★
	                        	</c:if>
	                        </td>
	                    </tr>
                	</c:forEach>
                	
                	<!--2022.2.21(월) 17h 실습 + 9h15 강사님 설명-->
                	<script>
                		$(function() { // 문서 전체가 읽어졌을 때 실행
                			$("#boardList>tbody>tr").click(function() { // 이렇게 선택된 tr을 클릭하면 -> 익명함수; JavaScript, jQuery 선택자 아주 중요
                				// location.href = "detail.bo?bno=" + $(this).children().eq(0).text(); // 게시글 번호 = 식별자 = tr의 첫번째의 자식의 text 요소 -> 특정 게시글을 선택하려면 식별자 필요 + query string 만들어서 함께 넘겨줘야 함
                				location.href = 'detail.bo?bno=' + $(this).children(".bno").text(); // 강사님 방식 = 선택하고자 하는 tr의 자식에 클래스 속성 부여 <- 선택하기 어려우면 클래스 속성 부여
                			})
                		})
                	</script>
                    
                    <!--dummy data
                    <tr>
                        <td>4</td>
                        <td>네번째 공지사항제목</td>
                        <td>admin</td>
                        <td>10</td>
                        <td>2020-02-07</td>
                        <td>★</td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>세번째 공지사항제목</td>
                        <td>admin</td>
                        <td>10</td>
                        <td>2020-02-03</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>두번째 공지사항제목</td>
                        <td>admin</td>
                        <td>100</td>
                        <td>2020-02-01</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>첫번째 공지사항제목</td>
                        <td>admin</td>
                        <td>45</td>
                        <td>2019-12-25</td>
                        <td>★</td>
                    </tr>
                    -->
                    
                </tbody>
            </table>
            <br>

            <div id="pagingArea">
                <ul class="pagination">
                	<!--2022.2.21(월) 14h10 Previous, Next 버튼 처리-->
                	<c:choose>
                		<c:when test="${ pi.currentPage eq 1 }"> <!--현재 페이지가 1페이지인 경우 -> Previous 버튼 비활성화; 나의 생각 = ${ pi.currentPage ne 1 } 조건으로 더 많은 경우의 수를 먼저 제외시키는 것이 더 좋을 것 같다?!-->
                			<li class="page-item disabled"><a class="page-link" href="#">Previous</a></li> <!--disabled = bootstrap 적용을 위해 기재한 속성-->
                		</c:when>
                		<c:otherwise>
                			<li class="page-item"><a class="page-link" href="list.bo?cpage=${ pi.currentPage - 1 }">Previous</a></li> <!--강사님께서 처음에 href url 맨 앞에 / 붙이셨었는데, controller에서  mapping한 url은 /가 없기 때문에, 404 오류(? 제대로 못 봄) 발생했음-->
                		</c:otherwise>
                	</c:choose>
                    
                    <c:forEach var="p" begin="${ pi.startPage }" end="${ pi.endPage }">
                    	<c:choose>
                    		<c:when test="${ p eq pi.currentPage }">
                    			<li class="page-item">&nbsp;&nbsp;${ p }&nbsp;&nbsp;</li>
                    		</c:when>
                    		<c:otherwise>
                    			<li class="page-item"><a class="page-link" href="list.bo?cpage=${ p }">${ p }</a></li>
                    		</c:otherwise>
                    	</c:choose>
                    </c:forEach>
                    
                    <c:choose>
                    	<c:when test="${ pi.currentPage eq pi.maxPage }"> <!--현재 페이지가 마지막 페이지인 경우 -> Next 버튼 비활성화-->
                    		<li class="page-item disabled"><a class="page-link" href="#">Next</a></li>
                    	</c:when>
                    	<c:otherwise> <!--현재 페이지가 마지막 페이지가 아닌 경우-->
                    		<li class="page-item"><a class="page-link" href="list.bo?cpage=${ pi.currentPage + 1 }">Next</a></li>
                    	</c:otherwise>
                    </c:choose>
                </ul>
            </div>

            <br clear="both"><br>

            <form id="searchForm" action="" method="get" align="center">
                <div class="select">
                    <select class="custom-select" name="condition">
                        <option value="writer">작성자</option>
                        <option value="title">제목</option>
                        <option value="content">내용</option>
                    </select>
                </div>
                <div class="text">
                    <input type="text" class="form-control" name="keyword">
                </div>
                <button type="submit" class="searchBtn btn btn-secondary">검색</button>
            </form>
            <br><br>
        </div>
        <br><br>

    </div>

    <jsp:include page="../common/footer.jsp" />

</body>
</html>