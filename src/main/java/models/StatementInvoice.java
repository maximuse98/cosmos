package models;

import entity.InvoiceProductEntity;
import entity.StatementInvoiceView;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.Objects;

public class StatementInvoice {
    private SimpleStringProperty id;
    private SimpleStringProperty clientName;
    private SimpleStringProperty surname;
    private SimpleStringProperty phone;
    private SimpleStringProperty adress;
    private SimpleStringProperty productName;
    private SimpleStringProperty count;
    private Boolean loaded;

    //это только для checkBox
    private Boolean checkBoxStatus = false;

    private StatementInvoiceView statementInvoiceView;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public StatementInvoice(StatementInvoiceView statementInvoiceView) {
        this.id = new SimpleStringProperty(Integer.toString(statementInvoiceView.getId()));
        this.clientName = new SimpleStringProperty(statementInvoiceView.getClientName());
        this.surname = new SimpleStringProperty(statementInvoiceView.getClientSurname());
        this.phone = new SimpleStringProperty(statementInvoiceView.getPhone());
        this.adress = new SimpleStringProperty(statementInvoiceView.getAdress());
        this.productName = new SimpleStringProperty(statementInvoiceView.getProductName());
        this.count = new SimpleStringProperty(Integer.toString(statementInvoiceView.getCount()));
        this.loaded = statementInvoiceView.getLoaded() != 0;
        this.statementInvoiceView = statementInvoiceView;
    }

    public Boolean getCheckBoxStatus() {
        return checkBoxStatus;
    }

    public void setCheckBoxStatus(Boolean checkBoxStatus) {
        this.checkBoxStatus = checkBoxStatus;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
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

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getAdress() {
        return adress.get();
    }

    public SimpleStringProperty adressProperty() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress.set(adress);
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
        Boolean s = this.getLoaded();
        try {
            this.loaded = loaded;
            statementInvoiceView.setLoaded(new Byte(String.valueOf(loaded ? 1 : 0)));
            this.updateEntity();
        } catch (Exception e) {
            this.loaded = s;
            statementInvoiceView.setLoaded(new Byte(String.valueOf(s ? 1 : 0)));
        }
    }

    public StatementInvoiceView getStatementInvoiceView() {
        return statementInvoiceView;
    }

    public void setStatementInvoiceView(StatementInvoiceView statementInvoiceView) {
        this.statementInvoiceView = statementInvoiceView;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatementInvoice that = (StatementInvoice) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clientName, that.clientName) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(adress, that.adress) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(count, that.count) &&
                Objects.equals(loaded, that.loaded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientName, surname, phone, adress, productName, count, loaded);
    }

    private void updateEntity() {
        //ищем соответствующий InvoiceProduct по id
        Session session2 = sessionFactory.openSession();
        String sql = " FROM InvoiceProductEntity WHERE id=" + statementInvoiceView.getId();
        Query query1 = session2.createQuery(sql);
        InvoiceProductEntity invoiceProductEntity = (InvoiceProductEntity) query1.list().get(0);
        session2.close();

        //обновляем статус loaded
        invoiceProductEntity.setLoaded(statementInvoiceView.getLoaded());

        //мы обновляем не вьюшку, а InvoiceProduct
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(invoiceProductEntity);
        session.getTransaction().commit();
        session.close();
    }
}
