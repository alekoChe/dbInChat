module ru.gbmodule3db.dbinchat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens ru.gbmodule3db.dbinchat to javafx.fxml;
    exports ru.gbmodule3db.dbinchat;
}