import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import util.HibernateUtil;
import com.jfoenix.controls.JFXDecorator;

import javax.persistence.metamodel.EntityType;

import java.util.Map;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fronts/login.fxml"));
        stage.setTitle("Authorization");

        //JFXDecorator decorator = new JFXDecorator(stage, root);
        //decorator.setCustomMaximize(true);
        //Scene scene = new Scene(decorator);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setResizable(false);
        scene.getStylesheets().add("/css/load.css");
        stage.getIcons().add(new Image("/pics/logo.png"));
        //scene.setFill(Color.TRANSPARENT);
        //stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}