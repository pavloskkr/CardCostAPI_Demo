package com.etraveli.cardcostapi;

import com.etraveli.cardcostapi.model.CardClearingCost;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class CardcostapiApplicationTests {

	@MockBean
	private RedisTemplate<String, CardClearingCost> redisTemplate;

	@Test
	void contextLoads() {
	}

}
