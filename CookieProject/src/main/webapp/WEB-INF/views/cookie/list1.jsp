<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%--EL을 통한 cookie 접근 = ${ cookie.cookie명 }
 --%>
<html>
<head>
<meta charset="UTF-8">
<title>cookies 확인</title>
</head>
<body>
	<!--2022.3.2(수) 10h25-->
	
	<div>
		쿠키 : ${ cookie.test }
	</div>
	
	<div>
		<%--쿠키 있나요? ${ .. } --%>
		쿠키 있나요? : ${ cookie.test != null } <br> <!--null 값만 확인하려면 이 줄 코드 사용-->
		쿠키 있나요? : ${ not empty cookie.test } <!--cookie.test != null && cookie.test != "" -> 좀 더 명확하게? 확인하려면 이 줄 코드 사용-->
		<!--강사님 질문 = 위 2줄의 차이는 무엇일까?
			나의 생각 = test라는 cookie가 있긴 있는데, 값이 비어있다?
			강사님 설명 = 위 2줄의 결과는 동일-->
	</div>
	
	<div>
		쿠키 값 : ${ cookie.test }
	</div>

</body>
</html>