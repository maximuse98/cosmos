package models;

import entity.RequestProductEntity;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

public class RequestProduct {
    private SimpleStringProperty id;
    private SimpleStringProperty productName;
    private SimpleStringProperty count;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public RequestProduct(RequestProductEntity requestProduct, String productName) {
        this.id = createCount(requestProduct.getId());
        this.productName = new SimpleStringProperty(productName);
        this.count = createCount(requestProduct.getCount());
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
        String s = this.getProductName();
        try {
            this.updateEntity();
        }catch (Exception e){
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
        }catch (Exception e){
            this.count.set(s);
        }
    }

    private void updateEntity(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(new RequestProductEntity(id,count,productName));
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
