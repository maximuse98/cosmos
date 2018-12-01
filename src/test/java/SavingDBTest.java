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
}
