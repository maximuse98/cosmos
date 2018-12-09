package entity;

import org.hibernate.SessionFactory;
import org.hibernate.annotations.Immutable;
import util.HibernateUtil;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "statement_invoice", schema = "cosmos")
public class StatementInvoiceView {
    private int id;
    private String clientName;
    private String clientSurname;
    private String phone;
    private String adress;
    private String productName;
    private Integer count;
    private Byte loaded;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public StatementInvoiceView() {
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
    @Column(name = "client_name", nullable = false)
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Basic
    @Column(name = "surname", nullable = false)
    public String getClientSurname() {
        return clientSurname;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    @Basic
    @Column(name = "phone", nullable = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "adress", nullable = false)
    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Basic
    @Column(name = "product_name", nullable = false)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
        if (loaded == null) return 0;
        return loaded;
    }

    public void setLoaded(Byte loaded) {
        this.loaded = loaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatementInvoiceView that = (StatementInvoiceView) o;
        return id == that.id &&
                Objects.equals(clientName, that.clientName) &&
                Objects.equals(clientSurname, that.clientSurname) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(adress, that.adress) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(count, that.count) &&
                Objects.equals(loaded, that.loaded) &&
                Objects.equals(sessionFactory, that.sessionFactory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientName, clientSurname, phone, adress, productName, count, loaded, sessionFactory);
    }
}
