package es.c3.ecofamesc.connection;
import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.model.PlanEconomico;
import es.c3.ecofamesc.model.Usuario;
import es.c3.ecofamesc.utils.UtilsJSON;
import javafx.beans.property.SimpleObjectProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class PlanConnection extends Connection {

    public PlanConnection(String base, EcoFamApplication ecoFamApplication) {
        this.base = base;
        this.ecoFamApplication = ecoFamApplication;
    }

    public List<PlanEconomico> getPlanesUsuario(Usuario usuario, String token) {
        List<PlanEconomico> planes = null;
        String cadena = "/plan/planes/"+usuario.getUsername().getValue();
        String response = realizaGet(cadena);
        try {
            if (response != null) {
                JSONArray jsonArray = new JSONArray(response);
                planes = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    PlanEconomico p = leePlan(jsonObject, base, token);
                    planes.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Error recuperando elementos "+e.getMessage());
        }
        return planes;
    }

    private PlanEconomico leePlan(JSONObject jsonObject, String base, String token) {
        try {
            int id = jsonObject.getInt("id");
            String nombre=jsonObject.getString("nombre");
            JSONObject creadorO = jsonObject.getJSONObject("creador");
            Usuario creador = new Usuario(creadorO.getInt("id"), creadorO.getString("nombreCompleto"),creadorO.getString("username"));
            List usuarios = getUsuariosPlan(id, token);
            PlanEconomico p = new PlanEconomico(id, nombre, creador, usuarios);
            return p;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }//leePlan

    public List<Usuario> getUsuariosPlan(int plan, String token) {
        List<Usuario> usuarios = null;
        String cadena = "/plan/usuPlan/"+plan;
        String response = realizaGet(cadena);
        try {
            if(response != null) {
                JSONArray jsonArray = new JSONArray(response);
                usuarios = new ArrayList<Usuario>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Usuario usuario = new Usuario(jsonObject.getInt("id"),jsonObject.getString("nombreCompleto"), jsonObject.getString("username"), jsonObject.getString("password"));
                    usuarios.add(usuario);
                }
            }
        } catch (Exception e) {
            System.err.println("Error recuperando elementos "+e.getMessage());
        }
        return usuarios;
    }//getUsuariosPlan

    public boolean guardaPlan(PlanEconomico plan,List<Usuario> usersAntiguos) {
        String jsonInputStream = UtilsJSON.creaPlanJSON(plan);
        int resultado = realizaPost("/plan", jsonInputStream);
        boolean ok = (resultado==200);
        if(ok && usersAntiguos != null) {
            List<Integer> idsAntiguos = new LinkedList<Integer>();

            for (Usuario u : usersAntiguos) {
                idsAntiguos.add(u.getId().getValue());
            }
            List<Integer> idsNuevos = new LinkedList<Integer>();
            for (Usuario u : plan.getUsuariosModelo()) {
                idsNuevos.add(u.getId().getValue());
            }
            for (Usuario usuario : plan.getUsuariosModelo()) {
                if (!idsAntiguos.contains(usuario.getId().getValue())) {
                    int id_usuario = usuario.getId().getValue();
                    int id_plan = plan.getId().getValue();
                    jsonInputStream = UtilsJSON.creaUsuarioPlanJSON(id_usuario, id_plan);
                    realizaPost("/usuarioplan", jsonInputStream);
                }
            }
            for (Usuario usuario : usersAntiguos) {
                if (!idsNuevos.contains(usuario.getId().getValue())) {
                    jsonInputStream = UtilsJSON.creaUsuarioPlanJSON(usuario.getId().getValue(), plan.getId().getValue());
                    realizaDelete("/usuarioplan/"+usuario.getId().getValue()+"/"+plan.getId().getValue());
                }
            }
            return true;
        }
        return ok;
    }

    public boolean borrarPlan(PlanEconomico plan) {
        return realizaDelete("/plan/"+plan.getId().getValue());
    }

}
