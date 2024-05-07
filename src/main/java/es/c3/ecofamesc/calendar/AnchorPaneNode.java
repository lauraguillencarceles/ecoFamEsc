package es.c3.ecofamesc.calendar;

import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.control.AnotacionController;
import es.c3.ecofamesc.control.PlanCalendarController;
import es.c3.ecofamesc.model.Anotacion;
import es.c3.ecofamesc.model.PlanEconomico;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Create an anchor pane that can store additional data.
 */
public class AnchorPaneNode extends AnchorPane {

    private final int MAX_DESC = 25;
    // Date associated with this pane
    private LocalDate date;
    private PlanCalendarController controller;
    private FullCalendarView fullCalendarView;
    /**
     * Create a anchor pane node. Date is not assigned in the constructor.
     * @param children children of the anchor pane
     */
    public AnchorPaneNode(PlanCalendarController controller, FullCalendarView fullCalendarView, EcoFamApplication ecoFamApplication, PlanEconomico planEconomico, Node... children) {
        super(children);
        this.controller = controller;
        this.fullCalendarView = fullCalendarView;
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            controller.getLblResumenDia().setText("Resumen del día "+date);
            if(e.getClickCount() > 1){
                try {
                    FXMLLoader loader = new FXMLLoader(EcoFamApplication.class.getResource("anotacion.fxml"));
                    Parent root = loader.load();
                    AnotacionController control = loader.getController();
                    control.setFecha(date);
                    control.setEcoFamApplication(ecoFamApplication, planEconomico);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();

                    if (control.isInsertada()) {
                        agregaAnotacion(control.getConcept(), control.getMoney(), control.isGasto(), control.getDateFecha().getValue(), this);
                        //Actualizar el total del mes
                        float ingresos = controller.getIngresosMes(fullCalendarView.getCurrentYearMonth().getMonthValue(), fullCalendarView.getCurrentYearMonth().getYear());
                        float gastos = controller.getGastosMes(fullCalendarView.getCurrentYearMonth().getMonthValue(), fullCalendarView.getCurrentYearMonth().getYear());
                        controller.setGastos(gastos);
                        controller.setIngresos(ingresos);

                    }
                } catch (IOException ignored) {
                    System.out.println(ignored.getMessage());
                }

                System.out.println("Double clicked");
            }
            else {
                //System.out.println("One clicked");
                List<Anotacion> anotacionesI = controller.getAnotacionesIngresos(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
                List<Anotacion> anotacionesG = controller.getAnotacionesGastos(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
                for (Node nodo : controller.getDetailPane().getChildren()) {
                    //System.out.println(nodo.getId()+" "+nodo.getClass());
                    if ("scrollIngresos".equals(nodo.getId())) {
                        AnchorPane nodoInterno = (AnchorPane) ((ScrollPane) nodo).getContent();
                        String ingresos = "\n";
                        for (Anotacion a : anotacionesI) {
                            String importe = UtilsCalendar.getDoubleFormateado(a.getImporte().getValue());
                            ingresos += getDescripcion(a.getDescripcion().getValue())+": "+importe+"\n";
                        }
                        Text nuevoIngreso = new Text(ingresos);
                        nuevoIngreso.setId("ingreso");
                        limpiarNodo(nodoInterno,"ingreso");
                        nodoInterno.getChildren().add(nuevoIngreso);
                        //System.out.println(ingresos);
                    }
                    if ("scrollGastos".equals(nodo.getId())) {
                        AnchorPane nodoInterno = (AnchorPane) ((ScrollPane) nodo).getContent();
                        String gastos = "\n";
                        for (Anotacion a : anotacionesG) {
                            String importe = UtilsCalendar.getDoubleFormateado(a.getImporte().getValue());
                            gastos += getDescripcion(a.getDescripcion().getValue())+": "+importe+"\n";
                        }
                        Text nuevoGasto = new Text(gastos);
                        nuevoGasto.setId("gasto");
                        limpiarNodo(nodoInterno,"gasto");
                        nodoInterno.getChildren().add(nuevoGasto);
                        //System.out.println(gastos);
                    }
                }
            }
        });
    }//AnchorPaneNode

