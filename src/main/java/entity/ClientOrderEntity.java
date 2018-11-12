package entity;

import javax.persistence.*;

@Entity
@Table(name = "client_order", schema = "cosmos")
public class ClientOrderEntity {
    private int id;
    private Integer requestId;
    private Byte payment;
    private ClientEntity clientByClientId;
    private ContractEntity contractByContractId;

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

    @Basic
    @Column(name = "payment", nullable = true)
    public Byte getPayment() {
        return payment;
    }

    public void setPayment(Byte payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientOrderEntity that = (ClientOrderEntity) o;

        if (id != that.id) return false;
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (payment != null ? !payment.equals(that.payment) : that.payment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (payment != null ? payment.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    public ClientEntity getClientByClientId() {
        return clientByClientId;
    }

    @ManyToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "id")
    public ContractEntity getContractByContractId(){ return contractByContractId;}

    public void setContractByContractId(ContractEntity contractByContractId){this.contractByContractId = contractByContractId;}

    public void setClientByClientId(ClientEntity clientByClientId) {
        this.clientByClientId = clientByClientId;
    }
}
