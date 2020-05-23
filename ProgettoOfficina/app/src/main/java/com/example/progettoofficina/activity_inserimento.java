package com.example.progettoofficina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class activity_inserimento extends AppCompatActivity {
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
        setContentView(R.layout.activity_inserimento);
        final Button btnAddRip = (Button)findViewById(R.id.btnAddRip);
        try {
            //Caricamento spinner macchine
            caricaMacchine();
            //Gestione aggiunta riparazione al click su apposito bottone
            btnAddRip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRiparazione();
                }
            });
        } catch(Exception ex){
            gestErrori(ex.getMessage());
        }
    }

    /*******************************/
    /*Caricamento combo con le auto*/
    /*******************************/
    private void caricaMacchine(){
        //Carico combo con le auto
        final Spinner cboAddAuto = (Spinner) findViewById(R.id.cboAutoAddRip);
        final ArrayList<Auto> listaAuto= new ArrayList<Auto>();

        HTTPRequest reqCamp = new HTTPRequest(url + "/WebServicesOfficina/elencoMacchine.php") {
            @Override
            protected void onPostExecute(String result) {
                if (result.contains("Exception: "))
                    gestErrori(result);
                else {
                    try {
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
                        PersonalAdAuto adC = new PersonalAdAuto(getApplicationContext(), R.id.linLayoutAuto, listaAuto);
                        cboAddAuto.setAdapter(adC);
                    } catch (JSONException ex) {
                        gestErrori(ex.getMessage());
                    }
                }
            }
        };
        reqCamp.execute();
    }

    /*******************************/
    /*Gestione Aggiunta Riparazione*/
    /*******************************/
    private void addRiparazione(){
        Spinner cboAutoAddRip = (Spinner)findViewById(R.id.cboAutoAddRip);
        TextView txtDataAddRip = (TextView)findViewById(R.id.txtDataAddRip);
        TextView txtCausaAddRip = (TextView)findViewById(R.id.txtCausaAddRip);
        TextView txtCostoAddRip = (TextView)findViewById(R.id.txtCostoAddRip);
        CheckBox chkPagatoAddRip = (CheckBox)findViewById(R.id.chkPagatoAddRip);

        try {
            //Controllo dati di input
            if (cboAutoAddRip.getSelectedItemPosition() != -1){
                if (txtDataAddRip.getText().toString().length() > 0){
                    if (txtCausaAddRip.getText().toString().length() > 0){
                        if (txtCostoAddRip.getText().toString().length() > 0){
                            HTTPRequest reqCamp = new HTTPRequest(url + "/WebServicesOfficina/aggiungiRiparazione.php") {
                                @Override
                                protected void onPostExecute(String result) {
                                    if (!result.contains("AddRiparazioneOk"))
                                        gestErrori(result);
                                    else {
                                        //Segnalo il buon esito dell'operazione e torno all'activity di elenco delle riparazione
                                        Toast.makeText(getApplicationContext(), "Riparazione aggiunta con successo", Toast.LENGTH_LONG).show();
                                        Intent iForm2 = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(iForm2);
                                        finish();
                                    }
                                }
                            };
                            //Recupero l'auto selezionata e il relativo ID
                            Auto auto = (Auto)cboAutoAddRip.getSelectedItem();
                            //Formatto la data prendento solo giorno mese ed anno
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            Date dataRip = format.parse(txtDataAddRip.getText().toString());
                            //Imposto la data nel formato accettato dal DBMS
                            format = new SimpleDateFormat("yyyy/MM/dd");
                            //Preparazione Parametri richiesta inserimento
                            reqCamp.addParam(HTTPRequest.POST,"idAuto",auto.getIdAuto());
                            reqCamp.addParam(HTTPRequest.POST,"dataRiparazione",format.format(dataRip));
                            reqCamp.addParam(HTTPRequest.POST,"causaRiparazione",txtCausaAddRip.getText().toString());
                            reqCamp.addParam(HTTPRequest.POST,"costoRiparazione",txtCostoAddRip.getText().toString());
                            reqCamp.addParam(HTTPRequest.POST,"pagatoRiparazione",String.valueOf(chkPagatoAddRip.isChecked()));
                            reqCamp.execute();
                        }else
                            gestErroriInserimento("Indicare il costo della riparazione");
                    }else
                        gestErroriInserimento("Indicare la causa della riparazione");
                }else
                    gestErroriInserimento("Indicare la data della riparazione");
            }else
                gestErroriInserimento("Indicare l'auto");
        }catch(ParseException ex){
            gestErroriInserimento("Data riparazione non valida");
        }catch(Exception ex){
            gestErrori(ex.getMessage());
        }
    }

    /*********************************/
    /*Segnalazione Parametri Mancanti*/
    /*********************************/
    private void gestErroriInserimento(String msgErroreIns){
        Toast.makeText(getApplicationContext(), "Operazione Fallita: " + msgErroreIns, Toast.LENGTH_LONG).show();
    }

    /*****************************/
    /*Stampa a video degli Errori*/
    /*****************************/
    private void gestErrori(String msgErrore){
        TextView lblAutoAddRip = (TextView)findViewById(R.id.lblAutoAddRip);
        lblAutoAddRip.setVisibility(View.INVISIBLE);
        Spinner cboAutoAddRip = (Spinner)findViewById(R.id.cboAutoAddRip);
        cboAutoAddRip.setVisibility(View.INVISIBLE);

        TextView lblDataAddRip = (TextView)findViewById(R.id.lblDataAddRip);
        lblDataAddRip.setVisibility(View.INVISIBLE);
        TextView txtDataAddRip = (TextView)findViewById(R.id.txtDataAddRip);
        txtDataAddRip.setVisibility(View.INVISIBLE);

        TextView lblCausaAddRip = (TextView)findViewById(R.id.lblCausaAddRip);
        lblCausaAddRip.setVisibility(View.INVISIBLE);
        TextView txtCausaAddRip = (TextView)findViewById(R.id.txtCausaAddRip);
        txtCausaAddRip.setVisibility(View.INVISIBLE);

        TextView lblCostoAddRip = (TextView)findViewById(R.id.lblCostoAddRip);
        lblCostoAddRip.setVisibility(View.INVISIBLE);
        TextView txtCostoAddRip = (TextView)findViewById(R.id.txtCostoAddRip);
        txtCostoAddRip.setVisibility(View.INVISIBLE);

        TextView lblPagatoAddRip = (TextView)findViewById(R.id.lblPagatoAddRip);
        lblPagatoAddRip.setVisibility(View.INVISIBLE);
        CheckBox chkPagatoAddRip = (CheckBox)findViewById(R.id.chkPagatoAddRip);
        chkPagatoAddRip.setVisibility(View.INVISIBLE);

        Button btnAddRip = (Button)findViewById(R.id.btnAddRip);
        btnAddRip.setVisibility(View.INVISIBLE);

        TextView lblVisErrori = (TextView) findViewById(R.id.lblVisErroreAddRip);
        lblVisErrori.setVisibility(View.VISIBLE);
        lblVisErrori.setText("Attenzione!!! Errore: "+msgErrore);
        Log.i(TAG_LOG,"**************** " + msgErrore + " **********************");
    }
}
