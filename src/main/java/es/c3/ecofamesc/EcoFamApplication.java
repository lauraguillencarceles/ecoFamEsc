package es.c3.ecofamesc;

import es.c3.ecofamesc.control.LoginController;
import es.c3.ecofamesc.control.PlanController;
import es.c3.ecofamesc.model.Anotacion;
import es.c3.ecofamesc.model.PlanEconomico;
import es.c3.ecofamesc.model.Usuario;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class EcoFamApplication extends Application {
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        if (guardadoUsuario()) {
            //TODO conseguir el usuario
            mostrarPlanesUsuario("usuario guardado");
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(EcoFamApplication.class.getResource("login-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            LoginController loginController = fxmlLoader.getController();
            loginController.setEcoFamApplication(this);
            stage.setTitle("Economía Familiar");
            stage.setScene(scene);
            stage.show();
        }
    }//start

    public static void main(String[] args) {
        launch();
    }


    private ObservableList<PlanEconomico> datosPlan = FXCollections.observableArrayList();

    public ObservableList<PlanEconomico> getDatosPlan() {
        return datosPlan;
    }


    public void mostrarPlanesUsuario(String  username)  {
        try {
            Usuario usuario = getUsuarioByUsername(username);
            cargaPlanesUsuario(usuario);
            FXMLLoader fxmlLoader = new FXMLLoader(EcoFamApplication.class.getResource("plan-controller.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 600);
            PlanController planController = fxmlLoader.getController();
            planController.setEcoFamApplication(this, usuario);
            stage.setTitle("Planes de " + usuario);
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            System.err.println("Error en mostrarPlanesUsuario: "+exception.toString());
            exception.printStackTrace();
        }
    }

    private boolean guardadoUsuario() {
        //TODO implementar método que busca si hay usuario almacenado de otra vez, si es así, solicitar información del usuario a la API
        return false;
    }

    public void registrarUsuario() {
        //TODO redirige a la pantalla para registrar un usuario
    }

    /* En datosPlan se cargan los planes del usuario sin anotaciones */
    private void cargaPlanesUsuario(Usuario usuario) {
        //TODO cargar datosPlan
        Usuario creador = new Usuario("Laura Guillén", "user1");
        Collection<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(new Usuario("Usuario 1", "usu1"));
        usuarios.add(new Usuario("Usuario 2", "usu2"));
        usuarios.add(new Usuario("Usuario 3", "usu3"));
        datosPlan.add(new PlanEconomico(1, "Mi Plan", creador, usuarios));

        creador = new Usuario("Pepe Pérez", "user2");
        usuarios = new ArrayList<Usuario>();
        usuarios.add(new Usuario("Usuario A", "usuA"));
        usuarios.add(new Usuario("Usuario B", "usuB"));
        usuarios.add(new Usuario("Usuario C", "usuC"));
        datosPlan.add(new PlanEconomico(2, "Otro Plan", creador, usuarios));

        creador = new Usuario("Juan Gómez", "user3");
        usuarios = new ArrayList<Usuario>();
        usuarios.add(new Usuario("User X", "usuX"));
        usuarios.add(new Usuario("User Y", "usuY"));
        datosPlan.add(new PlanEconomico(3, "Más Plan", creador, usuarios));
    }

    public boolean guardarPlan(PlanEconomico plan) {
        System.out.println("Guardar nuevo");
        //TODO guardar plan en API Rest
        return true;
    }
    public boolean borrarPlan(PlanEconomico plan) {
        System.out.println("Borrar");
        //TODO borrar plan en API Rest
        return true;
    }
    public boolean modificarPlan(PlanEconomico plan) {
        System.out.println("Modificar");
        //TODO guardar plan en API Rest
        return true;
    }

    public Usuario getUsuarioByUsername(String username) {
        //TODO solicitar el usuario con el username
        return new Usuario("Pepito Pérez", username);

    }
}