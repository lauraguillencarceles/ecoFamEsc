package es.c3.ecofamesc.connection;

import es.c3.ecofamesc.EcoFamApplication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Connection {
    String base;
    EcoFamApplication ecoFamApplication;
    HttpURLConnection conn;
    public static String token;

    public String realizaGet(String cadena) {

        String response = null;
        try {
            URL url = new URL(base+cadena);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                try {
                    response = scanner.useDelimiter("\\Z").next();
                } catch (NoSuchElementException ex) {}//no se trata, no hay next y da error

                scanner.close();
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public String realizaLogin(String cadena, String jsonInputStream) {
        String response = null;
        try {
            URL url = new URL(base+cadena);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream outputStream = conn.getOutputStream()) {
                byte[] input = jsonInputStream.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }
            Scanner scanner = new Scanner(conn.getInputStream());
            response = scanner.useDelimiter("\\Z").next();
            scanner.close();
        } catch (IOException e) {
            System.err.println("User/pass no válidos");
        }
        return response;
    }//

    public int realizaPost(String cadena, String jsonInputStream) {
        int devuelve = 0;
        try {
            URL url = new URL(base+cadena);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);

            try (OutputStream outputStream = conn.getOutputStream()) {
                byte[] input = jsonInputStream.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }
            devuelve = conn.getResponseCode();
            conn.disconnect();
            return devuelve;

        } catch (IOException e) {
            System.err.println("Error realizando el Post: "+e);;
        }
        return devuelve;
    }//

    protected boolean realizaDelete(String fin, String jsonInputStream) {
        boolean devuelve = false;
        try {
            URL url = new URL(base+fin);
            conn  = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            try (OutputStream outputStream = conn.getOutputStream()) {
                byte[] input = jsonInputStream.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            if(conn.getResponseCode() == 200) {
                //System.out.println("Elemento " + fin + " borrado");
                devuelve = true;
            }
            else
                System.err.println("Fallo en la conexión");

        } catch (Exception e) {
            System.err.println("Error borrando elementos: "+e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return devuelve;
    }

    protected boolean realizaDelete(String fin) {
        boolean devuelve = false;
        try {
            URL url = new URL(base+fin);
            conn  = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            if(conn.getResponseCode() == 200) {
                //System.out.println("Elemento " + fin + " borrado");
                devuelve = true;
            }
            else
                System.err.println("Fallo en la conexión");

        } catch (Exception e) {
            System.err.println("Error borrando elementos: "+e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return devuelve;
    }

}
