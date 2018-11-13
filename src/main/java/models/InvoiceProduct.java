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
}
