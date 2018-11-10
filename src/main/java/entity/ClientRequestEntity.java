package entity;

import javax.persistence.*;

@Entity
@Table(name = "client_request", schema = "cosmos", catalog = "")
public class ClientRequestEntity {
    private int id;
    private String request;
    private Byte checked;
    private Byte approved;
    private ClientEntity clientByClientId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "request", nullable = true, length = 200)
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Basic
    @Column(name = "checked", nullable = true)
    public Byte getChecked() {
        return checked;
    }

    public void setChecked(Byte checked) {
        this.checked = checked;
    }

    @Basic
    @Column(name = "approved", nullable = true)
    public Byte getApproved() {
        return approved;
    }

    public void setApproved(Byte approved) {
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientRequestEntity that = (ClientRequestEntity) o;

        if (id != that.id) return false;
        if (request != null ? !request.equals(that.request) : that.request != null) return false;
        if (checked != null ? !checked.equals(that.checked) : that.checked != null) return false;
        if (approved != null ? !approved.equals(that.approved) : that.approved != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (request != null ? request.hashCode() : 0);
        result = 31 * result + (checked != null ? checked.hashCode() : 0);
        result = 31 * result + (approved != null ? approved.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    public ClientEntity getClientByClientId() {
        return clientByClientId;
    }

    public void setClientByClientId(ClientEntity clientByClientId) {
        this.clientByClientId = clientByClientId;
    }
}
