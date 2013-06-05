package info.thinkingcloud.jersey.snmp;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;

import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Service;

@Service("snmp")
@Module(doc = "The snmp module for jersey")
public class SnmpService extends BaseService {
	private static Logger logger = LoggerFactory.getLogger(SnmpService.class);

	private TransportMapping<UdpAddress> transport;

	private int requestCounter = 1;

	@Override
	protected void init() throws Exception {
		LogFactory.setLogFactory(new Log4jLogFactory());
		logger.info(text("snmp.transport.init"));
		transport = new DefaultUdpTransportMapping();
		transport.listen();
	}

	public VariableBinding[] get(String host, String oid) throws IOException {
		return get(host, 161, oid);
	}

	@Function(doc = "Sending snmp get request", parameters = {
	        @Parameter(name = "host", type = "string", doc = "The host to send the request"),
	        @Parameter(name = "port", type = "int", doc = "The port to send the reuqest, default is 161", optional = true),
	        @Parameter(name = "oid", type = "string", doc = "The oid to request") })
	public VariableBinding[] get(String host, double port, String oid) throws IOException {
		logger.info(text("snmp.request.get", host, port, oid));
		CommunityTarget target = new CommunityTarget(
		        new UdpAddress(InetAddress.getByName(host), (int) Math.ceil(port)), new OctetString("public"));
		target.setVersion(SnmpConstants.version1);
		target.setRetries(2);
		target.setTimeout(1000);

		PDU data = new PDU();
		data.setType(PDU.GET);
		data.setRequestID(new Integer32(requestCounter++));
		data.add(new VariableBinding(new OID(oid)));

		Snmp snmp = new Snmp(transport);
		
		ResponseEvent event = snmp.get(data, target);
		if (event == null) {
			logger.error(text("snmp.request.get.timeout", host, port, oid));
		} else {
			PDU response = event.getResponse();
			if (response == null) {
				logger.error(text("snmp.request.get.pdu.null", host, port, oid));
			} else {
				if (response.getErrorStatus() != PDU.noError) {
					logger.error(text("snmp.request.get.error", host, port, oid, response.getErrorStatusText()));
				} else {
					return (VariableBinding[]) response.getVariableBindings().toArray(new VariableBinding[0]);
				}
			}
		}
		snmp.close();
		return new VariableBinding[0];
	}
	
	public void set() {
	    
    }

	@Override
	protected void destroy() throws Exception {
		logger.info(text("snmp.transport.destroy"));
		if (transport != null)
			transport.close();
	}
}
