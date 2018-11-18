package models;

import entity.ClientEntity;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class Client {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty phone;
    private SimpleStringProperty phone2;
    private SimpleStringProperty adress;
    private SimpleStringProperty email;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Client(ClientEntity client) {
        this.id = new SimpleStringProperty(Integer.toString(client.getId()));
        this.name = new SimpleStringProperty(client.getName());
        this.surname = new SimpleStringProperty(client.getSurname());
        this.phone = new SimpleStringProperty(client.getPhone());
        this.phone2 = new SimpleStringProperty(client.getPhone2());
        this.adress = new SimpleStringProperty(client.getAdress());
        this.email = new SimpleStringProperty(client.getEmail());
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public String getPhone2() {
        return phone2.get();
    }

    public SimpleStringProperty phone2Property() {
        return phone2;
    }

    public String getAdress() {
        return adress.get();
    }

    public SimpleStringProperty adressProperty() {
        return adress;
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setId(String id) {
        String s = this.getId();
        this.id.set(id);
        try {
            this.updateEntity();
        }catch (Exception e){
            this.id.set(s);
        }
    }

    public void setName(String name) {
        String s = this.getName();
        this.name.set(name);
        try {
            this.updateEntity();
        }catch (Exception e){
            this.name.set(s);
        }
    }

    public void setSurname(String surname) {
        String s = this.getSurname();
        this.surname.set(surname);
        try {
            this.updateEntity();
        }catch (Exception e){
            this.surname.set(s);
        }
    }

    public void setPhone(String phone) {
        String s = this.getPhone();
        this.phone.set(phone);
        try {
            this.updateEntity();
        }catch (Exception e){
            this.phone.set(s);
        }
    }

    public void setPhone2(String phone2) {
        String s = this.getPhone2();
        this.phone2.set(phone2);
        this.updateEntity();
        try {
            this.updateEntity();
        }catch (Exception e){
            this.phone2.set(s);
        }
    }

    public void setAdress(String adress) {
        String s = getAdress();
        this.adress.set(adress);
        this.updateEntity();
        try {
            this.updateEntity();
        }catch (Exception e){
            this.adress.set(s);
        }
    }

    public void setEmail(String email) {
        String s = getEmail();
        this.email.set(email);
        this.updateEntity();
        try {
            this.updateEntity();
        }catch (Exception e){
            this.email.set(s);
        }
    }

    private void updateEntity(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(new ClientEntity(id,name,surname,phone,phone2,adress,email));
        session.getTransaction().commit();
        session.close();
    }
}
