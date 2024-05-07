package es.c3.ecofamesc.calendar;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.text.DecimalFormat;

public class UtilsCalendar {
    public static Text getTextBigBlue(String texto) {
        Text dia = new Text(texto);
        dia.setTextAlignment(TextAlignment.LEFT);
        dia.setFont(Font.font (null, FontWeight.BOLD, 14));
        dia.setFill(Color.web("#000080"));
        return dia;
    }
    public static Text getTextIngreso(float ingresos){
        Text ingreso = new Text("+"+ingresos);
        ingreso.setTextAlignment(TextAlignment.RIGHT);
        //ingreso.setFont(Font.font (null, FontWeight.BOLD, 14));
        ingreso.setFill(Color.web("#065306"));
        return ingreso;
    }

    public static Text getTextGasto(float gastos){
        Text gasto = new Text("-"+gastos);
        gasto.setTextAlignment(TextAlignment.RIGHT);
        //ingreso.setFont(Font.font (null, FontWeight.BOLD, 14));
        gasto.setFill(Color.web("#eb0b12"));
        return gasto;
    }
    public static String getFloatFormateado(float num) {
        String pattern = "###,###,###.## €";

        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(num);
        return output;
    }

    public static String getDoubleFormateado(double num) {
        String pattern = "###,###,###.## €";

        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(num);
        return output;
    }

}
