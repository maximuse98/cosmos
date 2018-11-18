package entity;

import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.*;

@Entity
@Table(name = "client_request", schema = "cosmos")
public class ClientRequestEntity {
    private int id;
    private String request;
    private Byte checked;
    private Byte approved;
    private ClientEntity clientByClientId;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public ClientRequestEntity() {
    }

    public ClientRequestEntity(String id) {
        this.id = Integer.valueOf(id);
    }

    public ClientRequestEntity(SimpleStringProperty id, SimpleStringProperty request, Boolean checked, Boolean approved, SimpleStringProperty clientName) {
        this.id = Integer.valueOf(id.get());
        this.request = request.get();
        this.checked = new Byte(String.valueOf(checked? 1:0));
        this.approved = new Byte(String.valueOf(approved? 1:0));

        String client = clientName.get();
        String name = client.substring(0, client.indexOf(' '));
        String surname = client.substring(client.indexOf(' ')+1, client.length());

        String hql = "FROM ClientEntity" +
                " WHERE name LIKE '"+ name +"' AND surname LIKE '"+surname+"'";
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hql);
        ClientEntity result = (ClientEntity) query.list().get(0);
        session.close();

        this.clientByClientId = result;
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
    @Column(name = "request", nullable = true, length = 200)
    public String getRequest() {
        if(request==null) return "";
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Basic
    @Column(name = "checked", nullable = true)
    public Byte getChecked() {
        if(checked == null) return 0;
        return checked;
    }

    public void setChecked(Byte checked) {
        this.checked = checked;
    }

    @Basic
    @Column(name = "approved", nullable = true)
    public Byte getApproved() {
        if(approved == null) return 0;
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
