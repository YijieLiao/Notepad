package yijieliao.notepad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    //start()负责画出第一个界面，是 JavaFX 应用真正的启动入口
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/yijieliao/notepad/main.fxml"));
        //loader.load() 真正读取FXML并生成界面对应的控件树-parent类型
        Parent root = loader.load();

        // 拿到 main.fxml 绑定的 MainController 控制器实例，现在就可以在app内调用Controller相关
        MainController controller = loader.getController();
        // 将主窗口传给Controller，这个函数涉及对Controller的初始化（主要是Controller中的文件管理器需要管理主窗口）
        controller.setPrimaryStage(primaryStage);

        // 注册关闭事件的监听器
        primaryStage.setOnCloseRequest(event -> {
            if (!AppEventManager.handleExitConfirmation(primaryStage, controller)) {
                event.consume();
                //如果返回ture（不需要保存），就会直接结束，false就代表需要保存
                //如果返回了false event.consume() 是用来"吃掉"当前事件，阻止它继续传播和触发默认操作。
            }
        });

        primaryStage.setTitle("简易记事本");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    //程序的启动
    public static void main(String[] args) {
        launch(args);
        //会自动创建 Application 实例并调用它的 start(Stage primaryStage) 方法
        //创建一个主窗口 Stage 并调用了 start(Stage primaryStage) 方法，并且把主窗口作为参数传进来！
    }
}