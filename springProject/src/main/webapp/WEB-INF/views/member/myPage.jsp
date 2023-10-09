<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>my page</title>
</head>
<body>
	<!--2022.2.18(금) 14h40 강사님께서 bootstrap으로 만들어주신 것 복사해서 붙여넣음
		'마이페이지' a 태그는 로그인한 사용자에게만 보임 -> 사용자가 이 페이지에 왔다는 건 해당 사용자가 로그인된 상태 -> session에 loginUser 객체 들어있음
		session은 서버(Java 소스코드)의/서버가 가지고 있는 것(나는 이 web application의 것이라고 생각했음 >.<) -> 사용자의 입장에서 session은 브라우저의 것(브라우저 닫으면 session 만료됨)
		cookie = 서버가 가지고 있는 정보를 사용자(client)의 hard disc에 저장 e.g. 로그인 시 '이 광고 하루동안 보지 않기' 체크 여부
		cache = 작은 기억 장치
		cookie는 사용자의 hard disc에서 바로 가져오면 되므로 + 서버에서 받지 않아도 되므로, 사용자 입장에서 cookie의 속도는 session의 속도보다 더 빠름
		session은 서버에 있으므로 보안 측면에서 cookie보다 비교적 더 안전함 -> 14h50 나의 질문 = 왜, 어떻게, 그런거지?-->
	<jsp:include page="../common/header.jsp" />

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>마이페이지</h2>
            <br>
			
			<!--2022.2.18(금) 15h30 controller 작성 시작
				방법1) form 태그로 묶어서 Member 객체에 담아서 요청하기
				방법2) session에 담긴 loginUser 값들을 controller에서 빼고, Member 객체에 담아 db에 넘김 = 좀 더 귀찮은 방법-->
            <form action="update.me" method="post"> 
                <div class="form-group">
                    <label for="userId">* ID : </label> <!--id = 회원 식별자의 역할을 함 -> 요청 시 반드시 함께 넘겨야 db UPDATE 처리 가능-->
                    <input type="text" class="form-control" id="userId" value="${ loginUser.userId }" name="userId" readonly> <br>

                    <label for="userName">* Name : </label>
                    <input type="text" class="form-control" id="userName" value="${ loginUser.userName }" name="userName" required> <br>

                    <label for="email"> &nbsp; Email : </label>
                    <input type="text" class="form-control" id="email" value="${ loginUser.email }" name="email"> <br> <!--14h45 나의 질문 = 기존 입력해둔 email이 없는 경우, 어떻게 보이려나? null? ""?-->

                    <label for="age"> &nbsp; Age : </label>
                    <input type="number" class="form-control" id="age" value="${ loginUser.age }" name="age"> <br>

                    <label for="phone"> &nbsp; Phone : </label>
                    <input type="tel" class="form-control" id="phone" value="${ loginUser.phone }" name="phone"> <br>
                    
                    <label for="address"> &nbsp; Address : </label>
                    <input type="text" class="form-control" id="address" value="${ loginUser.address }" name="address"> <br>
                    
                    <label for=""> &nbsp; Gender : </label> &nbsp;&nbsp;
                    <input type="radio" id="Male" value="M" name="gender">
                    <label for="Male">남자</label> &nbsp;&nbsp;
                    <input type="radio" id="Female" value="F" name="gender">
                    <label for="Female">여자</label> &nbsp;&nbsp;
                    
                    <!--2022.2.18(금) 15h10-->
                    <script>
                    	$(function() {
                    		// 회원 가입 시 성별은 선택 입력 사항이므로, 값 없을 수 있음(현재 구현 화면에는 버튼 하나에 checked 속성을 주었기 때문에, 값 1개가 꼭 들어가 있긴 함) -> gender 필드의 값이  없는/빈 문자열일 경우, db에 담긴 값은 null vs el 특성상 빈 문자열이 반환되어 옴 -> 아래 조건절에서 빈 문자열 ""과 비교해야 함
                    		if ("${ loginUser.gender }" != "") { // db에 값이 없는 필드 출력 시, null(x) 빈 문자열 ""(o)이 나옴
                    			// 15h20 나의 질문 = ${ loginUser.gender }을 (쌍)따옴표로 감싸지 않으면, 어떤 문제가 있을까? -> 15h30 나의 관찰 = 'Syntax error, insert "}" to complete Block' 빨간줄 뜸 + 브라우저 개발자 도구 console에 "Uncaught ReferenceError: F is not defined" 오류 발생 + myPage.jsp 뜨긴 뜨는데 성별 라디오버튼 아무 것도 선택 안 되어있음
	                    		$('input[value="${ loginUser.gender }"]').attr("checked", true); // 15h55 나의 질문 = 사용자가 이름, 이메일 등에 그냥 M이나 F를 입력해둔 경우에 이 코드 실행에 문제 발생하지 않을까?
                    		}
                    	})
                    </script>
                    
                </div> 
                <br>
                <div class="btns" align="center">
                    <button type="submit" class="btn btn-primary">수정하기</button>
                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteForm">회원탈퇴</button>
                </div>
            </form>
        </div>
        <br><br>
        
    </div>

    <!-- 회원탈퇴 버튼 클릭 시 보여질 Modal -->
    <div class="modal fade" id="deleteForm">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">회원탈퇴</h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>

				<!--2022.2.18(금) 16h45 실습 + 17h20 강사님 설명-->
                <form action="delete.me" method="post">
                	<input type="hidden" name="userId" value="${ loginUser.userId }"> <!--2022.2.18(금) 17h25 강사님께서는 비밀번호 input 아래 줄에 쓰심-->
                    <!-- Modal body -->
                    <div class="modal-body">
                        <div align="center">
                            탈퇴 후 복구가 불가능합니다. <br>
                            정말로 탈퇴 하시겠습니까? <br>
                        </div>
                        <br>
                            <label for="userPwd" class="mr-sm-2">Password : </label>
                            <input type="password" class="form-control mb-2 mr-sm-2" placeholder="Enter Password" id="userPwd" name="userPwd"> <br>
                    </div>
                    <!-- Modal footer -->
                    <div class="modal-footer" align="center">
                        <button type="submit" class="btn btn-danger">탈퇴하기</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="../common/footer.jsp" />

</body>
</html>