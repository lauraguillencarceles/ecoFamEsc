package es.c3.ecofamesc.connection;
import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.model.Anotacion;
import es.c3.ecofamesc.model.PlanEconomico;
import es.c3.ecofamesc.model.Usuario;
import es.c3.ecofamesc.utils.UtilsJSON;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class AnotacionConnection extends Connection {

    public AnotacionConnection(String base, EcoFamApplication ecoFamApplication) {
        this.base = base;
        this.ecoFamApplication = ecoFamApplication;
    }

    public boolean agregarAnotacion(LocalDate fecha, boolean gasto, String concep, Float money, PlanEconomico planEconomico) {
        String jsonInputStream = UtilsJSON.creaAnotacionJSON(fecha, gasto, concep, money, planEconomico);
        System.out.println(jsonInputStream);
        int resultado = realizaPost("/anotacion", jsonInputStream);
        boolean ok = (resultado==200);
        return ok;
    }
    public float getIngresosDia(int dia, int mes, int anyo, PlanEconomico planEconomico) {
        String cadena = "/anotacion/plan/SI/"+planEconomico.getId().getValue()+"/"+dia+"/"+mes+"/"+anyo;
        String response = realizaGet(cadena);
        try {
            if (response != null) {
                return Float.parseFloat(response);
            }
        } catch (Exception e) {
            System.err.println("Error recuperando elementos "+e.getMessage());
        }
        return 0;
    }
    public float getGastosDia(int dia, int mes, int anyo, PlanEconomico planEconomico) {
        String cadena = "/anotacion/plan/SG/"+planEconomico.getId().getValue()+"/"+dia+"/"+mes+"/"+anyo;
        String response = realizaGet(cadena);
        try {
            if (response != null) {
                return Float.parseFloat(response);
            }
        } catch (Exception e) {
            System.err.println("Error recuperando elementos "+e.getMessage());
        }
        return 0;
    }

    public float getIngresosMes(int mes, int anyo, PlanEconomico planEconomico) {
        String cadena = "/anotacion/plan/SI/"+planEconomico.getId().getValue()+"/"+mes+"/"+anyo;
        String response = realizaGet(cadena);
        try {
            if (response != null) {
                return Float.parseFloat(response);
            }
        } catch (Exception e) {
            System.err.println("Error recuperando elementos "+e.getMessage());
        }
        return 0;
    }

    public float getGastosMes(int mes, int anyo, PlanEconomico planEconomico) {
        String cadena = "/anotacion/plan/SG/"+planEconomico.getId().getValue()+"/"+mes+"/"+anyo;
        String response = realizaGet(cadena);
        try {
            if (response != null) {
                return Float.parseFloat(response);
            }
        } catch (Exception e) {
            System.err.println("Error recuperando elementos "+e.getMessage());
        }
        return 0;
    }

    public List<Anotacion> getAnotacionesIngresos(int dia, int mes, int agno, PlanEconomico planEconomico) {

        List<Anotacion> anotacionesIngresos = new ArrayList<>();
        String cadena = "/anotacion/plan/I/"+planEconomico.getId().getValue()+"/"+dia+"/"+mes+"/"+agno;
        String response = realizaGet(cadena);
        try {
            if (response != null) {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    int id = jsonObject.getInt("id");
                    double importe = Double.parseDouble(jsonObject.getString("importe"));
                    String fecha = jsonObject.getString("fecha");
                    String descripcion = jsonObject.getString("descripcion");
                    Anotacion a = new Anotacion(id, importe, fecha, descripcion, "I", planEconomico);
                    anotacionesIngresos.add(a);
                }
            }
        } catch (Exception e) {
            System.err.println("Error recuperando elementos "+e.getMessage());
        }
        return anotacionesIngresos;
    }//getAnotacionesIngresos

    public List<Anotacion> getAnotacionesGastos(int dia, int mes, int agno, PlanEconomico planEconomico) {
        List<Anotacion> anotacionesGastos = new ArrayList<>();
        String cadena = "/anotacion/plan/G/"+planEconomico.getId().getValue()+"/"+dia+"/"+mes+"/"+agno;
        String response = realizaGet(cadena);
        try {
            if (response != null) {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    int id = jsonObject.getInt("id");
                    double importe = Double.parseDouble(jsonObject.getString("importe"));
                    String fecha = jsonObject.getString("fecha");
                    String descripcion = jsonObject.getString("descripcion");
                    Anotacion a = new Anotacion(id, importe, fecha, descripcion, "G", planEconomico);
                    anotacionesGastos.add(a);
                }
            }
        } catch (Exception e) {
            System.err.println("Error recuperando elementos "+e.getMessage());
        }
        return anotacionesGastos;
    }
  }
