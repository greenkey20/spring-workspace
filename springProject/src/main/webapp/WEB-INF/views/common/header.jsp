<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>header</title>
<!--2022.2.15(화) 12h15 강사님께서 만들어주신 view단 html 파일 복사해서 붙여넣음-->
<!-- jQuery 라이브러리 -->
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<!-- 부트스트랩에서 제공하고 있는 스타일 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<!-- 부트스트랩에서 제공하고 있는 스크립트 -->
<script	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	
<!--2022.2.18(금) 16h35 Alertify framework <- https://alertifyjs.com/guide.html CDN 방식 복사&붙여넣기-->
<!-- JavaScript -->
<script src="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/alertify.min.js"></script>
<!-- CSS -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/alertify.min.css"/>
<!-- Default theme -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/default.min.css"/>
<!-- Semantic UI theme -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/semantic.min.css"/>
	
<style>
div {
	box-sizing: border-box;
}

#header {
	width: 80%;
	height: 100px;
	padding-top: 20px;
	margin: auto;
}

#header>div {
	width: 100%;
	margin-bottom: 10px;
}

#header_1 {
	height: 80%;
}

#header_2 {
	height: 60%;
}

#header_1>div {
	height: 100%;
	float: left;
}

#header_1_left {
	width: 30%;
	position: relative;
}

#header_1_center {
	width: 40%;
}

#header_1_right {
	width: 30%;
}

#header_1_left>img {
	height: 100%;
	position: absolute;
	margin: auto;
	top: 0px;
	bottom: 0px;
	right: 0px;
	left: 0px;
}

#header_1_right {
	text-align: center;
	line-height: 35px;
	font-size: 12px;
	text-indent: 35px;
}

#header_1_right>a {
	margin: 5px;
}

#header_1_right>a:hover {
	cursor: pointer;
}

#header_2>ul {
	width: 100%;
	height: 100%;
	list-style-type: none;
	margin: auto;
	padding: 0;
}

#header_2>ul>li {
	float: left;
	width: 25%;
	height: 100%;
	line-height: 55px;
	text-align: center;
}

#header_2>ul>li a {
	text-decoration: none;
	color: black;
	font-size: 18px;
	font-weight: 900;
}

#header_2 {
	border-top: 1px solid lightgray;
}

#header a {
	text-decoration: none;
	color: black;
}

/* 세부페이지마다 공통적으로 유지할 style */
.content {
	background-color: rgb(247, 245, 245);
	width: 80%;
	margin: auto;
}

.innerOuter {
	border: 1px solid lightgray;
	width: 80%;
	margin: auto;
	padding: 5% 10%;
	background-color: white;
}
</style>
</head>
<body>
	
	<!--2022.2.18(금) 11h45 회원 가입 처리 후 응답 view 지정하며 추가
		html 문서 읽을 때 태그를 먼저 읽음 vs 만약 태그가 script 문서 안에 있으면 태그가 태그인 줄 모름/태그를 태그로 인식/해석 못 함 -> error 발생
		실행 순서가 달라서/html 문서에서 script 구문보다 태그가 먼저 읽히기 때문에  script 영역 밖에  action tags 써야 함; action tag가 먼저 읽혀야 하는데, action tag가 script 영역 안에 있으면 코드는 '위->아래'로 읽혀서 실행되는 바, script가 읽힌 다음에 action tag가 읽히게 되는데, 그러면 action tag가 태그로 인식되기에는 이미 순서가 늦음-->
	<c:if test="${ not empty alertMsg }"> <!--alertMsg가 있으면 -> 1번 alert 창으로 띄워줌 + 내용 삭제해서 이 페이지 읽힐 때마다 alert 다시 뜨지 않도록 함-->
		<script>
			alertify.alert("[session alert]", "${ alertMsg }"); // session.alertMsg로 scope 표시해주면 더 좋긴 하나, 여기서는 생략
		</script>
		<c:remove var="alertMsg" scope="session" />
	</c:if>

	<div id="header">
		<div id="header_1">
			<div id="header_1_left">
				<img src="https://iei.or.kr/upload/teacher/u1000e_teacher_photo.jpg"
					alt="">
			</div>
			<div id="header_1_center"></div>
			<div id="header_1_right">
				<!--2022.2.15(화) 12h35 el 구문을 사용하는 jstl/core library 사용하여 로그인 전/후에 보여줄 화면 코드를 보다 편/간단하게 작성 가능-->
				<c:choose>
					<c:when test="${ empty loginUser }">
						<!-- 로그인 전 -->
						<a href="enrollForm.me">회원가입</a> <!--회원 가입 양식으로 forwarding 해주는 controller의 메소드 생성 필요-->
						<a data-toggle="modal" data-target="#loginModal">로그인</a> <!-- 모달의 원리 : 이 버튼 클릭시 data-targer에 제시되어있는 해당 아이디의 div요소를 띄워줌 -->
					</c:when>
					<c:otherwise>
						<!-- 로그인 후 -->
		                <label>${ loginUser.userName }님 환영합니다</label> &nbsp;&nbsp; <!--2022.2.17(목) 16h 수정-->
		                <a href="myPage.me">마이페이지</a>
		                <a href="logout.me">로그아웃</a> <!--2022.2.17(목) 15h45 실습 -> logout.me = 상대 경로 방식으로 요청 보냄; logout.me는 현재 내가 있는 위치/주소/경로(http://localhost:8008/spring/)의 마지막/뒤에 붙음 -> (16h5 강사님 설명 정확히 못 들은 것 같음 ㅠ.ㅠ)DispatcherServlet이 요청 받음 -> controller의 관련 메소드가 처리-->
					</c:otherwise>
                </c:choose>
			</div>
		</div>
		<div id="header_2">
			<ul>
				<li><a href="">HOME</a></li>
				<li><a href="">공지사항</a></li>
				<li><a href="list.bo">게시판</a></li>
				<li><a href="">etc</a></li>
			</ul>
		</div>
	</div>

	<!-- 로그인 클릭 시 뜨는 모달 (기존에는 안보이다가 위의 a 클릭 시 보임) -->
	<div class="modal fade" id="loginModal">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">Login</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>

				<form action="login.me" method="post"> <!--로그인 요청 받아주는 서버/controller-->
					<!-- Modal body -->
					<div class="modal-body">
						<label for="userId" class="mr-sm-2">ID : </label>
						<input type="text" class="form-control mb-2 mr-sm-2" placeholder="Enter ID" id="userId" name="userId"> <br> <!--2022.2.17(목) 11h15 Member 객체의 필드의 값과 동일한 name 속성 값을 부여-->
						<label for="userPwd" class="mr-sm-2">Password : </label>
						<input type="password" class="form-control mb-2 mr-sm-2" placeholder="Enter Password" id="userPwd" name="userPwd">
					</div>

					<!-- Modal footer -->
					<div class="modal-footer">
						<button type="submit" class="btn btn-primary">로그인</button>
						<button type="button" class="btn btn-danger" data-dismiss="modal">취소</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<br clear="both">
</body>
</html>