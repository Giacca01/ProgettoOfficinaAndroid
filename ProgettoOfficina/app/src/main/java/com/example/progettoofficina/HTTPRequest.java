package com.example.progettoofficina;

import android.net.Uri;
import android.os.AsyncTask;

import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

//AsyncTask è una classe tipizzata, i parametri da indicare sono :
//TParams indica il tipo di parametri che passeremo al metodo doInBackground
//TProgress indica il tipo di parametri che passeremo al metodo onProgressUpdate
//(non useremo il metodo update quindi il secondo parametro sarà sempre Void)
//TResult indica il tipo di parametro restituito da doInBackground

public class HTTPRequest extends AsyncTask<String, Void, String>
{
    public static final String GET = "get";
    public static final String POST = "post";
    private String risorsa;

    // Lista dei parametri get
    private ArrayList<BasicNameValuePair> get = new ArrayList<BasicNameValuePair>();
    // Lista dei parametri post
    private ArrayList<BasicNameValuePair> post = new ArrayList<BasicNameValuePair>();

    public HTTPRequest(String risorsa)
    {
        this.risorsa = risorsa;
    }


    public void addParam(String method, String name, String value)
            throws IllegalArgumentException
    {
        if (method.toLowerCase().equals(HTTPRequest.GET))
        {
            this.get.add(new BasicNameValuePair(name, value));
        }
        else if(method.toLowerCase().equals(HTTPRequest.POST))
        {
            this.post.add(new BasicNameValuePair(name, value));
        }
        else
        {
            throw new IllegalArgumentException("Cannot parse method type");
        }
    }

    public void addParam(String method, String name, int value)
            throws IllegalArgumentException
    {
        this.addParam(method, name, Integer.toString(value));
    }

    @Override
    protected String doInBackground(String... args)
    {
        String res = risorsa;
        String ris = "";
        String data = "";
        String line = "";

        try
        {
            URLConnection conn = null;

            // Aggiunta parametri get
            for(BasicNameValuePair keypair : this.get)
            {
                String divisor;
                if(res.indexOf('?') == -1)
                    divisor="?";
                else
                    divisor="&";
                res += divisor + keypair.getName() + "=" + Uri.encode(keypair.getValue());
            }

            // Apertura asincrona della connessione
            // (già contenente i parametri GET)
            // new URL trasforma la url testuale in object
            conn = new URL(res).openConnection();

            // Aggiunta di eventuali parametri POST
            if (!this.post.isEmpty())
            {
                // abilita la scrittura dei parametri post sulla connessione
                conn.setDoOutput(true);

                for(BasicNameValuePair keypair : this.post)
                    data += keypair.getName() + "=" + Uri.encode(keypair.getValue()) + "&";
                // tolgo l'ultimo '&'
                data = data.substring(0, data.length() - 1);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                wr.close();
            }

            // Lettura risposta
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while((line = reader.readLine()) != null)
                ris += line + "\n";
            reader.close();
        }
        catch (Exception ex)
        {
            ris = "Exception: " + ex.getMessage();
        }
        return ris;
    }
}
