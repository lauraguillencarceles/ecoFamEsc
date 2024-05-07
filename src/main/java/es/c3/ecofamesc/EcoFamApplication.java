package es.c3.ecofamesc;

import es.c3.ecofamesc.calendar.FullCalendarView;
import es.c3.ecofamesc.connection.AnotacionConnection;
import es.c3.ecofamesc.connection.Connection;
import es.c3.ecofamesc.connection.PlanConnection;
import es.c3.ecofamesc.connection.UserConnection;
import es.c3.ecofamesc.control.LoginController;
import es.c3.ecofamesc.control.PlanCalendarController;
import es.c3.ecofamesc.control.PlanController;
import es.c3.ecofamesc.control.RegisterController;
import es.c3.ecofamesc.model.Anotacion;
import es.c3.ecofamesc.model.PlanEconomico;
import es.c3.ecofamesc.model.Usuario;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EcoFamApplication extends Application {
    private Stage stage;
    private String base = "http://localhost:8080/ecoFam-apiRest";
    private UserConnection userConnection;
    private PlanConnection planConnection;
    private AnotacionConnection anotacionConnection;


    private Usuario usuarioLogado;
    private String token;

    public Stage getStage() {
        return stage;
    }


    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        irLogin();
    }//start

    public static void main(String[] args) {
        launch();
    }


    private ObservableList<PlanEconomico> datosPlan = FXCollections.observableArrayList();

    public ObservableList<PlanEconomico> getDatosPlan() {
        return datosPlan;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }



    public boolean agregarAnotacion(LocalDate fecha, boolean gasto, String concep, Float money, PlanEconomico planEconomico) {
        return getAnotacionConnection().agregarAnotacion(fecha, gasto, concep, money, planEconomico);
    }
    public float getIngresosDia(int dia, int mes, int anyo, PlanEconomico planEconomico) {
        return getAnotacionConnection().getIngresosDia(dia, mes, anyo, planEconomico);
    }
    public float getGastosDia(int dia, int mes, int anyo, PlanEconomico planEconomico) {
        return getAnotacionConnection().getGastosDia(dia, mes, anyo, planEconomico);
    }

    public float getIngresosMes(int mes, int anyo, PlanEconomico planEconomico) {
        return getAnotacionConnection().getIngresosMes(mes, anyo, planEconomico);
    }

    public float getGastosMes(int mes, int anyo, PlanEconomico planEconomico) {
        return getAnotacionConnection().getGastosMes(mes, anyo, planEconomico);
    }

    public List<Anotacion> getAnotacionesIngresos(int dia, int mes, int agno, PlanEconomico planEconomico) {
        return getAnotacionConnection().getAnotacionesIngresos(dia, mes, agno, planEconomico);
    }

    public List<Anotacion> getAnotacionesGastos(int dia, int mes, int agno, PlanEconomico planEconomico) {
        return getAnotacionConnection().getAnotacionesGastos(dia, mes, agno, planEconomico);
    }

    public int registrarUsuario(String usuario, String nombreCompleto, String pass1, String pass2) {
        return getUserConnection().registrarUsuario(usuario, nombreCompleto, pass1, pass2);
    }

    /* En datosPlan se cargan los planes del usuario sin anotaciones */
    public void cargaPlanesUsuario(Usuario usuario) {
        datosPlan = FXCollections.observableArrayList();
        List<PlanEconomico> planes = getPlanConnection().getPlanesUsuario(usuarioLogado, token);
        for (PlanEconomico plan: planes) {
            datosPlan.add(plan);
        }
    }

    public boolean guardarPlan(PlanEconomico plan) {
        plan.setCreador(new SimpleObjectProperty<Usuario>(usuarioLogado));
        boolean resultado = getPlanConnection().guardaPlan(plan, null);
        return resultado;
    }
    public boolean borrarPlan(PlanEconomico plan) {
        return getPlanConnection().borrarPlan(plan);
    }
    public boolean modificarPlan(PlanEconomico plan, List<Usuario> usersAntiguos) {
        return getPlanConnection().guardaPlan(plan, usersAntiguos);
    }
    public Usuario getUsuarioByUsername(String user) {
        return getUserConnection().getUsuarioByUsername(user);
    }
    public void setToken(String token) {
        this.token = token;
        Connection.token = token;
        System.out.println(token);
    }

    public String getToken() {
        return token;
    }

    public UserConnection getUserConnection() {
        if (userConnection == null) {
            userConnection = new UserConnection(base, this);
        }
        return userConnection;
    }
    public PlanConnection getPlanConnection() {
        if (planConnection == null) {
            planConnection = new PlanConnection(base, this);
        }
        return planConnection;
    }

    public AnotacionConnection getAnotacionConnection() {
        if (anotacionConnection == null) {
            anotacionConnection = new AnotacionConnection(base, this);
        }
        return anotacionConnection;
    }
    /********************************************************************************/
    /**************************** redirigir a pantallas *****************************/
    /********************************************************************************/
    /**
     * Registro de un usuario
     */
    public void irRegistrarUsuario() {
        FXMLLoader fxmlLoader = new FXMLLoader(EcoFamApplication.class.getResource("register-controller.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 350, 300);
            RegisterController registerController = fxmlLoader.getController();
            registerController.setEcoFamApplication(this);
            stage.setTitle("Registrar usuario");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Pantalla de login
     */
    public void irLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EcoFamApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            LoginController loginController = fxmlLoader.getController();
            loginController.setEcoFamApplication(this);
            stage.setTitle("Economía Familiar");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Pantalla de mantenimiento de los planes de un usuario
     */
    public void mostrarPlanesUsuario()  {
        try {
            cargaPlanesUsuario(usuarioLogado);
            FXMLLoader fxmlLoader = new FXMLLoader(EcoFamApplication.class.getResource("plan-controller.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 600);
            PlanController planController = fxmlLoader.getController();
            planController.setEcoFamApplication(this, usuarioLogado);
            stage.setTitle("Planes de " + usuarioLogado);
            stage.setScene(scene);
            stage.setX(300);
            stage.setY(60);
            stage.show();
        } catch (IOException exception) {
            System.err.println("Error en mostrarPlanesUsuario: "+exception.toString());
            exception.printStackTrace();
        }
    }

    public void irCalendarioPlan(PlanEconomico planEconomico) {
        try {
            FXMLLoader loader = new FXMLLoader(EcoFamApplication.class.getResource("plan-calendar.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 650);
            PlanCalendarController controller = loader.getController();
            FullCalendarView fullCalendarView = new FullCalendarView(YearMonth.now(), controller, this, planEconomico);
            controller.getCalendarPane().getChildren().add(fullCalendarView.getView());
            controller.setPlanEconomico(planEconomico);
            controller.setEcoFamApplication(this);

            stage.setTitle("Gastos e ingresos del plan "+planEconomico.getNombre().getValue());
            stage.setScene(scene);
            stage.setX(90);
            stage.setY(40);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /********************************************************************************/
    /************************** fin redirigir a pantallas ***************************/
    /********************************************************************************/

}//EcoFamApplication