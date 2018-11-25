package controller;

import entity.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;

public class ManagerController implements Initializable {

    //client
    @FXML
    private TableColumn<Client,String> clientIdColumn;
    @FXML
    private TableColumn<Client,String> clientNameColumn;
    @FXML
    private TableColumn<Client,String> clientSurnameColumn;
    @FXML
    private TableColumn<Client,String> clientPhoneColumn;
    @FXML
    private TableColumn<Client,String> clientPhone2Column;
    @FXML
    private TableColumn<Client,String> clientEmailColumn;
    @FXML
    private TableColumn<Client,String> clientAdressColumn;
    @FXML
    private TableView clientTable;
    @FXML
    private TextField clientFilter;

    //request
    @FXML
    private TableColumn<Request,String> requestIdColumn;
    @FXML
    private TableColumn<Request,String> requestClientIdColumn;
    @FXML
    private TableColumn<Request,String> requestColumn;
    @FXML
    private TableColumn requestCheckedColumn;
    @FXML
    private TableColumn requestApprovedColumn;
    @FXML
    private TableColumn<RequestProduct,String> requestProductColumn;
    @FXML
    private TableColumn<RequestProduct,String> requestCountColumn;
    @FXML
    private TableView requestTable;
    @FXML
    private TableView requestProductTable;
    @FXML
    private TextField requestFilter;
    @FXML
    private TextField requestProductFilter;

    //order
    @FXML
    private TableColumn<Order,String> orderIdColumn;
    @FXML
    private TableColumn<Order,String> orderClientColumn;
    @FXML
    private TableColumn<Order,String> orderRequestColumn;
    @FXML
    private TableColumn<Order,String> orderContractColumn;
    @FXML
    private TableColumn orderPaymentColumn;
    @FXML
    private TableColumn<OrderProduct,String> orderProductColumn;
    @FXML
    private TableColumn<OrderProduct,String> orderCountColumn;
    @FXML
    private TableColumn<OrderProduct,String> orderRestColumn;
    @FXML
    private TableView orderTable;
    @FXML
    private TableView orderProductTable;
    @FXML
    private TextField orderFilter;
    @FXML
    private TextField orderProductFilter;

    //contract
    @FXML
    private TableColumn<Order,String> contractNameColumn;
    @FXML
    private TableColumn<Order,String> contractClientNameColumn;
    @FXML
    private TableColumn<Order,String> contractBeginDateColumn;
    @FXML
    private TableColumn<Order,String> contractEndDateColumn;
    @FXML
    private TableColumn contractPaymentColumn;
    @FXML
    private TableView contractTable;
    @FXML
    private TextField contractFilter;

    //invoice
    @FXML
    private TableColumn<Invoice,String> invoiceIdColumn;
    @FXML
    private TableColumn<Invoice,String> invoiceContractColumn;
    @FXML
    private TableColumn<Invoice,String> invoiceDateCreateColumn;
    @FXML
    private TableColumn invoiceAgreedColumn;
    @FXML
    private TableColumn<InvoiceProduct,String> invoiceProductNameColumn;
    @FXML
    private TableColumn<InvoiceProduct,String> invoiceProductCountColumn;
    @FXML
    private TableColumn invoiceProductLoadedColumn;
    @FXML
    private TableView invoiceTable;
    @FXML
    private TableView invoiceProductTable;
    @FXML
    private TextField invoiceFilter;
    @FXML
    private TextField invoiceProductFilter;

    //product
    @FXML
    private TableColumn<Product,String> productIdColumn;
    @FXML
    private TableColumn<Product,String> productNameColumn;
    @FXML
    private TableColumn<Product,String> productCategoryColumn;
    @FXML
    private TableColumn<Product,String> productPriceColumn;
    @FXML
    private TableColumn<Product,String> productCountColumn;
    @FXML
    private TableView productTable;
    @FXML
    private TextField productFilter;

    @FXML
    private Label loginLabel1;
    @FXML
    private Label loginLabel2;
    @FXML
    private Label loginLabel3;
    @FXML
    private Label loginLabel4;
    @FXML
    private Label loginLabel5;
    @FXML
    private Label loginLabel6;

