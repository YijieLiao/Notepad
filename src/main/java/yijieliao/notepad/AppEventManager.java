package yijieliao.notepad;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;

public class AppEventManager {

    // 管理退出时的确认事件
    public static boolean handleExitConfirmation(Stage primaryStage, MainController controller) {
        if (controller.confirmSave()) {
            primaryStage.close();
            return true;
        } else {
            return false;
        }
    }

    // 显示退出确认弹窗
    public static boolean showSaveConfirmation() {
        // 创建退出确认弹窗
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("退出确认");
        alert.setHeaderText("您有未保存的内容，是否保存？");
        alert.setContentText("选择“保存”以保存文件，选择“不保存”直接退出，或选择“取消”返回编辑。");

        ButtonType saveButton = new ButtonType("保存");
        ButtonType dontSaveButton = new ButtonType("不保存");
        ButtonType cancelButton = new ButtonType("取消");

        alert.getButtonTypes().setAll(saveButton, dontSaveButton, cancelButton);

        // 显示并等待用户的选择
        Optional<ButtonType> result = alert.showAndWait();

        // 判断用户选择的按钮
        if (result.isPresent()) {
            if (result.get() == saveButton) {
                return true; // 用户选择保存
            } else if (result.get() == dontSaveButton) {
                return true; // 用户选择不保存
            } else {
                return false; // 用户选择取消
            }
        }
        return false; // 如果没有选择，默认不关闭
    }

}
