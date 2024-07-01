package com.myweb.controller;

import java.io.IOException;

import com.myweb.board.service.BoardService;
import com.myweb.board.service.BoardServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("*.board") // .board로 끝나는 모든 요청은 서블릿으로 연결
public class BoardController extends HttpServlet {

	private BoardServiceImpl service;

	public BoardController() {
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//요청 분기
		request.setCharacterEncoding("utf-8");
		
		String uri = request.getRequestURI(); //ip, port번호 제외된 주소
		String path = request.getContextPath(); //프로젝트 식별 이름
		String command = uri.substring( path.length() );
		
		System.out.println(command);
		
		if(command.equals("/board/list.board")) { // 목록화면
			
			// mvc2의 기본 이름은 forward이다
			// 서비스 영역을 거쳐서 목록 data를 가지고 감
			service = new BoardServiceImpl();
			service.getList(request, response);
		}
		else if(command.equals("/board/write.board")) {
			request.getRequestDispatcher("board_write.jsp").forward(request, response);
		}
		else if(command.equals("/board/registForm.board")) { // 글 등록
			
			service = new BoardServiceImpl();
			service.regist(request, response);
		}
	}
}
	