    private String login;
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private ObservableList<Client> usersList;
    private ObservableList<Request> requestsList;
    private ObservableList<RequestProduct> requestProducts;
    private ObservableList<Order> ordersList;
    private ObservableList<OrderProduct> orderProducts;
    private ObservableList<Invoice> invoicesList;
    private ObservableList<InvoiceProduct> invoiceProducts;
    private ObservableList<Product> productsList;

    public ManagerController()  {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setFactories();
        this.setOnEditCommit();

        this.setAllClients();
        this.setAllRequests();
        this.setAllOrders();
        this.setAllInvoices();
        this.setAllProducts();

        this.setLoginLabel();
    }

    /**
     * добавление всех данных в соответствующие таблицы
     * запуск при инициализации и при манипулации со строками -
     * добавление и удаление
     **/
    private void setAllClients(){
        usersList = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM ClientEntity ");
        Iterator iter = query1.list().iterator();
        while(iter.hasNext()){
            ClientEntity clientEntity = (ClientEntity) iter.next();
            usersList.add(new Client(clientEntity));
        }
        clientTable.setItems(usersList);
        session.close();
        this.setClientFilter();
    }
    private void setAllRequests(){
        requestsList = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(" FROM ClientRequestEntity ");
        Iterator iter = query.list().iterator();
        while(iter.hasNext()){
            ClientRequestEntity requestEntity = (ClientRequestEntity) iter.next();

            Query query2 = session.createQuery(" FROM RequestProductEntity WHERE clientRequestByRequestId.id = "+requestEntity.getId());
            Iterator iter2 = query2.list().iterator();
            ObservableList<RequestProduct> requestProducts = FXCollections.observableArrayList();
            while(iter2.hasNext()){
                RequestProductEntity requestProduct =(RequestProductEntity) iter2.next();
                if(requestProduct.getProductByProductId()!=null) {
                    Product product = new Product(requestProduct.getProductByProductId());
                    requestProducts.add(new RequestProduct(requestProduct, product.getName()));
                }
                else {
                    requestProducts.add(new RequestProduct(requestProduct,""));
                }
            }
            Request request = new Request(requestEntity);
            request.setRequestsProducts(requestProducts);
            requestsList.add(request);
        }
        requestTable.setItems(requestsList);
        session.close();
        this.setRequestFilter();
    }
    private void setAllOrders(){
        ordersList = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(" FROM ClientOrderEntity ");
        Iterator iter = query.list().iterator();
        while(iter.hasNext()){
            ClientOrderEntity orderEntity = (ClientOrderEntity) iter.next();

            Query query2 = session.createQuery(" FROM OrderProductEntity WHERE clientOrderByOrderId.id = "+orderEntity.getId());
            Iterator iter2 = query2.list().iterator();
            ObservableList<OrderProduct> orderProducts = FXCollections.observableArrayList();
            while(iter2.hasNext()){
                OrderProductEntity orderProduct =(OrderProductEntity) iter2.next();
                if(orderProduct.getProductByProductId()!=null) {
                    ProductEntity product = orderProduct.getProductByProductId();
                    orderProducts.add(new OrderProduct(orderProduct, product.getName()));
                }
                else {
                    orderProducts.add(new OrderProduct(orderProduct,""));
                }
            }
            Order order = new Order(orderEntity);
            order.setOrdersProducts(orderProducts);
            ordersList.add(order);
        }
        orderTable.setItems(ordersList);
        contractTable.setItems(ordersList);
        session.close();
        this.setOrderFilter();
        this.setContractFilter();
    }
    private void setAllInvoices(){
        invoicesList = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(" FROM InvoiceEntity ");
        Iterator iter = query.list().iterator();
        while(iter.hasNext()){
            InvoiceEntity invoiceEntity = (InvoiceEntity) iter.next();

            Query query2 = session.createQuery(" FROM InvoiceProductEntity WHERE invoiceByInvoiceId.id = "+invoiceEntity.getId());
            Iterator iter2 = query2.list().iterator();
            ObservableList<InvoiceProduct> invoiceProducts = FXCollections.observableArrayList();
            while(iter2.hasNext()){
                InvoiceProductEntity invoiceProduct =(InvoiceProductEntity) iter2.next();
                if(invoiceProduct.getProductByProductId()!=null) {
                    ProductEntity product = invoiceProduct.getProductByProductId();
                    invoiceProducts.add(new InvoiceProduct(invoiceProduct, product.getName()));
                }
            }
            Invoice invoice = new Invoice(invoiceEntity);
            invoice.setInvoiceProducts(invoiceProducts);
            invoicesList.add(invoice);
        }
        invoiceTable.setItems(invoicesList);
        session.close();
        this.setInvoiceFilter();
    }
    private void setAllProducts(){
        productsList = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM ProductEntity ");
        Iterator iter = query1.list().iterator();
        while(iter.hasNext()){
            ProductEntity productEntity = (ProductEntity) iter.next();
            productsList.add(new Product(productEntity));
        }
        productTable.setItems(productsList);
        session.close();
        this.setProductFilter();
    }

