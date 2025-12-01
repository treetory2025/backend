package site.treetory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import site.treetory.global.config.TestContainersConfig;

@SpringBootTest
@Import(TestContainersConfig.class)
class TreetoryApplicationTests {

	@Test
	void contextLoads() {
	}

}
