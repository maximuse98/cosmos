package models;

import entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class Order {
    private SimpleStringProperty id;
    private SimpleStringProperty clientName;
    private SimpleStringProperty requestName;
    private SimpleStringProperty contractName;
    private Boolean payment;
    private SimpleStringProperty beginDate;
    private SimpleStringProperty endDate;

    private ClientEntity client;
    private ClientRequestEntity request;
    private ClientOrderEntity order;

    private ObservableList<OrderProduct> ordersProducts;
    private SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dt2 = new SimpleDateFormat("dd.MM.yyyy");

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public Order(ClientOrderEntity order) {
        this.id = new SimpleStringProperty(Integer.toString(order.getId()));
        this.client = order.getClientByClientId();
        this.clientName = createClientName();
        this.payment = order.getPayment()!=0;
        this.contractName = new SimpleStringProperty(order.getContract());
        this.beginDate = createDateString(order.getBeginDate());
        this.endDate = createDateString(order.getEndDate());
        this.requestName = new SimpleStringProperty();
        this.order = order;

        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM ClientRequestEntity WHERE id = "+order.getRequestId());
        Iterator iter2 = query1.list().iterator();
        while(iter2.hasNext()){
            this.request = (ClientRequestEntity) iter2.next();
            this.requestName = new SimpleStringProperty(request.getRequest());
        }
        session.close();
    }

    public ObservableList<OrderProduct> getOrdersProducts() {
        return ordersProducts;
    }

    public void setOrdersProducts(ObservableList<OrderProduct> ordersProducts) {
        this.ordersProducts = ordersProducts;
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        String s = this.getId();
        try {
            this.id.set(id);
            order.setId(Integer.valueOf(id));
            this.updateEntity();
        } catch (ParseException e) {
            this.id.set(s);
            order.setId(Integer.valueOf(s));
        }
    }

    public String getClientName() {
        return clientName.get();
    }

    public SimpleStringProperty clientNameProperty() {
        return clientName;
    }

    public void setClientName(String client) {
        String s = this.getClientName();
        try {
            this.clientName.set(client);

            String name = client.substring(0, client.indexOf(' '));
            String surname = client.substring(client.indexOf(' ') + 1, client.length());

            String hql1 = "FROM ClientEntity" +
                    " WHERE name LIKE '" + name + "' AND surname LIKE '" + surname + "'";
            Session session1 = sessionFactory.openSession();
            Query query1 = session1.createQuery(hql1);
            ClientEntity result = (ClientEntity) query1.list().get(0);

            order.setClientByClientId(result);
            this.updateEntity();
        } catch (Exception e) {
            this.clientName.set(s);
        }
    }

    public String getRequestName() {
        if(requestName.get() == null) return "";
        return requestName.get();
    }

    public SimpleStringProperty requestNameProperty() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        String s = this.getRequestName();
        this.requestName.set(requestName);
        try {
            String hql = "FROM ClientRequestEntity" +
                    " WHERE request LIKE '"+requestName+"'";
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql);
            ClientRequestEntity result = (ClientRequestEntity) query.list().get(0);
            session.close();

            order.setRequestId(result.getId());
            this.updateEntity();
        } catch (Exception e) {
            this.requestName.set(s);
        }
    }

    public String getContractName() {
        return contractName.get();
    }

    public SimpleStringProperty contractNameProperty() {
        return contractName;
    }

    public void setContractName(String contratName) {
        String s = this.getContractName();
        try {
            this.contractName.set(contratName);
            order.setContract(contratName);
            this.updateEntity();
        } catch (ParseException e) {
            this.contractName.set(s);
        }
    }

    public Boolean getPayment() {
        return payment;
    }

    public void setPayment(Boolean payment) {
        Boolean s = this.getPayment();
        try {
            this.payment = payment;
            order.setPayment(new Byte(String.valueOf(payment? 1:0)));
            this.updateEntity();
        } catch (ParseException e) {
            this.payment = s;
        }
    }

    public String getBeginDate() {
        return beginDate.get();
    }

    public SimpleStringProperty beginDateProperty() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) throws ParseException {
        String s = this.getBeginDate();
        try {
            this.beginDate.set(beginDate);
            order.setBeginDate(dt2.parse(beginDate));
            this.updateEntity();
        } catch (Exception e) {
            this.beginDate.set(s);
            order.setBeginDate(dt2.parse(beginDate));
        }
    }

    public String getEndDate() {
        return endDate.get();
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) throws ParseException {
        String s = this.getEndDate();
        try {
            this.endDate.set(endDate);
            order.setEndDate(dt2.parse(endDate));
            this.updateEntity();
        } catch (Exception e) {
            this.endDate.set(s);
            order.setEndDate(dt2.parse(s));
        }
    }

    public ClientOrderEntity getOrder() {
        return order;
    }

    public void setOrder(ClientOrderEntity order) {
        this.order = order;
    }

    private void updateEntity() throws ParseException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(order);
        session.getTransaction().commit();
        session.close();
    }

    private SimpleStringProperty createDateString(Date date){
        try{
            return new SimpleStringProperty(format.format(date));
        }
        catch (NullPointerException e){
            return new SimpleStringProperty("");
        }
    }

    private SimpleStringProperty createClientName(){
        try{
            return new SimpleStringProperty(client.getName()+" "+client.getSurname());
        }catch (NullPointerException e){
            return new SimpleStringProperty("");
        }
    }
}
