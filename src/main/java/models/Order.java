package models;

import entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.Iterator;

public class Order {
    private SimpleStringProperty id;
    private SimpleStringProperty clientName;
    private SimpleStringProperty requestName;
    private SimpleStringProperty contratName;
    private Boolean payment;

    private ClientEntity client;
    private ClientRequestEntity request;
    private ContractEntity contract;

    private ObservableList<OrderProduct> ordersProducts;

    private final Session session = HibernateUtil.getSessionFactory();

    public Order(ClientOrderEntity order) {
        this.id = new SimpleStringProperty(Integer.toString(order.getId()));
        this.client = order.getClientByClientId();
        this.clientName = new SimpleStringProperty(client.getName()+" "+client.getSurname());
        this.payment = order.getPayment() != 0;

        Query query1 = session.createQuery(" FROM ClientRequestEntity WHERE id = "+order.getRequestId());
        Iterator iter2 = query1.list().iterator();
        while(iter2.hasNext()){
            request = (ClientRequestEntity) iter2.next();
            this.requestName = new SimpleStringProperty(request.getRequest());
            this.contract = order.getContractByContractId();
            this.contratName = new SimpleStringProperty(contract.getContract());
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

    public String getContratName() {
        return contratName.get();
    }

    public SimpleStringProperty contratNameProperty() {
        return contratName;
    }

    public void setContratName(String contratName) {
        this.contratName.set(contratName);
    }

    public Boolean getPayment() {
        return payment;
    }

    public void setPayment(Boolean payment) {
        this.payment = payment;
    }
}
