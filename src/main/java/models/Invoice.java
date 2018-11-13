package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class Invoice {
    private SimpleStringProperty id;
    private Contract contract;
    private ObservableList<InvoiceProduct> invoiceProducts;
}
