<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 게시글 상세 보기</title>
<style>
	table * {margin:5px;}	
    table {width:100%;}
</style>
</head>
<body>
	<!--2022.2.21(월) 17h 실습 + 2022.2.22(화) 9h45 강사님 설명-->
	<jsp:include page="../common/header.jsp" />

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>게시글 상세 보기</h2>
            <br>

            <a class="btn btn-secondary" style="float:right;" href="">목록으로</a>
            <br><br>

            <table id="contentArea" algin="center" class="table">
                <tr>
                    <th width="100">제목</th>
                    <td colspan="3">${ b.boardTitle }</td>
                </tr>
                <tr>
                    <th>작성자</th>
                    <td>${ b.boardWriter }</td>
                    <th>작성일</th>
                    <td>${ b.createDate }</td>
                </tr>
                <tr>
                    <th>첨부파일</th>
                    <td colspan="3">
                    	<c:choose>
                    		<c:when test="${ empty b.originName }"> <!--첨부 파일이 없는 경우 = controller에서 b를 console에 찍어보면, 'originName=null' = originName 아무 것도 안 나옴/공백으로 뜸 -> 나는 ${ b.originName eq null }로 썼음 vs 강사님께서는 ${ empty b.originName }이라고 쓰심 -> 2022.2.22(화) 10h10 나의 질문 = ${ b.originName eq "" }로 써도 될까?-->
                    			첨부된 파일이 없습니다
                    		</c:when>
                    		<c:otherwise> <!--첨부 파일이 있는 경우-->
                    			<a href="${ b.changeName }" download="${ b.originName }">${ b.originName }</a> <!--download 속성 값 = 파일을 어떤 이름으로 download 받을 수 있게 할 것인지 지정 vs download 속성 부여하지 않고 파일명 클릭하면 그 파일이 브라우저에서 열림-->
                    			<!--2022.2.21(월) 18h5 테스트 시 '파일 없음'으로 파일 다운로드 실패 -> 이 때 접근하려고 한 파일 경로 = http://localhost:8008/resources/uploadFiles/2022022116315333270.jpg-->
                    			<!--첨부 파일이 물리적으로 저장되어 있는 서버 컴퓨터 hard disc 경로 = C:\final-spring-workspace\springProject\src\main\webapp\resources\uploadFiles-->
                    			<!--나의 시도 = spring/ 또는 /spring/src/main/webapp 또는 ${ request.getContextPath() } 등 -> 모두 '파일 없음'으로 다운로드 실패 -> 18h25 문제 해결 중단 ㅠ.ㅠ-->
                    		</c:otherwise>
                    	</c:choose>
                    </td>
                </tr>
                <tr>
                    <th>내용</th>
                    <td colspan="3"></td>
                </tr>
                <tr>
                    <td colspan="4"><p style="height:150px;">${ b.boardContent }</p></td>
                </tr>
            </table>
            <br>

			<!--2022.2.22(화) 10h10 글 작성한 사람만 이 버튼들이 보여야 함 + 관리자도 가능하도록 추가해봄-->
			<c:if test="${ (loginUser.userId eq b.boardWriter) or (loginUser.userId == 'admin') }"> <!--로그인한 사용자가 현재 상세 조회 중인 게시글 작성자와 동일한 경우-->
				<div align="center">
	                <!-- 수정하기, 삭제하기 버튼은 이 글이 본인이 작성한 글일 경우에만 보여져야 함 -->
	                <a class="btn btn-primary" onclick="postFormSubmit(1)">수정하기</a>
	                <a class="btn btn-danger" onclick="postFormSubmit(2)">삭제하기</a> <!--href 속성 값으로 요청보내면 url 노출/get 방식 요청 -> 게시글 수정/삭제 시 보안에 취약할 수 있음; 2022.2.22(화) 11h15 나의 관찰 = 'href=""'를 a 태그 안에 남겨두었더니 onclick 속성 적용 안 되는 듯-->
	            </div>
	            <br><br>
			</c:if>
			
			<!--post 방식으로 요청 보내면서 name 속성 값을 보낼 수 있는 방법-->
			<form method="post" action="" id="postForm">
				<input type="hidden" name="bno" value="${ b.boardNo }">
				<input type="hidden" name="filePath" value="${ b.changeName }">
			</form>
			
			<script>
				function postFormSubmit(num) {
					if (num == 1) {
						$("#postForm").attr("action", "updateForm.bo").submit(); // JavaScript의 submit() 함수?메소드? -> 2022.3.24(목) 23h30 JavaScript form 객체의 submit() 함수를 이용해서 클라이언트에서 입력한 값을 서버로 전송할 수 있음; 보통 이 때 input 태그의 type="submit" 사용 + a, button 태그에서도 사용 가능(출처: https://m.blog.naver.com/love_junim/221979999800)
					}
					else {
						$("#postForm").attr("action", "delete.bo").submit();
					}
				}
			</script>

            <!-- 댓글 기능은 나중에 ajax 배우고 나서 구현할 예정! 우선은 화면구현만 해놓음 -->
            <table id="replyArea" class="table" align="center">
                <thead>
                    <tr>
                    	<!--2022.2.24(목) 10h10-->
                    	<c:choose>
                    		<c:when test="${ empty loginUser }">
                    		<!--로그인 전-->
                    			<th colspan="2">
		                            <textarea class="form-control" cols="55" rows="2" style="resize:none; width:100%;" readonly>로그인 후 이용 가능합니다. 로그인해 주세요~</textarea> <!--10h10 왜 name="" id="content" 속성 삭제하셨는지 강사님 설명 제대로 못 들음 ㅠ.ㅠ-->
		                        </th>
		                        <th style="vertical-align:middle"><button class="btn btn-secondary disabled">등록하기</button></th>
                    		</c:when>
                    		
                    		<c:otherwise>
                   			<!--로그인 후-->
		                        <th colspan="2">
		                            <textarea class="form-control" id="content" cols="55" rows="2" style="resize:none; width:100%;"></textarea> <!--AJAX로 요청 보낼 거니까 name="" 속성도 필요 없으므로 삭제함-->
		                        </th>
		                        <th style="vertical-align:middle"><button class="btn btn-secondary" onclick="addReply();">등록하기</button></th>
                    		</c:otherwise>
                    	</c:choose>
                    </tr>
                    
                    <tr>
                        <td colspan="3">댓글(<span id="rcount"></span>)</td>
                    </tr>
                </thead>
                <tbody>
                	<!--
                    <tr>
                        <th>user02</th>
                        <td>ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ꿀잼</td>
                        <td>2020-03-12</td>
                    </tr>
                    <tr>
                        <th>user01</th>
                        <td>재밌어요</td>
                        <td>2020-03-11</td>
                    </tr>
                    <tr>
                        <th>admin</th>
                        <td>댓글입니다!!</td>
                        <td>2020-03-10</td>
                    </tr>
                    -->
                </tbody>
            </table>
        </div>
        <br><br>

    </div>
    
    <!--2022.2.23(수) 17h 추가-->
    <script>
    	$(function() { // 전체 문서가 조회되면
    		selectReplyList(); // 이 함수 호출; 이 함수는 아래에서 만듦
    	})
    	
    	// 2022.2.24(목) 10h25  추가 -> 댓글 작성 처리 함수
    	function addReply() {
    		// 아무 것도 입력 안 되어 있을 때는 댓글 입력 요청 불가능하도록 함
    		if ($("#content").val().trim() != 0) { // trim() -> 양쪽에 있는 공백 삭제; 공백이 아니라면; JavaScript에서 0, false, "", NaN 등 falsey한 값들은 모두 false
    			$.ajax({
    				url : "rinsert.bo",
    				data : {
    					refBoardNo : ${ b.boardNo }, // el 구문
    					replyContent : $("#content").val(), // jQuery 구문; val() = 요소의 값을 읽어오는 기능/메소드
    					replyWriter : '${ loginUser.userId }' // el 구문 -> ${ loginUser.userId } 이렇게 뽑아온 값을 JavaScript의 문자열로 취급하기 위해 ''나 ""로 감싸줌 -> 10h35 나의 질문 = ""로 안 감싸주면 값이 제대로 안 넘어갈까? -> 11h15 테스트 시 브라우저 개발자 도구 > sources에서 나의 발견 = ''로 안 묶어주면 아무 값도 안 읽힘
    				},
    				success : function(status) {
    					if (status == "success") { // 댓글 insert/작성/추가 성공한 경우
    						selectReplyList();
    						$("#content").val("");
    					}
    					// 댓글 작성에 실패한 경우 특별히 해줄 일은 없음
    				},
    				error : function() {
    					console.log("댓글 작성 실패");
    				}
    			})
    		}
    		else {
    			alertify.alert("올바른 형식의 댓글을 작성해 주세요~")
    		}
    	}
    	
    	function selectReplyList() {
    		$.ajax({
    			url : "rlist.bo",
    			data : {bno : ${ b.boardNo }}, // 전체(x) 현재 상세 조회 중인 게시글에 딸린 댓글만(o) 조회 -> 현재 게시글 번호를 넘김
    			success : list => { // function(list)
    				// console.log(list);
    				
    				let value = "";
    				for (let i in list) {
    					value += "<tr>"
    								+ "<th>" + list[i].replyWriter + "</th>"
    								+ "<th>" + list[i].replyContent + "</th>"
    								+ "<th>" + list[i].createDate + "</th>"
    							+ "</tr>";
    				}
    				
    				$("#replyArea tbody").html(value);
    				$("#rcount").text(list.length); // 해당 게시글에 달린 댓글 개수도 동적으로 표시
    			},
    			error : () => { // function()
    				console.log("댓글 조회 실패");
    			}
    		})
    	}
    	
    </script>
    
    <jsp:include page="../common/footer.jsp" />

</body>
</html>