package com.example.demo.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Memo;

@Mapper
public interface MemoMapper {

//	Memo findById (Long id, String userName);
//	
//	Memo findByTitle (String title, String userName);
//	
//	Long memoCount(String userName);
	
	List<Memo> findAll(String userName);
	
	Memo findById (@Param("id")Long id, @Param("user_name")String userName);
	
	void insert (Memo memo);
	
	void update (@Param("id") Long id, @Param("title") String title,
			@Param("content") String content, @Param("updated") LocalDateTime updated);
	
	void delete (Long id);
//	
//	void rmvAl (String userName) ;
}
