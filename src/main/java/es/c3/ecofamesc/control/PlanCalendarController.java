package es.c3.ecofamesc.control;

import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.calendar.UtilsCalendar;
import es.c3.ecofamesc.model.Anotacion;
import es.c3.ecofamesc.model.PlanEconomico;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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

    @FXML
    protected void volverAplanes() {
        ecoFamApplication.mostrarPlanesUsuario();
    }

}
