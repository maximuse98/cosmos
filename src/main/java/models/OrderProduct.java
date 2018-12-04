package models;

import entity.ClientOrderEntity;
import entity.InvoiceProductEntity;
import entity.OrderProductEntity;
import entity.ProductEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

public class OrderProduct {
    private SimpleStringProperty id;
    private SimpleStringProperty productName;
    private SimpleStringProperty count;
    private SimpleStringProperty rest;
    private ClientOrderEntity order;
    private OrderProductEntity orderProduct;
    private ProductEntity productEntity;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public OrderProduct(OrderProductEntity orderProduct, String productName) {
        this.id = new SimpleStringProperty(Integer.toString(orderProduct.getId()));
        this.productName = new SimpleStringProperty(productName);
        this.count = createCount(orderProduct.getCount());
        this.rest = createCount(orderProduct.getRest());

        this.order = orderProduct.getClientOrderByOrderId();
        this.productEntity = orderProduct.getProductByProductId();
        this.orderProduct = orderProduct;
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
            orderProduct.setId(Integer.valueOf(id));
            this.updateEntity();
        }
        catch (Exception e){
            this.id.set(s);
            orderProduct.setId(Integer.valueOf(s));
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
                    " WHERE name LIKE '" + productName + "'";
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql);
            ProductEntity result = (ProductEntity) query.list().get(0);
            session.close();

            orderProduct.setProductByProductId(result);
            this.updateEntity();
        }
        catch (Exception e){
            this.productName.set(s);
        }
    }
    public ClientOrderEntity getOrder() {
        return order;
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
            if(productEntity.getCount()<Integer.valueOf(count)){
                this.setAlert("На складе недостаточно данного товара");
                return;
            }
            this.count.set(count);
            orderProduct.setCount(Integer.valueOf(count));
            this.updateEntity();
        }
        catch (NullPointerException f){
            this.setAlert("Укажите вначале имя товара");
        }
        catch (Exception e){
            this.count.set(s);
            orderProduct.setCount(Integer.valueOf(s));
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
        try {
            this.rest.set(rest);
            if(Integer.valueOf(count.get())<Integer.valueOf(rest)){
                this.setAlert("Остаток не может превышать количество");
                return;
            }
            orderProduct.setRest(Integer.valueOf(rest));
            this.updateEntity();
        }
        catch (NullPointerException f){
            this.setAlert("Укажите вначале количество товара");
        }
        catch (Exception e){
            this.rest.set(s);
            orderProduct.setRest(Integer.valueOf(s));
        }
    }

    private void updateEntity(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(orderProduct);
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

    public OrderProductEntity getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(OrderProductEntity orderProduct) {
        this.orderProduct = orderProduct;
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
