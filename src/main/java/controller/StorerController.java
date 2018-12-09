package controller;

import entity.StatementInvoiceView;
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
import javafx.util.Callback;
import models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class StorerController implements Initializable {
    @FXML
    private TableColumn<StatementInvoice, String> idInvoiceProductColumn;
    @FXML
    private TableColumn<StatementInvoice, String> clientNameColumn;
    @FXML
    private TableColumn<StatementInvoice, String> clientSurnameColumn;
    @FXML
    private TableColumn<StatementInvoice, String> clientPhoneColumn;
    @FXML
    private TableColumn<StatementInvoice, String> productNameColumn;
    @FXML
    private TableColumn<StatementInvoice, String> clientAdressColumn;
    @FXML
    private TableColumn<StatementInvoice, String> invoiceProductCountColumn;
    @FXML
    private TableColumn invoiceProductLoadedColumn;
    @FXML
    private TableView statementInvoiceTable;
    @FXML
    private TextField statementInvoiceFilter;

    @FXML
    private Label loginLabel1;


    private String login;
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private ObservableList<StatementInvoice> statementInvoicesList;

    public StorerController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setFactories();
        //this.setOnEditCommit();
        this.setAllStatementsInvoices();
        this.setLoginLabel();
    }

    private void setFactories() {
        idInvoiceProductColumn.setCellValueFactory(new PropertyValueFactory<StatementInvoice, String>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<StatementInvoice, String>("clientName"));
        clientSurnameColumn.setCellValueFactory(new PropertyValueFactory<StatementInvoice, String>("surname"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<StatementInvoice, String>("phone"));

        clientAdressColumn.setCellValueFactory(new PropertyValueFactory<StatementInvoice, String>("adress"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<StatementInvoice, String>("productName"));
        invoiceProductCountColumn.setCellValueFactory(new PropertyValueFactory<StatementInvoice, String>("count"));
        invoiceProductLoadedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                StatementInvoice statementInvoice = (StatementInvoice) param.getValue();
                SimpleBooleanProperty p = new SimpleBooleanProperty(statementInvoice.getLoaded());
                p.addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                        //мы еще никаких обновлений в бд не делаем!!
                        statementInvoice.setCheckBoxStatus(new_val);
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
    }

    @Deprecated
    private void setOnEditCommit() {
        invoiceProductLoadedColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StatementInvoice, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<StatementInvoice, String> t) {
                ((StatementInvoice) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setCheckBoxStatus(Boolean.getBoolean(t.getNewValue()));
                refreshStatementInvoiceTable();
            }
        });
    }

    private void setAllStatementsInvoices() {
        statementInvoicesList = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        Query query1 = session.createQuery(" FROM StatementInvoiceView ");
        Iterator iter = query1.list().iterator();
        while (iter.hasNext()) {
            StatementInvoiceView statementInvoice = (StatementInvoiceView) iter.next();
            statementInvoicesList.add(new StatementInvoice(statementInvoice));
        }
        statementInvoiceTable.setItems(statementInvoicesList);
        session.close();
        this.setStatementInvoiceFilter();
    }


    private void setStatementInvoiceFilter() {
        FilteredList<StatementInvoice> filteredData = new FilteredList<>(statementInvoicesList, p -> true);

        statementInvoiceFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(statementInvoice -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (statementInvoice.getId().toLowerCase().contains(lowerCaseFilter) ||
                        statementInvoice.getClientName().toLowerCase().contains(lowerCaseFilter) ||
                        statementInvoice.getSurname().toLowerCase().contains(lowerCaseFilter) ||
                        statementInvoice.getAdress().toLowerCase().contains(lowerCaseFilter) ||
                        statementInvoice.getPhone().toLowerCase().contains(lowerCaseFilter) ||
                        statementInvoice.getProductName().toLowerCase().contains(lowerCaseFilter) ||
                        statementInvoice.getCount().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches.
                } else {
                    return false; // Does not match.
                }
            });
        });
        SortedList<StatementInvoice> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(statementInvoiceTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        statementInvoiceTable.setItems(sortedData);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private void refreshStatementInvoiceTable() {
        statementInvoiceTable.refresh();
        try {
            statementInvoiceTable.refresh();
        } catch (NullPointerException e) {
        }
        this.setAllStatementsInvoices();
    }

    private void setLoginLabel() {
        loginLabel1.setText(login);
    }

    public void onLoadedChangeClick() {
        Iterator<StatementInvoice> iterator = statementInvoicesList.iterator();
        while(iterator.hasNext()){
            StatementInvoice statementInvoice = iterator.next();
            if(statementInvoice.getCheckBoxStatus()) {
                statementInvoice.setLoaded(true);
            }
        }
        this.refreshStatementInvoiceTable();
//        StatementInvoice selectedChange = (StatementInvoice) statementInvoiceTable.getSelectionModel().getSelectedItem();
//
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        Iterator<StatementInvoice> itr = statementInvoicesList.iterator();
//        Query query = session.createQuery("update InvoiceProductEntity set loaded = :newLoaded where id = :selectedId");
//        while (itr.hasNext()) {
//            StatementInvoice result = itr.next();
//            if (result.getId() == selectedChange.getId()) {
//                query.setParameter("newLoaded", selectedChange.getStatementInvoiceView().getLoaded());
//                query.setParameter("selectedId", selectedChange.getStatementInvoiceView().getId());
//                query.executeUpdate();
//            }
//        }
//        session.getTransaction().commit();
//        session.close();
//        this.refreshStatementInvoiceTable();
    }
}
