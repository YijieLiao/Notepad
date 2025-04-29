package yijieliao.notepad; // 注意换成你的包名

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;


//当你用 FXMLLoader.load() 加载 main.fxml 文件时，fxml加载器会自己创建控件树和实例化Controller类
public class MainController {

    @FXML
    private TextArea textArea;

    @FXML
    private Label statusLabel;

    private EditHandler editHandler;
    private FileHandler fileHandler; // 用来处理文件操作
    private FormatHandler formatHandler;

    private String fontnow="Arial";
    private int sizenow =14;
    private String lastSavedText = "";  // 保存时的文本内容快照

    // 初始化方法，在界面加载后自动执行
    @FXML
    public void initialize() {
        System.out.println("界面加载完成！");

        // 监听光标位置变化和文本变化：两个时间监听
        textArea.caretPositionProperty().addListener((obs, oldPos, newPos) -> updateStatus());
        textArea.textProperty().addListener((obs, oldText, newText) -> {
            updateStatus();
        });
        // 初始化的时候也更新一次
        updateStatus();
    }

    //等FXML控件注入完，用完成初始化的信息对handlers进行初始化
    public void setPrimaryStage(Stage stage) {
        this.fileHandler = new FileHandler(stage); // 初始化文件处理器
        this.formatHandler = new FormatHandler(textArea); // 初始化格式模块
        this.editHandler = new EditHandler(textArea);
        lastSavedText = textArea.getText();
    }
    // 以下是菜单栏事件绑定方法

    @FXML
    private void newFile() {
        fileHandler.newFile(textArea);
        lastSavedText = textArea.getText();  // 同步更新“已保存内容”
    }

    @FXML
    private void openFile() {
        fileHandler.openFile(textArea);
        lastSavedText = textArea.getText();  // 同步更新“已保存内容”
    }

    @FXML
    private void saveFile() {
        fileHandler.saveFile(textArea);
        lastSavedText = textArea.getText();  // 同步更新“已保存内容”
    }

    @FXML
    private void saveFileAs() {
        fileHandler.saveFileAs(textArea);
        lastSavedText = textArea.getText();  // 同步更新“已保存内容”
    }

    @FXML
    private void toggleWrap(javafx.event.ActionEvent event) {
        formatHandler.toggleWrap((CheckMenuItem) event.getSource());
    }
    //ActionEvent 是 JavaFX 中按钮、菜单项、CheckMenuItem点击时产生的标准事件。
    //事件对象 event 记录了是谁触发了这个事件。然后回返回这个对象
    //在这里只有一个按钮绑定了这个方法，所以不用担心一些问题

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
        alert.setContentText("作者：廖羿杰 厦门大学信息学院 2023级本科生\n版本：v1.0\n开发工具：JavaFX + SceneBuilder");
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

        //注册时间，点击了查找下一个按钮之后，就会…
        findNextBtn.setOnAction(e -> {
            //从当前内容开始查找下一个
            int pos = editHandler.findText(findField.getText(), textArea.getCaretPosition());
            if (pos >= 0) {
                //如果查找到了目标文字的开头，就调用selectrange
                //用来在 TextArea 里选中一段特定范围的文本
                //选中的区域是 [start, end)，即包含 start，不包含 end的位置！
                textArea.selectRange(pos, pos + findField.getText().length());
            } else {
                //Alert 是 JavaFX 中专门用来弹出消息对话框的类。
                //它继承自 Dialog<ButtonType>，也是一种弹窗控件。
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

        //设置好布局
        //getchildren返回 VBox 里面的 子节点列表，类型是 ObservableList<Node>
        //可以理解为VBox的内部控件清单
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
        //创建一个新的窗口并命名

        //添加一个VBox容器
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // 字体选择下拉菜单
        ComboBox<String> fontComboBox = new ComboBox<>();
        fontComboBox.getItems().addAll(Font.getFamilies());
        //font.getfamilies返回当前系统上可用的字体家族（字体名）列表
        //返回一个 List<String>
        fontComboBox.setValue(fontnow);  // 默认选择上一次保存的字体

        // 字号选择下拉菜单
        ComboBox<Integer> sizeComboBox = new ComboBox<>();
        sizeComboBox.setEditable(true);//设置可以编辑
        sizeComboBox.getItems().addAll(10, 12, 14, 16, 18, 20, 24, 30, 36);
        sizeComboBox.setValue(sizenow);  // 默认字号 14


        // 应用按钮
        Button applyButton = new Button("应用");

        //现在因为变成可编辑了，用户输入的值是个String（文本）！
        //所以要稍微修改一下：
        //点击应用按钮的事件注册
        applyButton.setOnAction(e -> {
            String selectedFont = fontComboBox.getValue();

            int selectedSize;
            Object value = sizeComboBox.getValue();

            if (value instanceof Integer) {
                selectedSize = (Integer) value;
            } else if (value instanceof String) {
                try {
                    selectedSize = Integer.parseInt(((String) value).trim());
                } catch (NumberFormatException ex) {
                    showErrorAlert("请输入有效的数字字号！");
                    return;
                }
            } else {
                showErrorAlert("未知错误，请重新输入字号！");
                return;
            }


            formatHandler.setFont(selectedFont, selectedSize);  // 调用FormatHandler设置字体和字号
            fontnow = selectedFont;
            sizenow = selectedSize;//将这次的结果保存下来

            dialog.close();
        });

        // 布局
        vbox.getChildren().addAll(new Label("选择字体:"), fontComboBox, new Label("选择字号:"), sizeComboBox, applyButton);

        // 新的场景，将vbox容器加入景区，并给dialog窗口设置这个场景
        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.initOwner(textArea.getScene().getWindow());//设置父级窗口
        dialog.show();
    }

    //判断是否可以退出，ture代表不需要（关闭就好了）
    public boolean confirmSave() {
        // 如果当前文本内容和上次保存时一致，直接退出
        if (textArea.getText().equals(lastSavedText)) {
            return true;
        }

        Optional<ButtonType> result = AppEventManager.showSaveConfirmation();
        if (result.isPresent()) {
            ButtonType selected = result.get();
            if (selected.getText().equals("保存")) {
                saveFile();
                lastSavedText = textArea.getText(); // ⭐ 同步保存快照
                return true;
            } else if (selected.getText().equals("不保存")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //一个显示错误的函数
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
