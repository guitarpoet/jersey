package info.thinkingcloud.jersey.snmp;

import java.io.IOException;
import java.net.InetAddress;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class TestApplication {
	public static void main(String[] args) throws IOException {
		TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
		transport.listen();

		CommunityTarget target = new CommunityTarget(new UdpAddress(InetAddress.getByName("192.168.11.10"), 161),
		        new OctetString("public"));
		target.setVersion(SnmpConstants.version1);
		target.setRetries(2);
		target.setTimeout(1000);

		PDU data = new PDU();
		data.setType(PDU.GET);
		data.setRequestID(new Integer32(1));
		data.add(new VariableBinding(new OID("1.3.6.1.4.1.915.3.1.3.0")));

		Snmp snmp = new Snmp(transport);

		ResponseEvent event = snmp.get(data, target);
		System.out.println(event.getResponse());
	}
}
