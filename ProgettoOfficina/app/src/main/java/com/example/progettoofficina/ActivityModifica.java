package com.example.progettoofficina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class ActivityModifica extends AppCompatActivity {
    //Indirizzo per la connessione
    private static final String url="http://192.168.1.189:80";
    //Tag di riferimento per i log generati dal progetto
    String TAG_LOG = "LOG_OFFICINA";
    private int idRiparazione = 0;

    /********************/
    /*Routine Principale*/
    /********************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica);

        Button btnModRip = (Button)findViewById(R.id.btnModRip);
        try {
            //Caricamento spinner macchine
            caricaMacchine();
            //Gestione modifica riparazione al click su apposito bottone
            btnModRip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modificaRiparazione();
                }
            });
        }catch (Exception ex){
            gestErrori(ex.getMessage());
        }

    }

    /*******************************/
    /*Caricamento combo con le auto*/
    /*******************************/
    private void caricaMacchine(){
        //Carico combo con le auto
        final Spinner cboAutoModRip = (Spinner) findViewById(R.id.cboAutoModRip);
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
                        cboAutoModRip.setAdapter(adC);
                        //Carico i dati della riparazione nei campi di input
                        //Eseguito in questo punto perchè altrimenti se venisse fatto prima della conclusione della richiesta HTTP
                        //il codice dell'OnPostRequest sovrascriverebbe il valore selezionato nello spinner delle auto
                        caricaDatiRiparazione(getIntent().getExtras());
                    } catch (JSONException ex) {
                        gestErrori(ex.getMessage());
                    }
                }
            }
        };
        reqCamp.execute();
    }

    /*************************************/
    /*Caricamento dati riparazione scelta*/
    /*************************************/
    protected void caricaDatiRiparazione(Bundle datiRiparazione){
        Spinner cboAutoModRip = (Spinner)findViewById(R.id.cboAutoModRip);
        TextView txtDataModRip = (TextView)findViewById(R.id.txtDataModRip);
        TextView txtCausaModRip = (TextView)findViewById(R.id.txtCausaModRip);
        TextView txtCostoModRip = (TextView)findViewById(R.id.txtCostoModRip);
        CheckBox chkPagatoModRip = (CheckBox)findViewById(R.id.chkPagatoModRip);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        //Recupero i dati della riparazione scelta dall'utente dal bundle passato all'activity
        idRiparazione = datiRiparazione.getInt("idRip");
        cboAutoModRip.setSelection(datiRiparazione.getInt("idAuto")-1);
        txtDataModRip.setText(format.format(new Date(datiRiparazione.getString("dataRip"))));
        txtCausaModRip.setText(datiRiparazione.getString("causaRip"));
        txtCostoModRip.setText(String.valueOf(datiRiparazione.getInt("costoRip")));
        if (datiRiparazione.getInt("pagatoRip") == 0)
            chkPagatoModRip.setChecked(false);
        else
            chkPagatoModRip.setChecked(true);
    }

    /*******************************/
    /*Gestione Modifica Riparazione*/
    /*******************************/
    private void modificaRiparazione(){
        Spinner cboAutoModRip = (Spinner)findViewById(R.id.cboAutoModRip);
        TextView txtDataModRip = (TextView)findViewById(R.id.txtDataModRip);
        TextView txtCausaModRip = (TextView)findViewById(R.id.txtCausaModRip);
        TextView txtCostoModRip = (TextView)findViewById(R.id.txtCostoModRip);
        CheckBox chkPagatoModRip = (CheckBox)findViewById(R.id.chkPagatoModRip);

        try {
            //Controllo dati di input
            if (cboAutoModRip.getSelectedItemPosition() != -1){
                if (txtDataModRip.getText().toString().length() > 0){
                    if (txtCausaModRip.getText().toString().length() > 0){
                        if (txtCostoModRip.getText().toString().length() > 0){
                            HTTPRequest reqCamp = new HTTPRequest(url + "/WebServicesOfficina/modificaRiparazione.php") {
                                @Override
                                protected void onPostExecute(String result) {
                                    if (!result.contains("ModRiparazioneOk"))
                                        gestErrori(result);
                                    else {
                                        //Segnalo il buon esito dell'operazione e torno all'activity di elenco delle riparazione
                                        Toast.makeText(getApplicationContext(), "Riparazione modificata con successo", Toast.LENGTH_LONG).show();
                                        Intent iForm2 = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(iForm2);
                                        finish();
                                    }
                                }
                            };
                            //Recupero l'auto selezionata e il relativo ID
                            Auto auto = (Auto)cboAutoModRip.getSelectedItem();
                            //Formatto la data prendento solo giorno mese ed anno
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            Date dataRip = format.parse(txtDataModRip.getText().toString());
                            //Imposto la data nel formato accettato dal DBMS
                            format = new SimpleDateFormat("yyyy/MM/dd");
                            //Preparazione Parametri richiesta modifica
                            reqCamp.addParam(HTTPRequest.POST,"idRip",idRiparazione);
                            reqCamp.addParam(HTTPRequest.POST,"idAuto",auto.getIdAuto());
                            reqCamp.addParam(HTTPRequest.POST,"dataRiparazione",format.format(dataRip));
                            reqCamp.addParam(HTTPRequest.POST,"causaRiparazione",txtCausaModRip.getText().toString());
                            reqCamp.addParam(HTTPRequest.POST,"costoRiparazione",txtCostoModRip.getText().toString());
                            reqCamp.addParam(HTTPRequest.POST,"pagatoRiparazione",String.valueOf(chkPagatoModRip.isChecked()));
                            reqCamp.execute();
                        }else
                            gestErroriModifica("Indicare il costo della riparazione");
                    }else
                        gestErroriModifica("Indicare la causa della riparazione");
                }else
                    gestErroriModifica("Indicare la data della riparazione");
            }else
                gestErroriModifica("Indicare l'auto");
        }catch(ParseException ex){
            gestErroriModifica("Data riparazione non valida");
        }catch(Exception ex){
            gestErrori(ex.getMessage());
        }
    }

    /*********************************/
    /*Segnalazione Parametri Mancanti*/
    /*********************************/
    private void gestErroriModifica(String msgErroreIns){
        Toast.makeText(getApplicationContext(), "Operazione Fallita: " + msgErroreIns, Toast.LENGTH_LONG).show();
    }

    /*****************************/
    /*Stampa a video degli Errori*/
    /*****************************/
    private void gestErrori(String msgErrore){
        TextView lblAutoModRip = (TextView)findViewById(R.id.lblAutoModRip);
        lblAutoModRip.setVisibility(View.INVISIBLE);
        Spinner cboAutoModRip = (Spinner)findViewById(R.id.cboAutoModRip);
        cboAutoModRip.setVisibility(View.INVISIBLE);

        TextView lblDataModRip = (TextView)findViewById(R.id.lblDataModRip);
        lblDataModRip.setVisibility(View.INVISIBLE);
        TextView txtDataModRip = (TextView)findViewById(R.id.txtDataModRip);
        txtDataModRip.setVisibility(View.INVISIBLE);

        TextView lblCausaModRip = (TextView)findViewById(R.id.lblCausaModRip);
        lblCausaModRip.setVisibility(View.INVISIBLE);
        TextView txtCausaModRip = (TextView)findViewById(R.id.txtCausaModRip);
        txtCausaModRip.setVisibility(View.INVISIBLE);

        TextView lblCostoModRip = (TextView)findViewById(R.id.lblCostoModRip);
        lblCostoModRip.setVisibility(View.INVISIBLE);
        TextView txtCostoModRip = (TextView)findViewById(R.id.txtCostoModRip);
        txtCostoModRip.setVisibility(View.INVISIBLE);

        TextView lblPagatoModRip = (TextView)findViewById(R.id.lblPagatoModRip);
        lblPagatoModRip.setVisibility(View.INVISIBLE);
        CheckBox chkPagatoModRip = (CheckBox)findViewById(R.id.chkPagatoModRip);
        chkPagatoModRip.setVisibility(View.INVISIBLE);

        Button btnModRip = (Button)findViewById(R.id.btnModRip);
        btnModRip.setVisibility(View.INVISIBLE);

        TextView lblVisErrori = (TextView) findViewById(R.id.lblVisErroriModRip);
        lblVisErrori.setVisibility(View.VISIBLE);
        lblVisErrori.setText("Attenzione!!! Errore: "+msgErrore);
        Log.i(TAG_LOG,"**************** " + msgErrore + " **********************");
    }
}
