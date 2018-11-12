package models;

import entity.ClientEntity;
import entity.ClientRequestEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.util.List;

public class Request {
    private SimpleStringProperty id;
    private SimpleStringProperty clientName;
    private SimpleStringProperty request;
    private Boolean checked;
    private Boolean approved;

    private ClientEntity client;
    private ObservableList<RequestProduct> requestsProducts;

    public Request(ClientRequestEntity clientRequest) {
        this.id = new SimpleStringProperty(Integer.toString(clientRequest.getId()));
        this.client = clientRequest.getClientByClientId();
        this.request = new SimpleStringProperty(clientRequest.getRequest());
        this.checked = new Boolean(clientRequest.getChecked()!=0);
        this.approved = new Boolean(clientRequest.getApproved()!=0);
        this.clientName = new SimpleStringProperty(client.getName()+" "+client.getSurname());
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

    public String getRequest() {
        return request.get();
    }

    public SimpleStringProperty requestProperty() {
        return request;
    }

    public void setRequest(String request) {
        this.request.set(request);
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
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
}
