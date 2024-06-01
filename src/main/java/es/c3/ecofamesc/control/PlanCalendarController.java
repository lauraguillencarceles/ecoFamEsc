package es.c3.ecofamesc.control;

import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.calendar.AnchorPaneNode;
import es.c3.ecofamesc.calendar.UtilsCalendar;
import es.c3.ecofamesc.model.Anotacion;
import es.c3.ecofamesc.model.PlanEconomico;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanCalendarController {

    // Get the pane to put the calendar on
    @FXML
    private Pane calendarPane;
    @FXML
    HBox hBox;

    @FXML
    private Pane detailPane;
    @FXML
    Label lblValorIngresos;
    @FXML
    Label lblValorGastos;
    @FXML
    Label lblResumenDia;

    private PlanEconomico planEconomico;
    private EcoFamApplication ecoFamApplication;
    private LocalDate diaActual;
    private AnchorPaneNode nodoActual;

    public void setEcoFamApplication(EcoFamApplication ecoFamApplication) {
        this.ecoFamApplication = ecoFamApplication;
    }

    public Pane getCalendarPane() {
        return calendarPane;
    }

    public void setCalendarPane(Pane calendarPane) {
        this.calendarPane = calendarPane;
    }

    public Pane getDetailPane() {
        return detailPane;
    }

    public void setDetailPane(Pane detailPane) {
        this.detailPane = detailPane;
    }

    public PlanEconomico getPlanEconomico() {
        return planEconomico;
    }

    public void setPlanEconomico(PlanEconomico planEconomico) {
        this.planEconomico = planEconomico;
    }

    public float getIngresos(int dia, int mes, int anyo) {
        return ecoFamApplication.getIngresosDia(dia, mes, anyo, planEconomico);
    }

    public float getGastos(int dia, int mes, int anyo) {
        return ecoFamApplication.getGastosDia(dia, mes, anyo, planEconomico);
    }

    public float getGastosMes(int mes, int anyo) {
        return ecoFamApplication.getGastosMes(mes, anyo, planEconomico);
    }

    public float getIngresosMes(int mes, int anyo) {
        return ecoFamApplication.getIngresosMes(mes, anyo, planEconomico);
    }

    public void setGastos(float gastos) {
        if (lblValorGastos == null)
            lblValorGastos = new Label();
        lblValorGastos.setText(UtilsCalendar.getFloatFormateado(gastos));

    }
    public  void setIngresos(float ingresos) {
        if(lblValorIngresos ==null)
            lblValorIngresos =new Label();
        lblValorIngresos.setText(UtilsCalendar.getFloatFormateado(ingresos));
    }

    public List<Anotacion> getAnotacionesIngresos(int dia, int mes, int agno) {
        return ecoFamApplication.getAnotacionesIngresos(dia, mes, agno, planEconomico);
    }

    public List<Anotacion> getAnotacionesGastos(int dia, int mes, int agno) {
        return ecoFamApplication.getAnotacionesGastos(dia, mes, agno, planEconomico);
    }

    public Label getLblResumenDia() {
        return lblResumenDia;
    }

    public void setLblResumenDia(Label lblResumenDia) {
        this.lblResumenDia = lblResumenDia;
    }

    public LocalDate getDiaActual() {
        return diaActual;
    }

    public void setDiaActual(LocalDate diaActual) {
        this.diaActual = diaActual;
    }

    public void setNodoActual(AnchorPaneNode nodoActual) {
        this.nodoActual = nodoActual;
    }

    public AnchorPaneNode getNodoActual() {
        return nodoActual;
    }

    @FXML
    protected void volverAplanes() {
        ecoFamApplication.mostrarPlanesUsuario();
    }

    @FXML
    protected void borrarIngreso() {
        borrarAnotacion("I");
    }

    @FXML
    protected void borrarGasto() {
        borrarAnotacion("G");
    }

    protected void borrarAnotacion(String tipo) {
        try {
            FXMLLoader loader = new FXMLLoader(EcoFamApplication.class.getResource("borrarAnotacion.fxml"));
            Parent root = loader.load();
            BorrarAnotacionController control = loader.getController();
            control.setParametros(ecoFamApplication, planEconomico, diaActual, tipo);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

/*
            if (control.isInsertada()) {
                agregaAnotacion(control.getConcept(), control.getMoney(), control.isGasto(), control.getDateFecha().getValue(), this);
                //Actualizar el total del mes
                float ingresos = controller.getIngresosMes(fullCalendarView.getCurrentYearMonth().getMonthValue(), fullCalendarView.getCurrentYearMonth().getYear());
                float gastos = controller.getGastosMes(fullCalendarView.getCurrentYearMonth().getMonthValue(), fullCalendarView.getCurrentYearMonth().getYear());
                controller.setGastos(gastos);
                controller.setIngresos(ingresos);

            }
*/
        } catch (IOException ignored) {
            System.out.println(ignored.getMessage());
        }
    }



}
