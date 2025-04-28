package yijieliao.notepad;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextArea;

public class FormatHandler {

    private final TextArea textArea;

    public FormatHandler(TextArea textArea) {
        this.textArea = textArea;//JavaFX的TextArea控件本身就内置了一个属性：
    }

    // 自动换行
    public void toggleWrap(CheckMenuItem menuItem) {
        boolean selected = menuItem.isSelected();
        textArea.setWrapText(selected);
    }

    // 字体设置、字号调整等方法
    public void setFont(String font, int size) {
        textArea.setStyle("-fx-font-family: '" + font + "'; -fx-font-size: " + size + "px;");
    }
}
