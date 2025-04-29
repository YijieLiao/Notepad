package yijieliao.notepad;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.io.*;


public class FileHandler {

    private final Stage stage;//是当前窗口，给 FileChooser 用的
    private File currentFile;//java的io库的文件类型，记录当前文件，保存时直接覆盖，默认是null就可以

    public FileHandler(Stage stage) {
        this.stage = stage;
    }//初始化

    // 新建文件
    public void newFile(TextArea textArea) {
        textArea.clear();
        currentFile = null;
        stage.setTitle("简易记事本 - 新建文件");
    }

    // 打开文件
    public void openFile(TextArea textArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开文件");

        //给fileChooser添加过滤器，只能选择.txt文件。
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("文本文件 (*.txt)", "*.txt"));

        //fileChooser.showOpenDialog(stage) 会阻塞程序，直到用户选择了文件或者取消。
        //获取到的结果返回file
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                //StringBuilder是可变对象,追加内容时只是往数组里加，不创建新对象。
                String line;
                while ((line = reader.readLine()) != null) {
                    //BufferedReader 的 readLine() 不包含换行符！
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
                currentFile = file;
                stage.setTitle("简易记事本 - " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 保存文件
    public void saveFile(TextArea textArea) {
        if (currentFile != null) {
            writeToFile(currentFile, textArea);
        } else {
            saveFileAs(textArea);
        }
    }//如果当前并没有currentfile，就启用另存为；否则使用写入文件函数

    // 另存为
    public void saveFileAs(TextArea textArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("另存为");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("文本文件 (*.txt)", "*.txt"));
        //弹出保存文件对话框。
        //如果用户选好了路径并点击保存，返回一个 File 对象。取消则返回null
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            writeToFile(file, textArea);
            currentFile = file;
            stage.setTitle("简易记事本 - " + file.getName());
        }
    }

    // 工具方法：实际写文件
    private void writeToFile(File file, TextArea textArea) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
