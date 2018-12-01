import entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import util.HibernateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class SavingDBTest {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-mm-dd");
    private SimpleDateFormat dt2 = new SimpleDateFormat("dd.mm.yyyy");
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Test
    public void checkClientChange(){
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1);
        clientEntity.setName("name1");
        clientEntity.setSurname("surname1");

        Client client = new Client(clientEntity);
        client.setName("changedName");

        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM ClientEntity WHERE surname LIKE 'surname1'");
        ClientEntity clientEntity1 = (ClientEntity) query1.list().get(0);
        session.close();

        assertEquals("error", client.getName(), clientEntity1.getName());
    }

    @Test
    public void checkClientAdd(){
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setName("name2");
        clientEntity.setSurname("surname2");
        clientEntity.setEmail("");
        clientEntity.setAdress("");
        clientEntity.setPhone("");
        clientEntity.setPhone2("");

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(clientEntity);
        session.getTransaction().commit();
        session.close();

        Session session2 = sessionFactory.openSession();
        Query query1 = session2.createQuery(" FROM ClientEntity WHERE name LIKE 'name2' ");
        ClientEntity clientEntity1 = (ClientEntity) query1.list().get(0);
        session2.close();

        clientEntity.setId(clientEntity1.getId());//так как у меня автоинкремент, я id заранее не знаю

        assertTrue("error", clientEntity.equals(clientEntity1));
    }

    /**
     * для срабатывания теста в бд НЕ должно быть клиента с именем "simpleClientName"
     */
    @Test
    public void checkClientDelete(){
        this.addSimpleClient();//клиент добавляется в бд

        Session session1 = sessionFactory.openSession();
        Query query1 = session1.createQuery(" FROM ClientEntity WHERE name LIKE 'simpleClientName'");
        ClientEntity clientEntity1 = (ClientEntity) query1.list().get(0);

        Client client = new Client(clientEntity1);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(client.getClientEntity());
        session.getTransaction().commit();
        session.close();

        Session session2 = sessionFactory.openSession();
        Query query2 = session2.createQuery(" FROM ClientEntity WHERE name LIKE '"+client.getName()+"'");
        assertTrue("error",query2.list().isEmpty());
        session2.close();
    }

    public void addSimpleClient(){
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setName("simpleClientName");

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(clientEntity);
        session.getTransaction().commit();
        session.close();
    }

    //doesn`t work
//    @Test
//    public void checkInvoiceChange() throws ParseException {
//        InvoiceEntity invoiceEntity = new InvoiceEntity();
//        invoiceEntity.setDateCreate(dt1.parse(dt1.format(dt2.parse("14.10.2018"))));
//
//        Session session1 = sessionFactory.openSession();
//        session1.beginTransaction();
//        session1.save(invoiceEntity);
//        session1.getTransaction().commit();
//        session1.close();
//
//        Invoice invoice = new Invoice(invoiceEntity);
//        invoice.setDateCreate("14.10.2018");
//
//        Session session = sessionFactory.openSession();
//        //String date = new Date(2018,12,10).toString();
//        String sql = " FROM InvoiceEntity WHERE dateCreate LIKE "+format.format(invoiceEntity.getDateCreate());
//        Query query1 = session.createQuery(sql);
//        InvoiceEntity  invoiceEntity1 = (InvoiceEntity) query1.list().get(0);
//        session.close();
//        assertEquals("error", invoiceEntity.getDateCreate(), invoiceEntity1.getDateCreate());
//    }
//    //doesn`t work
//    @Test
//    public void checkInvoiceAdd(){
////        InvoiceEntity invoiceEntity = new InvoiceEntity();
////        invoiceEntity.setId(1);
////        invoiceEntity.setDateCreate();
////        clientEntity.setSurname("surname2");
////        clientEntity.setEmail("");
////        clientEntity.setAdress("");
////        clientEntity.setPhone("");
////        clientEntity.setPhone2("");
////
////        Session session = sessionFactory.openSession();
////        session.beginTransaction();
////        session.save(clientEntity);
////        session.getTransaction().commit();
////        session.close();
////
////        Session session2 = sessionFactory.openSession();
////        Query query1 = session2.createQuery(" FROM ClientEntity WHERE name LIKE 'name2' ");
////        ClientEntity clientEntity1 = (ClientEntity) query1.list().get(0);
////        session2.close();
////
////        clientEntity.setId(clientEntity1.getId());//так как у меня автоинкремент, я id заранее не знаю
////
////        assertTrue("error", clientEntity.equals(clientEntity1));
////    }
//    }

    @Test
    public void checkOrderChange(){
        ClientOrderEntity orderEntity = new ClientOrderEntity();
        orderEntity.setId(4);
        orderEntity.setContract("contract1");

        Order order = new Order(orderEntity);
        order.setContractName("changedContract");

        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM ClientOrderEntity WHERE id=4");
        ClientOrderEntity clientOrderEntity1 = (ClientOrderEntity) query1.list().get(0);
        session.close();

        assertEquals("error", order.getContractName(), clientOrderEntity1.getContract());
    }

    @Test
    public void checkInvoiceProductChange(){
        InvoiceProductEntity invoiceProductEntity = new InvoiceProductEntity();
        invoiceProductEntity.setId(1);
        invoiceProductEntity.setCount(11);

        InvoiceProduct invoiceProduct = new InvoiceProduct(invoiceProductEntity, "fghjkl");
        invoiceProduct.setCount(Integer.toString(15));

        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM InvoiceProductEntity WHERE id=1 ");
        InvoiceProductEntity invoiceProductEntity1 = (InvoiceProductEntity) query1.list().get(0);
        session.close();

        assertEquals("error", invoiceProduct.getCount(), invoiceProductEntity1.getCount().toString());
    }

    @Test
    public void checkOrderProductChange(){
        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setId(1);
        orderProductEntity.setCount(14);

        OrderProduct orderProduct = new OrderProduct(orderProductEntity, "fvjkl");
        orderProduct.setCount(Integer.toString(10));

        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM OrderProductEntity WHERE id=1 ");
        OrderProductEntity orderProductEntity1 = (OrderProductEntity) query1.list().get(0);
        session.close();

        assertEquals("error", orderProduct.getCount(), orderProductEntity1.getCount().toString());
    }

    @Test
    public void checkRequestChange(){
        ClientRequestEntity clientRequestEntity = new ClientRequestEntity();
        clientRequestEntity.setId(1);
        clientRequestEntity.setRequest("some request");

        Request request = new Request(clientRequestEntity);
        request.setRequest("changed request");

        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM ClientRequestEntity WHERE id=1 ");
        ClientRequestEntity clientRequestEntity1 = (ClientRequestEntity) query1.list().get(0);
        session.close();

        assertEquals("error", request.getRequest(), clientRequestEntity.getRequest());
    }

    @Test
    public void checkRequestProductChange(){
        RequestProductEntity requestProductEntity = new RequestProductEntity();
        requestProductEntity.setId(1);
        requestProductEntity.setCount(15);

        RequestProduct requestProduct = new RequestProduct(requestProductEntity, "fvjkl");
        requestProduct.setCount(Integer.toString(10));

        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM RequestProductEntity WHERE id=1 ");
        RequestProductEntity requestProductEntity1 = (RequestProductEntity) query1.list().get(0);
        session.close();

        assertEquals("error", requestProduct.getCount(), requestProductEntity1.getCount().toString());
    }
}