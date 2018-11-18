package entity;

import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.*;

@Entity
@Table(name = "order_product", schema = "cosmos")
public class OrderProductEntity {
    private int id;
    private Integer count;
    private Integer rest;
    private ClientOrderEntity clientOrderByOrderId;
    private ProductEntity productByProductId;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public OrderProductEntity() {
    }

    public OrderProductEntity(String id) {
        this.id = Integer.valueOf(id);
    }

    public OrderProductEntity(SimpleStringProperty id, SimpleStringProperty count, SimpleStringProperty rest, SimpleStringProperty productName) {
        this.id = Integer.valueOf(id.get());
        this.count = Integer.valueOf(count.get());
        this.rest = Integer.valueOf(rest.get());

        String hql = " FROM ProductEntity " +
                " WHERE name LIKE '"+ productName.get()+"'";
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hql);
        ProductEntity result = (ProductEntity) query.list().get(0);
        session.close();

        this.productByProductId = result;
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "count", nullable = true)
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Basic
    @Column(name = "rest", nullable = true)
    public Integer getRest() {
        return rest;
    }

    public void setRest(Integer rest) {
        this.rest = rest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderProductEntity that = (OrderProductEntity) o;

        if (id != that.id) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (rest != null ? !rest.equals(that.rest) : that.rest != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (rest != null ? rest.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    public ClientOrderEntity getClientOrderByOrderId() {
        return clientOrderByOrderId;
    }

    public void setClientOrderByOrderId(ClientOrderEntity clientOrderByOrderId) {
        this.clientOrderByOrderId = clientOrderByOrderId;
    }

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public ProductEntity getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(ProductEntity productByProductId) {
        this.productByProductId = productByProductId;
    }
}
