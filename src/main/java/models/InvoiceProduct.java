package models;

import entity.InvoiceProductEntity;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

public class InvoiceProduct {
    private SimpleStringProperty id;
    private SimpleStringProperty productName;
    private SimpleStringProperty count;
    private Boolean loaded;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public InvoiceProduct(InvoiceProductEntity invoiceProduct, String productName) {
        this.id = new SimpleStringProperty(Integer.toString(invoiceProduct.getId()));
        this.productName = new SimpleStringProperty(productName);
        this.count = createCount(invoiceProduct.getCount());
        this.loaded = invoiceProduct.getLoaded()!=0;
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
        }catch (Exception e) {
            this.id.set(s);
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
        this.productName.set(productName);
        try {
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
        this.count.set(count);
        try {
            this.updateEntity();
        }catch (Exception e) {
            this.count.set(s);
        }
    }

    public Boolean getLoaded() {
        return loaded;
    }

    public void setLoaded(Boolean loaded) {
        Boolean s = this.getLoaded();
        this.loaded = loaded;
        try {
            this.updateEntity();
        }catch (Exception e) {
            this.loaded = s;
        }
    }

    private void updateEntity(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(new InvoiceProductEntity(id,productName,count,loaded));
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
}
