package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invoice", schema = "cosmos", catalog = "")
public class InvoiceEntity {
    private int id;
    private Byte agreed;
    private Date dateCreate;

    private ClientOrderEntity clientOrderByOrderId;

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
