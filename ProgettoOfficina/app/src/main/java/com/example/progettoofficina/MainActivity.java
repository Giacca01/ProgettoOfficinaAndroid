package com.example.progettoofficina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Indirizzo per la connessione
    private static final String url="http://192.168.1.189:80";
    //Tag di riferimento per i log generati dal progetto
    String TAG_LOG = "LOG_OFFICINA";

    /********************/
    /*Routine Principale*/
    /********************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView lblVisErrori = (TextView) findViewById(R.id.lblVisErrori);
        lblVisErrori.setVisibility(View.INVISIBLE);
        final Spinner cboAuto = (Spinner) findViewById(R.id.cboMacchine);
        final ArrayList<Auto> listaAuto= new ArrayList<Auto>();
        final Button btnAddRip = (Button)findViewById(R.id.btnAddRiparazione);
        try {
            caricaMacchine(listaAuto);
            //Gestione Selezione Elemento da ComboBox Automobili
            cboAuto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Carico l'elenco delle riparazioni in cui è coinvolta l'auto scelta
                    int codAuto=listaAuto.get(i).getIdAuto();
                    String marcaAuto = listaAuto.get(i).getMarcaAuto();
                    String modelloAuto = listaAuto.get(i).getModelloAuto();
                    Toast.makeText(getApplicationContext(), "Elemento selezionato: " + marcaAuto+" "+modelloAuto, Toast.LENGTH_LONG).show();
                    if (codAuto > 0) {
                        HTTPRequest reqFilter = new HTTPRequest(url + "/WebServicesOfficina/elencoRiparazioni.php") {
                            @Override
                            protected void onPostExecute(String result) {
                                if (result.contains("Exception: "))
                                    gestErrori(result);
                                else {
                                    caricaClasseRiparazioni(result);
                                }
                            }
                        };
                        reqFilter.addParam(HTTPRequest.POST,"idAuto",codAuto);
                        reqFilter.execute();
                    }
                    else{
                        //Carico l'elenco completo delle riparazioni
                        caricaRiparazioni();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            btnAddRip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent iForm2 = new Intent(getApplicationContext(), activity_inserimento.class); //Intent: oggetto che consente di passare da un intent all'altra
                    startActivity(iForm2);
                }
            });
        }
        catch(Exception ex){
            gestErrori(ex.getMessage());
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Spinner cboAuto = (Spinner) findViewById(R.id.cboMacchine);
        cboAuto.setSelection(0);
    }

    /*************************************************************/
    /*Caricamento list view con elenco completo delle riparazioni*/
    /*************************************************************/
    private void caricaRiparazioni(){
        HTTPRequest req = new HTTPRequest(url + "/WebServicesOfficina/elencoRiparazioni.php"){
            @Override
            protected void onPostExecute(String result){
                if (result.contains("Exception: "))
                    gestErrori(result);
                else {
                    caricaClasseRiparazioni(result);
                }
            }
        };
        req.execute();
    }

    /******************************************************/
    /*Caricamento dati su istanza della classe riparazioni*/
    /******************************************************/
    private void caricaClasseRiparazioni(String result){
        final ArrayList<Riparazioni> listaRiparazioni = new ArrayList<Riparazioni>();
        final ListView lstRiparazioni = (ListView) findViewById(R.id.lstRiparazioni);
        TextView lblNumRip = (TextView) findViewById(R.id.txtNumeroRiparazioni);
        try {
            JSONArray json = new JSONArray(result);
            int i = 0;
            listaRiparazioni.clear();
            while (i < json.length()) {
                JSONObject jsonRiparazioni = json.getJSONObject(i);
                Riparazioni riparazione = new Riparazioni();
                riparazione.setIdRiparazione(jsonRiparazioni.getInt("idRip"));
                riparazione.setIdAutoRiparazione(jsonRiparazioni.getInt("idAuto"));
                String dateString = jsonRiparazioni.getString("data");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                riparazione.setDataRiparazione(dateFormat.parse(dateString));
                riparazione.setCausaRiparazione(jsonRiparazioni.getString("causa"));
                riparazione.setCostoRiparazione(jsonRiparazioni.getInt("costo"));
                riparazione.setPagatoRiparazione(jsonRiparazioni.getInt("pagato"));
                riparazione.setMarcaAutoRiparazione(jsonRiparazioni.getString("marca"));
                riparazione.setModelloAutoRiparazione(jsonRiparazioni.getString("modello"));
                listaRiparazioni.add(riparazione);
                i++;
            }
            lblNumRip.setText("Riparazioni Presenti: " + listaRiparazioni.size());
            //Stampo le partite sull'adapter
            PersonalAd ad = new PersonalAd(MainActivity.this, R.id.layoutRow, listaRiparazioni);
            //aggancio le partite alla list view
            lstRiparazioni.setAdapter(ad);
        } catch (JSONException ex) {
            gestErrori(ex.getMessage());
        } catch (ParseException ex) {
            gestErrori(ex.getMessage());
        }
    }

    /*******************************/
    /*Caricamento combo con le auto*/
    /*******************************/
    private void caricaMacchine(final ArrayList<Auto> listaAuto){
        //Carico combo con le auto
        final Spinner cboAuto = (Spinner) findViewById(R.id.cboMacchine);
        //final ArrayList<Auto> listaAuto= new ArrayList<Auto>();
        final TextView lblNumAuto = (TextView) findViewById(R.id.txtNumeroAuto);
        HTTPRequest reqCamp = new HTTPRequest(url + "/WebServicesOfficina/elencoMacchine.php") {
            @Override
            protected void onPostExecute(String result) {
                if (result.contains("Exception: "))
                    gestErrori(result);
                else {
                    try {
                        Auto opzioneIniziale = new Auto();
                        opzioneIniziale.setIdAuto(0);
                        opzioneIniziale.setMarcaAuto("Tutte le Auto");
                        opzioneIniziale.setModelloAuto("");
                        listaAuto.add(opzioneIniziale);
                        JSONArray json = new JSONArray(result);
                        int i = 0;
                        while (i < json.length()) {
                            JSONObject jsonAuto = json.getJSONObject(i);
                            Auto auto = new Auto();
                            auto.setIdAuto(jsonAuto.getInt("idAuto"));
                            auto.setModelloAuto(jsonAuto.getString("modello"));
                            auto.setMarcaAuto(jsonAuto.getString("marca"));
                            listaAuto.add(auto);
                            i++;
                        }
                        lblNumAuto.setText("Auto Presenti: " + listaAuto.size());
                        PersonalAdAuto adC = new PersonalAdAuto(MainActivity.this, R.id.linLayoutAuto, listaAuto);
                        cboAuto.setAdapter(adC);
                    } catch (JSONException ex) {
                        gestErrori(ex.getMessage());
                    }
                }
            }
        };
        reqCamp.execute();
    }

    /*****************************/
    /*Stampa a video degli Errori*/
    /*****************************/
    private void gestErrori(String msgErrore){
        Spinner cboMacchine = (Spinner)findViewById(R.id.cboMacchine);
        cboMacchine.setVisibility(View.INVISIBLE);
        TextView lblNAuto = (TextView) findViewById(R.id.txtNumeroAuto);
        lblNAuto.setVisibility(View.INVISIBLE);
        ListView lstRip = (ListView)findViewById(R.id.lstRiparazioni);
        lstRip.setVisibility(View.INVISIBLE);
        TextView txtAuto = (TextView)findViewById(R.id.txtMarcaIntestaz);
        txtAuto.setVisibility(View.INVISIBLE);
        TextView txtData = (TextView)findViewById(R.id.txtDataIntestaz);
        txtData.setVisibility(View.INVISIBLE);
        TextView txtCausa = (TextView)findViewById(R.id.txtCausaIntestaz);
        txtCausa.setVisibility(View.INVISIBLE);
        TextView txtCosto = (TextView)findViewById(R.id.txtCostoIntestaz);
        txtCosto.setVisibility(View.INVISIBLE);
        TextView txtPagato = (TextView)findViewById(R.id.txtPagatoIntestaz);
        txtPagato.setVisibility(View.INVISIBLE);
        TextView lblNRip = (TextView)findViewById(R.id.txtNumeroRiparazioni);
        lblNRip.setVisibility(View.INVISIBLE);
        TextView lblVisErrori = (TextView) findViewById(R.id.lblVisErrori);
        lblVisErrori.setVisibility(View.VISIBLE);
        lblVisErrori.setText("Attenzione!!! Errore: "+msgErrore);
        //Mettere il tag globale
        Log.i(TAG_LOG,"**************** " + msgErrore + " **********************");
    }
}
