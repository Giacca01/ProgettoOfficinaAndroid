package com.example.progettoofficina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Button btn = (Button)findViewById(R.id.btnprova);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iForm2 = new Intent(getApplicationContext(), MainActivity.class); //Intent: oggetto che consente di passare da un intent all'altra
                startActivity(iForm2);
                finish();
            }
        });
    }
}
