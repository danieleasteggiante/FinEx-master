module it.gend.finex {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.logging;
    requires com.opencsv;


    opens it.gend.finex to javafx.fxml;
    exports it.gend.finex;
    exports it.gend.finex.domain;
    opens it.gend.finex.domain to javafx.fxml;
    exports it.gend.finex.parser;
    opens it.gend.finex.parser to javafx.fxml;
}