import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("JavaFx Drawing hw5");
        Parent root = FXMLLoader.load(getClass().getResource("ShapePadController.fxml")); //載入fxml檔
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
 public static void main(String[] args) {
        launch(args);
    }
} 