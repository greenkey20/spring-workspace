<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 게시글 수정</title>
<style>
	#updateForm>table {width:100%;}
    #updateForm>table * {margin:5px;}
</style>
</head>
<body>
	<!--2022.2.22(화) 11h35 실습 + 14h15 강사님 설명-->
	<jsp:include page="../common/header.jsp" />

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>게시글 수정하기</h2>
            <br>

            <form id="updateForm" method="post" action="update.bo" enctype="multipart/form-data">
            	<input type="hidden" name="boardNo" value="${ b.boardNo }">
            	
                <table algin="center">
                    <tr>
                        <th><label for="title">제목</label></th>
                        <td><input type="text" id="title" class="form-control" value="${ b.boardTitle }" name="boardTitle" required></td>
                    </tr>
                    <tr>
                        <th><label for="writer">작성자</label></th>
                        <td><input type="text" id="writer" class="form-control" value="${ b.boardWriter }" readonly></td>
                        <!--게시글 작성 input 태그는 readonly 속성이 부여되어 있는 바, 수정 안 되기 때문에 name="boardWriter" 필요 없을 것 같아서 삭제함-->
                        <!--${ loginUser.userId } = 현재 로그인된 사용자의 userId -> 글 작성자 본인만 수정할 수 있으니까 이렇게 써도 될 것 같지만, request scope 찾은 다음, session scope 찾아 올라가야 하므로, 연산 비효율적일 수 있는 바, request scope의 b 객체 내용 쓰기로 함-->
                    </tr>
                    <tr>
                        <th><label for="upfile">첨부파일</label></th>
                        <td>
                            <input type="file" id="upfile" class="form-control-file border" name="reUpfile">
                            
                            <!--강사님께서는 아래와 같이, 기존 첨부파일 있는 경우에만 '현재 업로드된 파일' 메뉴 보이게 하심-->
                            <c:if test="${ not empty b.originName }">
	                           	 현재 업로드된 파일 : 
	                            <a href="${ b.changeName }" download="${ b.originName }">${ b.originName }</a>
	                            
	                            <!--아래 hidden input 태그(나는 필요한데, 사용자에게 보여줄 필요 없는 내용)는 form 태그 내에 아무 데나 적어도 되지만, 아래 값들은 첨부 파일이 있을 때만 넘기면 되므로, 관련 if/조건문 있는 곳에 기재하기로 함-->
				            	<input type="hidden" name="originName" value="${ b.originName }">
				            	<input type="hidden" name="changeName" value="${ b.changeName }">
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th><label for="content">내용</label></th>
                        <td><textarea id="content" class="form-control" rows="10" style="resize:none;" name="boardContent" required>${ b.boardContent }</textarea></td>
                    </tr>
                </table>
                <br>

                <div align="center">
                    <button type="submit" class="btn btn-primary">수정하기</button>
                    <button type="reset" class="btn btn-danger">이전으로</button>
                </div>
            </form>
        </div>
        <br><br>

    </div>
    
    <jsp:include page="../common/footer.jsp" />

</body>
</html>