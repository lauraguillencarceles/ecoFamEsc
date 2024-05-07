package es.c3.ecofamesc.model;


import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Anotacion {

    private SimpleIntegerProperty id;
    private SimpleDoubleProperty importe;
    private SimpleDateFormat fecha;
    private SimpleStringProperty descripcion;
    private ObjectProperty<Categoria> categoria;
    private ObjectProperty<PlanEconomico> planEconomico;

    public Anotacion(int id, double importe, String fecha, String descripcion, String categoria, PlanEconomico planEconomico) {
        this.id = new SimpleIntegerProperty(id);
        this.importe = new SimpleDoubleProperty(importe);
        this.fecha = new SimpleDateFormat(fecha);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.planEconomico = new SimpleObjectProperty<>(planEconomico) ;
        //TODO dar solución a lo de las categorías
        if ("G".equals(categoria)) {
            this.categoria = new SimpleObjectProperty<>(Categoria.getGasto());
        }
        else {
            this.categoria = new SimpleObjectProperty<>(Categoria.getIngreso());
        }
    }
    public SimpleIntegerProperty getId() {
        return id;
    }
    public void setId(SimpleIntegerProperty id) {
        this.id = id;
    }

    public SimpleDoubleProperty getImporte() {
        return importe;
    }
    public void setImporte(SimpleDoubleProperty importe) {
        this.importe = importe;
    }

    public SimpleDateFormat getFecha() {
        return fecha;
    }
    public void setFecha(SimpleDateFormat fecha) {
        this.fecha = fecha;
    }

    public SimpleStringProperty getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(SimpleStringProperty descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anotacion anotacion = (Anotacion) o;
        return id == anotacion.id && Double.compare(anotacion.importe.doubleValue(), importe.doubleValue()) == 0 && Objects.equals(fecha, anotacion.fecha) && Objects.equals(descripcion, anotacion.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, importe, fecha, descripcion);
    }

    public ObjectProperty<Categoria> getCategoria() {
        return categoria;
    }
    public void setCategoria(ObjectProperty<Categoria> categoria) {
        this.categoria = categoria;
    }

    public ObjectProperty<PlanEconomico> getPlanEconomico() {
        return planEconomico;
    }
    public void setPlanEconomico(ObjectProperty<PlanEconomico> planEconomico) {
        this.planEconomico = planEconomico;
    }

    @Override
    public String toString() {
        return "Anotacion{" +
                "id=" + id +
                ", importe=" + importe +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", categoria=" +(categoria.getValue().getTipo().getValue() =="I" ? "Ingreso":"Gasto")+ categoria.getValue().getNombre() +
                ", planEconomico=" + planEconomico.getValue().getNombre() +
                '}';
    }
}//Anotacion
