package es.c3.ecofamesc.model;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Collection;
import java.util.Objects;


public class Categoria {

    private SimpleIntegerProperty id;
    private SimpleStringProperty nombre;
    private SimpleStringProperty descripcion;
    private SimpleStringProperty tipo;
    private Collection<ObjectProperty<Anotacion>> anotaciones;

    public SimpleIntegerProperty getId() {
        return id;
    }
    public void setId(SimpleIntegerProperty id) {
        this.id = id;
    }

    public SimpleStringProperty getNombre() {
        return nombre;
    }
    public void setNombre(SimpleStringProperty nombre) {
        this.nombre = nombre;
    }

    public SimpleStringProperty getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(SimpleStringProperty descripcion) {
        this.descripcion = descripcion;
    }

    public SimpleStringProperty getTipo() {
        return tipo;
    }
    public void setTipo(SimpleStringProperty tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return id == categoria.id && Objects.equals(nombre, categoria.nombre) && Objects.equals(descripcion, categoria.descripcion) && Objects.equals(tipo, categoria.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, tipo);
    }

    public Collection<ObjectProperty<Anotacion>> getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(Collection<ObjectProperty<Anotacion>> anotaciones) {
        this.anotaciones = anotaciones;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre +
                ", descripcion='" + descripcion +
                ", tipo='" + ("I".equals(tipo) ? "Ingreso":"Gasto") +
                //", anotaciones=" + anotaciones +
                "}";
    }

    public static Categoria getIngreso() {
        Categoria c = new Categoria();
        c.setId(new SimpleIntegerProperty(1));
        c.setNombre(new SimpleStringProperty("Ingreso"));
        c.setTipo(new SimpleStringProperty("I"));
        return c;
    }
    public static Categoria getGasto() {
        Categoria c = new Categoria();
        c.setId(new SimpleIntegerProperty(2));
        c.setNombre(new SimpleStringProperty("Gasto"));
        c.setTipo(new SimpleStringProperty("G"));
        return c;
    }
}
