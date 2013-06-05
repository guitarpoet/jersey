package info.thinkingcloud.jersey.snmp;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test_snmp_context.xml")
public class TestSNMPService {
	@Autowired
	private SnmpService snmp;

	@Test
	public void testSnmp() throws IOException {
		assertThat(snmp, notNullValue());
	}
}