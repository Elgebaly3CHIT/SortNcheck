package com.example.sortncheck;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
        final TextView info = findViewById(R.id.hmpginfotxt);
        final Button help = (Button)findViewById(R.id.helpButton);
        final Button cnt = findViewById(R.id.contactButton);
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Holo_Dialog)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to reset Your Files? Everything will be lost!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Hauptmenue.delete();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText("THIS IS THE SORTNCHECK TUTORIAL");
            }
        });
        cnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                info.setText("Fuckme@africamail.com");
            }
        });


    }
}