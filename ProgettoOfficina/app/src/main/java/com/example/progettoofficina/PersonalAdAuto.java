package com.example.progettoofficina;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonalAdAuto extends ArrayAdapter<Auto> {
    private Context cont;
    private int layout;
    private ArrayList<Auto> listAuto;

    public PersonalAdAuto(Context cont, int layout, ArrayList<Auto> listAuto){
        super(cont, layout, listAuto);
        this.cont=cont;
        this.layout=layout;
        this.listAuto=listAuto;
    }

    @Override
    public View getView(int pos, View layout, ViewGroup parent){
        return intiView(pos, layout, parent);
    }

    //Necessario per fare funzionare la combo in dropdown, diversamente quando si cerca di aprire la combo l'app crasha
    @Override
    public View getDropDownView(int pos, View layout, ViewGroup parent){
        return intiView(pos, layout, parent);
    }

    public View intiView(int pos, View layout, ViewGroup parent){
        View v=null;
        if(layout==null){
            LayoutInflater LayoutInf=(LayoutInflater)cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=LayoutInf.inflate(R.layout.layout_auto,parent,false);
        }
        else
            v=layout;
        TextView txtIdAuto=(TextView)v.findViewById(R.id.txtIdAuto);
        TextView txtMarcaAuto=(TextView)v.findViewById(R.id.txtMarcaAuto);
        TextView txtModelloAuto=(TextView)v.findViewById(R.id.txtModelloAuto);
        //Recupero l'auto corrente
        Auto auto=listAuto.get(pos);
        //Carico i dati dell'auto sull'adapter
        if (auto.getIdAuto() != 0)
            txtIdAuto.setText(String.valueOf(auto.getIdAuto()));
        else
            txtIdAuto.setText("");
        txtMarcaAuto.setText(auto.getMarcaAuto());
        txtModelloAuto.setText(auto.getModelloAuto());
        return v;
    }
}
