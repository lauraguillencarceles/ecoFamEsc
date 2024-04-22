module es.c3.ecofamesc {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires android.json;

    opens es.c3.ecofamesc to javafx.fxml;
    exports es.c3.ecofamesc;
    exports es.c3.ecofamesc.control;
    opens es.c3.ecofamesc.control to javafx.fxml;
}