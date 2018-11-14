package entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "client_order", schema = "cosmos")
public class ClientOrderEntity {
    private int id;
    private Integer requestId;
    private ClientEntity clientByClientId;
    private String contract;
    private Date beginDate;
    private Date endDate;
    private Byte payment;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "request_id", nullable = true)
    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    public ClientEntity getClientByClientId() {
        return clientByClientId;
    }

    @Basic
    @Column(name = "contract", nullable = true)
    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    @Basic
    @Column(name = "begin_date", nullable = true)
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @Basic
    @Column(name = "end_date", nullable = true)
    public Date getEndDate() {
        return endDate;
    }

    @Basic
    @Column(name = "payment", nullable = true)
    public Byte getPayment() {
        return payment;
    }

    public void setPayment(Byte payment) {
        this.payment = payment;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setClientByClientId(ClientEntity clientByClientId) {
        this.clientByClientId = clientByClientId;
    }
}
