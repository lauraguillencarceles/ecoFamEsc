package es.c3.ecofamesc.model;



import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Objects;


public class UsuarioPlan {

    private SimpleIntegerProperty id;
    private ObjectProperty<Usuario> usuario;

    private ObjectProperty<PlanEconomico> planEconomico;

    public SimpleIntegerProperty getId() {
        return id;
    }

    public void setId(SimpleIntegerProperty id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioPlan that = (UsuarioPlan) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public ObjectProperty<Usuario> getUsuario() {
        return usuario;
    }

    public void setUsuario(ObjectProperty<Usuario> usuario) {
        this.usuario = usuario;
    }

    public ObjectProperty<PlanEconomico> getPlanEconomico() {
        return planEconomico;
    }

    public void setPlanEconomico(ObjectProperty<PlanEconomico> planEconomico) {
        this.planEconomico = planEconomico;
    }

    @Override
    public String toString() {
        return "UsuarioPlan{" +
                "id=" + id +
                ", usuario=" + usuario.getValue().getUsername() +
                ", planEconomico=" + planEconomico.getValue().getNombre() +
                '}';
    }
}
