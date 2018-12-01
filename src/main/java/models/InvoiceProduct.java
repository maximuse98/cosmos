package models;

import entity.InvoiceEntity;
import entity.InvoiceProductEntity;
import entity.ProductEntity;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class InvoiceProduct {
    private SimpleStringProperty id;
    private SimpleStringProperty productName;
    private SimpleStringProperty count;
    private Boolean loaded;
    private InvoiceEntity invoice;

    private InvoiceProductEntity invoiceProductEntity;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public InvoiceProduct(InvoiceProductEntity invoiceProduct, String productName) {
        this.id = new SimpleStringProperty(Integer.toString(invoiceProduct.getId()));
        this.productName = new SimpleStringProperty(productName);
        this.count = createCount(invoiceProduct.getCount());
        this.loaded = invoiceProduct.getLoaded()!=0;
        this.invoice = invoiceProduct.getInvoiceByInvoiceId();

        this.invoiceProductEntity = invoiceProduct;
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
            invoiceProductEntity.setId(Integer.valueOf(id));
            this.updateEntity();
        }catch (Exception e) {
            this.id.set(s);
            invoiceProductEntity.setId(Integer.valueOf(s));
        }
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        String s = this.getProductName();
        try {
            this.productName.set(productName);

            String hql = " FROM ProductEntity " +
                    " WHERE name LIKE '"+ productName+"'";
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql);
            ProductEntity result = (ProductEntity) query.list().get(0);
            session.close();

            invoiceProductEntity.setProductByProductId(result);
            this.updateEntity();
        }catch (Exception e) {
            this.productName.set(s);
        }
    }

    public String getCount() {
        return count.get();
    }

    public SimpleStringProperty countProperty() {
        return count;
    }

    public void setCount(String count) {
        String s = this.getCount();
        try {
            this.count.set(count);
            invoiceProductEntity.setCount(Integer.valueOf(count));
            this.updateEntity();
        }catch (Exception e) {
            this.count.set(s);
            invoiceProductEntity.setCount(Integer.valueOf(s));
        }
    }

    public Boolean getLoaded() {
        return loaded;
    }

    public void setLoaded(Boolean loaded) {
        Boolean s = this.getLoaded();
        try {
            this.loaded = loaded;
            invoiceProductEntity.setLoaded(new Byte(String.valueOf(loaded? 1:0)));
            this.updateEntity();
        }catch (Exception e) {
            this.loaded = s;
            invoiceProductEntity.setLoaded(new Byte(String.valueOf(s? 1:0)));
        }
    }

    private void updateEntity(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(invoiceProductEntity);
        session.getTransaction().commit();
        session.close();
    }

    private SimpleStringProperty createCount(Integer count){
        try {
            return new SimpleStringProperty(Integer.toString(count));
        }catch (NullPointerException e){
            return new SimpleStringProperty("");
        }
    }

    public InvoiceProductEntity getInvoiceProductEntity() {
        return invoiceProductEntity;
    }

    public void setInvoiceProductEntity(InvoiceProductEntity invoiceProductEntity) {
        this.invoiceProductEntity = invoiceProductEntity;
    }
}
