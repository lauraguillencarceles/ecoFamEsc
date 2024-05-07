package es.c3.ecofamesc.calendar;

import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.control.PlanCalendarController;
import es.c3.ecofamesc.model.PlanEconomico;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;


public class FullCalendarView {

    public ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private YearMonth currentYearMonth;
    private PlanCalendarController controller;



    /**
     * Create a calendar view
     * @param yearMonth year month to create the calendar of
     */
    public FullCalendarView(YearMonth yearMonth, PlanCalendarController controller, EcoFamApplication ecoFamApplication, PlanEconomico planEconomico) {
        //this.fxmlMain = fxmlMain;
        this.controller = controller;
        controller.setEcoFamApplication(ecoFamApplication);
        controller.setPlanEconomico(planEconomico);

        currentYearMonth = yearMonth;
        // Create the calendar grid pane
        GridPane calendar = new GridPane();
        calendar.setPrefSize(840, 500);
        calendar.setGridLinesVisible(true);

        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode(controller, this, ecoFamApplication, planEconomico);
                ap.setPrefSize(200,200);
                //ap.getStyleClass().add("text-id");
                calendar.add(ap,j,i);
                allCalendarDays.add(ap);
            }
        }
        // Days of the week labels
        String[] dayNames = new String[]{ "Lunes","Martes","Miércoles","Jueves","Viernes",
                                        "Sábado","Domingo" };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        Integer col = 0;
        for (String nombreDia : dayNames) {
            Text txt = UtilsCalendar.getTextBigBlue(nombreDia);
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            ap.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        Button previousMonth = new Button("<<");
        previousMonth.setOnAction(e -> previousMonth());
        Button nextMonth = new Button(">>");
        nextMonth.setOnAction(e -> nextMonth());
        HBox titleBar = new HBox(previousMonth, calendarTitle, nextMonth);
        titleBar.setAlignment(Pos.BASELINE_CENTER);
        // Populate calendar with the appropriate day numbers
        populateCalendar(yearMonth);
        // Create the calendar view
        view = new VBox(titleBar, dayLabels, calendar);

    }

    /**
     * Set the days of the calendar to correspond to the appropriate date
     * @param yearMonth year and month of month to render
     */
    public void populateCalendar(YearMonth yearMonth) {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("MONDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        for (AnchorPaneNode ap : allCalendarDays) {
            int hijos = ap.getChildren().size();
            for (int i=0; i<hijos; i++) {
                ap.getChildren().remove(0);
            }

            float ingresos = controller.getIngresos(calendarDate.getDayOfMonth(), calendarDate.getMonthValue(), calendarDate.getYear());
            float gastos = controller.getGastos(calendarDate.getDayOfMonth(), calendarDate.getMonthValue(), calendarDate.getYear());
            ap.setDate(calendarDate);
            ap.setId(calendarDate.toString());

            //Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth())+"\n\nIngresos: "+ingresos+"\nGastos: "+gastos);
            Text dia = UtilsCalendar.getTextBigBlue(String.valueOf(calendarDate.getDayOfMonth()));
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

            calendarDate = calendarDate.plusDays(1);
        }
        // Change the title of the calendar
        String mes = yearMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("es","ES"));
        calendarTitle.setText(" " + mes.toUpperCase() + " " + String.valueOf(yearMonth.getYear())+" ");
        calendarTitle.setFont(Font.font (null, FontWeight.BOLD, 14));
        calendarTitle.setFill(Color.web("#000080"));

        float ingresos = controller.getIngresosMes(yearMonth.getMonthValue(), yearMonth.getYear());
        float gastos = controller.getGastosMes(yearMonth.getMonthValue(), yearMonth.getYear());
        controller.setGastos(gastos);
        controller.setIngresos(ingresos);
    }


    /**
     * Move the month back by one. Repopulate the calendar with the correct dates.
     */
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }

    /**
     * Move the month forward by one. Repopulate the calendar with the correct dates.
     */
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }

    public VBox getView() {
        return view;
    }

    public ArrayList<AnchorPaneNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }

    public YearMonth getCurrentYearMonth() {
        return currentYearMonth;
    }
}//FullCalendarView
