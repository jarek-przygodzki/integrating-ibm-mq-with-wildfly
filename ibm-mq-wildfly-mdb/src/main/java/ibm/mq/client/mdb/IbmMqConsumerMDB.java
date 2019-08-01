package ibm.mq.client.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "useJNDI", propertyValue = "true"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jboss/test"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class IbmMqConsumerMDB implements MessageListener {

    @Override
    public void onMessage(Message m) {
        System.out.println(">> Received message: " + m);
    }
}