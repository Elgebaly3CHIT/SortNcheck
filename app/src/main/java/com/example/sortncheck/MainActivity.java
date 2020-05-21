package com.example.sortncheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sortncheck.backend.Hauptmenue;

/**
 * Main Activity is actually the Homepage, can't be renamed I think, sorry.
 */
public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //onclick from Homepage => RoomPage
        final Button btn = (Button)findViewById(R.id.RoomButton);
        final Button resetbtn = findViewById(R.id.resetbtn);
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Hauptmenue.delete();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, startpage.class);
                Bundle b = new Bundle();
                b.putInt("objectType", 0); // 0 = Room, 1 = Storage, 2 = Item
                b.putLong("Id",-1);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

    }
}