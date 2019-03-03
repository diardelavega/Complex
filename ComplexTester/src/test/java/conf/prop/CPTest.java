package conf.prop;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.before.jobs.ConfigProperties;

public class CPTest {

	@Autowired
	ConfigProperties conpro;
	
	@Test
	public void test() {
//		fail("Not yet implemented");
		System.out.println(conpro.toString());
	}

}
