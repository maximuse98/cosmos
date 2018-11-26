package entity;

import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.*;

@Entity
@Table(name = "request_product", schema = "cosmos")
public class RequestProductEntity {
    private int id;
    private Integer count;
    private ClientRequestEntity clientRequestByRequestId;
    private ProductEntity productByProductId;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public RequestProductEntity() {
    }

    public RequestProductEntity(String id) {
        this.id = Integer.valueOf(id);
    }

    public RequestProductEntity(SimpleStringProperty id, SimpleStringProperty count, SimpleStringProperty productName, ClientRequestEntity request) {
        this.id = Integer.valueOf(id.get());
        this.count = Integer.valueOf(count.get());
        this.clientRequestByRequestId = request;

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
        if (count == null) return 0;
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestProductEntity that = (RequestProductEntity) o;

        if (id != that.id) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    public ClientRequestEntity getClientRequestByRequestId() {
        return clientRequestByRequestId;
    }

    public void setClientRequestByRequestId(ClientRequestEntity clientRequestByRequestId) {
        this.clientRequestByRequestId = clientRequestByRequestId;
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
