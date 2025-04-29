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
        boolean selected = menuItem.isSelected();//获取当前item的选择属性并影响TextArea本身内置的 wrapText 属性
        textArea.setWrapText(selected);
    }

    // 字体设置、字号调整等方法
    public void setFont(String font, int size) {
        textArea.setStyle("-fx-font-family: '" + font + "'; -fx-font-size: " + size + "px;");
    }
    //通过给TextArea设置CSS样式字符串，来动态改变字体和字号
}
