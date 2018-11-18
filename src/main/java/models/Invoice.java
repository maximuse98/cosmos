package models;

import entity.ClientOrderEntity;
import entity.ClientRequestEntity;
import entity.InvoiceEntity;
import entity.InvoiceProductEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class Invoice {
    private SimpleStringProperty id;
    private SimpleStringProperty dateCreate;
    private Boolean agreed;
    private ClientOrderEntity order;
    private SimpleStringProperty contractName;

    private ObservableList<InvoiceProduct> invoiceProducts;

    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Invoice(InvoiceEntity invoice) {
        this.id = new SimpleStringProperty(Integer.toString(invoice.getId()));
        this.dateCreate = createDateString(invoice.getDateCreate());
        this.order = invoice.getClientOrderByOrderId();
        this.contractName = createContract();
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
        String s = this.getDateCreate();
        this.dateCreate.set(dateCreate);
        try {
            this.updateEntity();
        } catch (Exception e) {
            this.dateCreate.set(s);
        }
    }

    public Boolean getAgreed() {
        return agreed;
    }

    public void setAgreed(Boolean agreed) {
        Boolean s = this.getAgreed();
        this.agreed = agreed;
        try {
            this.updateEntity();
        } catch (ParseException e) {
            this.agreed = s;
        }
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        String s = this.getId();
        this.id.set(id);
        try {
            this.updateEntity();
        } catch (ParseException e) {
            this.id.set(s);
        }
    }

    public String getContractName() {
        return contractName.get();
    }

    public SimpleStringProperty contractNameProperty() {
        return contractName;
    }

    public void setContractName(String contractName) {
        String s = this.getContractName();
        this.contractName.set(contractName);
        try {
            this.updateEntity();
        } catch (ParseException e) {
            this.contractName.set(s);
        }
    }

    private void updateEntity() throws ParseException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(new InvoiceEntity(id,agreed,dateCreate,contractName));
        session.getTransaction().commit();
        session.close();
    }

    private SimpleStringProperty createDateString(Date date){
        try{
            return new SimpleStringProperty(format.format(date));
        }
        catch (NullPointerException e){
            return new SimpleStringProperty("");
        }
    }

    private SimpleStringProperty createContract(){
        try{
            return new SimpleStringProperty(order.getContract());
        }catch (NullPointerException e){
            return new SimpleStringProperty("");
        }
    }
}
