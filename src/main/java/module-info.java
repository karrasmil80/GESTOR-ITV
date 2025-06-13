module dev.kkarrasmil80.gestoritv {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens dev.kkarrasmil80.gestoritv to javafx.fxml;
    exports dev.kkarrasmil80.gestoritv;
}