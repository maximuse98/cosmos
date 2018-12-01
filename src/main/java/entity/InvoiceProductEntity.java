package entity;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.*;

@Entity
@Table(name = "invoice_product", schema = "cosmos")
public class InvoiceProductEntity {
    private int id;
    private Integer count;
    private Byte loaded;
    private InvoiceEntity invoiceByInvoiceId;
    private ProductEntity productByProductId;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public InvoiceProductEntity() {
    }
    @Deprecated
    public InvoiceProductEntity(String id) {
        this.id = Integer.valueOf(id);
    }
    @Deprecated
    public InvoiceProductEntity(SimpleStringProperty id, SimpleStringProperty productName, SimpleStringProperty count, Boolean loaded,InvoiceEntity invoice) {
        this.id = Integer.valueOf(id.get());
        this.count = Integer.valueOf(count.get());
        this.loaded = new Byte(String.valueOf(loaded? 1:0));
        this.invoiceByInvoiceId = invoice;

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
    @Column(name = "loaded", nullable = true)
    public Byte getLoaded() {
        if(loaded == null) return 0;
        return loaded;
    }

    public void setLoaded(Byte loaded) {
        this.loaded = loaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoiceProductEntity that = (InvoiceProductEntity) o;

        if (id != that.id) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (loaded != null ? !loaded.equals(that.loaded) : that.loaded != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (loaded != null ? loaded.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    public InvoiceEntity getInvoiceByInvoiceId() {
        return invoiceByInvoiceId;
    }

    public void setInvoiceByInvoiceId(InvoiceEntity invoiceByInvoiceId) {
        this.invoiceByInvoiceId = invoiceByInvoiceId;
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
