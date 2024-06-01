package es.c3.ecofamesc.control;

import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.model.Anotacion;
import es.c3.ecofamesc.model.PlanEconomico;
import es.c3.ecofamesc.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BorrarAnotacionController {

    private LocalDate fecha;
    private String tipo;

    private EcoFamApplication ecoFamApplication;
    private PlanEconomico planEconomico;


    @FXML
    ListView<Anotacion> listView;
    @FXML
    Label lblAnotacion;
    ObservableList<Anotacion> anotaciones;

    public BorrarAnotacionController() {
    }

    public void setParametros(EcoFamApplication ecoFamApplication, PlanEconomico planEconomico, LocalDate fecha, String tipo) {
        this.ecoFamApplication = ecoFamApplication;
        this.planEconomico = planEconomico;
        this.fecha  = fecha;
        this.tipo = tipo;
        if ("I".equals(tipo)) {
            lblAnotacion.setText("Ingresos del día "+fecha);
            anotaciones = FXCollections.observableArrayList(ecoFamApplication.getAnotacionesIngresos(fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getYear(), planEconomico));
        }
        else {
            lblAnotacion.setText("Gastos del día "+fecha);
            anotaciones = FXCollections.observableArrayList(ecoFamApplication.getAnotacionesGastos(fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getYear(), planEconomico));
        }
        listView.setItems(anotaciones);

    }


    public void aceptarBorrarAnotacion(ActionEvent actionEvent) {
        Anotacion aSeleccionada = (Anotacion) listView.getSelectionModel().getSelectedItem();
        if (aSeleccionada == null) {
            mostrarMensaje(Alert.AlertType.ERROR, "Borrar anotación", "Error borrando anotación", "Debe seleccionar alguna anotación para borrar");
        }
        else if (ecoFamApplication.eliminarAnotacion(aSeleccionada.getId().getValue())) {
            mostrarMensaje(Alert.AlertType.INFORMATION, "Borrar anotación", "Borrar anotación", "Anotación borrada correctamente");
        }
        //ecoFamApplication.getFullCalendarView().populateCalendar(YearMonth.of(fecha.getYear(), fecha.getMonth()));
        ecoFamApplication.getFullCalendarView().getController().getNodoActual().recargaPanelDetalleAnchor();


        Stage stage = (Stage) listView.getScene().getWindow();
        stage.close();
    }//aceptarBorrarAnotacion

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(tipo);
        //alert.initOwner(this.getStage());
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void cancelarBorrarAnotacion(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();     //Me devuelve el elemento al que hice click
        Stage stage = (Stage) source.getScene().getWindow();    //Me devuelve la ventana donde se encuentra el elemento
        stage.close();
    }


}
