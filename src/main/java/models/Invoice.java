package models;

import entity.ClientOrderEntity;
import entity.ClientRequestEntity;
import entity.InvoiceEntity;
import entity.InvoiceProductEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
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
    private InvoiceEntity invoiceEntity;

    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dt2 = new SimpleDateFormat("dd.MM.yyyy");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Invoice(InvoiceEntity invoice) {
        this.id = new SimpleStringProperty(Integer.toString(invoice.getId()));
        this.dateCreate = createDateString(invoice.getDateCreate());
        this.order = invoice.getClientOrderByOrderId();
        this.contractName = createContract();
        this.agreed = invoice.getAgreed()!=0;

        this.invoiceEntity = invoice;
    }

    public ClientOrderEntity getOrder() {
        return order;
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

    public void setDateCreate(String dateCreate) throws ParseException {
        String s = this.getDateCreate();
        try {
            this.dateCreate.set(dateCreate);
            invoiceEntity.setDateCreate(dt2.parse(dateCreate));
            this.updateEntity();
        } catch (Exception e) {
            this.dateCreate.set(s);
            invoiceEntity.setDateCreate(dt2.parse(s));
        }
    }

    public Boolean getAgreed() {
        return agreed;
    }

    public void setAgreed(Boolean agreed) {
        Boolean s = this.getAgreed();
        this.agreed = agreed;
        try {
            this.agreed = agreed;
            invoiceEntity.setAgreed(new Byte(String.valueOf(agreed? 1:0)));
            this.updateEntity();
        } catch (ParseException e) {
            this.agreed = s;
            invoiceEntity.setAgreed(new Byte(String.valueOf(s? 1:0)));
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
        try {
            this.id.set(id);
            invoiceEntity.setId(Integer.valueOf(id));
            this.updateEntity();
        } catch (ParseException e) {
            this.id.set(s);
            invoiceEntity.setId(Integer.valueOf(s));
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
            this.contractName.set(contractName);

            String hql = "FROM ClientOrderEntity " +
                    " WHERE contract LIKE '"+ contractName+"'";
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql);
            ClientOrderEntity result = (ClientOrderEntity) query.list().get(0);
            session.close();

            if (result.getPayment()!=1){
                this.setAlert("Данный заказ еще не оплачен");
                return;
            }

            invoiceEntity.setClientOrderByOrderId(result);

            this.updateEntity();
        } catch (Exception e) {
            this.contractName.set(s);
        }
    }

    private void updateEntity() throws ParseException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(invoiceEntity);
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

    public InvoiceEntity getInvoiceEntity() {
        return invoiceEntity;
    }

    public void setInvoiceEntity(InvoiceEntity invoiceEntity) {
        this.invoiceEntity = invoiceEntity;
    }

    private void setAlert(String reason){
        Exception e = new Exception(reason);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Добавление невозможно");
        alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.toString())));
        alert.showAndWait();
    }
}
