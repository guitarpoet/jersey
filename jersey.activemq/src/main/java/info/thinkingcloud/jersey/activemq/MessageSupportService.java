package info.thinkingcloud.jersey.activemq;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;
import info.thinkingcloud.jersey.core.utils.BaseService;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.stereotype.Service;

@Service("activemq")
@Module(doc = "The jms message support plugin.")
public class MessageSupportService extends BaseService {
	@Autowired
	private JmsTemplate template;

	private static Logger logger = LoggerFactory.getLogger(MessageSupportService.class);

	@Function(doc = "Send the message to the message broker", parameters = {
	        @Parameter(name = "destination", type = "string", doc = "The destination of the message"),
	        @Parameter(name = "message", type = "string", doc = "The message to send.") })
	public void send(final String destination, final String message) {
		template.execute(destination, new ProducerCallback<TextMessage>() {
			@Override
			public TextMessage doInJms(Session session, MessageProducer producer) throws JMSException {
				TextMessage m = session.createTextMessage(message);
				logger.info("Ready to send message {} to destination {}", m, destination);
				producer.send(m);
				return m;
			}
		});
	}

	@Function(doc = "Receive the message from message broker", parameters = @Parameter(name = "destination", type = "string", doc = "The destination to receive."))
	public String receive(String destination) throws JMSException {
		TextMessage message = (TextMessage) template.receive(destination);
		logger.info("Received message {} from destination {}", message, destination);
		return message.getText();
	}
}
