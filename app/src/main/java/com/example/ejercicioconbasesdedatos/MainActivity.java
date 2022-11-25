package com.example.ejercicioconbasesdedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    final static String SERVIDOR = "http://192.168.3.68/nube/";

    Button btnCSV, btnXML, btnJSON;
    ListView listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        listaDatos = findViewById(R.id.listaDatos);
        btnCSV = findViewById(R.id.buttonCSV);
        btnXML = findViewById(R.id.buttonXML);
        btnJSON = findViewById(R.id.buttonJSON);

        btnCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DescargarCSV descargarCSV = new DescargarCSV();
                descargarCSV.execute("listadoCSV.php");

            }
        });

        btnXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescargarXML descargarXML = new DescargarXML();
                descargarXML.execute("listadoXML.php");

            }
        });

    }

    private class DescargarXML extends AsyncTask<String, Void, Void>{
        String todo = "";
        DefaultTableModel modelo;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            modelo = new DefaultTableModel();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String url = SERVIDOR + "listadoXML.php";

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document doc = db.parse(new URL(url).openStream());
                Element raiz = doc.getDocumentElement();
                System.out.println("Raíz: " + raiz.getNodeName());

                NodeList hijos = doc.getElementsByTagName("articulo");

                for (int i = 0; i < hijos.getLength(); i++) {
                    Node nodo = hijos.item(i);

                    if (nodo instanceof Element) {
                        NodeList nietos = nodo.getChildNodes();
                        String[] fila = new String[nietos.getLength()];
                        for (int j = 0; j < nietos.getLength(); j++) {
                            if (i == 0) {
                                modelo.addColumn(nietos.item(j).getNodeName());
                            }
                            if (nietos.item(j) instanceof Element) {
                                fila[j] = nietos.item(j).getTextContent();
                            }
                        }
                        modelo.addRow(fila);
                    }

                }
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(AccesoBD.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(AccesoBD.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AccesoBD.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(AccesoBD.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

    }

    private class DescargarCSV extends AsyncTask<String, Void, Void> {
        String todo = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ArrayAdapter<String> adapter;
            List<String> list = new ArrayList<String>();
            String[] lineas = todo.split("\n");

            for(String linea:lineas){
                String[] campos = linea.split(";");
                String dato="ID: "+campos[0];
                dato+=" PRODUCTO: "+campos[1];
                dato+=" MODELO: "+campos[2];
                dato+=" PRECIO: "+campos[3];
                list.add(dato);
            }
            adapter = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
            listaDatos.setAdapter(adapter);

        }

        @Override
        protected Void doInBackground(String... strings) {
            String script = strings[0];
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(SERVIDOR + script);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String linea = "";
                    while ((linea = br.readLine()) != null) {
                        todo += linea+"\n";
                    }
                    br.close();
                    inputStream.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}