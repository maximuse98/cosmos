package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.IOException;

public class LoginController{
    private Callback<Class<?>, Object> callback;
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView wrong1;
    @FXML
    private ImageView wrong2;

    public LoginController() {
        callback = new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> controllerClass) {
                if (controllerClass == ManagerController.class) {
                    ManagerController controller = null;
                    try {
                        controller = new ManagerController();
                        controller.setLogin(loginField.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return controller;
                } else if (controllerClass == StorerController.class) {
                    StorerController controller = null;
                    try {
                        controller = new StorerController();
                        controller.setLogin(loginField.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return controller;
                } else {
                    try {
                        return controllerClass.newInstance();
                    } catch (Exception exc) {
                        throw new RuntimeException(exc);
                    }
                }
            }
        };
    }

    public void onLoginClick(ActionEvent actionEvent) throws IOException {
        String hql = "SELECT role" +
                " FROM UsersEntity " +
                " WHERE login LIKE '"+ loginField.getText()+"' AND password LIKE '"+ passwordField.getText() +"'";
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hql);
        try {
            String result = (String) query.getSingleResult();
            if(result.equals("manager")) this.createManager(actionEvent);
            if(result.equals("storer")) this.createStorer(actionEvent);
        }
        catch (Exception e){
            wrong1.setVisible(true);
            wrong2.setVisible(true);
            passwordField.clear();
        }
        session.close();

    }

    private void createManager(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fronts/manager.fxml"));

        fxmlLoader.setControllerFactory(callback);
        fxmlLoader.load();

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        //stage.getIcons().add(new Image(getClass().getResource("/pictures/logo.png").toString(), 512, 512, true, true));
        //scene.getStylesheets().add("/styles/main.css");
        stage.setScene(scene);
        stage.setTitle("Manager");
        //stage.setMinHeight(560);
        //stage.setMinWidth(550);
        scene.getStylesheets().add("/css/main.css");
        stage.getIcons().add(new Image("/pics/logo.png"));

        stage.show();
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //Platform.exit();
                //System.exit(0);
                Stage stage = new Stage();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/fronts/login.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setTitle("Authorization");
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setResizable(false);
                scene.getStylesheets().add("/css/load.css");
                stage.getIcons().add(new Image("/pics/logo.png"));
                stage.show();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
        });

        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
    private void createStorer(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fronts/storer.fxml"));

        fxmlLoader.setControllerFactory(callback);
        fxmlLoader.load();

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Storer");

        scene.getStylesheets().add("/css/main.css");
        stage.getIcons().add(new Image("/pics/logo.png"));

        stage.show();
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //Platform.exit();
                //System.exit(0);

                Stage stage = new Stage();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/fronts/login.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setTitle("Authorization");
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setResizable(false);
                scene.getStylesheets().add("/css/load.css");
                stage.getIcons().add(new Image("/pics/logo.png"));
                stage.show();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
        });

        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
