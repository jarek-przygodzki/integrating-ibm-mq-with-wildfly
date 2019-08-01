package ibm.mq.client.mdb;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Stateless
@Path("/message")
public class IbmMqProducerResource {

    @Resource(mappedName = "java:/jboss/IbmMqConnectionFactory")
    ConnectionFactory cf;

    @Resource(mappedName = "java:/jboss/test")
    Queue queue;

    @POST
    @Path("")
    public String publishMessage(@QueryParam("body") String body) throws JMSException {
        try (JMSContext context = cf.createContext()) {
            TextMessage m = context.createTextMessage(body);
            context.createProducer()
                    .send(queue, m);
            System.out.println(">> Sent messsage: " + m);
            return m.getJMSMessageID();
        }
    }

}
