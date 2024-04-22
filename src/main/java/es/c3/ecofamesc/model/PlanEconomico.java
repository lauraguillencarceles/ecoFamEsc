package es.c3.ecofamesc.model;


import javafx.beans.property.*;

import java.util.*;


public class PlanEconomico {

    private SimpleIntegerProperty id;
    private SimpleStringProperty nombre;
    private Collection<ObjectProperty<Anotacion>> anotaciones;
    private SimpleObjectProperty<Usuario> creador;
    private Collection<SimpleObjectProperty<Usuario>> usuarios;

    public PlanEconomico(int id, String nombre, Usuario creador, Collection<Usuario> usuarios) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.creador = new SimpleObjectProperty<Usuario>(creador);
        this.usuarios = new ArrayList<SimpleObjectProperty<Usuario>>();
        for (Usuario usuario : usuarios) {
            this.usuarios.add(new SimpleObjectProperty<Usuario>(usuario));
        }
    }

    public PlanEconomico() {
        this.id = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.creador = new SimpleObjectProperty<>();
        this.usuarios = new ArrayList<>();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanEconomico that = (PlanEconomico) o;
        return id == that.id && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    public Collection<ObjectProperty<Anotacion>> getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(Collection<ObjectProperty<Anotacion>> anotaciones) {
        this.anotaciones = anotaciones;
    }

    public SimpleObjectProperty<Usuario> getCreador() {
        return creador;
    }

    public void setCreador(SimpleObjectProperty<Usuario> creador) {
        this.creador = creador;
    }

    public Collection<SimpleObjectProperty<Usuario>> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Collection<SimpleObjectProperty<Usuario>> usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public String toString() {
        return "PlanEconomico{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                //", anotaciones=" + anotaciones +
                ", creador=" + creador.getValue().getUsername() +
                ", usuarios=" + usuarios +
                '}';
    }

    public void agregarUsuario(Usuario user) {
        this.getUsuarios().add(new SimpleObjectProperty<Usuario>(user));
    }
    public void quitarUsuario(Usuario user) {
        try {
            for (SimpleObjectProperty<Usuario> usuario : this.getUsuarios()) {
                if (usuario != null && user != null
                    &&
                    usuario.getValue().getUsername() != null
                    &&
                    usuario.getValue().getUsername().getValue().equals(user.getUsername().getValue())) {
                    this.getUsuarios().remove(usuario);
                }
            }
        } catch(ConcurrentModificationException cme) {}
    }//quitarUsuario
    public List<Usuario> getUsuariosModelo() {
        List<Usuario> listaNormal = new ArrayList<>();
        for (SimpleObjectProperty<Usuario> usuario : usuarios) {
            listaNormal.add(usuario.getValue());
        }
        return listaNormal;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        Collection<Usuario> usuarios = new ArrayList<>();
        if (!this.getUsuarios().isEmpty()) {
            for (SimpleObjectProperty<Usuario> u : this.getUsuarios())
            usuarios.add(u.getValue());
        }
        PlanEconomico plan = new PlanEconomico(this.id.getValue(), this.nombre.getValue(), this.creador.getValue(),usuarios);

        return plan;
    }
}


