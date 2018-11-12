package controller;

import entity.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.net.URL;
import java.util.*;

public class ManagerController implements Initializable {

    //client
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

    //request
    @FXML
    private TableColumn requestIdColumn;
    @FXML
    private TableColumn requestClientIdColumn;
    @FXML
    private TableColumn requestColumn;
    @FXML
    private TableColumn requestCheckedColumn;
    @FXML
    private TableColumn requestApprovedColumn;
    @FXML
    private TableColumn requestProductColumn;
    @FXML
    private TableColumn requestCountColumn;
    @FXML
    private TableView requestTable;
    @FXML
    private TableView requestProductTable;

    //order
    @FXML
    private TableColumn orderIdColumn;
    @FXML
    private TableColumn orderClientColumn;
    @FXML
    private TableColumn orderRequestColumn;
    @FXML
    private TableColumn orderContractColumn;
    @FXML
    private TableColumn orderPaymentColumn;
    @FXML
    private TableColumn orderProductColumn;
    @FXML
    private TableColumn orderCountColumn;
    @FXML
    private TableColumn orderRestColumn;
    @FXML
    private TableView orderTable;
    @FXML
    private TableView orderProductTable;

    private String login;
    private final Session session = HibernateUtil.getSessionFactory();

    public ManagerController()  {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setFactories();
        this.setAllClients();
        this.setAllRequests();
        this.setAllOrders();
    }

    private void setAllClients(){
        ObservableList<Client> usersList = FXCollections.observableArrayList();
        this.setFactories();
        Query query1 = session.createQuery(" FROM ClientEntity ");
        Iterator iter = query1.list().iterator();
        while(iter.hasNext()){
            ClientEntity clientEntity = (ClientEntity) iter.next();
            usersList.add(new Client(clientEntity));
        }
        clientTable.setItems(usersList);
    }
    private void setAllRequests(){
        ObservableList<Request> requestsList = FXCollections.observableArrayList();

        Query query = session.createQuery(" FROM ClientRequestEntity ");
        Iterator iter = query.list().iterator();
        while(iter.hasNext()){
            ClientRequestEntity requestEntity = (ClientRequestEntity) iter.next();

            Query query2 = session.createQuery(" FROM RequestProductEntity WHERE clientRequestByRequestId.id = "+requestEntity.getId());
            Iterator iter2 = query2.list().iterator();
            ObservableList<RequestProduct> products = FXCollections.observableArrayList();
            while(iter2.hasNext()){
                RequestProductEntity requestProduct =(RequestProductEntity) iter2.next();
                Product product = new Product(requestProduct.getProductByProductId());
                products.add(new RequestProduct(product.getName(),requestProduct.getCount()));
            }
            Request request = new Request(requestEntity);
            request.setRequestsProducts(products);
            requestsList.add(request);
        }
        requestTable.setItems(requestsList);
    }
    private void setAllOrders(){
        ObservableList<Order> ordersList = FXCollections.observableArrayList();

        Query query = session.createQuery(" FROM ClientOrderEntity ");
        Iterator iter = query.list().iterator();
        while(iter.hasNext()){
            ClientOrderEntity orderEntity = (ClientOrderEntity) iter.next();

            Query query2 = session.createQuery(" FROM OrderProductEntity WHERE clientOrderByOrderId.id = "+orderEntity.getId());
            Iterator iter2 = query2.list().iterator();
            ObservableList<OrderProduct> products = FXCollections.observableArrayList();
            while(iter2.hasNext()){
                OrderProductEntity orderProduct =(OrderProductEntity) iter2.next();
                Product product = new Product(orderProduct.getProductByProductId());
                products.add(new OrderProduct(product.getName(),orderProduct.getCount(),orderProduct.getRest()));
            }
            Order order = new Order(orderEntity);
            order.setOrdersProducts(products);
            ordersList.add(order);
        }
        orderTable.setItems(ordersList);
    }

    public void onRequestTableClick(){
        Request selectedRequest = (Request) requestTable.getSelectionModel().getSelectedItem();
        ObservableList<RequestProduct> products = selectedRequest.getRequestsProducts();
        requestProductTable.setItems(products);
    }
    public void onOrderTableClick(){
        Order selectedRequest = (Order) orderTable.getSelectionModel().getSelectedItem();
        ObservableList<OrderProduct> products = selectedRequest.getOrdersProducts();
        if(!products.isEmpty()){orderProductTable.setItems(products);}
        else{orderProductTable.setItems(null);}
    }

    private void setFactories() {
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("name"));
        clientSurnameColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("surname"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("phone"));
        clientPhone2Column.setCellValueFactory(new PropertyValueFactory<Client,String>("phone2"));
        clientAdressColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("adress"));
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<Client,String>("email"));

        requestIdColumn.setCellValueFactory(new PropertyValueFactory<Request,String>("id"));
        requestClientIdColumn.setCellValueFactory(new PropertyValueFactory<Request,String>("clientName"));
        requestColumn.setCellValueFactory(new PropertyValueFactory<Request,String>("request"));
        requestCheckedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                Request request = (Request) param.getValue();
                SimpleBooleanProperty p = new SimpleBooleanProperty(request.getChecked());
                return p;
            }
        });
        requestCheckedColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                CheckBoxTableCell cell = new CheckBoxTableCell();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        requestApprovedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                Request request = (Request) param.getValue();
                SimpleBooleanProperty p = new SimpleBooleanProperty(request.getApproved());
                return p;
            }
        });
        requestApprovedColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                CheckBoxTableCell cell = new CheckBoxTableCell();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        requestProductColumn.setCellValueFactory(new PropertyValueFactory<RequestProduct,String>("productName"));
        requestCountColumn.setCellValueFactory(new PropertyValueFactory<RequestProduct,String>("count"));

        orderIdColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("id"));
        orderClientColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("clientName"));
        orderRequestColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("requestName"));
        orderContractColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("contratName"));
        orderPaymentColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                Order order = (Order) param.getValue();
                SimpleBooleanProperty p = new SimpleBooleanProperty(order.getPayment());
                return p;
            }
        });
        orderPaymentColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                CheckBoxTableCell cell = new CheckBoxTableCell();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        orderProductColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct,String>("productName"));
        orderCountColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct,String>("count"));
        orderRestColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct,String>("rest"));
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
