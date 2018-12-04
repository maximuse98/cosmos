package entity;

import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.*;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

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

    private SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dt2 = new SimpleDateFormat("dd.MM.yyyy");
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public ClientOrderEntity() {
    }
    @Deprecated
    public ClientOrderEntity(String id) {
        this.id = Integer.valueOf(id);
    }
    @Deprecated
    public ClientOrderEntity(SimpleStringProperty id, SimpleStringProperty requestName, SimpleStringProperty contractName, SimpleStringProperty clientName, SimpleStringProperty beginDate, SimpleStringProperty endDate, Boolean payment) throws ParseException {
        this.id = Integer.valueOf(id.get());
        this.contract = contractName.get();
        if(!beginDate.get().equals("")){this.beginDate = dt2.parse(beginDate.get());}
        if(!endDate.get().equals("")){this.endDate = dt2.parse(endDate.get());}
        this.payment = new Byte(String.valueOf(payment? 1:0));

        try {
            String hql = "FROM ClientRequestEntity" +
                    " WHERE request LIKE '"+requestName.get()+"'";
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql);
            ClientRequestEntity result = (ClientRequestEntity) query.list().get(0);
            session.close();

            this.requestId = result.getId();
        }catch (Exception e){
        }
        try {
            String client = clientName.get();
            String name = client.substring(0, client.indexOf(' '));
            String surname = client.substring(client.indexOf(' ') + 1, client.length());

            String hql1 = "FROM ClientEntity" +
                    " WHERE name LIKE '" + name + "' AND surname LIKE '" + surname + "'";
            Session session1 = sessionFactory.openSession();
            Query query1 = session1.createQuery(hql1);
            ClientEntity result1 = (ClientEntity) query1.list().get(0);
            this.clientByClientId = result1;
        }catch (Exception e){}
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
    @Column(name = "request_id", nullable = true)
    public Integer getRequestId() {
        if(requestId == null) return null;
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
        if(contract==null) return "";
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
        if(payment == null) return 0;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientOrderEntity that = (ClientOrderEntity) o;

        if (id != that.id) return false;
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (clientByClientId != null ? !clientByClientId.equals(that.clientByClientId) : that.clientByClientId != null)
            return false;
        if (contract != null ? !contract.equals(that.contract) : that.contract != null) return false;
        if (beginDate != null ? !beginDate.equals(that.beginDate) : that.beginDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (payment != null ? !payment.equals(that.payment) : that.payment != null) return false;
        if (dt1 != null ? !dt1.equals(that.dt1) : that.dt1 != null) return false;
        if (dt2 != null ? !dt2.equals(that.dt2) : that.dt2 != null) return false;
        return sessionFactory != null ? sessionFactory.equals(that.sessionFactory) : that.sessionFactory == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (clientByClientId != null ? clientByClientId.hashCode() : 0);
        result = 31 * result + (contract != null ? contract.hashCode() : 0);
        result = 31 * result + (beginDate != null ? beginDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (payment != null ? payment.hashCode() : 0);
        result = 31 * result + (dt1 != null ? dt1.hashCode() : 0);
        result = 31 * result + (dt2 != null ? dt2.hashCode() : 0);
        result = 31 * result + (sessionFactory != null ? sessionFactory.hashCode() : 0);
        return result;
    }
}
