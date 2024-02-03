package es.c3.ecofamesc.model;



import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Collection;
import java.util.Objects;


public class Usuario  {

    private SimpleIntegerProperty id;
    private SimpleStringProperty nombreCompleto;
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private Collection<ObjectProperty<PlanEconomico>> planesEconomicosCreados;
    private Collection<ObjectProperty<UsuarioPlan>> planesEconomicosAcceso;

    public Usuario(String nombreCompleto, String username, String password
                   //,List<UserAuthority> authorities
                    ) {
        this.nombreCompleto = new SimpleStringProperty(nombreCompleto);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        //     this.authorities = authorities;
    }
    public Usuario(String nombreCompleto, String username
    ) {
        this.nombreCompleto = new SimpleStringProperty(nombreCompleto);
        this.username = new SimpleStringProperty(username);
    }

    public SimpleIntegerProperty getId() {
        return id;
    }
    public void setId(SimpleIntegerProperty id) {
        this.id = id;
    }

    public SimpleStringProperty getNombreCompleto() {
        return nombreCompleto;
    }
    public void setNombreCompleto(SimpleStringProperty nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public SimpleStringProperty getUsername() {
        return username;
    }
    public void setUsername(SimpleStringProperty username) {
        this.username = username;
    }

    public SimpleStringProperty getPassword() {
        return password;
    }
    public void setPassword(SimpleStringProperty contra) {
        this.password = contra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id && Objects.equals(nombreCompleto, usuario.nombreCompleto) && Objects.equals(username, usuario.username) && Objects.equals(password, usuario.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreCompleto, username, password);
    }

    public Collection<ObjectProperty<PlanEconomico>> getPlanesEconomicosCreados() {
        return planesEconomicosCreados;
    }
    public void setPlanesEconomicosCreados(Collection<ObjectProperty<PlanEconomico>> planesEconomicosCreados) {
        this.planesEconomicosCreados = planesEconomicosCreados;
    }

    public Collection<ObjectProperty<UsuarioPlan>> getPlanesEconomicosAcceso() {
        return planesEconomicosAcceso;
    }
    public void setPlanesEconomicosAcceso(Collection<ObjectProperty<UsuarioPlan>> planesEconomicosAcceso) {
        this.planesEconomicosAcceso = planesEconomicosAcceso;
    }

    @Override
    public String toString() {
        return this.username.getValue();
                /*"Usuario{" +
                //"id=" + id +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", nick='" + username + '\'' +
                //", contra='" + contra + '\'' +
               // ", planesEconomicosCreados=" + planesEconomicosCreados +
                //", planesEconomicosAcceso=" + planesEconomicosAcceso +
                '}';*/
    }

}//Usuario
