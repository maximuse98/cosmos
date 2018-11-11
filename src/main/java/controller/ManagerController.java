package controller;

import entity.ClientEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Client;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {

    @FXML
    private TableColumn clientIdColumn;
    @FXML
    private TextField clientName;
    @FXML
    private TableColumn<Client, String> clientNameColumn;
    @FXML
    private TextField clientSurname;
    @FXML
    private TableColumn clientSurnameColumn;
    @FXML
    private TextField clientPhone;
    @FXML
    private TableColumn clientPhoneColumn;
    @FXML
    private TextField clientPhone2;
    @FXML
    private TableColumn clientPhone2Column;
    @FXML
    private TextField clientEmail;
    @FXML
    private TableColumn clientEmailColumn;
    @FXML
    private Button clientAddButton;
    @FXML
    private Button clientRemoveButton;
    @FXML
    private Button clientChangeButton;
    @FXML
    private TextField clientAdress;
    @FXML
    private TableColumn clientAdressColumn;
    @FXML
    private TableView clientTable;

    private String login;
    private ObservableList<Client> users;
    private final Session session = HibernateUtil.getSessionFactory();

    public ManagerController()  {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users = FXCollections.observableArrayList();
        String hql = " FROM ClientEntity ";
        Query query = session.createQuery(hql);
        Collection data = query.list();
        Iterator iter = data.iterator();
        while(iter.hasNext()){
            ClientEntity user = (ClientEntity) iter.next();
            users.add(new Client(Integer.toString(user.getId()),user.getName(),user.getSurname(),user.getPhone(),user.getPhone2(),user.getAdress(),user.getEmail()));
        }
        clientTable.setItems(users);
        this.setFactories();
    }

    private void setFactories() {
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("name"));
        clientSurnameColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("surname"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("phone"));
        clientPhone2Column.setCellValueFactory(new PropertyValueFactory<Client,String>("phone2"));
        clientAdressColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("adress"));
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("email"));

    }

    public void setLogin(String login) {
        this.login = login;
    }
}
