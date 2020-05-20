package com.example.progettoofficina;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonalAd extends ArrayAdapter<Riparazioni> {
    private Context cont;
    private int layout;
    private ArrayList<Riparazioni> listRiparazioni;

    public PersonalAd(Context cont, int layout, ArrayList<Riparazioni> listaRiparazioni){
        super(cont, layout, listaRiparazioni);
        this.cont=cont;
        this.layout=layout;
        this.listRiparazioni=listaRiparazioni;
    }

    public View getView(int pos, View layout, ViewGroup parent){
        View v=null;
        if(layout==null){
            LayoutInflater LayoutInf=(LayoutInflater)cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=LayoutInf.inflate(R.layout.layout_riparazioni,parent,false);
        }
        else
            v=layout;
        TextView txtMarca=(TextView)v.findViewById(R.id.txtMarca);
        TextView txtModello=(TextView)v.findViewById(R.id.txtModello);
        TextView txtData=(TextView)v.findViewById(R.id.txtData);
        TextView txtCausa=(TextView)v.findViewById(R.id.txtCausa);
        TextView txtCosto=(TextView)v.findViewById(R.id.txtCosto);
        CheckBox chkPagato = (CheckBox)v.findViewById(R.id.chkPagato);
        Riparazioni riparazione=listRiparazioni.get(pos);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        txtData.setText(df.format("dd/MM/yyyy",riparazione.getDataRiparazione()));
        txtMarca.setText(riparazione.getMarcaAutoRiparazione());
        txtModello.setText(riparazione.getModelloAutoRiparazione());
        txtCausa.setText(riparazione.getCausaRiparazione());
        txtCosto.setText(String.valueOf(riparazione.getCostoRiparazione()) +"€");
        if (riparazione.getPagatoRiparazione() == 0)
            chkPagato.setChecked(false);
        else
            chkPagato.setChecked(true);
        //chkPagato.setEnabled(false);
        return v;
    }
}
