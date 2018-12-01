package models;

import entity.ClientEntity;
import entity.ClientRequestEntity;
import entity.InvoiceProductEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
    private ClientRequestEntity requestEntity;

    public Request(ClientRequestEntity clientRequest) {
        this.id = new SimpleStringProperty(Integer.toString(clientRequest.getId()));
        this.client = clientRequest.getClientByClientId();
        this.request = new SimpleStringProperty(clientRequest.getRequest());
        this.checked = clientRequest.getChecked() != 0;
        this.approved = clientRequest.getApproved() != 0;
        this.clientName = createClientName();

        this.requestEntity = clientRequest;
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
            requestEntity.setId(Integer.valueOf(id));
            this.updateEntity();
        }catch (Exception e){
            this.id.set(s);
            requestEntity.setId(Integer.valueOf(s));
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
        try {
            this.clientName.set(clientName);

            String name = clientName.substring(0, clientName.indexOf(' '));
            String surname = clientName.substring(clientName.indexOf(' ')+1, clientName.length());
            String hql = "FROM ClientEntity" +
                    " WHERE name LIKE '"+ name +"' AND surname LIKE '"+surname+"'";
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql);
            ClientEntity result = (ClientEntity) query.list().get(0);
            session.close();

            requestEntity.setClientByClientId(result);
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
        try {
            this.request.set(request);
            requestEntity.setRequest(request);
            this.updateEntity();
        }catch (Exception e){
            this.request.set(s);
            requestEntity.setRequest(s);
        }
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        Boolean s = this.getChecked();
        try {
            this.checked = checked;
            requestEntity.setChecked(new Byte(String.valueOf(checked? 1:0)));
            this.updateEntity();
        }catch (Exception e){
            this.checked = s;
            requestEntity.setChecked(new Byte(String.valueOf(s? 1:0)));
        }
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        Boolean s = this.getApproved();
        try {
            this.approved = approved;
            requestEntity.setApproved(new Byte(String.valueOf(approved? 1:0)));
            this.updateEntity();
        }catch (Exception e){
            this.approved = s;
            requestEntity.setApproved(new Byte(String.valueOf(s? 1:0)));
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
        session.update(requestEntity);
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

    public ClientRequestEntity getRequestEntity() {
        return requestEntity;
    }

    public void setRequestEntity(ClientRequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }
}
