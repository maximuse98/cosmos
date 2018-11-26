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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class SavingDBTest {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Test
    public void checkClientSet(){
        ClientEntity clientEntity = new ClientEntity(new SimpleStringProperty("1"),new SimpleStringProperty("name1"),new SimpleStringProperty("surname1"),new SimpleStringProperty( "phone1"),new SimpleStringProperty("phone2"),new SimpleStringProperty("address1"),new SimpleStringProperty("email1"));
        Client client = new Client(clientEntity);
        client.setName("changedName");
        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM ClientEntity WHERE id=1");
        ClientEntity clientEntity1 = (ClientEntity) query1.list().get(0);
        assertEquals("error", client.getName(), clientEntity1.getName());
        session.close();
    }

    @Test
    public void checkInvoiceSet(){
        Session session = sessionFactory.openSession();
        InvoiceEntity invoiceEntity = new InvoiceEntity(Integer.toString(1));
        InvoiceProductEntity invoiceProductEntity = new InvoiceProductEntity();
        invoiceProductEntity.setInvoiceByInvoiceId(invoiceEntity);
        assertEquals("error", invoiceEntity.getId(), invoiceProductEntity.getInvoiceByInvoiceId().getId());
        session.close();
    }

    @Test
    public void checkOrderSet(){
        Session session = sessionFactory.openSession();
        ClientOrderEntity clientOrderEntity = new ClientOrderEntity(Integer.toString(1));
        OrderProductEntity orderProductEntity = new OrderProductEntity(Integer.toString(1));
        orderProductEntity.setClientOrderByOrderId(clientOrderEntity);
        assertEquals("error", orderProductEntity.getClientOrderByOrderId().getId(), clientOrderEntity.getId());
        session.close();
    }

    @Test
    public void checkRequestSet(){
        Session session = sessionFactory.openSession();
        ClientRequestEntity clientRequestEntity = new ClientRequestEntity(Integer.toString(1));
        RequestProductEntity requestProductEntity = new RequestProductEntity(Integer.toString(1));
        requestProductEntity.setClientRequestByRequestId(clientRequestEntity);
        assertEquals("error", requestProductEntity.getClientRequestByRequestId().getId(), clientRequestEntity.getId());
        session.close();
    }

    @Test
    public void checkProductSet(){
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        productsList.add(new Product(productEntity));
        assertEquals("error", productsList.get(0).getId(), Integer.toString(productEntity.getId()));

    }

    @Test
    public void checkAddClient(){
        ClientEntity clientEntity = new ClientEntity(new SimpleStringProperty("1"),new SimpleStringProperty("name1"),new SimpleStringProperty("surname1"),new SimpleStringProperty( "phone1"),new SimpleStringProperty("phone2"),new SimpleStringProperty("address1"),new SimpleStringProperty("email1"));
        clientEntity.setId(1);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new ClientEntity());
        session.getTransaction().commit();
        Query query1 = session.createQuery(" FROM ClientEntity");
        ClientEntity clientEntity1 = (ClientEntity) query1.list().get(1);
        //т.к. у нас автоинкремент, то айдишник добавленной сутности будет 1++=2
        assertTrue("error", clientEntity1.getId()==2);
        session.close();
    }

    @Test
    public void checkAddInvoice(){
        InvoiceEntity invoiceEntity = new InvoiceEntity(Integer.toString(1));
        invoiceEntity.setId(1);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new InvoiceEntity());
        session.getTransaction().commit();
        Query query1 = session.createQuery(" FROM InvoiceEntity");
        InvoiceEntity invoiceEntity1 = (InvoiceEntity) query1.list().get(1);
        //т.к. у нас автоинкремент, то айдишник добавленной сутности будет 1++=2
        assertTrue("error", invoiceEntity1.getId()==2);
        session.close();
    }
}
