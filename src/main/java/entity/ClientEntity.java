package entity;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;

@Entity
@Table(name = "client", schema = "cosmos")
public class ClientEntity {
    private int id;
    private String name;
    private String surname;
    private String phone;
    private String phone2;
    private String adress;
    private String email;

    public ClientEntity() {
    }

    public ClientEntity(String id) {
        this.id = Integer.valueOf(id);
    }

    public ClientEntity(SimpleStringProperty id, SimpleStringProperty name, SimpleStringProperty surname, SimpleStringProperty phone, SimpleStringProperty phone2, SimpleStringProperty adress, SimpleStringProperty email) {
        this.id = Integer.valueOf(id.get());
        this.name = name.get();
        this.surname = surname.get();
        this.phone = phone.get();
        this.phone2 = phone2.get();
        this.adress = adress.get();
        this.email = email.get();
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
    @Column(name = "name", nullable = true, length = 50)
    public String getName() {
        if(name==null) return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "surname", nullable = true, length = 50)
    public String getSurname() {
        if(surname==null) return "";
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "phone", nullable = true, length = 12)
    public String getPhone() {
        if(phone==null) return "";
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "phone2", nullable = true, length = 12)
    public String getPhone2() {
        if(phone2==null) return "";
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    @Basic
    @Column(name = "adress", nullable = true, length = 200)
    public String getAdress() {
        if(adress==null) return "";
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 50)
    public String getEmail() {
        if(email==null) return "";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientEntity that = (ClientEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (phone2 != null ? !phone2.equals(that.phone2) : that.phone2 != null) return false;
        if (adress != null ? !adress.equals(that.adress) : that.adress != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (phone2 != null ? phone2.hashCode() : 0);
        result = 31 * result + (adress != null ? adress.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
