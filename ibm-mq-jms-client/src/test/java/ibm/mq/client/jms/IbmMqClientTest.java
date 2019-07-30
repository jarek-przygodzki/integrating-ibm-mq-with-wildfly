
package ibm.mq.client.jms;

import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.jms.*;
import java.time.Duration;
import java.util.UUID;

import static org.junit.Assert.*;

public class IbmMqClientTest {

    // will wait for up to 60 seconds for the container's first
    // mapped network port (1414) to start accepting connections
    @Rule
    public GenericContainer ibmMq = new GenericContainer<>("ibmcom/mq:9.1.3.0")
            .withExposedPorts(1414)
            .withEnv("LICENSE", "accept")
            .withEnv("MQ_QMGR_NAME", "QM1")
            .withStartupTimeout(Duration.ofMinutes(1));

    @Test public void testCreateJmsContext() throws Exception{
        String connName = String.format("%s (%d)", ibmMq.getContainerIpAddress(), ibmMq.getMappedPort(1414));
        try (JMSContext context = IbmMqClient.createContext(
                "DEV.ADMIN.SVRCONN", "QM1",
                "admin", "passw0rd", connName)) {
        }
    }

    @Test public void testJmsSendReceive() throws Exception{
        String connName = String.format("%s (%d)", ibmMq.getContainerIpAddress(), ibmMq.getMappedPort(1414));
        try (JMSContext context = IbmMqClient.createContext(
                "DEV.ADMIN.SVRCONN", "QM1",
                "admin", "passw0rd", connName)) {
            Queue queue = context.createQueue("queue:///" + "DEV.QUEUE.1");
            String msgId = UUID.randomUUID().toString();
            TextMessage m = context.createTextMessage(msgId);
            m.setStringProperty("msgId", msgId);
            context.createProducer().send(queue, m);
            String messageSelector = String.format("msgId = '%s'", msgId);
            JMSConsumer consumer = context.createConsumer(queue, messageSelector);
            TextMessage receivedMessage = (TextMessage) consumer.receive();
            assertEquals(receivedMessage.getText(), msgId);
        }
    }
}
