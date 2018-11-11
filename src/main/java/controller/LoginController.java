package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.IOException;

public class LoginController{
    private Callback<Class<?>, Object> callback;
    private final Session session = HibernateUtil.getSessionFactory();

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    public LoginController() {
        callback = new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> controllerClass) {
                if (controllerClass == ManagerController.class) {
                    ManagerController controller = null;
                    try {
                        controller = new ManagerController();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    controller.setLogin(loginField.getText());
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
        Query query = session.createQuery(hql);
        String result = (String) query.getSingleResult();

        if(result.equals("manager")) this.createManager();
    }

    private void createManager() throws IOException {
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

        stage.show();
        stage.setResizable(false);
    }
    private void createSteward(String login){

    }
}
