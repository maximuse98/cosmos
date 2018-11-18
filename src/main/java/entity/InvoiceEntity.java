package entity;

import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "invoice", schema = "cosmos")
public class InvoiceEntity {
    private int id;
    private Byte agreed;
    private Date dateCreate;
    private ClientOrderEntity clientOrderByOrderId;
    private int order_id;

    private SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-mm-dd");
    private SimpleDateFormat dt2 = new SimpleDateFormat("dd.mm.yyyy");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public InvoiceEntity() {
    }

    public InvoiceEntity(String id) {
        this.id = Integer.valueOf(id);
    }

    public InvoiceEntity(SimpleStringProperty id, Boolean agreed, SimpleStringProperty dateCreate, SimpleStringProperty contractName) throws ParseException {
        this.id = Integer.valueOf(id.get());
        this.agreed = new Byte(String.valueOf(agreed? 1:0));
        this.dateCreate = dt1.parse(dt1.format(dt2.parse(dateCreate.get())));

        String hql = "FROM ClientOrderEntity " +
                    " WHERE contract LIKE '"+ contractName.get()+"'";
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hql);
        ClientOrderEntity result = (ClientOrderEntity) query.list().get(0);
        session.close();

        this.clientOrderByOrderId = result;
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
    @Column(name = "agreed", nullable = true)
    public Byte getAgreed() {
        if(agreed == null) return 0;
        return agreed;
    }

    public void setAgreed(Byte agreed) {
        this.agreed = agreed;
    }

    @Basic
    @Column(name = "date_create", nullable = true)
    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoiceEntity that = (InvoiceEntity) o;

        if (id != that.id) return false;
        if (agreed != null ? !agreed.equals(that.agreed) : that.agreed != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (agreed != null ? agreed.hashCode() : 0);
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
}
