package com.example.demo.mapper;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemoMapperTest {

	@Autowired
	private MemoMapper mapper;
//	@Test
//	void findAllTest() {
//		var memos = mapper.findAll("taro");
//		
//		var date = LocalDateTime.of(2000, 12, 10, 0, 0, 0);
//		List<Memo> expected = List.of(new Memo(27L, "hoge", "fuga", date, date, "taro"),
//				new Memo(28L, "hoge1", "fuga1", date, date, "taro"),
//				new Memo(29L, "hoge2", "fuga2", date, date, "taro"));
//		
//		assertThat(memos.containsAll(expected) && expected.containsAll(memos), is(true));
//	}
	
	//あとで動くテストを書く。
	
//	@Test
//	void findByIdTest () {
//		Long id = 27L;
//		String userName = "taro";
//		var date = LocalDateTime.of(2000, 12, 10, 0, 0, 0);
//		var expected = new Memo(27L, "hoge", "fuga",date, date, "taro"); 
//		var result = mapper.findById(id, userName);
//		assertThat(expected, is(result));
//	}
	
//	@Test
//	void insertTest() {
//		var memo = new Memo();
//		memo.setContent("content");
//		memo.setTitle("title");
//		memo.setCreated(LocalDateTime.now());
//		memo.setUserName("Alice");
//		mapper.insert(memo);
//		assertThat(0, is(0));
//	}

	@Test
	void deleteTest () {
		Long id = 39L;
		String userName = "taro";
		
		mapper.delete(id);
		
		assertThat(mapper.findById(id, userName), nullValue());
	}
	
	@Test
	void updateTest () {
		var pastObj = mapper.findById(49L, "Alice");
		var updated = LocalDateTime.now();
		mapper.update(49L, "h", "content" , updated);
		var result = mapper.findById(49L, "Alice");
		
		assertThat(result.getTitle(), is("h"));
		assertThat(result.getContent(), is("content"));
		var lag = Duration.between(result.getUpdated(),updated);
		assertThat(lag.toSeconds() < 1, is(true));
		
		assertThat(result.getId(), is(pastObj.getId()));
		assertThat(result.getCreated(), is(pastObj.getCreated()));
		assertThat(result.getUserName(), is(pastObj.getUserName()));
	}
}