    public void onRequestTableClick(){
        requestProductFilter.setText("");
        Request selectedRequest = (Request) requestTable.getSelectionModel().getSelectedItem();
        requestProducts = selectedRequest.getRequestsProducts();
        if(!requestProducts.isEmpty()){
            requestProductTable.setItems(requestProducts);
            this.setRequestProductFilter();}
        else{requestProductTable.setItems(null);}
    }
    public void onOrderTableClick(){
        orderProductFilter.setText("");
        Order selectedRequest = (Order) orderTable.getSelectionModel().getSelectedItem();
        orderProducts = selectedRequest.getOrdersProducts();
        if(!orderProducts.isEmpty()){
            orderProductTable.setItems(orderProducts);
            this.setOrderProductFilter();
        }
        else{orderProductTable.setItems(null);}
    }
    public void onInvoiceTableClick(){
        invoiceProductFilter.setText("");
        Invoice selectedRequest = (Invoice) invoiceTable.getSelectionModel().getSelectedItem();
        invoiceProducts = selectedRequest.getInvoiceProducts();
        if(!invoiceProducts.isEmpty()){
            invoiceProductTable.setItems(invoiceProducts);
            this.setInvoiceProductFilter();
        }
        else{invoiceProductTable.setItems(null);}
    }

    public void onClientAddClick(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new ClientEntity());
        session.getTransaction().commit();
        session.close();

