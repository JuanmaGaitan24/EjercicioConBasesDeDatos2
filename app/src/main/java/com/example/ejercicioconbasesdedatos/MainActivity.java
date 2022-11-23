package com.example.ejercicioconbasesdedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    final static String SERVIDOR = "http://192.168.3.68/nube/";

    Button btnCSV, btnXML, btnJSON;
    ListView listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaDatos = findViewById(R.id.listaDatos);
        btnCSV = findViewById(R.id.buttonCSV);
        btnXML = findViewById(R.id.buttonXML);
        btnJSON = findViewById(R.id.buttonJSON);

        btnCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListarPorCSV();

            }
        });

    }

    private void ListarPorCSV() {

        String url = SERVIDOR + "listadoCSV.php";
        String linea = "";
        String consulta = "";

        try {

            URLConnection conexion = null;
            conexion = new URL(url).openConnection();
            InputStream inputStream = conexion.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            ArrayList<String> lineas = null;

            while ((linea = br.readLine()) != null) {

                lineas.add(linea);

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lineas);
            listaDatos.setAdapter(adapter);

        } catch (MalformedURLException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}