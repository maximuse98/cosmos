package models;

import entity.RequestProductEntity;
import javafx.beans.property.SimpleStringProperty;

public class RequestProduct {
    private SimpleStringProperty id;
    private SimpleStringProperty productName;
    private SimpleStringProperty count;

    public RequestProduct(RequestProductEntity requestProduct, String productName) {
        this.id = new SimpleStringProperty(Integer.toString(requestProduct.getId()));
        this.productName = new SimpleStringProperty(productName);
        this.count = new SimpleStringProperty(Integer.toString(requestProduct.getCount()));
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
}
