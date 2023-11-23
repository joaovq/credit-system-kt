package com.joaovq.appsystem

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(properties = ["PORT"])
@ActiveProfiles("test")
class AppSystemApplicationTests {

	@Test
	fun contextLoads() {
	}

}
