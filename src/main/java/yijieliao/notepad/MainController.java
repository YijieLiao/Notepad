package yijieliao.notepad; // 注意换成你的包名

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class MainController {

    @FXML
    private TextArea textArea;

    @FXML
    private Label statusLabel;

    private EditHandler editHandler;
    private FileHandler fileHandler; // 用来处理文件操作
    private FormatHandler formatHandler;

    // 初始化方法，在界面加载后自动执行
    @FXML
    public void initialize() {
        System.out.println("界面加载完成！");

        // 监听光标位置变化和文本变化：两个时间监听
        textArea.caretPositionProperty().addListener((obs, oldPos, newPos) -> updateStatus());
        textArea.textProperty().addListener((obs, oldText, newText) -> updateStatus());

        // 初始化的时候也更新一次
        updateStatus();
    }

    //等FXML控件注入完，用完成初始化的信息对handlers进行初始化
    public void setPrimaryStage(Stage stage) {
        this.fileHandler = new FileHandler(stage); // 初始化文件处理器
        this.formatHandler = new FormatHandler(textArea); // 初始化格式模块
        this.editHandler = new EditHandler(textArea);
    }
    // 以下是菜单栏事件绑定方法

    @FXML
    private void newFile() {
        fileHandler.newFile(textArea);
    }

    @FXML
    private void openFile() {
        fileHandler.openFile(textArea);
    }

    @FXML
    private void saveFile() {
        fileHandler.saveFile(textArea);
    }

    @FXML
    private void saveFileAs() {
        fileHandler.saveFileAs(textArea);
    }

    @FXML
    private void toggleWrap(javafx.event.ActionEvent event) {
        formatHandler.toggleWrap((CheckMenuItem) event.getSource());
    }

    private void updateStatus() {
        // 获取光标在文本中的位置（从0开始计数）
        int caretPos = textArea.getCaretPosition();
        String text = textArea.getText();

        // 计算行号，在这里需要转换为字符流
        int lineNumber = (int) text.substring(0, Math.min(caretPos, text.length()))
                .chars()
                .filter(ch -> ch == '\n')
                .count() + 1;

        // 计算列号
        int lastNewLine = text.lastIndexOf('\n', Math.max(caretPos - 1, 0));
        int columnNumber = caretPos - lastNewLine;

        // 字数
        int charCount = text.length();

        // 更新状态栏文本
        statusLabel.setText(String.format("行:%d 列:%d 字数:%d", lineNumber, columnNumber, charCount));
    }

    @FXML
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("关于 MyNotepad");
        alert.setHeaderText("MyNotepad - 简易记事本");
        alert.setContentText("作者：yijieliao\n版本：v1.0\n开发工具：JavaFX + SceneBuilder");
        alert.showAndWait();
    }

    @FXML
    private void undoAction() {
        textArea.undo();
    }

    @FXML
    private void redoAction() {
        textArea.redo();
    }


    @FXML
    private void openFindReplaceDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("查找与替换");

        VBox vbox = new VBox(10);
        vbox.setPadding(new javafx.geometry.Insets(10));

        TextField findField = new TextField();
        findField.setPromptText("输入查找内容");

        TextField replaceField = new TextField();
        replaceField.setPromptText("输入替换内容");

        Button findNextBtn = new Button("查找下一个");
        Button replaceBtn = new Button("替换");
        Button replaceAllBtn = new Button("全部替换");

        findNextBtn.setOnAction(e -> {
            int pos = editHandler.findText(findField.getText(), textArea.getCaretPosition());
            if (pos >= 0) {
                textArea.selectRange(pos, pos + findField.getText().length());
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("查找结果");
                alert.setHeaderText(null);
                alert.setContentText("未找到匹配内容！");
                alert.showAndWait();
            }
        });

        replaceBtn.setOnAction(e -> {
            editHandler.replaceSelectedText(replaceField.getText());
        });

        replaceAllBtn.setOnAction(e -> {
            editHandler.replaceAll(findField.getText(), replaceField.getText());
        });

        vbox.getChildren().addAll(findField, replaceField, findNextBtn, replaceBtn, replaceAllBtn);

        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.initOwner(textArea.getScene().getWindow());
        dialog.show();
    }

    @FXML
    private void openFontSettingsDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("字体设置");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // 字体选择下拉菜单
        ComboBox<String> fontComboBox = new ComboBox<>();
        fontComboBox.getItems().addAll(Font.getFamilies());
        fontComboBox.setValue("Arial");  // 默认选择 Arial

        // 字号选择下拉菜单
        ComboBox<Integer> sizeComboBox = new ComboBox<>();
        sizeComboBox.getItems().addAll(10, 12, 14, 16, 18, 20, 24, 30, 36);
        sizeComboBox.setValue(14);  // 默认字号 14

        // 应用按钮
        Button applyButton = new Button("应用");
        applyButton.setOnAction(e -> {
            String selectedFont = fontComboBox.getValue();
            int selectedSize = sizeComboBox.getValue();
            formatHandler.setFont(selectedFont, selectedSize);  // 调用FormatHandler设置字体和字号
            dialog.close();
        });

        // 布局
        vbox.getChildren().addAll(new Label("选择字体:"), fontComboBox, new Label("选择字号:"), sizeComboBox, applyButton);

        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.initOwner(textArea.getScene().getWindow());
        dialog.show();
    }

    public boolean confirmSave() {
        if (textArea.getText().isEmpty()) {
            return true; // 如果没有内容，直接退出
        }

        // 这里直接调用 showSaveConfirmation() 来弹出确认窗口
        return AppEventManager.showSaveConfirmation();
    }
}
