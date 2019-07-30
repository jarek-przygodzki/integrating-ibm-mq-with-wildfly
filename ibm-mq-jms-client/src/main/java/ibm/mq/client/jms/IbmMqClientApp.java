
package ibm.mq.client.jms;

import org.testcontainers.containers.GenericContainer;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import java.time.Duration;

public class IbmMqClientApp {
    public static void main(String[] args) throws JMSException {
        // The default configuration for the IBM MQ Server includes a user of admin with a password of passw0rd.
        // https://github.com/ibm-messaging/mq-container
        try (GenericContainer ibmMq = new GenericContainer<>("ibmcom/mq:9.1.3.0")
                .withExposedPorts(1414)
                .withEnv("LICENSE", "accept")
                .withEnv("MQ_QMGR_NAME", "QM1")
                .withStartupTimeout(Duration.ofMinutes(1))) {
            ibmMq.start();
            String connName = String.format("%s (%d)", ibmMq.getContainerIpAddress(), ibmMq.getMappedPort(1414));
            try (JMSContext context = IbmMqClient.createContext(
                    "DEV.ADMIN.SVRCONN", "QM1",
                    "admin", "passw0rd", connName)) {
                Queue queue = context.createQueue("queue:///" + "DEV.QUEUE.1");
            }
        }
    }
}
