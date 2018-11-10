package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {
    private String login;

    @FXML
    private TextField clientName;
    @FXML
    private TextField clientSurname;
    @FXML
    private TextField clientPhone;
    @FXML
    private TextField clientPhone2;
    @FXML
    private TextField clientEmail;
    @FXML
    private Button clientAddButton;
    @FXML
    private Button clientRemoveButton;
    @FXML
    private Button clientChangeButton;
    @FXML
    private TextField clientAdress;
    @FXML
    private TableView clientTable;

    public ManagerController()  {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setLogin(String login) {
        this.login = login;
        System.out.println(login);
    }
}
