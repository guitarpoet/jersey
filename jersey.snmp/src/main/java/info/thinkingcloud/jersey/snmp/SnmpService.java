package info.thinkingcloud.jersey.snmp;

import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.utils.BaseService;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Service;

@Service("snmp")
@Module(doc = "The snmp module for jersey")
public class SnmpService extends BaseService {
	private static Logger logger = LoggerFactory.getLogger(SnmpService.class);

	TransportMapping<UdpAddress> transport;

	protected void init() throws Exception {
		logger.info("Transport module is initializing...");
		transport = new DefaultUdpTransportMapping();
		transport.listen();
	}

	@PreDestroy
	public void cleanup() throws IOException {
		if (transport != null)
			transport.close();
	}
}
