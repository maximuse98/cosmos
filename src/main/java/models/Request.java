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
    private SimpleStringProperty checked;
    private SimpleStringProperty approved;

    private ClientEntity client;
    private ObservableList<RequestProduct> requestsProducts;

    public Request(ClientRequestEntity clientRequest) {
        this.id = new SimpleStringProperty(Integer.toString(clientRequest.getId()));
        this.client = clientRequest.getClientByClientId();
        this.request = new SimpleStringProperty(clientRequest.getRequest());
        this.checked = new SimpleStringProperty(clientRequest.getChecked().toString());
        this.approved = new SimpleStringProperty(clientRequest.getApproved().toString());
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

    public String getChecked() {
        return checked.get();
    }

    public SimpleStringProperty checkedProperty() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked.set(checked);
    }

    public String getApproved() {
        return approved.get();
    }

    public SimpleStringProperty approvedProperty() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved.set(approved);
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
