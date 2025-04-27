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

    // （预留）以后可以加字体设置、字号调整等方法
}
