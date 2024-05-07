package es.c3.ecofamesc.control;

import es.c3.ecofamesc.EcoFamApplication;
import javafx.fxml.FXML;
import javafx.scene.control.*;




public class LoginController {

    @FXML private TextField user;
    @FXML private PasswordField pass;
    @FXML private Label mensaje;


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


        if (ecoFamApplication.getUserConnection().login(usuario, password)) {
            ecoFamApplication.mostrarPlanesUsuario();
        }
        else {
            mensaje.setText("Usuario y contraseña incorrectos");
        }
    }

    @FXML
    protected void onRegistroLinkClick() {
        this.ecoFamApplication.irRegistrarUsuario();
    }


}