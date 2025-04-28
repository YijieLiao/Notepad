package yijieliao.notepad;

import javafx.scene.control.TextArea;

public class EditHandler {

    private final TextArea textArea;

    //JavaFX里的 TextArea 已经自带了撤销/重做机制
    public EditHandler(TextArea textArea) {
        this.textArea = textArea;
    }

    // 撤销
    public void undo() {
        textArea.undo();
    }

    // 重做
    public void redo() {
        textArea.redo();
    }

    // 查找关键词
    public int findText(String keyword, int startPos) {
        String text = textArea.getText();
        return text.indexOf(keyword, startPos);
    }

    // 替换当前选中
    public void replaceSelectedText(String replacement) {
        textArea.replaceSelection(replacement);
    }

    // 替换全部匹配
    public void replaceAll(String keyword, String replacement) {
        String text = textArea.getText();
        textArea.setText(text.replace(keyword, replacement));
    }
}
