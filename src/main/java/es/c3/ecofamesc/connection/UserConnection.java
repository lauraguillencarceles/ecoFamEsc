package es.c3.ecofamesc.connection;
import es.c3.ecofamesc.EcoFamApplication;
import es.c3.ecofamesc.model.Usuario;
import javafx.beans.property.SimpleIntegerProperty;
import org.json.JSONException;
import org.json.JSONObject;


public class UserConnection extends Connection {

    public UserConnection(String base, EcoFamApplication ecoFamApplication) {
        this.base = base;
        this.ecoFamApplication = ecoFamApplication;
    }

    public boolean login(String usuario, String password) {
        Usuario user = null;
        String fin = "/usuario/auth/login";
        try {
            String jsonInputStream = new JSONObject().put("username",usuario).put("password", password).toString();
            String response = realizaLogin(fin, jsonInputStream);

            JSONObject jsonObject = new JSONObject(response);
            user = new Usuario(jsonObject.get("nombre_completo").toString(), usuario, password);
            user.setId(new SimpleIntegerProperty(jsonObject.getInt("id")));
            ecoFamApplication.setUsuarioLogado(user);
            ecoFamApplication.setToken(jsonObject.get("token").toString());

            return true;
        } catch (Exception e) {
            System.err.println("Error en el login: "+e);
        }
        return false;
    }//login


    public Usuario getUsuarioByUsername(String username) {
            Usuario usuario = null;
            String response = realizaGet("/usuario/username/"+username);
            try {
                if (response != null) {
                    JSONObject jsonObject = new JSONObject(response);
                    usuario = new Usuario(jsonObject.getInt("id"), jsonObject.getString("nombreCompleto"), jsonObject.getString("username"));
                }
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
             catch (Exception e) {
                System.err.println("Error recuperando elementos "+e.getMessage());
            }
            return usuario;
    }

    public int registrarUsuario(String user, String name, String pass1, String pass2) {
        String fin = "/usuario/auth/register";
        try {
            String jsonInputStream = new JSONObject().put("username",user).put("password", pass1).put("password2", pass2).put("nombreCompleto", name).toString();
            return  realizaPost(fin, jsonInputStream);
        } catch (Exception e) {
            System.err.println("Error en el login: "+e);
        }
        return 0;
    }//registrarUsuario

}//UserConnection
