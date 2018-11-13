package models;

import entity.OrderProductEntity;
import javafx.beans.property.SimpleStringProperty;

public class OrderProduct {
    private SimpleStringProperty id;
    private SimpleStringProperty productName;
    private SimpleStringProperty count;
    private SimpleStringProperty rest;

    public OrderProduct(OrderProductEntity orderProduct, String productName) {
        this.id = new SimpleStringProperty(Integer.toString(orderProduct.getId()));
        this.productName = new SimpleStringProperty(productName);
        this.count = new SimpleStringProperty(Integer.toString(orderProduct.getCount()));
        this.rest = new SimpleStringProperty(Integer.toString(orderProduct.getRest()));
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

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getCount() {
        return count.get();
    }

    public SimpleStringProperty countProperty() {
        return count;
    }

    public void setCount(String count) {
        this.count.set(count);
    }

    public String getRest() {
        return rest.get();
    }

    public SimpleStringProperty restProperty() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest.set(rest);
    }
}
