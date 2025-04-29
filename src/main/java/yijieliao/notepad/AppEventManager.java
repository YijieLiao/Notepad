package yijieliao.notepad;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;

public class AppEventManager {

    // 管理退出时的确认事件，需要输入主窗口和maincontroller
    public static boolean handleExitConfirmation(Stage primaryStage, MainController controller) {
        boolean decision = controller.confirmSave();
        if (decision) {
            primaryStage.close();
            return true;
        } else {
            return false;
        }
    }

    // 弹出一个退出确认弹窗，让用户选择要不要保存未保存的内容
    public static Optional<ButtonType> showSaveConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("退出确认");
        alert.setHeaderText("您有未保存的内容，是否保存？");
        alert.setContentText("选择“保存”以保存文件，选择“不保存”直接退出，或选择“取消”返回编辑。");

        ButtonType saveButton = new ButtonType("保存");
        ButtonType dontSaveButton = new ButtonType("不保存");
        ButtonType cancelButton = new ButtonType("取消");

        alert.getButtonTypes().setAll(saveButton, dontSaveButton, cancelButton);

        return alert.showAndWait(); // 把用户选择返回给外面判断
    }

}
