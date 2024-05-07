package es.c3.ecofamesc.control;

import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.model.PlanEconomico;
import es.c3.ecofamesc.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AnotacionController {

    // Get the pane to put the calendar on
    @FXML
    AnchorPane anotacionPane;
    @FXML
    private DatePicker dateFecha;
    @FXML
    private RadioButton radioIngreso;
    @FXML
    private RadioButton radioGasto;

    @FXML
    private TextField concepto;

    @FXML
    private TextField cantidad;

    private  boolean insertada = false;
    private  Float money;
    private  String concept;
    private  boolean gasto;
    private EcoFamApplication ecoFamApplication;
    private PlanEconomico planEconomico;

    public AnotacionController() {
    }

    public void setEcoFamApplication(EcoFamApplication ecoFamApplication, PlanEconomico planEconomico) {
        this.ecoFamApplication = ecoFamApplication;
        this.planEconomico = planEconomico;

    }
    public void setFecha(LocalDate fecha) {
        //LocalDate localDate = LocalDate.of(fecha.getYear(), fecha.getMonthValue(), fecha.getDayOfMonth());
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy" );
        dateFecha.setValue(fecha);
        dateFecha.setConverter(new LocalDateStringConverter(formato, null));
        System.out.println(fecha.toString());
    }

    public void aceptarAnotacion(ActionEvent actionEvent) {

        LocalDate fecha = dateFecha.getValue();
        gasto = true;
        if (radioIngreso.isSelected()) {
            gasto = false;
        }

        try {
            String cant = cantidad.getText();
            if (cant != null && cant.length() > 0) {
                if (cant.contains(",")) {
                    cant = cant.replace(",", ".");
                }
                money = Float.parseFloat(cant);
                concept = concepto.getText();
                if (agregarAnotacion(fecha, gasto, concept, money)) {
                    insertada = true;
                    mostrarMensaje(Alert.AlertType.INFORMATION, "Anotación", (gasto ? "Gasto" : "Ingreso") + " anotado", "Anotación incluida correctamente");
                } else {
                    mostrarMensaje(Alert.AlertType.ERROR, "Anotación", "Error insertando anotación", "Datos incorrectos");
                }
            }
            else mostrarMensaje(Alert.AlertType.ERROR, "Anotación", "Error insertando anotación", "Debe indicar una cantidad");
            Node source = (Node) actionEvent.getSource();     //Me devuelve el elemento al que hice click
            cerrarVentana(source);
        } catch (NumberFormatException nfe) {
            System.err.println(nfe.getMessage());
        }

    }
    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(tipo);
        //alert.initOwner(this.getStage());
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private boolean agregarAnotacion(LocalDate fecha, boolean gasto, String concep, Float money) {
        if (fecha == null) return false;
        if (concep == null || concep.length() == 0) return false;
        return ecoFamApplication.agregarAnotacion(fecha, gasto, concep, money, planEconomico);
    }
    public void cancelarAnotacion(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();     //Me devuelve el elemento al que hice click
        cerrarVentana(source);
    }

    private void cerrarVentana(Node source) {
        //Node source = (Node) actionEvent.getSource();     //Me devuelve el elemento al que hice click
        Stage stage = (Stage) source.getScene().getWindow();    //Me devuelve la ventana donde se encuentra el elemento
        stage.close();
    }

    public boolean isInsertada() {
        return insertada;
    }

    public void setInsertada(boolean insertada) {
        this.insertada = insertada;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public boolean isGasto() {
        return gasto;
    }

    public void setGasto(boolean gasto) {
        this.gasto = gasto;
    }

    public DatePicker getDateFecha() {
        return dateFecha;
    }

    public void setDateFecha(DatePicker dateFecha) {
        this.dateFecha = dateFecha;
    }
}
