package com.myweb.board.service;

import java.io.IOException;
import java.util.ArrayList;

import com.myweb.board.model.BoardDAO;
import com.myweb.board.model.BoardDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BoardServiceImpl implements BoardService {

	@Override
	public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 사용자가 입력한 값을 받음
		String writer = request.getParameter("writer");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		BoardDAO dao = BoardDAO.getInstance();
		dao.regist(writer, title, content);
		
		// 목록 화면으로 이동시키기
		// 목록 화면을 redirect로 태워서 나감
		response.sendRedirect("list.board");
	}
	
	@Override
	public void getList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// DAO 생성
		BoardDAO dao = BoardDAO.getInstance();
		
		// 목록 조회
		ArrayList<BoardDTO> list = dao.getList();
		
		// request에 저장, 포워드로 나감
		request.setAttribute("list", list);
		request.getRequestDispatcher("board_list.jsp").forward(request, response);
		
	}

}