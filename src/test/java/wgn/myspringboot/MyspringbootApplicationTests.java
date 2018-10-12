package wgn.myspringboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wgn.myspringboot.service.WgnTmpService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyspringbootApplicationTests {
	@Autowired
	private WgnTmpService wgnTmpService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testTransatction(){
		wgnTmpService.testTransaction();
	}


}
