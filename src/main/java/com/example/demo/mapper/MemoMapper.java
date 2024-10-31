package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.Memo;

@Mapper
public interface MemoMapper {

	Memo findById (Long id);
	
	List<Memo> findAll();
	
	void insert (Memo memo);
	
	void update (Memo memo);
	
	void delete (Long id);
	
	void rmvAl () ;
}
