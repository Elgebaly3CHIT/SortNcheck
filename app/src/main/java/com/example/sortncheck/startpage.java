package com.example.sortncheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Die java für die Room/Storage/Item Page. java und xml need renaming, or dont idrk
 * Es settet die onclicks und den content auf
 */
public class startpage extends AppCompatActivity {
    public TextView headerText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  <-- design choice, if you want full screen or the thing with the notifications and clock and stuff*/
        setContentView(R.layout.activity_startpage);

        //start of the "fun" stuff
        headerText = (TextView)findViewById(R.id.typeofObject); // The thing that says in what type of object you are
        Bundle b = getIntent().getExtras();
        int objecttype = -1; // or other values
        if(b != null) {
            objecttype = b.getInt("objectType");
        }
        setToObject(objecttype);
        final Button oval = findViewById(R.id.oval);
        oval.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ConstraintLayout infoBox = (ConstraintLayout) findViewById(R.id.infoBox);
                if (infoBox.getVisibility() == View.VISIBLE) {
                    infoBox.setVisibility(View.GONE);
                }
                else {
                    infoBox.setVisibility(View.VISIBLE);
                }
            }
        });
        final ImageButton btn = (ImageButton)findViewById(R.id.homebutton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(startpage.this, MainActivity.class));
            }
        });
    }

    /**
     * This method sets the buttons, texts etc. to the according Object,
     * For example the headertext would read "Room" "Storage" or "Item" according to what object is needed
     * @param objecttype what kind of object is needed, 0 = Room, 1 = storage and 2 = item. Everything else is an error, and should be looked into
     */
    public void setToObject(int objecttype) {

        switch(objecttype) {
            case 0: //if its 0, which means ROOM
                headerText.setText("Room");
                break;
            case 1: //if its 0, which means ROOM
                headerText.setText("Storage");
                break;
            case 2: //if its 0, which means ROOM
                headerText.setText("Item");
                break;
            case 69: //if its 0, which means ROOM
                headerText.setText("lol nice");
                break;
            default:
                headerText.setText("Objecttype not Found");
        }

    }
}
