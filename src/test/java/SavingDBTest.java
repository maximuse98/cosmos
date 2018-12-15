import entity.*;
import javafx.beans.property.SimpleStringProperty;
import models.*;
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
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class SavingDBTest {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dt2 = new SimpleDateFormat("dd.MM.yyyy");
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    //change part

    @Test
    public void checkClientChange() {
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
    public void checkInvoiceChange() throws ParseException {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(15);
        invoiceEntity.setDateCreate(dt2.parse("14.10.2018"));

        Session session1 = sessionFactory.openSession();
        session1.beginTransaction();
        session1.saveOrUpdate(invoiceEntity);
        session1.getTransaction().commit();
        session1.close();

        Invoice invoice = new Invoice(invoiceEntity);
        invoice.setDateCreate("14.10.2018");

        Session session = sessionFactory.openSession();
        //String date = new Date(2018,12,10).toString();
        String sql = " FROM InvoiceEntity WHERE id = " + invoiceEntity.getId();
        Query query1 = session.createQuery(sql);
        InvoiceEntity invoiceEntity1 = (InvoiceEntity) query1.list().get(0);
        session.close();
        assertEquals("error", invoiceEntity.getDateCreate(), invoiceEntity1.getDateCreate());
    }

    @Test
    public void checkOrderChange() {
        ClientOrderEntity orderEntity = new ClientOrderEntity();
        orderEntity.setId(1);
        orderEntity.setContract("contract1");

        Order order = new Order(orderEntity);
        order.setContractName("changedContract");

        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM ClientOrderEntity WHERE id=1");
        ClientOrderEntity clientOrderEntity1 = (ClientOrderEntity) query1.list().get(0);
        session.close();

        assertEquals("error", order.getContractName(), clientOrderEntity1.getContract());
    }

    @Test
    public void checkInvoiceProductChange() {
        InvoiceProductEntity invoiceProductEntity = new InvoiceProductEntity();
        invoiceProductEntity.setId(1);
        invoiceProductEntity.setCount(11);

        InvoiceProduct invoiceProduct = new InvoiceProduct(invoiceProductEntity, "Бинты");//нужно указать существующий продукт!!
        invoiceProduct.setCount(Integer.toString(15));

        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM InvoiceProductEntity WHERE id=1 ");
        InvoiceProductEntity invoiceProductEntity1 = (InvoiceProductEntity) query1.list().get(0);
        session.close();

        assertEquals("error", invoiceProduct.getCount(), invoiceProductEntity1.getCount().toString());
    }

    @Test
    public void checkOrderProductChange() {
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
    public void checkRequestChange() {
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
    public void checkRequestProductChange() {
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

    //add part

    @Test
    public void checkClientAdd() {
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

    @Test
    public void checkInvoiceAdd() throws ParseException {
        ClientOrderEntity clientOrderEntity = new ClientOrderEntity();
        clientOrderEntity.setId(4);

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setDateCreate(dt1.parse(dt1.format(dt2.parse("14.10.2018"))));
        invoiceEntity.setClientOrderByOrderId(clientOrderEntity);
        invoiceEntity.setAgreed(new Byte("1"));

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(invoiceEntity);
        session.getTransaction().commit();
        session.close();

        Session session2 = sessionFactory.openSession();
        Query query1 = session2.createQuery(" FROM InvoiceEntity WHERE clientOrderByOrderId=" + invoiceEntity.getClientOrderByOrderId().getId());
        InvoiceEntity invoiceEntity1 = (InvoiceEntity) query1.list().get(0);
        session2.close();

        invoiceEntity.setId(invoiceEntity1.getId());//так как у меня автоинкремент, я id заранее не знаю

        assertTrue("error", invoiceEntity.equals(invoiceEntity1));
    }

    @Test
    public void checkInvoiceProductAdd() throws ParseException {
        ClientOrderEntity clientOrderEntity = new ClientOrderEntity();
        clientOrderEntity.setId(3);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(21);
        invoiceEntity.setDateCreate(dt1.parse(dt1.format(dt2.parse("14.10.2018"))));
        invoiceEntity.setClientOrderByOrderId(clientOrderEntity);
        invoiceEntity.setAgreed(new Byte("1"));

        InvoiceProductEntity invoiceProductEntity = new InvoiceProductEntity();
        invoiceProductEntity.setCount(12);
        invoiceProductEntity.setInvoiceByInvoiceId(invoiceEntity);
        invoiceProductEntity.setLoaded(new Byte("1"));
        invoiceProductEntity.setProductByProductId(productEntity);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(invoiceProductEntity);
        session.getTransaction().commit();
        session.close();

        Session session2 = sessionFactory.openSession();
        String sql = " FROM InvoiceProductEntity WHERE productByProductId=" + invoiceProductEntity.getProductByProductId().getId();
        Query query1 = session2.createQuery(sql);
        InvoiceProductEntity invoiceProductEntity1 = (InvoiceProductEntity) query1.list().get(0);
        session2.close();

        invoiceProductEntity.setId(invoiceProductEntity1.getId());//так как у меня автоинкремент, я id заранее не знаю

        assertTrue("error", invoiceProductEntity.equals(invoiceProductEntity1));
    }


    @Test
    public void checkOrderAdd() throws ParseException {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(7);
        clientEntity.setName("");
        clientEntity.setSurname("");
        clientEntity.setEmail("");
        clientEntity.setAdress("");
        clientEntity.setPhone("");
        clientEntity.setPhone2("");

        ClientOrderEntity clientOrderEntity = new ClientOrderEntity();
        clientOrderEntity.setClientByClientId(clientEntity);
        clientOrderEntity.setContract("");
        clientOrderEntity.setPayment(new Byte("0"));
        clientOrderEntity.setRequestId(2);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(clientOrderEntity);
        session.getTransaction().commit();
        session.close();

        Session session2 = sessionFactory.openSession();
        Query query1 = session2.createQuery(" FROM ClientOrderEntity WHERE requestId=" + clientOrderEntity.getRequestId());
        ClientOrderEntity clientOrderEntity1 = (ClientOrderEntity) query1.list().get(0);
        session2.close();

        clientOrderEntity.setId(clientOrderEntity1.getId());//так как у меня автоинкремент, я id заранее не знаю
        clientOrderEntity.equals(clientOrderEntity1);
        assertTrue("error", clientOrderEntity.equals(clientOrderEntity1));
    }

    @Test
    public void checkOrderProductAdd(){
        ClientOrderEntity clientOrderEntity = new ClientOrderEntity();
        clientOrderEntity.setId(3);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);

        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setCount(12);
        orderProductEntity.setClientOrderByOrderId(clientOrderEntity);
        orderProductEntity.setRest(12);
        orderProductEntity.setProductByProductId(productEntity);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(orderProductEntity);
        session.getTransaction().commit();
        session.close();

        Session session2 = sessionFactory.openSession();
        String sql = " FROM OrderProductEntity WHERE productByProductId=" + orderProductEntity.getProductByProductId().getId();
        Query query1 = session2.createQuery(sql);
        OrderProductEntity orderProductEntity1 = (OrderProductEntity) query1.list().get(0);
        session2.close();

        orderProductEntity.setId(orderProductEntity1.getId());//так как у меня автоинкремент, я id заранее не знаю

        assertTrue("error", orderProductEntity.equals(orderProductEntity1));
    }

    @Test
    public void checkRequestAdd(){
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1);
        clientEntity.setName("name2");
        clientEntity.setSurname("surname2");
        clientEntity.setEmail("");
        clientEntity.setAdress("");
        clientEntity.setPhone("");
        clientEntity.setPhone2("");

        ClientRequestEntity clientRequestEntity = new ClientRequestEntity();
        clientRequestEntity.setRequest("some request");
        clientRequestEntity.setApproved(new Byte("0"));
        clientRequestEntity.setChecked(new Byte("0"));
        clientRequestEntity.setClientByClientId(clientEntity);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(clientRequestEntity);
        session.getTransaction().commit();
        session.close();

        Session session2 = sessionFactory.openSession();
        Query query1 = session2.createQuery(" FROM ClientRequestEntity WHERE clientByClientId=" + clientRequestEntity.getClientByClientId().getId());
        ClientRequestEntity clientRequestEntity1 = (ClientRequestEntity) query1.list().get(0);
        session2.close();

        clientRequestEntity.setId(clientRequestEntity1.getId());//так как у меня автоинкремент, я id заранее не знаю

        assertTrue("error", clientRequestEntity.equals(clientRequestEntity1));
    }

    @Test
    public void checkRequestProductAdd(){
        ClientRequestEntity clientRequestEntity = new ClientRequestEntity();
        clientRequestEntity.setId(3);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);

        RequestProductEntity requestProductEntity = new RequestProductEntity();
        requestProductEntity.setCount(12);
        requestProductEntity.setProductByProductId(productEntity);
        requestProductEntity.setClientRequestByRequestId(clientRequestEntity);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(requestProductEntity);
        session.getTransaction().commit();
        session.close();

        Session session2 = sessionFactory.openSession();
        Query query1 = session2.createQuery(" FROM RequestProductEntity WHERE clientRequestByRequestId=" + requestProductEntity.getClientRequestByRequestId().getId());
        RequestProductEntity requestProductEntity1 = (RequestProductEntity) query1.list().get(0);
        session2.close();

        requestProductEntity.setId(requestProductEntity1.getId());//так как у меня автоинкремент, я id заранее не знаю

        assertTrue("error", requestProductEntity.equals(requestProductEntity1));
    }

    //delete part

    @Test
    public void checkClientDelete() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setName("simpleClientName");

        Session session1 = sessionFactory.openSession();
        session1.beginTransaction();
        session1.save(clientEntity);
        session1.getTransaction().commit();
        session1.close();
        //клиент добавляется в бд

        Session session2 = sessionFactory.openSession();
        Query query1 = session2.createQuery(" FROM ClientEntity WHERE name LIKE 'simpleClientName'");
        ClientEntity clientEntity1 = (ClientEntity) query1.list().get(0);
        session2.close();

        Client client = new Client(clientEntity1);

        Session session3 = sessionFactory.openSession();
        session3.beginTransaction();
        session3.delete(client.getClientEntity());
        session3.getTransaction().commit();
        session3.close();

        Session session4 = sessionFactory.openSession();
        Query query2 = session4.createQuery(" FROM ClientEntity WHERE name LIKE '" + client.getName() + "'");
        assertTrue("error", query2.list().isEmpty());
        session4.close();
    }

    @Test
    public void checkInvoiceDelete(){
        ClientOrderEntity clientOrderEntity = new ClientOrderEntity();
        clientOrderEntity.setId(6);

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setClientOrderByOrderId(clientOrderEntity);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(invoiceEntity);
        session.getTransaction().commit();
        session.close();

        Session session1 = sessionFactory.openSession();
        Query query1 = session1.createQuery(" FROM InvoiceEntity WHERE clientOrderByOrderId=" + invoiceEntity.getClientOrderByOrderId().getId());
        InvoiceEntity invoiceEntity1 = (InvoiceEntity) query1.list().get(0);
        session1.close();

        Invoice invoice = new Invoice(invoiceEntity1);

        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        session2.delete(invoice.getInvoiceEntity());
        session2.getTransaction().commit();
        session2.close();

        Session session3 = sessionFactory.openSession();
        Query query2 = session3.createQuery(" FROM InvoiceEntity WHERE clientOrderByOrderId=" + invoice.getOrder().getId());
        assertTrue("error", query2.list().isEmpty());
        session3.close();
    }

    @Test
    public void InvoiceProductDelete(){
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(14);

        InvoiceProductEntity invoiceProductEntity = new InvoiceProductEntity();
        invoiceProductEntity.setInvoiceByInvoiceId(invoiceEntity);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(invoiceProductEntity);
        session.getTransaction().commit();
        session.close();

        Session session1 = sessionFactory.openSession();
        Query query1 = session1.createQuery(" FROM InvoiceProductEntity WHERE invoiceByInvoiceId=" + invoiceProductEntity.getInvoiceByInvoiceId().getId());
        InvoiceProductEntity invoiceProductEntity1 = (InvoiceProductEntity) query1.list().get(0);

        InvoiceProduct invoiceProduct = new InvoiceProduct(invoiceProductEntity1,"productNAme");

        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        session2.delete(invoiceProduct.getInvoiceProductEntity());
        session2.getTransaction().commit();
        session2.close();

        Session session3 = sessionFactory.openSession();
        Query query2 = session3.createQuery(" FROM InvoiceProductEntity WHERE invoiceByInvoiceId=" + invoiceProduct.getInvoiceProductEntity().getId());
        assertTrue("error", query2.list().isEmpty());
        session3.close();
    }

    @Test
    public void OrderDelete(){
        ClientOrderEntity clientOrderEntity = new ClientOrderEntity();
        clientOrderEntity.setContract("SimpleContractName");

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(clientOrderEntity);
        session.getTransaction().commit();
        session.close();

        Session session1 = sessionFactory.openSession();
        Query query1 = session1.createQuery(" FROM ClientOrderEntity WHERE contract LIKE 'SimpleContractName'");
        ClientOrderEntity clientOrderEntity1 = (ClientOrderEntity) query1.list().get(0);
        session1.close();

        Order order = new Order(clientOrderEntity1);

        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        session2.delete(order.getOrder());
        session2.getTransaction().commit();
        session2.close();

        Session session3 = sessionFactory.openSession();
        Query query2 = session3.createQuery(" FROM ClientOrderEntity WHERE contract LIKE '" + order.getContractName() + "'");
        assertTrue("error", query2.list().isEmpty());
        session3.close();
    }

    @Test
    public void OrderProductDelete(){
        ClientOrderEntity clientOrderEntity = new ClientOrderEntity();
        clientOrderEntity.setId(6);

        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setClientOrderByOrderId(clientOrderEntity);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(orderProductEntity);
        session.getTransaction().commit();
        session.close();

        Session session1 = sessionFactory.openSession();
        Query query1 = session1.createQuery(" FROM OrderProductEntity WHERE clientOrderByOrderId=" + orderProductEntity.getClientOrderByOrderId().getId());
        OrderProductEntity orderProductEntity1 = (OrderProductEntity) query1.list().get(0);

        OrderProduct orderProduct = new OrderProduct(orderProductEntity1, "someProductName");

        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        session2.delete(orderProduct.getOrderProduct());
        session2.getTransaction().commit();
        session2.close();

        Session session3 = sessionFactory.openSession();
        Query query2 = session3.createQuery(" FROM OrderProductEntity WHERE clientOrderByOrderId=" + orderProduct.getOrder().getId());
        assertTrue("error", query2.list().isEmpty());
        session3.close();
    }

    @Test
    public void RequestDelete(){
        ClientRequestEntity clientRequestEntity = new ClientRequestEntity();
        clientRequestEntity.setRequest("simpleRequestName");

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(clientRequestEntity);
        session.getTransaction().commit();
        session.close();

        Session session1 = sessionFactory.openSession();
        Query query1 = session1.createQuery(" FROM ClientRequestEntity WHERE request LIKE 'simpleRequestName'");
        ClientRequestEntity clientRequestEntity1 = (ClientRequestEntity) query1.list().get(0);
        session1.close();

        Request request = new Request(clientRequestEntity1);

        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        session2.delete(request.getRequestEntity());
        session2.getTransaction().commit();
        session2.close();

        Session session3 = sessionFactory.openSession();
        Query query2 = session3.createQuery(" FROM ClientRequestEntity WHERE request LIKE '" + request.getRequest() + "'");
        assertTrue("error", query2.list().isEmpty());
        session3.close();
    }

    @Test
    public void RequestProductDelete(){
        ClientRequestEntity clientRequestEntity = new ClientRequestEntity();
        clientRequestEntity.setId(3);

        RequestProductEntity requestProductEntity = new RequestProductEntity();
        requestProductEntity.setClientRequestByRequestId(clientRequestEntity);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(requestProductEntity);
        session.getTransaction().commit();
        session.close();

        Session session1 = sessionFactory.openSession();
        Query query1 = session1.createQuery(" FROM RequestProductEntity WHERE clientRequestByRequestId=" + requestProductEntity.getClientRequestByRequestId().getId());
        RequestProductEntity requestProductEntity1 = (RequestProductEntity) query1.list().get(0);

        RequestProduct requestProduct = new RequestProduct(requestProductEntity1, "someProductName");

        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        session2.delete(requestProduct.getRequestProduct());
        session2.getTransaction().commit();
        session2.close();

        Session session3 = sessionFactory.openSession();
        Query query2 = session3.createQuery(" FROM RequestProductEntity WHERE clientRequestByRequestId=" + requestProduct.getRequestProduct().getId());
        assertTrue("error", query2.list().isEmpty());
        session3.close();
    }
}

//    @Test
//    //setAllClients
//        public void checkClientName(){
//        ClientEntity clientEntity = new ClientEntity(new SimpleStringProperty("1"),new SimpleStringProperty("name1"),new SimpleStringProperty("surname1"),new SimpleStringProperty( "phone1"),new SimpleStringProperty("phone2"),new SimpleStringProperty("address1"),new SimpleStringProperty("email1"));
//        Client client = new Client(clientEntity);
//        client.setName("changedName");
//        Session session = sessionFactory.openSession();
//        Query query1 = session.createQuery(" FROM ClientEntity WHERE id=1");
//        ClientEntity clientEntity1 = (ClientEntity) query1.list().get(0);
//        assertEquals("error", client.getName(), clientEntity1.getName());
//        session.close();
//    }

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
//
//    @Test
//    public void checkOrders() throws ParseException {
//        Session session = sessionFactory.openSession();
//        ClientOrderEntity clientOrderEntity = new ClientOrderEntity(new SimpleStringProperty("1"), new SimpleStringProperty ("requestName1"), new SimpleStringProperty ("contractName1"), new SimpleStringProperty ("clientName1 clientSurname1"), new Boolean("true"));
//        OrderProductEntity orderProductEntity = new OrderProductEntity(new SimpleStringProperty ("1"),new SimpleStringProperty ("1"), new SimpleStringProperty ("1"), new SimpleStringProperty ("productName1"));
//        orderProductEntity.setClientOrderByOrderId(clientOrderEntity);
//        Query query = session.createQuery(" FROM OrderProductEntity WHERE clientOrderByOrderId.id = 1");
//        OrderProductEntity orderProductEntity2 = (OrderProductEntity) query.list().get(0);
//        assertEquals("error",clientOrderEntity.getId(),orderProductEntity2.getClientOrderByOrderId().getId());
//        session.close();
//    }
//}
