package com.myweb.user.service;

import java.io.IOException;
import java.io.PrintWriter;

import com.myweb.user.model.UserDAO;
import com.myweb.user.model.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserServiceImpl implements UserService{

	@Override
	public void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// 값을 대신 받을 수 있음
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		
		// 중복되는 회원이 있는지 확인
		// 중복이 없는 경우 회원가입 처리 
		UserDAO dao = UserDAO.getInstance();
		int cnt = dao.findUser(id);
		
		if(cnt == 1) { // 아이디 중복
			request.setAttribute("msg", "이미 존재하는 회원입니다");
			request.getRequestDispatcher("join.jsp").forward(request, response); 
			// 경로에 /user를 붙이면 화면에서 보이지 않으며, 아이디가 중복되면 원래 화면으로 돌아옴
		}
		else { // 중복 x - 회원가입
			dao.insertUser(id, pw, name, email, gender); 
			response.sendRedirect("login.user"); // MVC2 방식에서는 리다이렉트는 다시 컨트롤러를 태울 때 사용 
		}
	}

	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		//로그인 시도
		UserDAO dao = UserDAO.getInstance();
		UserDTO dto = dao.login(id, pw);
		
		if(dto == null) { //아이디 or 비밀번호가 틀렸음
			request.setAttribute("msg", "아이디 또는 비밀번호를 확인하세요");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			
		} else { //로그인 성공
			// 세션에 로그인 성공에 대한 내용을 저장
			HttpSession session = request.getSession(); //리퀘스트에서 현재 세션을 얻음
			session.setAttribute("user_id", dto.getId() );
			session.setAttribute("user_name", dto.getName() );
			
			response.sendRedirect("mypage.user"); // 다시 컨트롤러를 채워 나간다
		}
	}
	@Override
	public void getInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//아이디 기반으로 회원정보를 조회해서 데이터를 가지고, 수정페이지로 이동
		//실습...
		/*
		1. 아이디는 세션이 있습니다.
		2. 아이디 기반으로 회원정보를 조회하는 getInfo() DAO에 생성합니다.
		3. 서비스에서는 getInfo() 호출하고, 조회한 데이터를 request에 저장합니다.
		4. forward를 이용해서 modify.jsp로 이동합니다.
		5. 회원정보를 input태그에 미리 출력해주면 됩니다.			
		*/	
		
		HttpSession session = request.getSession(); // 세션값에 저장되어 있는 걸 뽑음
		
		String id = (String)session.getAttribute("user_id"); 
		// request에는 따로 설정하지 않아 id가 없어서 session에서 뽑아서 써야함
		// session.getAttribute("user_id")은 반환 타입이 object이기 때문에 스트링으로 형변환
		
		// DAO 객체 생성
		UserDAO dao = UserDAO.getInstance(); // 데이터베이스 준비
		UserDTO dto = dao.getInfo(id);
		
		// dto를 클라이언트로 가지고 감
		request.setAttribute("dto", dto); // 이름, 값
		request.getRequestDispatcher("modify.jsp").forward(request, response);
	}
	
	@Override
	public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 클라이언트의 값을 받음
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		
		// parameter를 dto에 저장
		UserDTO dto = new UserDTO(id, pw, name, email, gender, null);
		
		// DAO 객체 생성
		UserDAO dao = UserDAO.getInstance();
		// 업데이트
		int result = dao.update(dto);
		
		if(result == 1) { // update 성공
			// 세션 name을 수정
			HttpSession session = request.getSession();
			session.setAttribute("user_name", name);
			
			// java에서 알림창을 화면에 보내는 방법
			// out객체 - 클라이언트로 문서 내용 
			response.setContentType("text/html; charset=UTF-8"); // 문서에 대한 타입
			PrintWriter	out = response.getWriter(); // 브라우저의 출력 타입
			out.println("<script>");
			out.println("alert('회원정보가 정상 수정되었습니다');");
			out.println("location.href='mypage.user';");
			out.println("</script>");	
		}
		else { // update 실패
			// 유저 페이지로 보냄
			response.sendRedirect("mypage.user"); // 보내줄 데이터가 없어서 리다이텍트 - MVC2는 리다이렉트가 컨트롤러의 경로가 된다.
		}
	}
	
	@Override
	public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 화면에서 넘어오는 pw값을 받기 (아이디는 세션에 있음)
		 * 2. 회원탈퇴는 비밀번호가 일치하는지 확인하고, 탈퇴를 진행함
		 * 	  -> login메서드는 id, pw를 받아서 비밀번호가 일치하는지 확인(재활용)
		 * 3. login메서드가 DTO를 반환하면 DAO에 delete() 메서드를 만들고, 회원 삭제를 진행함 
		 * 4. 탈뢰 성공시에는 세션을 전부 삭제하고, "홈화면"으로 리다이렉트, 
		 * 	  login 메서드가 null을 반환하면(비밀번호가 틀린 경우) delete.jsp에게 "비밀번호를 확인하세요" 메세지를 남겨주면 된다 
		 */
		
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("user_id"); // 아이디가 세션에 있다
		String pw = request.getParameter("pw");
		
		// pw가 일치하는지 login메서드로 확인
		UserDAO dao = UserDAO.getInstance();
		UserDTO dto = dao.login(id, pw);
				
		if(dto != null) { // pw가 일치한다는 의미
			// delete 실행
			dao.delete(id); // delete 실행
			session.invalidate(); // 세션 삭제
			
			response.sendRedirect(request.getContextPath() + "/index.jsp"); // 홈화면 절대경로로
		}
		else {
			request.setAttribute("msg", "비밀번호를 확인하세요");
			request.getRequestDispatcher("delete.jsp").forward(request, response);
			// 보낼 게 있으면 forward, 보낼 게 없으면 리다이렉트
		}
	}
}