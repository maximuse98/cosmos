package models;

import javafx.beans.property.SimpleStringProperty;

public class Client {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty phone;
    private SimpleStringProperty phone2;
    private SimpleStringProperty adress;
    private SimpleStringProperty email;

    public Client(String id, String name, String surname, String phone, String phone2, String adress, String email) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.phone = new SimpleStringProperty(phone);
        this.phone2 = new SimpleStringProperty(phone2);
        this.adress = new SimpleStringProperty(adress);
        this.email = new SimpleStringProperty(email);
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

        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public void setPhone2(String phone2) {
        this.phone2.set(phone2);
    }

    public void setAdress(String adress) {
        this.adress.set(adress);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
