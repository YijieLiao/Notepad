package yijieliao.notepad; // 注意换成你的包名

import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class MainController {

    @FXML
    private TextArea textArea;

    @FXML
    private Label statusLabel;

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



}
