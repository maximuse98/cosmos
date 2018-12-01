package models;

import entity.ClientRequestEntity;
import entity.ProductEntity;
import entity.RequestProductEntity;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class RequestProduct {
    private SimpleStringProperty id;
    private SimpleStringProperty productName;
    private SimpleStringProperty count;

    private ClientRequestEntity request;
    private RequestProductEntity requestProduct;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public RequestProduct(RequestProductEntity requestProduct, String productName) {
        this.id = createCount(requestProduct.getId());
        this.productName = new SimpleStringProperty(productName);
        this.count = createCount(requestProduct.getCount());
        this.request = requestProduct.getClientRequestByRequestId();

        this.requestProduct = requestProduct;
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
        String s = this.getProductName();
        try {
            this.productName.set(productName);

            String hql = " FROM ProductEntity " +
                    " WHERE name LIKE '"+ productName+"'";
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql);
            ProductEntity result = (ProductEntity) query.list().get(0);
            session.close();

            requestProduct.setProductByProductId(result);
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
        try {
            this.count.set(count);
            requestProduct.setCount(Integer.valueOf(count));
            this.updateEntity();
        }catch (Exception e){
            this.count.set(s);
            requestProduct.setCount(Integer.valueOf(s));
        }
    }

    private void updateEntity(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(requestProduct);
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

    public RequestProductEntity getRequestProduct() {
        return requestProduct;
    }

    public void setRequestProduct(RequestProductEntity requestProduct) {
        this.requestProduct = requestProduct;
    }
}