    private String getDescripcion(String base) {
        if (base != null && base.length() > MAX_DESC) {
            return base.substring(0, MAX_DESC);
        }
        /*for (int i = base.length(); i<MAX_DESC; i++) {
            base += " ";
        }*/
        return base;
    }
    private String agregarTexto(AnchorPane nodo, String id, String texto) {
        for (Node n : nodo.getChildren()) {
            if (id.equals(n.getId())) {
                Text text = (Text) n;
                return text.getText()+texto+"\n";
            }
        }
        return "";
    }
    private void limpiarNodo(AnchorPane nodo, String id) {
        Node nborrar = null;
        for (Node n : nodo.getChildren()) {
            if (id.equals(n.getId())) {
                nborrar = n;
            }
        }
        if (nborrar != null)
            nodo.getChildren().remove(nborrar);
    }

    public void agregaAnotacion(String concepto, Float money, boolean gasto, LocalDate fechaAnotacion, AnchorPaneNode node) {
        if (date.isEqual(fechaAnotacion)) {
            for (Node nodo : controller.getDetailPane().getChildren()) {
                if ("scrollIngresos".equals(nodo.getId()) && !gasto) {
                    AnchorPane nodoInterno = (AnchorPane) ((ScrollPane) nodo).getContent();
                    String ingresos = agregarTexto(nodoInterno, "ingreso", concepto+": "+UtilsCalendar.getFloatFormateado(money));
                    Text nuevoIngreso = new Text(ingresos);
                    nuevoIngreso.setId("ingreso");
                    limpiarNodo(nodoInterno,"ingreso");
                    nodoInterno.getChildren().add(nuevoIngreso);
                }
                if ("scrollGastos".equals(nodo.getId()) && gasto) {
                    AnchorPane nodoInterno = (AnchorPane) ((ScrollPane) nodo).getContent();
                    String gastos = agregarTexto(nodoInterno, "gasto", concepto+": "+UtilsCalendar.getFloatFormateado(money));
                    Text nuevoGasto = new Text(gastos);
                    nuevoGasto.setId("gasto");
                    limpiarNodo(nodoInterno,"gasto");
                    nodoInterno.getChildren().add(nuevoGasto);
                }
            }
            float ingresos = controller.getIngresos(fechaAnotacion.getDayOfMonth(), fechaAnotacion.getMonthValue(), fechaAnotacion.getYear());
            float gastos = controller.getGastos(fechaAnotacion.getDayOfMonth(), fechaAnotacion.getMonthValue(), fechaAnotacion.getYear());
            rellenaAP(node,String.valueOf(fechaAnotacion.getDayOfMonth()), ingresos,gastos);
        }
        else {
            //Busca si la anotación se ha hecho en este mes y debe actualizar el panel
            for (AnchorPaneNode ap : fullCalendarView.allCalendarDays) {
                if (ap.getId().equals(fechaAnotacion.toString())) {
                    float ingresos = controller.getIngresos(fechaAnotacion.getDayOfMonth(), fechaAnotacion.getMonthValue(), fechaAnotacion.getYear());
                    float gastos = controller.getGastos(fechaAnotacion.getDayOfMonth(), fechaAnotacion.getMonthValue(), fechaAnotacion.getYear());
                    rellenaAP(node,String.valueOf(fechaAnotacion.getDayOfMonth()), ingresos,gastos);
                }
            }
        }
    }

    private void rellenaAP(AnchorPane ap, String day, float ingresos, float gastos) {
        int hijos = ap.getChildren().size();
        for (int i=0; i<hijos; i++) {
            ap.getChildren().remove(0);
        }

        Text dia = UtilsCalendar.getTextBigBlue(day);
        //dia.setId("dia");
        ap.setTopAnchor(dia, 5.0);
        ap.setLeftAnchor(dia, 5.0);
        ap.getChildren().add(dia);

        Text ingresosTxt = UtilsCalendar.getTextIngreso(ingresos);
        //dia.setId("ingreso");
        ap.setTopAnchor(ingresosTxt, 30.0);
        ap.setRightAnchor(ingresosTxt, 5.0);
        ap.getChildren().add(ingresosTxt);

        Text gastosTxt = UtilsCalendar.getTextGasto(gastos);
        //dia.setId("gasto");
        ap.setTopAnchor(gastosTxt, 55.0);
        ap.setRightAnchor(gastosTxt, 5.0);
        ap.getChildren().add(gastosTxt);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
