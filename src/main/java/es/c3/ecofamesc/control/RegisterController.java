package es.c3.ecofamesc.control;

import es.c3.ecofamesc.EcoFamApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.HttpURLConnection;


public class RegisterController {

    @FXML private TextField user;
    @FXML private TextField nombre;
    @FXML private PasswordField pass1;
    @FXML private PasswordField pass2;
    @FXML private Label mensaje;


    private EcoFamApplication ecoFamApplication;


    public void setEcoFamApplication(EcoFamApplication ecoFamApplication) {
        this.ecoFamApplication = ecoFamApplication;
    }


    public RegisterController() {

    }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.initOwner(ecoFamApplication.getStage());
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    @FXML
    protected void onRegisterButtonClick() {
        String usuario = user.getText();
        String nombreCompleto = nombre.getText();
        String password1 = pass1.getText();
        String password2 = pass2.getText();

        if (!iguales(password1, password2)) {
            mensaje.setText("Las contraseñas no coinciden o tienen más de 255 caracteres.");
        } else if (!usuarioValido(usuario)) {
            mensaje.setText("El nombre de usuario no es válido, no debe tener espacios la logitud entre 5 y 16 caracteres .");
        } else {
            if (nombreCompleto != null && nombreCompleto.length()>= 255)
                nombreCompleto = nombreCompleto.substring(0, 255);
            int resultado =ecoFamApplication.registrarUsuario(usuario, nombreCompleto, password1, password2);
            if (resultado == HttpURLConnection.HTTP_OK){
                mostrarMensaje(Alert.AlertType.INFORMATION, "Usuario "+usuario, "Usuario registrado", "El usuario "+usuario+" se ha registrado correctamente");
                ecoFamApplication.irLogin();
            } else if (resultado == HttpURLConnection.HTTP_CONFLICT) {
                mensaje.setText("Usuario " + usuario + " ya existe en la base de datos");
            } else {
                mensaje.setText("Usuario " + usuario + " no se ha podido registrar");
            }
        }
    }//onRegisterButtonClick

    private boolean iguales(String cadena1, String cadena2) {
        if (cadena1 != null && cadena2 != null && cadena1.length() <= 255 && cadena2.length() <= 255) {
            return cadena1.equals(cadena2);
        }
        return false;
    }//iguales

    private boolean usuarioValido(String user) {
        if (user == null) return false;
        if (user.length() < 5 || user.length()>16) return false;
        if (user.contains(" ")) return false;
        if (user.contains("\t")) return false;
        return true;
    }
    @FXML
    protected void onVolverLinkClick() {
        ecoFamApplication.irLogin();
    }




}