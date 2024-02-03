package es.c3.ecofamesc.control;

import es.c3.ecofamesc.EcoFamApplication;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField user;
    @FXML private PasswordField pass;
    @FXML private Label mensaje;
    @FXML private CheckBox recuerda;
    private EcoFamApplication ecoFamApplication;
    public void setEcoFamApplication(EcoFamApplication ecoFamApplication) {
        this.ecoFamApplication = ecoFamApplication;
    }

    public LoginController() {
    }

    @FXML
    protected void onLoginButtonClick() {
        String usuario = user.getText();
        String password = pass.getText();

        if (esCorrecto(usuario, password)) {
            System.out.println("Usuario "+usuario+", Contraseña "+password);
            if (recuerda.isSelected()) {
                //TODO guardar el usuario para futuras versiones
                System.out.println("Recuerda seleccionado");
            }
            this.ecoFamApplication.mostrarPlanesUsuario(usuario);
        }
        else {
            mensaje.setText("Usuario y contraseña incorrectos");
        }
    }

    @FXML
    protected void onRegistroLinkClick() {
        System.out.println("El usuario se quiere registrar");
        this.ecoFamApplication.registrarUsuario();
    }

    private boolean esCorrecto(String usuario, String password) {
        //TODO comprueba si el usuario y contraseña es correcto
        return true;
    }
}