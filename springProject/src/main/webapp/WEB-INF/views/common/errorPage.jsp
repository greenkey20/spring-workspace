<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>error page</title>
</head>
<body>
	<!--2022.2.17(목) 15h10-->
	<jsp:include page="header.jsp" /> <!--같은 폴더에 있는 파일을 include하고자 하므로, 파일명만 기재하면 됨-->

    <br>
    <div align="center">
        <img src="https://cdn2.iconfinder.com/data/icons/oops-404-error/64/208_balloon-bubble-chat-conversation-sorry-speech-256.png">
        <br><br>
        <h1 style="font-weight:bold;">${ errorMsg }</h1>
    </div>
    <br>

    <jsp:include page="footer.jsp" /> <!--같은 폴더에 있는 파일을 include하고자 하므로, 파일명만 기재하면 됨-->

</body>
</html>