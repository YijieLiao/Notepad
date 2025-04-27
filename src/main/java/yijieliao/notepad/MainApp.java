package yijieliao.notepad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/yijieliao/notepad/main.fxml"));
        Parent root = loader.load();

        // 这里拿到Controller，并设置Stage
        MainController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("简易记事本");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}