        this.refreshClientTable();
    }
    public void onInvoiceAddClick(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new InvoiceEntity());
        session.getTransaction().commit();
        session.close();

        this.refreshInvoiceTable();
    }
    public void onInvoiceProductAddClick(){
        Invoice selectedRequest = (Invoice) invoiceTable.getSelectionModel().getSelectedItem();

        Session session1 = sessionFactory.openSession();
        Query query = session1.createQuery(" FROM InvoiceEntity WHERE id="+selectedRequest.getId());
        InvoiceEntity result = (InvoiceEntity) query.list().get(0);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        InvoiceProductEntity invoiceProductEntity = new InvoiceProductEntity();
        invoiceProductEntity.setInvoiceByInvoiceId(result);
        session.save(invoiceProductEntity);
        session.getTransaction().commit();
        session.close();

        this.refreshInvoiceTable();
    }
    public void onOrderAddClick(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new ClientOrderEntity());
        session.getTransaction().commit();
        session.close();

        this.refreshOrderTable();
    }
    public void onOrderProductAddClick(){
        Order selectedOrder = (Order) orderTable.getSelectionModel().getSelectedItem();

        Session session1 = sessionFactory.openSession();
        Query query = session1.createQuery(" FROM ClientOrderEntity WHERE id="+selectedOrder.getId());
        ClientOrderEntity result = (ClientOrderEntity) query.list().get(0);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setClientOrderByOrderId(result);
        session.save(orderProductEntity);
        session.getTransaction().commit();
        session.close();

        this.refreshOrderTable();
    }
    public void onRequestAddClick(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new ClientRequestEntity());
        session.getTransaction().commit();
        session.close();

        this.refreshRequestTable();
    }
    public void onRequestProductAddClick(){
        Request selectedRequest = (Request) requestTable.getSelectionModel().getSelectedItem();

        Session session1 = sessionFactory.openSession();
        Query query = session1.createQuery(" FROM ClientRequestEntity WHERE id="+selectedRequest.getId());
        ClientRequestEntity result = (ClientRequestEntity) query.list().get(0);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        RequestProductEntity orderProductEntity = new RequestProductEntity();
        orderProductEntity.setClientRequestByRequestId(result);
        session.save(orderProductEntity);
        session.getTransaction().commit();
        session.close();

        this.refreshRequestTable();
    }

    public void onClientDeleteClick(){
        Client selected = (Client) clientTable.getSelectionModel().getSelectedItem();

        try {
            String hql1 = "FROM ClientRequestEntity";
            Session session3 = sessionFactory.openSession();
            Query query3 = session3.createQuery(hql1);
            Iterator iterator2 = query3.list().iterator();
            while (iterator2.hasNext()) {
                ClientRequestEntity result = (ClientRequestEntity) iterator2.next();
                if (result.getClientByClientId().getId() == Integer.valueOf(selected.getId())) {
                    this.setAlert("Удалите все связанные заявки");
                    return;
                }
            }
            session3.close();
        }catch (NullPointerException e){}

        String hql = "FROM ClientOrderEntity";
        Session session2 = sessionFactory.openSession();
        Query query2 = session2.createQuery(hql);
        Iterator iterator1 = query2.list().iterator();
        while(iterator1.hasNext()) {
            ClientOrderEntity result = (ClientOrderEntity) iterator1.next();
            try {
                if (result.getClientByClientId().getId() == Integer.valueOf(selected.getId())) {
                    this.setAlert("Удалите все связанные заказы");
                    return;
                }
            }
            catch (NullPointerException e){}
        }
        session2.close();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(new ClientEntity(selected.getId()));
        session.getTransaction().commit();
        session.close();

        this.refreshClientTable();
    }
    public void onRequestDeleteClick(){
        Request selected = (Request) requestTable.getSelectionModel().getSelectedItem();
        ObservableList<RequestProduct> requestsProducts = selected.getRequestsProducts();
        Iterator<RequestProduct> iterator = requestsProducts.iterator();
        while(iterator.hasNext()){
            RequestProduct next = iterator.next();

            Session session1 = sessionFactory.openSession();
            session1.beginTransaction();
            session1.delete(new RequestProductEntity(next.getId()));
            session1.getTransaction().commit();
            session1.close();
        }

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(new ClientRequestEntity(selected.getId()));
        session.getTransaction().commit();
        session.close();

        this.refreshRequestTable();
    }
    public void onRequestProductDeleteClick(){
        RequestProduct selected = (RequestProduct) requestProductTable.getSelectionModel().getSelectedItem();

        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(new RequestProductEntity(selected.getId()));
            session.getTransaction().commit();
            session.close();
        }catch (NullPointerException e){}

        this.refreshRequestTable();
    }
    public void onOrderDeleteClick(){
        Order selected = (Order) orderTable.getSelectionModel().getSelectedItem();

        String hql = "FROM InvoiceEntity";
        Session session2 = sessionFactory.openSession();
        Query query2 = session2.createQuery(hql);
        Iterator iterator1 = query2.list().iterator();
        while(iterator1.hasNext()) {
            InvoiceEntity result = (InvoiceEntity) iterator1.next();
            try {
                if(result.getClientOrderByOrderId().getId() == Integer.valueOf(selected.getId())){
                    this.setAlert("Удалите все связанные счета-фактуры");
                    return;
                }
            }
            catch (NullPointerException e){
            }
        }
        session2.close();

        ObservableList<OrderProduct> orderProducts = selected.getOrdersProducts();
        Iterator<OrderProduct> iterator = orderProducts.iterator();
        while(iterator.hasNext()){
            OrderProduct next = iterator.next();

            Session session1 = sessionFactory.openSession();
            session1.beginTransaction();
            session1.delete(new OrderProductEntity(next.getId()));
            session1.getTransaction().commit();
            session1.close();
        }

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(new ClientOrderEntity(selected.getId()));
        session.getTransaction().commit();
        session.close();

        this.refreshOrderTable();
        this.refreshContractTable();
    }
    public void onOrderProductDeleteClick(){
        OrderProduct selected = (OrderProduct) orderProductTable.getSelectionModel().getSelectedItem();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(new OrderProductEntity(selected.getId()));
        session.getTransaction().commit();
        session.close();

        this.refreshOrderTable();
    }
    public void onInvoiceDeleteClick(){
        Invoice selected = (Invoice) invoiceTable.getSelectionModel().getSelectedItem();

        ObservableList<InvoiceProduct> invoiceProducts = selected.getInvoiceProducts();
        Iterator<InvoiceProduct> iterator = invoiceProducts.iterator();
        while(iterator.hasNext()){
            InvoiceProduct next = iterator.next();

            Session session1 = sessionFactory.openSession();
            session1.beginTransaction();
            session1.delete(new InvoiceProductEntity(next.getId()));
            session1.getTransaction().commit();
            session1.close();
        }

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(new InvoiceEntity(selected.getId()));
        session.getTransaction().commit();
        session.close();

        this.refreshInvoiceTable();
    }
    public void onInvoiceProductDeleteClick(){
        InvoiceProduct selected = (InvoiceProduct) invoiceProductTable.getSelectionModel().getSelectedItem();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(new InvoiceProductEntity(selected.getId()));
        session.getTransaction().commit();
        session.close();

        this.refreshInvoiceTable();
    }

    /**
     * setFactories() - маппинг столбцов и соотвествующих атрибутов в models
     * setOnEditCommit() - добавление хендлеров на изменение данных - запуск соответствующих сетеров в models
     *
     * возможность изменять id отключена
     * для включения - раскомментировать setCellFactory() соответствующих столбцов
     */
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
                p.addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                        request.setChecked(new_val);
                    }
                });
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
                p.addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                        request.setApproved(new_val);
                    }
                });
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

        orderIdColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("id"));
        orderClientColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("clientName"));
        orderRequestColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("requestName"));
        orderContractColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("contractName"));
        orderPaymentColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                Order order = (Order) param.getValue();
                SimpleBooleanProperty p = new SimpleBooleanProperty(order.getPayment());
                p.addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                        order.setPayment(new_val);
                        refreshContractTable();
                    }
                });
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

        contractNameColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("contractName"));
        contractClientNameColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("clientName"));
        contractBeginDateColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("beginDate"));
        contractEndDateColumn.setCellValueFactory(new PropertyValueFactory<Order,String>("endDate"));
        contractPaymentColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                Order order = (Order) param.getValue();
                SimpleBooleanProperty p = new SimpleBooleanProperty(order.getPayment());
                p.addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                        order.setPayment(new_val);
                        refreshOrderTable();
                    }
                });
                return p;
            }
        });
        contractPaymentColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                CheckBoxTableCell cell = new CheckBoxTableCell();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        invoiceIdColumn.setCellValueFactory(new PropertyValueFactory<Invoice,String>("id"));
        invoiceContractColumn.setCellValueFactory(new PropertyValueFactory<Invoice,String>("contractName"));
        invoiceDateCreateColumn.setCellValueFactory(new PropertyValueFactory<Invoice,String>("dateCreate"));
        invoiceAgreedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                Invoice invoice = (Invoice) param.getValue();
                SimpleBooleanProperty p = new SimpleBooleanProperty(invoice.getAgreed());
                p.addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                        invoice.setAgreed(new_val);
                    }
                });
                return p;
            }
        });
        invoiceAgreedColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                CheckBoxTableCell cell = new CheckBoxTableCell();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        invoiceProductNameColumn.setCellValueFactory(new PropertyValueFactory<InvoiceProduct,String>("productName"));
        invoiceProductCountColumn.setCellValueFactory(new PropertyValueFactory<InvoiceProduct,String>("count"));
        invoiceProductLoadedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                InvoiceProduct invoice = (InvoiceProduct) param.getValue();
                SimpleBooleanProperty p = new SimpleBooleanProperty(invoice.getLoaded());
                p.addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                        invoice.setLoaded(new_val);
                    }
                });
                return p;
            }
        });
        invoiceProductLoadedColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                CheckBoxTableCell cell = new CheckBoxTableCell();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        productIdColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));
        productCategoryColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("category"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("price"));
        productCountColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("count"));
    }
    private void setOnEditCommit(){
        //clientIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        clientIdColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Client, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Client, String> t) {
                ((Client) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setId(t.getNewValue());
            }
        });
        clientNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        clientNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Client, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Client, String> t) {
                ((Client) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setName(t.getNewValue());
            }
        });
        clientSurnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        clientSurnameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Client, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Client, String> t) {
                ((Client) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setSurname(t.getNewValue());
            }
        });
        clientPhoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        clientPhoneColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Client, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Client, String> t) {
                ((Client) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPhone(t.getNewValue());
            }
        });
        clientPhone2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        clientPhone2Column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Client, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Client, String> t) {
                ((Client) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPhone2(t.getNewValue());
            }
        });
        clientAdressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        clientAdressColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Client, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Client, String> t) {
                ((Client) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setAdress(t.getNewValue());
            }
        });
        clientEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        clientEmailColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Client, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Client, String> t) {
                ((Client) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setEmail(t.getNewValue());
            }
        });

        //requestIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        requestIdColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Request, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Request, String> t) {
                ((Request) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setId(t.getNewValue());
            }
        });
        requestClientIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        requestClientIdColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Request, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Request, String> t) {
                ((Request) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setClientName(t.getNewValue());
            }
        });
        requestColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        requestColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Request, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Request, String> t) {
                ((Request) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setRequest(t.getNewValue());
            }
        });
        //requestCheckedColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        requestCheckedColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Request, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Request, String> t) {
                ((Request) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setChecked(Boolean.getBoolean(t.getNewValue()));
            }
        });
        //requestApprovedColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        requestApprovedColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Request, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Request, String> t) {
                ((Request) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setApproved(Boolean.getBoolean(t.getNewValue()));
            }
        });
        requestProductColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        requestProductColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RequestProduct, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<RequestProduct, String> t) {
                ((RequestProduct) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setProductName(t.getNewValue());
            }
        });
        requestCountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        requestCountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RequestProduct, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<RequestProduct, String> t) {
                ((RequestProduct) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setCount(t.getNewValue());
            }
        });

        //orderIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderIdColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Order, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Order, String> t) {
                ((Order) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setId(t.getNewValue());
            }
        });
        orderClientColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderClientColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Order, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Order, String> t) {
                ((Order) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setClientName(t.getNewValue());
            }
        });
        orderRequestColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderRequestColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Order, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Order, String> t) {
                ((Order) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setRequestName(t.getNewValue());
            }
        });
        orderContractColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderContractColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Order, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Order, String> t) {
                ((Order) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setContractName(t.getNewValue());
            }
        });
        //orderPaymentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderPaymentColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Order, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Order, String> t) {
                ((Order) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPayment(Boolean.getBoolean(t.getNewValue()));
            }
        });
        orderProductColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderProductColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<OrderProduct, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<OrderProduct, String> t) {
                ((OrderProduct) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setProductName(t.getNewValue());
            }
        });
        orderCountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderCountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<OrderProduct, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<OrderProduct, String> t) {
                ((OrderProduct) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setCount(t.getNewValue());
            }
        });
        orderRestColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderRestColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<OrderProduct, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<OrderProduct, String> t) {
                ((OrderProduct) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setRest(t.getNewValue());
            }
        });

        contractNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        contractNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Order, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Order, String> t) {
                ((Order) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setContractName(t.getNewValue());
            }
        });
        contractClientNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        contractClientNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Order, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Order, String> t) {
                ((Order) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setClientName(t.getNewValue());
            }
        });
        contractBeginDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        contractBeginDateColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Order, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Order, String> t) {
                ((Order) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setBeginDate(t.getNewValue());
            }
        });
        contractEndDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        contractEndDateColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Order, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Order, String> t) {
                ((Order) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setEndDate(t.getNewValue());
            }
        });
        //contractPaymentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        contractPaymentColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Order, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Order, String> t) {
                ((Order) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPayment(Boolean.getBoolean(t.getNewValue()));
            }
        });

        //invoiceIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        invoiceIdColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Invoice, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Invoice, String> t) {
                ((Invoice) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setId(t.getNewValue());
            }
        });
        invoiceContractColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        invoiceContractColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Invoice, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Invoice, String> t) {
                ((Invoice) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setContractName(t.getNewValue());
            }
        });
        invoiceDateCreateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        invoiceDateCreateColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Invoice, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Invoice, String> t) {
                ((Invoice) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setDateCreate(t.getNewValue());
            }
        });
        //invoiceAgreedColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        invoiceAgreedColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Invoice, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Invoice, String> t) {
                ((Invoice) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setAgreed(Boolean.getBoolean(t.getNewValue()));
            }
        });
        invoiceProductNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        invoiceProductNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InvoiceProduct, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InvoiceProduct, String> t) {
                ((InvoiceProduct) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setProductName(t.getNewValue());
            }
        });
        invoiceProductCountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        invoiceProductCountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InvoiceProduct, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InvoiceProduct, String> t) {
                ((InvoiceProduct) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setCount(t.getNewValue());
            }
        });
        //invoiceProductLoadedColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        invoiceProductLoadedColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InvoiceProduct, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InvoiceProduct, String> t) {
                ((InvoiceProduct) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setLoaded(Boolean.getBoolean(t.getNewValue()));
            }
        });
    }

    private void refreshClientTable(){
        clientTable.refresh();
        this.setAllClients();
    }
    private void refreshRequestTable(){
        requestTable.refresh();
        try {
            requestProductTable.refresh();
        }catch (NullPointerException e){}
        this.setAllRequests();
    }
    private void refreshOrderTable(){
        orderTable.refresh();
        try {
            orderProductTable.refresh();
        }catch (NullPointerException e){}
        this.setAllOrders();
    }
    private void refreshInvoiceTable(){
        invoiceTable.refresh();
        try {
            invoiceProductTable.refresh();
        }catch (NullPointerException e){}
        this.setAllInvoices();
    }
    private void refreshContractTable(){
        orderTable.refresh();
        contractTable.refresh();
        this.setAllOrders();
    }

    /**
     * запускается с помощью Callback в LoginController до initialize
     *
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    private void setLoginLabel(){
        loginLabel1.setText(login);
        loginLabel2.setText(login);
        loginLabel3.setText(login);
        loginLabel4.setText(login);
        loginLabel5.setText(login);
        loginLabel6.setText(login);
    }
    private void setAlert(String reason){
        Exception e = new Exception(reason);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Удаление невозможно");
        alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.toString())));
        alert.showAndWait();
    }

    /**
     * фильтры таблиц, связанные с продуктами, устанавливаются
     * непосредственно в момент нажатия на конкретное поле основной таблицы
     **/
    private void setClientFilter(){
        FilteredList<Client> filteredData = new FilteredList<>(usersList, p -> true);
        clientFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(client -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (client.getId().toLowerCase().contains(lowerCaseFilter) ||
                        client.getName().toLowerCase().contains(lowerCaseFilter) ||
                        client.getSurname().toLowerCase().contains(lowerCaseFilter) ||
                        client.getAdress().toLowerCase().contains(lowerCaseFilter) ||
                        client.getEmail().toLowerCase().contains(lowerCaseFilter) ||
                        client.getAdress().toLowerCase().contains(lowerCaseFilter) ||
                        client.getPhone().toLowerCase().contains(lowerCaseFilter) ||
                        client.getPhone2().toLowerCase().contains(lowerCaseFilter))
                {
                    return true; // Filter matches.
                }else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<Client> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(clientTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        clientTable.setItems(sortedData);
    }
    private void setRequestFilter(){
        FilteredList<Request> filteredData = new FilteredList<>(requestsList, p -> true);
        requestFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(request -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (request.getId().toLowerCase().contains(lowerCaseFilter) ||
                        request.getClientName().toLowerCase().contains(lowerCaseFilter) ||
                        request.getRequest().toLowerCase().contains(lowerCaseFilter) ||
                        request.getChecked().toString().toLowerCase().contains(lowerCaseFilter) ||
                        request.getApproved().toString().toLowerCase().contains(lowerCaseFilter))
                {
                    return true; // Filter matches.
                }else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<Request> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(requestTable.comparatorProperty());
        requestTable.setItems(sortedData);
    }
    private void setRequestProductFilter(){
        FilteredList<RequestProduct> filteredData = new FilteredList<>(requestProducts, p -> true);
        requestProductFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(requestProduct -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (
                        requestProduct.getProductName().toLowerCase().contains(lowerCaseFilter) ||
                        requestProduct.getCount().toLowerCase().contains(lowerCaseFilter))
                {
                    return true; // Filter matches.
                }else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<RequestProduct> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(requestProductTable.comparatorProperty());
        requestProductTable.setItems(sortedData);
    }
    private void setOrderFilter(){
        FilteredList<Order> filteredData = new FilteredList<>(ordersList, p -> true);
        orderFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (order.getId().toLowerCase().contains(lowerCaseFilter) ||
                        order.getClientName().toLowerCase().contains(lowerCaseFilter) ||
                        order.getRequestName().toLowerCase().contains(lowerCaseFilter) ||
                        order.getContractName().toLowerCase().contains(lowerCaseFilter) ||
                        order.getPayment().toString().toLowerCase().contains(lowerCaseFilter))
                {
                    return true; // Filter matches.
                }else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<Order> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(orderTable.comparatorProperty());
        orderTable.setItems(sortedData);
    }
    private void setOrderProductFilter(){
        FilteredList<OrderProduct> filteredData = new FilteredList<>(orderProducts, p -> true);
        orderProductFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(orderProduct -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (
                        orderProduct.getProductName().toLowerCase().contains(lowerCaseFilter) ||
                        orderProduct.getCount().toLowerCase().contains(lowerCaseFilter))
                {
                    return true; // Filter matches.
                }else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<OrderProduct> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(orderProductTable.comparatorProperty());
        orderProductTable.setItems(sortedData);
    }
    private void setInvoiceFilter(){
        FilteredList<Invoice> filteredData = new FilteredList<>(invoicesList, p -> true);
        invoiceFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(invoice -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (invoice.getId().toLowerCase().contains(lowerCaseFilter) ||
                        invoice.getContractName().toLowerCase().contains(lowerCaseFilter) ||
                        invoice.getDateCreate().toLowerCase().contains(lowerCaseFilter) ||
                        invoice.getAgreed().toString().toLowerCase().contains(lowerCaseFilter))
                {
                    return true; // Filter matches.
                }else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<Invoice> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(invoiceTable.comparatorProperty());
        invoiceTable.setItems(sortedData);
    }
    private void setInvoiceProductFilter(){
        FilteredList<InvoiceProduct> filteredData = new FilteredList<>(invoiceProducts, p -> true);
        invoiceProductFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(invoiceProduct -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (invoiceProduct.getProductName().toLowerCase().contains(lowerCaseFilter) ||
                    invoiceProduct.getCount().toLowerCase().contains(lowerCaseFilter) ||
                    invoiceProduct.getProductName().toLowerCase().contains(lowerCaseFilter) )
                {
                    return true; // Filter matches.
                }else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<InvoiceProduct> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(invoiceProductTable.comparatorProperty());
        invoiceProductTable.setItems(sortedData);
    }
    private void setContractFilter(){
        FilteredList<Order> filteredData = new FilteredList<>(ordersList, p -> true);
        contractFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (order.getBeginDate().toLowerCase().contains(lowerCaseFilter) ||
                        order.getEndDate().toLowerCase().contains(lowerCaseFilter) ||
                        order.getPayment().toString().toLowerCase().contains(lowerCaseFilter) ||
                        order.getContractName().toLowerCase().contains(lowerCaseFilter) ||
                        order.getClientName().toLowerCase().contains(lowerCaseFilter))
                {
                    return true; // Filter matches.
                }else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<Order> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(contractTable.comparatorProperty());
        contractTable.setItems(sortedData);
    }
    private void setProductFilter(){
        FilteredList<Product> filteredData = new FilteredList<>(productsList, p -> true);
        productFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (product.getId().toLowerCase().contains(lowerCaseFilter) ||
                        product.getName().toLowerCase().contains(lowerCaseFilter) ||
                        product.getCategory().toLowerCase().contains(lowerCaseFilter) ||
                        product.getCount().toLowerCase().contains(lowerCaseFilter) ||
                        product.getPrice().toLowerCase().contains(lowerCaseFilter))
                {
                    return true; // Filter matches.
                }else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<Product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedData);
    }
}
