module yijieliao.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens yijieliao.notepad to javafx.fxml;
    exports yijieliao.notepad;
}