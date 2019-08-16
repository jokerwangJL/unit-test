package com.creators.unit.test;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.JSONString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UnitTestApplicationTests {

	@Test(timeout = 1000)
	public void testTimeout() throws InterruptedException {
		TimeUnit.SECONDS.sleep(2);
		log.info("Complete");
	}

	@Test(expected = NullPointerException.class)
	public void testNullException() {
		throw new NullPointerException();
	}





}
