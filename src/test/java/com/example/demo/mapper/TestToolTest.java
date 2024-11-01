package com.example.demo.mapper;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Memo;

@Transactional
@SpringBootTest
class TestToolTest {
//MemoMapperTestで使うかもしれないメソッドに対してテスト
	
	@Autowired
	private MemoMapper memoMapper;
	 
	@Test
	void testMemoCount () {
		//expected
		var expected = 6L;
		
		var rst = memoMapper.memoCount();
		assertThat(rst, is(expected));
	}
	
	@Test
	void rmvAl () {
		var expected = 0L;
		
		memoMapper.rmvAl();
		var rst = memoMapper.memoCount();
		
		assertThat(rst, is(expected));
	}
	
	@Test
	void testFindByTitle () {
		var result = memoMapper.findByTitle("いい世来いよ");
		
		var expected = new Memo();
		expected.setId(5L);
		expected.setTitle("いい世来いよ");
		expected.setContent("胸にかけて胸に");
		expected.setCreated(LocalDateTime.of(1919, 8, 10, 0, 0));
		expected.setUpdated(LocalDateTime.of(2024, 10, 31, 0, 0));
		
		assertThat(result, is(expected));
	}

}
