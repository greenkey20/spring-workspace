<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입 양식</title>
</head>
<body>
	<!--2022.2.17(목) 16h15 강사님께서 bootstrap으로 만들어주신 것 복사해서 붙여넣음
		이 페이지의 목적 = 사용자가 여러 정보들을 입력; 이 정보들은 form 태그로 감싸져있음 -> '회원 가입' 버튼 누름 = 회원 가입 처리 요청을 보냄 -> dispatcherServlet이 요청을 받음 -> MemberController의 어떤 메소드가 이 요청을 처리 = 사용자가 입력한 정보를 db로 들고 가서, MEMBER 테이블에 이 데이터를 추가/INSERT해줘야 함-->
	
	<!-- 메뉴바 -->
    <jsp:include page="../common/header.jsp" /> <!--상대 경로 -> 현재 위치로부터 1번/칸 상위로 올라감(../) = common 폴더 있음 -> 그 안의 파일에 접근-->

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>회원가입</h2>
            <br>

            <form action="insert.me" method="post" id="enrollForm">
                <div class="form-group">
                	<!--* = 필수 작성 영역 표시-->
                    <label for="userId">* ID : </label>                                           <!--name="userId" -> Spring에서는 Member 객체의 setUserId() 처리함-->
                    <input type="text" class="form-control" id="userId" placeholder="Please Enter ID" name="userId" required>
                    <!--2022.2.23(수) 12h 추가-->
                    <div id="checkResult" style="font-size:0.7em; display:none"></div>
                    <br>

                    <label for="userPwd">* Password : </label>
                    <input type="password" class="form-control" id="userPwd" placeholder="Please Enter Password" name="userPwd" required> <br>

                    <label for="checkPwd">* Password Check : </label>
                    <input type="password" class="form-control" id="checkPwd" placeholder="Please Enter Password" required> <br> <!--서버로 전송할 필요 없이, 이 페이지에서 비교하면 되므로, name 속성 부여하지 않음-->

                    <label for="userName">* Name : </label>
                    <input type="text" class="form-control" id="userName" placeholder="Please Enter Name" name="userName" required> <br>

                    <label for="email"> &nbsp; Email : </label>
                    <input type="text" class="form-control" id="email" placeholder="Please Enter Email" name="email"> <br>

                    <label for="age"> &nbsp; Age : </label>
                    <input type="number" class="form-control" id="age" placeholder="Please Enter Age" name="age"> <br>

                    <label for="phone"> &nbsp; Phone : </label>
                    <input type="tel" class="form-control" id="phone" placeholder="Please Enter Phone (-없이)" name="phone"> <br> <!--2022.2.17(목) 16h40 나의 관찰 = MEMBER 테이블 PHONE 컬럼의 자료형은 VARCHAR2(13 BYTE)으로 되어있는 바, - 포함 기재해도 문제는 없을 듯-->
                    
                    <label for="address"> &nbsp; Address : </label>
                    <input type="text" class="form-control" id="address" placeholder="Please Enter Address" name="address"> <br>
                    
                    <label for=""> &nbsp; Gender : </label> &nbsp;&nbsp;
                    <input type="radio" id="Male" value="M" name="gender"> <!--name 속성 안 써두면 아래 라디오버튼 2개 모두 체크 가능함 vs name 속성의 값이 동일한 라디오버튼 중 1개만 선택 가능함-->
                    <label for="Male">남자</label> &nbsp;&nbsp;
                    <input type="radio" id="Female" value="F" name="gender" checked>
                    <label for="Female">여자</label> &nbsp;&nbsp;
                </div> 
                <br>
                <div class="btns" align="center">
                    <button type="submit" class="btn btn-primary disabled">회원가입</button> <!--클래스 속성 disabled는 현재 적용 중인 bootstrap 관련임-->
                    <button type="reset" class="btn btn-danger">초기화</button>
                </div>
            </form>
        </div>
        <br><br>

    </div>
    
    <!--2022.2.23(수) 12h5 id 중복 체크-->
    <script>
    	$(function() {
    		const $idInput = $("#enrollForm input[name=userId]"); // 해당 요소를 변수로 빼둠; userId라는 id 속성 값은 header.jsp에도 존재하기 때문에, #userId로 식별할 수 없는 바, form 태그에 #enrollForm 속성 추가해서 이용/선택함
    		
    		$idInput.keyup(function() { // key가 눌렸다가 떼어지는 순간 이벤트 발생
    			// console.log($idInput.val()); // 회원 가입 양식의 id 입력창에 사용자가 입력 시, 뭐가 어떻게 찍히는지 확인
    			
    			// 회사 환경에 따라 쿼리문/select문 날리는 횟수로 Oracle 사용 요금 과금될 수 있는 바, 쿼리문 실행은 최소화하면 좋음 -> 최소 5글자(이상) 입력되어 있을 때만 id 중복 확인 처리 AJAX 요청 보내고자 함
    			if ($idInput.val().length >= 5) {
    				$.ajax({
    					url : "idCheck.me",
    					data : {checkId : $idInput.val()},
    					success : function(result) {
    						if (result == "NNNNN") { // 중복 id -> 사용 불가능
    							$("#checkResult").show();
    							$("#checkResult").css("color", "orange").text("해당 id가 이미 존재합니다/중복됩니다");
    							$("#enrollForm :submit").attr("disabled", true); // 12h35 나의 질문 = 선택자 :submit의 의미 모르겠음 ㅠ.ㅠ -> 12h45 jQuery 수업 '추가적인 선택자'상, :type속성값 = 입력 양식 선택자
                                // $idInput.focus(); // id 재입력 유도 <- workspace2 JSP project에서 작성했던 코드
    						}
    						else {
    							$("#checkResult").show(); // 12h40 나의 질문 = 이 메소드는 아래 css, text 메소드와  method chaining 안 되나?
    							$("#checkResult").css("color", "green").text("사용할 수 있는 id입니다");
    							$("#enrollForm :submit").removeAttr("disabled");
    						}
    					},
    					error : function() {
    						console.log("id 중복 체크용 AJAX 통신 실패")
    					}
    				})
    			} // if문 영역 끝
    			else { // 사용자가 5글자 이상 입력해서 id 중복 체크한 뒤, 몇 글자 지워서 회원 가입하려고 할 수도 있으니까, 아래 처리 내용 추가
    				$("#checkResult").hide();
    				$("#enrollForm :submit").attr("disabled", true);
    			}
    		})
    		
    	})
    </script>

    <!-- 푸터바 -->
    <jsp:include page="../common/footer.jsp" />

</body>
</html>