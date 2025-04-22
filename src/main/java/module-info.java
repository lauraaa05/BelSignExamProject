module dk.easv.belsignexamproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens dk.easv.belsignexamproject to javafx.fxml;
    exports dk.easv.belsignexamproject;
}