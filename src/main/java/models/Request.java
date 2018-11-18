package models;

import entity.ClientEntity;
import entity.ClientRequestEntity;
import entity.InvoiceProductEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

import java.util.List;

public class Request {
    private SimpleStringProperty id;
    private SimpleStringProperty clientName;
    private SimpleStringProperty request;
    private Boolean checked;
    private Boolean approved;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private ClientEntity client;
    private ObservableList<RequestProduct> requestsProducts;

    public Request(ClientRequestEntity clientRequest) {
        this.id = new SimpleStringProperty(Integer.toString(clientRequest.getId()));
        this.client = clientRequest.getClientByClientId();
        this.request = new SimpleStringProperty(clientRequest.getRequest());
        this.checked = clientRequest.getChecked() != 0;
        this.approved = clientRequest.getApproved() != 0;
        this.clientName = createClientName();
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
        }catch (Exception e){
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
        }catch (Exception e){
            this.clientName.set(s);
        }
    }

    public String getRequest() {
        return request.get();
    }

    public SimpleStringProperty requestProperty() {
        return request;
    }

    public void setRequest(String request) {
        String s = this.getRequest();
        this.request.set(request);
        try {
            this.updateEntity();
        }catch (Exception e){
            this.request.set(s);
        }
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        Boolean s = this.getChecked();
        this.checked = checked;
        try {
            this.updateEntity();
        }catch (Exception e){
            this.checked = s;
        }
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        Boolean s = this.getApproved();
        this.approved = approved;
        try {
            this.updateEntity();
        }catch (Exception e){
            this.approved = s;
        }
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public ObservableList<RequestProduct> getRequestsProducts() {
        return requestsProducts;
    }

    public void setRequestsProducts(ObservableList<RequestProduct> requestsProducts) {
        this.requestsProducts = requestsProducts;
    }

    private void updateEntity(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(new ClientRequestEntity(id,request,checked,approved,clientName));
        session.getTransaction().commit();
        session.close();
    }

    private SimpleStringProperty createClientName(){
        try{
            return new SimpleStringProperty(client.getName()+" "+client.getSurname());
        }catch (NullPointerException e){
            return new SimpleStringProperty("");
        }
    }
}
