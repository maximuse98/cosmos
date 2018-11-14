package models;

import entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

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

    private ObservableList<OrderProduct> ordersProducts;

    private final Session session = HibernateUtil.getSessionFactory();
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public Order(ClientOrderEntity order) {
        this.id = new SimpleStringProperty(Integer.toString(order.getId()));
        this.client = order.getClientByClientId();
        this.clientName = new SimpleStringProperty(client.getName()+" "+client.getSurname());
        this.payment = order.getPayment() != 0;
        this.contractName = new SimpleStringProperty(order.getContract());
        this.beginDate = new SimpleStringProperty(format.format(order.getBeginDate()));
        this.endDate = new SimpleStringProperty(format.format(order.getEndDate()));

        Query query1 = session.createQuery(" FROM ClientRequestEntity WHERE id = "+order.getRequestId());
        Iterator iter2 = query1.list().iterator();
        while(iter2.hasNext()){
            this.request = (ClientRequestEntity) iter2.next();
            this.requestName = new SimpleStringProperty(request.getRequest());
        }
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
        this.id.set(id);
    }

    public String getClientName() {
        return clientName.get();
    }

    public SimpleStringProperty clientNameProperty() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }

    public String getRequestName() {
        return requestName.get();
    }

    public SimpleStringProperty requestNameProperty() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName.set(requestName);
    }

    public String getContractName() {
        return contractName.get();
    }

    public SimpleStringProperty contractNameProperty() {
        return contractName;
    }

    public void setContractName(String contratName) {
        this.contractName.set(contratName);
    }

    public Boolean getPayment() {
        return payment;
    }

    public void setPayment(Boolean payment) {
        this.payment = payment;
    }

    public String getBeginDate() {
        return beginDate.get();
    }

    public SimpleStringProperty beginDateProperty() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate.set(beginDate);
    }

    public String getEndDate() {
        return endDate.get();
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }
}
