package models;

import javafx.beans.property.SimpleStringProperty;

public class OrderProduct {
    private SimpleStringProperty productName;
    private SimpleStringProperty count;
    private SimpleStringProperty rest;

    public OrderProduct(String productName, int count, int rest) {
        this.productName = new SimpleStringProperty(productName);
        this.count = new SimpleStringProperty(Integer.toString(count));
        this.rest = new SimpleStringProperty(Integer.toString(rest));
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
