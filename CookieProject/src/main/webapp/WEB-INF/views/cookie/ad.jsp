<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>팝업 광고 차단</title>
</head>
<body>
	<!--2022.3.2(수) 11h10
		구현 계획 = pop이라는 cookie가 있는지 검사 -> cookie가 없을 경우에만 광고(새창) 출력
		
		팝업 창에서 '오늘 하루 안 보기' 체크박스 체크하면 AJAX/비동기 방식으로 __?
		
		이걸 cookie 안 쓰고, server에 하루 저장해 두는 방법도 있긴 한데, 서버에 굳이 이렇게 덜 중요한 데이터 저장할 필요 없음
		중요한 정보는 서버(귀중한 자원)/session에 저장 vs 비교적 덜 중요한 정보는 client-side cookie(=사용자의 hard disc 어딘가)에 저장-->

	<!--if (cookie가 없으면) { -->
	<c:if test="${ empty cookie.pop }">
		<script>
			window.open("https://www.naver.com", "naver", "width=400, height=400");
		</script>
	</c:if>
	<!-- } -->

</body>
</html>