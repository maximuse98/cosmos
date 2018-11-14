package models;

import entity.InvoiceProductEntity;
import javafx.beans.property.SimpleStringProperty;

public class InvoiceProduct {
    private SimpleStringProperty id;
    private SimpleStringProperty productName;
    private SimpleStringProperty count;
    private Boolean loaded;

    public InvoiceProduct(InvoiceProductEntity invoiceProduct, String productName) {
        this.id = new SimpleStringProperty(Integer.toString(invoiceProduct.getId()));
        this.productName = new SimpleStringProperty(productName);
        this.count = new SimpleStringProperty(Integer.toString(invoiceProduct.getCount()));
        this.loaded = invoiceProduct.getLoaded()!=0;
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

    public Boolean getLoaded() {
        return loaded;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }
}
