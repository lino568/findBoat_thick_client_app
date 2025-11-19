module fr.cda.findboat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens fr.cda.findboat to javafx.fxml;
    exports fr.cda.findboat;
}