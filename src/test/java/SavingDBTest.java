import entity.*;
import javafx.beans.property.SimpleStringProperty;
import models.Client;
import models.Order;
import models.OrderProduct;
import models.Request;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import util.HibernateUtil;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class SavingDBTest {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Test
    //setAllClients
        public void checkClientName(){
        ClientEntity clientEntity = new ClientEntity(new SimpleStringProperty("1"),new SimpleStringProperty("name1"),new SimpleStringProperty("surname1"),new SimpleStringProperty( "phone1"),new SimpleStringProperty("phone2"),new SimpleStringProperty("address1"),new SimpleStringProperty("email1"));
        Client client = new Client(clientEntity);
        client.setName("changedName");
        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM ClientEntity WHERE id=1");
        ClientEntity clientEntity1 = (ClientEntity) query1.list().get(0);
        assertEquals("error", client.getName(), clientEntity1.getName());
        session.close();
    }

//    @Test
////    setAllRequests
//    public void checkClientID(){
//        ClientEntity clientEntity = new ClientEntity(new SimpleStringProperty("1"),new SimpleStringProperty("name1"),new SimpleStringProperty("surname1"),new SimpleStringProperty( "phone1"),new SimpleStringProperty("phone2"),new SimpleStringProperty("address1"),new SimpleStringProperty("email1"));
//        ClientRequestEntity clientRequestEntity = new ClientRequestEntity(new SimpleStringProperty("1"),new SimpleStringProperty("request1"),new Boolean("true"),new Boolean( "false"),new SimpleStringProperty("name1 surname1"));
//
//        String name = clientRequestEntity.getClientByClientId().getName();
//        String surname = clientRequestEntity.getClientByClientId().getSurname();
//
//        String hql = "FROM ClientEntity" +
//                " WHERE name LIKE '"+ name +"' AND surname LIKE '"+surname+"'";
//        Session session = sessionFactory.openSession();
//        Query query = session.createQuery(hql);
//        ClientEntity result = (ClientEntity) query.list().get(0);//check this
//        session.close();
//
//        ClientEntity clientByClientId = result;
//
//        RequestProductEntity requestProductEntity = new RequestProductEntity(Integer.toString(1));
//        requestProductEntity.setClientRequestByRequestId(clientRequestEntity);
//
//        //set ClientRequestEntity clientName
////        ClientEntity clientEntity = clientRequestEntity.getClientByClientId();
////        String name = clientEntity.getName();
////        String surname = clientEntity.getSurname();
////
////        String hql = "FROM ClientEntity" +
////                " WHERE name LIKE '"+ name +"' AND surname LIKE '"+surname+"'";
////        Session session = sessionFactory.openSession();
////        Query query = session.createQuery(hql);
////        ClientEntity clientRequestEntity_clientByClientID = (ClientEntity) query.list().get(0);
////        session.close();
////        requestProductEntity.setClientRequestByRequestId(clientRequestEntity);
//        assertEquals("have not such client id",Integer.toString(clientByClientId.getId()),requestProductEntity.getClientRequestByRequestId());
////        //request
//        //getClientName
//    }

    @Test
    public void checkOrders() throws ParseException {
        Session session = sessionFactory.openSession();
        ClientOrderEntity clientOrderEntity = new ClientOrderEntity(new SimpleStringProperty("1"), new SimpleStringProperty ("requestName1"), new SimpleStringProperty ("contractName1"), new SimpleStringProperty ("clientName1 clientSurname1"), new Boolean("true"));
        OrderProductEntity orderProductEntity = new OrderProductEntity(new SimpleStringProperty ("1"),new SimpleStringProperty ("1"), new SimpleStringProperty ("1"), new SimpleStringProperty ("productName1"));
        orderProductEntity.setClientOrderByOrderId(clientOrderEntity);
        Query query = session.createQuery(" FROM OrderProductEntity WHERE clientOrderByOrderId.id = 1");
        OrderProductEntity orderProductEntity2 = (OrderProductEntity) query.list().get(0);
        assertEquals("error",clientOrderEntity.getId(),orderProductEntity2.getClientOrderByOrderId().getId());
        session.close();
    }
}
