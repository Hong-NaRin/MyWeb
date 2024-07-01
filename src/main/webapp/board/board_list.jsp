<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>    

<%@ include file="../include/header.jsp" %>

	<div class="container">
		<h3>My Web게시판</h3>

		<table class="table table-bordered">
			<thead>
				<tr>
					<th>글 번호</th>
					<th>작성자</th>
					<th>제목</th>
					<th>날짜</th>
					<th>조회수</th>
				</tr>
			</thead>

			<tbody>
				<c:forEach var="dto" items="${list }"> <!-- item은 반복 -->
			    <!-- BoardServiceImpl의 getlist()에 request.setAttribute("list", list);를 참조하여 "list"를 반복한다 -->
				<tr>
					<td>${dto.bno }</td>
					<td>${dto.writer }</td>
					<td>${dto.title }</td>
					<td><fmt:formatDate value="${dto.regdate }" pattern="yyyy년 MM월 dd일 HH시 mm분 ss초"/></td>
					<td>${dto.hit }</td>
				</tr>
				</c:forEach>
			</tbody>
			
			<tbody>
				<tr>
					<td colspan="5" align="right">
						<form action="" class="form-inline" >
						  <div class="form-group">
						    <input type="text" name="search" placeholder="제목검색" class="form-control" >
						  	<input type="submit" value="검색" class="btn btn-default">
							<input type="button" value="글 작성" class="btn btn-default" onclick="location.href='write.board';">
						  </div>
						</form> 
					</td>
				</tr>
			</tbody>
		
		</table>
	</div>

<%@ include file="../include/footer.jsp" %>
