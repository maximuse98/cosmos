package models;

import entity.ClientOrderEntity;
import entity.ClientRequestEntity;
import entity.InvoiceEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Iterator;

public class Invoice {
    private SimpleStringProperty id;
    private SimpleStringProperty dateCreate;
    private Boolean agreed;
    private ClientOrderEntity order;
    private SimpleStringProperty contractName;

    private ObservableList<InvoiceProduct> invoiceProducts;

    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public Invoice(InvoiceEntity invoice) {
        this.id = new SimpleStringProperty(Integer.toString(invoice.getId()));
        this.dateCreate = new SimpleStringProperty(format.format(invoice.getDateCreate()));
        this.order = invoice.getClientOrderByOrderId();
        this.contractName = new SimpleStringProperty(order.getContract());
        this.agreed = invoice.getAgreed()!=0;
    }

    public ObservableList<InvoiceProduct> getInvoiceProducts() {
        return invoiceProducts;
    }

    public void setInvoiceProducts(ObservableList<InvoiceProduct> invoiceProducts) {
        this.invoiceProducts = invoiceProducts;
    }

    public String getDateCreate() {
        return dateCreate.get();
    }

    public SimpleStringProperty dateCreateProperty() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate.set(dateCreate);
    }

    public Boolean getAgreed() {
        return agreed;
    }

    public void setAgreed(Boolean agreed) {
        this.agreed = agreed;
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

    public String getContractName() {
        return contractName.get();
    }

    public SimpleStringProperty contractNameProperty() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName.set(contractName);
    }
}
