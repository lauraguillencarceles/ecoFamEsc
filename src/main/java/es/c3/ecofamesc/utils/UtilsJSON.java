package es.c3.ecofamesc.utils;

import es.c3.ecofamesc.model.PlanEconomico;
import es.c3.ecofamesc.model.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UtilsJSON {
    public static String creaPlanJSON(PlanEconomico plan)  {
        JSONObject jsonObject = new JSONObject();
        try {
            if (plan.getId().getValue() != 0) {
                jsonObject.put("id", plan.getId().getValue());
            }
            jsonObject.put("nombre", plan.getNombre().getValue());

            //jsonObject.put("creador", plan.getCreador().getValue().getId().getValue());
            /*if (plan.getUsuariosModelo().size() != 0) {
                jsonObject.put("usuarios", creaUsuariosJSONid(plan.getUsuariosModelo()));
            }*/
            jsonObject.put("creador", creaUsuarioJSON(plan.getCreador().getValue()));
            /*if (plan.getUsuariosModelo().size() != 0) {
                jsonObject.put("usuarios", creaUsuariosJSON(plan.getUsuariosModelo()));
            }*/
        } catch (JSONException e) {
            System.err.println("Error creando el plan "+e);;
        }
        return jsonObject.toString().replace("\"{","{").replace("}\"", "}").replace("\\","");
    }

    public static String creaUsuariosJSON(List<Usuario> usuarios)  {
        if (usuarios.size() != 0) {
            JSONArray jsonArray = new JSONArray();
            for (Usuario usuario : usuarios) {
                jsonArray.put(creaUsuarioJSON(usuario));
            }
            return jsonArray.toString().replace("\"{", "{").replace("}\"", "}").replace("\\", "");
        }
        return null;
    }//creaUsuarioJSON
    public static String creaUsuariosJSONid(List<Usuario> usuarios) throws JSONException {
        if (usuarios.size() != 0) {
            JSONArray jsonArray = new JSONArray();
            for (Usuario usuario : usuarios) {
                JSONObject jsonObject = (new JSONObject()).put("id", usuario.getId().getValue());
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString().replace("\"{", "{").replace("}\"", "}").replace("\\", "");
        }
        return null;
    }//creaUsuarioJSONid


    public static String creaUsuarioJSON(Usuario usuario)  {
        JSONObject jsonObject = new JSONObject();
        try {
            if (usuario.getId() != null) {
                jsonObject.put("id", usuario.getId().getValue());
            }
            if (usuario.getNombreCompleto() != null) {
                jsonObject.put("nombreCompleto",usuario.getNombreCompleto().getValue());
            }
            if (usuario.getUsername() != null) {
                jsonObject.put("username",usuario.getUsername().getValue());
            }
            if (usuario.getPassword() != null) {
                jsonObject.put("password",usuario.getPassword().getValue());
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject.toString().replace("\"{","{").replace("}\"", "}").replace("\\","");
    }//creaUsuarioJSON

    public static String creaUsuarioPlanJSON(int usuario, int plan)  {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("usuario", usuario);
            jsonObject.put("planEconomico", plan);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject.toString().replace("\"{","{").replace("}\"", "}").replace("\\","");
    }//creaUsuarioJSON
}
