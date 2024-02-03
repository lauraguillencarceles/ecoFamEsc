package es.c3.ecofamesc.control;

import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.model.PlanEconomico;
import es.c3.ecofamesc.model.Usuario;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Collection;

public class PlanController  {

    @FXML
    TableView<PlanEconomico> planesTable;
    @FXML
    private TableColumn<PlanEconomico, String> planColumn;
    @FXML
    private TableColumn<PlanEconomico, String> creadorColumn;

    @FXML
    private TextField nombre;
    @FXML
    private TextField creador;
    @FXML
    private ListView usuariosList = new ListView();
    @FXML
    private TextField usuarioMas;
    @FXML
    private Label errores;

    private Usuario userActual;

    private EcoFamApplication ecoFamApplication;
    public void setEcoFamApplication(EcoFamApplication ecoFamApplication, Usuario userActual) {
        this.ecoFamApplication = ecoFamApplication;
        planesTable.setItems(ecoFamApplication.getDatosPlan());
        this.userActual = userActual;
        creador.textProperty().setValue(userActual.getUsername().getValue());

    }
    private PlanEconomico planDuplicado;
    @FXML
    private void initialize() {
        planColumn.setCellValueFactory(cellData -> cellData.getValue().getNombre());
        creadorColumn.setCellValueFactory(cellData -> cellData.getValue().getCreador().getValue().getUsername());
        this.planDuplicado = new PlanEconomico();
        conectaDatos();

        planesTable.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (ov != null) {
                desconectaDatos(ov);
            }
            if (nv != null) {
                try {
                    this.planDuplicado = (PlanEconomico) nv.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                this.planDuplicado = new PlanEconomico();
            }
            conectaDatos();
        });
    }
    private void conectaDatos() {
        nombre.textProperty().bindBidirectional(this.planDuplicado.getNombre());

        String nombreCreador = "";
        if (this.planDuplicado.getCreador().getValue() == null && this.userActual != null) {
            this.planDuplicado.setCreador(new SimpleObjectProperty(this.userActual));
        }
        if (this.planDuplicado.getCreador().getValue() != null) {
            nombreCreador = this.planDuplicado.getCreador().getValue().getUsername().getValue();
        }
        else {
            nombreCreador = "";
        }

        creador.textProperty().setValue(nombreCreador);

        Collection<Usuario> items = new ArrayList<Usuario>();
        for (SimpleObjectProperty<Usuario> usuario : this.planDuplicado.getUsuarios()) {
            items.add(usuario.getValue());
        }
        usuariosList.getItems().addAll(items);
    }

    private void desconectaDatos(PlanEconomico plan) {
        nombre.textProperty().unbindBidirectional(plan.getNombre());
        creador.textProperty().setValue(userActual.getUsername().getValue());
        usuariosList.getItems().clear();
    }

    private void mensaje(Alert.AlertType tipo, String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.initOwner(ecoFamApplication.getStage());
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    @FXML
    private void handleNuevoPlan() {
        planesTable.getSelectionModel().clearSelection();
        usuarioMas.setText("");
    }
    @FXML
    private void handleGuardarPlan() {
        PlanEconomico plan = planesTable.getSelectionModel().getSelectedItem();
        String nombrePlan = planDuplicado.getNombre().getValue();
        if (plan != null) {
            if (ecoFamApplication.modificarPlan(this.planDuplicado)) {
                //editar un plan
                plan.setNombre(this.planDuplicado.getNombre());
                plan.setCreador(this.planDuplicado.getCreador());
                plan.setUsuarios(this.planDuplicado.getUsuarios());
                planesTable.refresh();
                mensaje (Alert.AlertType.INFORMATION, "Plan "+nombrePlan, "Plan modificado", "El plan "+nombrePlan+" se ha modificado correctamente");
            }
            else {
                mensaje (Alert.AlertType.ERROR, "Plan "+nombrePlan, "Error modificando el plan", "No se ha podido modificar el plan "+nombrePlan);
            }

        } else {
            this.planDuplicado.setCreador(new SimpleObjectProperty<>(userActual));
            if (ecoFamApplication.guardarPlan(this.planDuplicado)) {
                //es uno nuevo
                desconectaDatos(this.planDuplicado);
                planesTable.getItems().add(this.planDuplicado);
                planesTable.getSelectionModel().select(this.planDuplicado);
                mensaje (Alert.AlertType.INFORMATION, "Plan "+nombrePlan, "Plan creado", "El plan "+nombrePlan+" se ha creado correctamente");
            }
            else {
                mensaje (Alert.AlertType.ERROR, "Plan "+nombrePlan, "Error creando el plan", "No se ha podido crear el plan "+nombrePlan);
            }
        }
        usuarioMas.setText("");
    }
    @FXML
    private void handleEliminarPlan() {
        int selectedIndex = planesTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >=0 ) {
            String nombrePlan = planesTable.getItems().get(selectedIndex).getNombre().getValue();
            if (ecoFamApplication.borrarPlan(planesTable.getItems().get(selectedIndex))) {
                planesTable.getItems().remove(selectedIndex);
                mensaje (Alert.AlertType.INFORMATION, "Plan "+nombrePlan, "Plan eliminado", "El plan "+nombrePlan+" se ha eliminado correctamente");
            }
            else {
                mensaje (Alert.AlertType.ERROR, "Plan "+nombrePlan, "Error eliminando el plan", "No se ha podido eliminar el plan "+nombrePlan);
            }
        }
        else {
            mensaje (Alert.AlertType.WARNING, "Sin selección", "Ningún plan seleccionado", "Por favor, seleccione un plan en la tabla.");
        }
    }

    @FXML
    private void handleAgregarUsuario() {
        Usuario user = ecoFamApplication.getUsuarioByUsername(usuarioMas.getText());
        if (user != null) {
            usuariosList.getItems().add(user);
            this.planDuplicado.agregarUsuario(user);
            usuarioMas.setText("");
        }
        else
            errores.textProperty().set("El usuario '"+usuarioMas.getText()+"' no está registrado en la base de datos");
    }


    @FXML
    private void handleQuitarUsuario() {
        Usuario uSeleccionado = (Usuario) usuariosList.getSelectionModel().getSelectedItem();
        usuariosList.getItems().remove(uSeleccionado);
        this.planDuplicado.quitarUsuario(uSeleccionado);
    }
}

