package com.example.demo.mapper;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Memo;

@Transactional
@SpringBootTest
class MemoMapperTest {

	@Autowired
	private MemoMapper mapper;
	@Test
	void test() {
		var memos = mapper.findAll("taro");
		
		var date = LocalDateTime.of(2000, 12, 10, 0, 0, 0);
		List<Memo> expected = List.of(new Memo(27L, "hoge", "fuga", date, date, "taro"),
				new Memo(28L, "hoge1", "fuga1", date, date, "taro"),
				new Memo(29L, "hoge2", "fuga2", date, date, "taro"));
		
		assertThat(memos.containsAll(expected) && expected.containsAll(memos), is(true));
	}

}
