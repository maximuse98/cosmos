package models;

import entity.ClientOrderEntity;
import entity.InvoiceProductEntity;
import entity.OrderProductEntity;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

public class OrderProduct {
    private SimpleStringProperty id;
    private SimpleStringProperty productName;
    private SimpleStringProperty count;
    private SimpleStringProperty rest;
    private ClientOrderEntity order;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public OrderProduct(OrderProductEntity orderProduct, String productName) {
        this.id = new SimpleStringProperty(Integer.toString(orderProduct.getId()));
        this.productName = new SimpleStringProperty(productName);
        this.count = createCount(orderProduct.getCount());
        this.rest = createCount(orderProduct.getRest());
        this.order = orderProduct.getClientOrderByOrderId();
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
        }
        catch (Exception e){
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
        }
        catch (Exception e){
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
        }
        catch (Exception e){
            e.printStackTrace();
            this.count.set(s);
        }
    }

    public String getRest() {
        return rest.get();
    }

    public SimpleStringProperty restProperty() {
        return rest;
    }

    public void setRest(String rest) {
        String s = this.getRest();
        this.rest.set(rest);
        try {
            this.updateEntity();
        }
        catch (Exception e){
            this.rest.set(s);
        }
    }

    private void updateEntity(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(new OrderProductEntity(id,count,rest,productName,order));
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
