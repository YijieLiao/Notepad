package yijieliao.notepad;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.io.*;


public class FileHandler {

    private final Stage stage;//primaryStage 是当前窗口，给 FileChooser 用的
    private File currentFile;//currentFile 是记录当前文件，保存时直接覆盖

    public FileHandler(Stage stage) {
        this.stage = stage;
    }

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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("文本文件 (*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
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
    }

    // 另存为
    public void saveFileAs(TextArea textArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("另存为");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("文本文件 (*.txt)", "*.txt"));
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
