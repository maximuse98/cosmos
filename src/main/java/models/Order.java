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
        this.id.set(id);
        try {
            this.updateEntity();
        } catch (ParseException e) {
            this.id.set(s);
        }
    }

    public String getClientName() {
        return clientName.get();
    }

    public SimpleStringProperty clientNameProperty() {
        return clientName;
    }

    public void setClientName(String clientName) {
        String s = this.getClientName();
        this.clientName.set(clientName);
        try {
            this.updateEntity();
        } catch (ParseException e) {
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
            this.updateEntity();
        } catch (ParseException e) {
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
        this.contractName.set(contratName);
        try {
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
        this.payment = payment;
        try {
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

    public void setBeginDate(String beginDate) {
        String s = this.getBeginDate();
        this.beginDate.set(beginDate);
        try {
            this.updateEntity();
        } catch (ParseException e) {
            this.beginDate.set(s);
        }
    }

    public String getEndDate() {
        return endDate.get();
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        String s = this.getEndDate();
        this.endDate.set(endDate);
        try {
            this.updateEntity();
        } catch (ParseException e) {
            this.endDate.set(s);
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
        session.update(new ClientOrderEntity(id,requestName,contractName,clientName,beginDate,endDate,payment));
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
