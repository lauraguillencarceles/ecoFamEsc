package es.c3.ecofamesc.control;

import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.model.PlanEconomico;
import es.c3.ecofamesc.model.Usuario;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    @FXML
    private Button quitarUsuario;
    @FXML
    private Button agregarUsuario;
    private Usuario userActual;

    private EcoFamApplication ecoFamApplication;

    private PlanEconomico planDuplicado;
    private boolean modificado;

    public void setEcoFamApplication(EcoFamApplication ecoFamApplication, Usuario userActual) {
        errores.textProperty().set("");
        this.ecoFamApplication = ecoFamApplication;
        planesTable.setItems(ecoFamApplication.getDatosPlan());
        this.userActual = userActual;
        creador.textProperty().setValue(userActual.getUsername().getValue());
        modificado = false;
    }

    @FXML
    private void initialize() {
        planColumn.setCellValueFactory(cellData -> cellData.getValue().getNombre());
        creadorColumn.setCellValueFactory(cellData -> cellData.getValue().getCreador().getValue().getUsername());
        this.planDuplicado = new PlanEconomico();
        conectaDatos();
        agregarUsuario.setDisable(true);
        quitarUsuario.setDisable(true);
        usuarioMas.setDisable(true);
        planesTable.setRowFactory((TableView<PlanEconomico> tv) -> {
            TableRow<PlanEconomico> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    PlanEconomico rowData = row.getItem();
                    //System.out.println("Doble click en "+rowData.getNombre().getValue());
                    this.ecoFamApplication.irCalendarioPlan(rowData);
                }
            });
            return row;
        });
        planesTable.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            try {
                if (ov != null) {
                    desconectaDatos(ov);
                }
                boolean disable = true;
                if (nv != null) {
                    this.planDuplicado = (PlanEconomico) nv.clone();
                    disable = false;
                } else {
                    this.planDuplicado = new PlanEconomico();

                }
                agregarUsuario.setDisable(disable);
                quitarUsuario.setDisable(disable);
                usuarioMas.setDisable(disable);
                conectaDatos();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
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
        errores.textProperty().set("");
        nombre.textProperty().unbindBidirectional(plan.getNombre());
        creador.textProperty().setValue(userActual.getUsername().getValue());
        usuariosList.getItems().clear();

        if (modificado) {
            ecoFamApplication.cargaPlanesUsuario(this.userActual);
            modificado = false;
        }

    }

    private void mensaje(Alert.AlertType tipo, String titulo, String cabecera, String mensaje) {
        errores.textProperty().set("");
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
        errores.textProperty().set("");
    }
    @FXML
    private void handleGuardarPlan() {
        errores.textProperty().set("");
        PlanEconomico plan = planesTable.getSelectionModel().getSelectedItem();
        String nombrePlan = planDuplicado.getNombre().getValue();
        if (plan != null) {
            int idPlan = this.planDuplicado.getId().getValue();
            String token = ecoFamApplication.getToken();
            List<Usuario>  usersAntiguos = ecoFamApplication.getPlanConnection().getUsuariosPlan(idPlan,token);
            //planesTable.getSelectionModel().getSelectedItem().getUsuariosModelo();
            if (ecoFamApplication.modificarPlan(this.planDuplicado, usersAntiguos)) {
                //editar un plan
                plan.setNombre(this.planDuplicado.getNombre());
                plan.setCreador(this.planDuplicado.getCreador());
                plan.setUsuarios(this.planDuplicado.getUsuarios());
                planesTable.refresh();
                modificado = false;
                mensaje (Alert.AlertType.INFORMATION, "Plan "+nombrePlan, "Plan modificado", "El plan "+nombrePlan+" se ha modificado correctamente");
            }
            else {
                mensaje (Alert.AlertType.ERROR, "Plan "+nombrePlan, "Error modificando el plan", "No se ha podido modificar el plan "+nombrePlan);
            }

        } else {
            this.planDuplicado.setCreador(new SimpleObjectProperty<>(userActual));
            if (ecoFamApplication.guardarPlan(this.planDuplicado)) {
                ecoFamApplication.cargaPlanesUsuario(userActual);
                planesTable.setItems(ecoFamApplication.getDatosPlan());
                this.planDuplicado= planesTable.getItems().get(planesTable.getItems().size() - 1);
                //es uno nuevo
                desconectaDatos(this.planDuplicado);
                //ecoFamApplication.cargaPlanesUsuario(userActual);
                //planesTable.getItems().add(this.planDuplicado);
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
        errores.textProperty().set("");
        int selectedIndex = planesTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >=0 ) {
            String nombrePlan = planesTable.getItems().get(selectedIndex).getNombre().getValue();
            if (ecoFamApplication.borrarPlan(planesTable.getItems().get(selectedIndex))) {
                planesTable.getItems().remove(selectedIndex);
                mensaje (Alert.AlertType.INFORMATION, "Plan "+nombrePlan, "Plan eliminado", "El plan "+nombrePlan+" se ha eliminado correctamente");
            }
            else {
                mensaje (Alert.AlertType.ERROR, "Plan "+nombrePlan, "Error eliminando el plan", "No se ha podido eliminar el plan "+nombrePlan+".\nCompruebe que el plan no tiene usuarios asignados.");
            }
        }
        else {
            mensaje (Alert.AlertType.WARNING, "Sin selección", "Ningún plan seleccionado", "Por favor, seleccione un plan en la tabla.");
        }
    }

    @FXML
    private void handleAgregarUsuario() {
        errores.textProperty().set("");
        Usuario user = ecoFamApplication.getUsuarioByUsername(usuarioMas.getText());
        if (user != null) {
            usuariosList.getItems().add(user);
            this.planDuplicado.agregarUsuario(user);
            usuarioMas.setText("");
        }
        else
            errores.textProperty().set("El usuario '"+usuarioMas.getText()+"' no está registrado en la base de datos");
        modificado = true;
    }


    @FXML
    private void handleQuitarUsuario() {
        errores.textProperty().set("");
        Usuario uSeleccionado = (Usuario) usuariosList.getSelectionModel().getSelectedItem();
        usuariosList.getItems().remove(uSeleccionado);
        this.planDuplicado.quitarUsuario(uSeleccionado);
        this.modificado = true;
    }

    @FXML
    protected void onLoginLinkClick() {
        this.ecoFamApplication.irLogin();
    }
}

