package models;

import javafx.beans.property.SimpleStringProperty;
import entity.ProductEntity;

public class Product {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty category;
    private SimpleStringProperty price;
    private SimpleStringProperty count;

    public Product(ProductEntity product) {
        this.id = new SimpleStringProperty(Integer.toString(product.getId()));
        this.name = new SimpleStringProperty(product.getName());
        this.category = new SimpleStringProperty(product.getCategory());
        this.price = new SimpleStringProperty(Integer.toString(product.getPrice()));
        this.count = new SimpleStringProperty(Integer.toString(product.getCount()));
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getCount() {
        return count.get();
    }

    public SimpleStringProperty countProperty() {
        return count;
    }

    public void setCount(String count) {
        this.count.set(count);
    }
}
