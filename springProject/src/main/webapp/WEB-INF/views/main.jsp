<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>main</title>
</head>
<body>
	<!--2022.2.15(화) 12h5-->
	여긴 main.jsp에요~
	
	<!--2022.2.15(화) 12h15 강사님께서 만들어주신 header 및 footer 파일을 여기에 include-->
	<jsp:include page="common/header.jsp" /> <!--맨 앞에 slash 없음 = 상대 경로 표시 -> 이 jsp 파일과 common 폴더는 같은 level에 있으므로, common 폴더에 바로 접근 가능-->
	
	<!--<div style="height:600px;"></div>-->

	<!--2022.2.24(목) 11h30 추가-->	
	<div class="content">
	<br><br>
		<div class="innerOuter">
			<h4>게시글 top 5</h4>
			<br>
			
			<!--2022.2.24(목) 12h35 추가-->
			<a href="list.bo" style="float:right; color:lightgreen;">더 보기 >></a>
			<br><br>
			
			<table id="boardList" class="table table-hover" align="center">
				<thead>
					<tr>
						<th>글번호</th>
						<th>제목</th>
						<th>작성자</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>첨부파일</th>
					</tr>
				</thead>
				<tbody>
					<!--현재 조회 수가 가장 높은 상위 5개의 게시글을 조회해서 표시 <- AJAX 이용-->
				</tbody>
			</table>
		</div>
		<br><br>
	</div>
	
	<script>
		$(function() {
			topBoardList();
			
			// 2022.2.24(목) 12h35 추가 -> 1초마다 갱신하며 페이지 느려질 수 있는 바, 작동하는 것 확인만 하고, 주석 처리해 둠
			// setInterval(topBoardList, 1000); // AJAX 요청/응답하면 (브라우저에?)resources 쌓이고 페이지가 느려지기 때문에, 1초마다 갱신하기보다는 보통 5분, 10분 단위로 setInterval 설정
			
			// 2022.2.24(목) 12h40
			// boardListView.jsp에서 했던 아래 방식은 작동 안 됨 <- 나의 생각 = 코드 실행 순서 상 안 되는 것 같아요(x) jQuery 수업 '12이벤트' 내용 = 동적으로 생성된(html 문서에 작성한(x) 데이터 받아서 추후에 생성한(o)) 요소에는 이 방법 적용이 불가능함(o)
			$("#boardList>tbody>tr").click(function() {
				location.href = 'detail.bo?bno=' + $(this).children().eq(0).text();
			})
			
			// 동적으로 만들어진 요소에 이벤트 부여하는 방법★★★★★꼭 기억하기★★★★★
			$(document).on("click", "#boardList>tbody>tr", function() {
				location.href = 'detail.bo?bno=' + $(this).children().eq(0).text();
			})
		})
		
		function topBoardList() {
			$.ajax({
				url : "topList.bo",
				success : function(data) {
					console.log(data); // 어떻게 data가 넘어오는지 보고, 내가 어떻게 활용할지 생각해보기 -> '객체 배열'로 옴 -> '인덱스'로 객체의 각 원소에 접근 + '.속성명(GSON 내부적으로 Board 객체의 각 필드명으로 잡힘)'으로 각 원소로 담긴 객체의 속성/필드에 접근

					let value = "";
					for (let i in data) {
						value += "<tr>"
									+ "<td>" + data[i].boardNo + "</td>"
									+ "<td>" + data[i].boardTitle + "</td>"
									+ "<td>" + data[i].boardWriter + "</td>"
									+ "<td>" + data[i].count + "</td>"
									+ "<td>" + data[i].createDate + "</td>"
									+ "<td>"; // 여기에 if문 등 이어서 쓰면 가독성 떨어지므로, 한 번 끊고, 아래 단락에 작성 -> 이렇게 필요할 때는 끊어서 작성하기
									
									if (data[i].originName != 0) { // 강사님께서는 null과 비교하시는데, 나는 falsey한 0으로 비교해봄
										value += "★"
									}
									
									value += "</td></tr>"
					} // for문 영역 끝
					
					$("#boardList>tbody").html(value);
				},
				error : function() {
					console.log("top5 게시글 조회 실패")
				}
			})
		}
	</script>
	
	<jsp:include page="common/footer.jsp" />

</body>
</html>