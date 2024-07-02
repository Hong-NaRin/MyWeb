package com.myweb.board.model;

import java.util.ArrayList;

public interface BoardMapper {
	
	// 마이바티스는 인터페이스를 호출시키면, 연결될 수 있는 mapper.xml파일이 실행됨
	public String now();
	public ArrayList<BoardDTO> getList(); // 글 목록 조회
	public int regist(BoardDTO dto); // 글 등록 -> regist는 매개변수가 2개이기 때문에 DTO타입에 담아서 던진다
	public BoardDTO getContent(String bno); // 글 상세내용 -> 반환 타입은 BoardDTO, 매개변수 bno
	public int update(BoardDTO dto); // 글 수정
	public int delete(String bno); // 글 삭제 -> 성공 실패 여부를 받고 싶으면 int, 아니면 void
	public void increaseHit(String bno); // 조회수
}
