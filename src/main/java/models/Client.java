package models;

import entity.ClientEntity;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;

public class Client {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty phone;
    private SimpleStringProperty phone2;
    private SimpleStringProperty adress;
    private SimpleStringProperty email;

    private ClientEntity clientEntity;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Client(ClientEntity client) {
        this.id = new SimpleStringProperty(Integer.toString(client.getId()));
        this.name = new SimpleStringProperty(client.getName());
        this.surname = new SimpleStringProperty(client.getSurname());
        this.phone = new SimpleStringProperty(client.getPhone());
        this.phone2 = new SimpleStringProperty(client.getPhone2());
        this.adress = new SimpleStringProperty(client.getAdress());
        this.email = new SimpleStringProperty(client.getEmail());

        this.clientEntity = client;
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
        String s = getId();
        try {
            this.id.set(id);
            clientEntity.setId(Integer.valueOf(id));
            this.updateEntity();
        }catch (Exception e){
            this.id.set(s);
            clientEntity.setId(Integer.valueOf(s));
        }
    }

    public void setName(String name) {
        String s = this.getName();
        try {
            this.name.set(name);
            clientEntity.setName(name);
            this.updateEntity();
        }catch (Exception e){
            e.printStackTrace();
            this.name.set(s);
            clientEntity.setName(s);
        }
    }

    public void setSurname(String surname) {
        String s = this.getSurname();
        try {
            this.surname.set(surname);
            clientEntity.setSurname(surname);
            this.updateEntity();
        }catch (Exception e){
            this.surname.set(s);
            clientEntity.setSurname(s);
        }
    }

    public void setPhone(String phone) {
        String s = this.getPhone();
        try {
            this.phone.set(phone);
            clientEntity.setPhone(phone);
            this.updateEntity();
        }catch (Exception e){
            this.phone.set(s);
            clientEntity.setPhone(s);
        }
    }

    public void setPhone2(String phone2) {
        String s = this.getPhone2();
        try {
            this.phone2.set(phone2);
            clientEntity.setPhone2(phone2);
            this.updateEntity();
        }catch (Exception e){
            this.phone2.set(s);
            clientEntity.setPhone2(s);
        }
    }

    public void setAdress(String adress) {
        String s = getAdress();
        try {
            this.adress.set(adress);
            clientEntity.setAdress(adress);
            this.updateEntity();
        }catch (Exception e){
            this.adress.set(s);
            clientEntity.setAdress(s);
        }
    }

    public void setEmail(String email) {
        String s = getEmail();
        try {
            this.email.set(email);
            clientEntity.setEmail(email);
            this.updateEntity();
        }catch (Exception e){
            this.email.set(s);
            clientEntity.setEmail(s);
        }
    }

    public ClientEntity getClientEntity() {
        return clientEntity;
    }

    public void setClientEntity(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }

    private void updateEntity(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(clientEntity);
        session.getTransaction().commit();
        session.close();
    }
}
