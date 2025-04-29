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

    // 替换当前选中（其实就是直接用了textarea的方法，他会自己用replacement替换我们选中的内容）
    public void replaceSelectedText(String replacement) {
        textArea.replaceSelection(replacement);
    }

    // 替换全部匹配，使用的是String的replace方法，先获取text转换为String，替换之后转回去
    public void replaceAll(String keyword, String replacement) {
        String text = textArea.getText();
        textArea.setText(text.replace(keyword, replacement));
    }
}
