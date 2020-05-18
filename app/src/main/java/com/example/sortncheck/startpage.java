package com.example.sortncheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.sortncheck.backend.Hauptmenue;
import com.example.sortncheck.backend.Raum;

import java.util.Iterator;
import java.util.Map;

/**
 * Die java für die Room/Storage/Item Page. java und xml need renaming, or dont idrk
 * Es settet die onclicks und den content auf
 */
public class startpage extends AppCompatActivity {
    public TextView headerText;

    public TextView descriptionView;
    public TextView titleView;
    public TextView descriptionEdit;
    public TextView titleEdit;
    public  ConstraintLayout infoBox;
    public Button strgbtn;
    public Button itembtn;
    public Button rmbtn;
    LinearLayout buttonarea;
    public Button createItm;
    public Button createStrg;
    public Button createRoom;
    public Button saveButton;
    public Button[] roombuttons;
    public Button[] storagebuttons;
    public Button[] itembuttons;
    public int edittype = 0;

    Hauptmenue overhauptmenue;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Hauptmenue x = new Hauptmenue();
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  <-- design choice, if you want full screen or the thing with the notifications and clock and stuff*/
        setContentView(R.layout.activity_startpage);
        //start of the "fun" stuff
        //variable Declaration
        overhauptmenue = new Hauptmenue();
        headerText = (TextView) findViewById(R.id.typeofObject); // The thing that says in what type of object you are
        infoBox = (ConstraintLayout) findViewById(R.id.infoBox); // the box with info of currently selected object
        final Button oval = findViewById(R.id.oval); //oval "button", only used once
        strgbtn = findViewById(R.id.strgbtn); // storage button, to make a new storage
        itembtn = findViewById(R.id.itembtn); // item button, to make a new item
        rmbtn = findViewById(R.id.rmbtn); // room button, to make a new room
        buttonarea = (LinearLayout) findViewById(R.id.buttonarea); //area where the buttons are (scrollable)
        // views for Object title and description
        final TextView descriptionView = (TextView) findViewById(R.id.description);
        TextView titleView = (TextView) findViewById(R.id.objecttitle);
        // edits for Object title and description
        TextView descriptionEdit = (TextView) findViewById(R.id.enterdescription);
        TextView titleEdit = (TextView) findViewById(R.id.titleenter);
        saveButton = findViewById(R.id.save);

        createItm = findViewById(R.id.itembtn); // Button to make new Item

        //setting up page, according to objecttype
        Bundle b = getIntent().getExtras();
        int objecttype = -1; // or other values
        if (b != null) {
            objecttype = b.getInt("objectType");
        }
        setToObject(objecttype);

        //listeners

        //so that the box vanishes on click
        oval.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (infoBox.getVisibility() == View.VISIBLE) {
                    infoBox.setVisibility(View.GONE);
                } else {
                    infoBox.setVisibility(View.VISIBLE);
                }
            }
        });
        //home button
        final ImageButton btn = (ImageButton) findViewById(R.id.homebutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(startpage.this, MainActivity.class));
            }
        });

        //Automatically Creates all needed Buttons, and assigns them their Respective Id's

        Button btn1 = new Button(this);
        btn1.setText("Room1");
        btn1.setBackgroundResource( R.drawable.funbtn);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(btn1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        buttonarea.addView(btn1);
        btn1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map <Long ,Raum> raume = overhauptmenue.getRaume();
                String x = "";
                for (Map.Entry<Long, Raum> entry : raume.entrySet()) {
                    x += entry.getValue().getName();
                }
                descriptionView.setText(x);
            }
        });

        itembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editType(-1);
            }
        });
        strgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editType(-1);
            }
        });
        rmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editType(-1);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overhauptmenue.addRaum("EEEE","TEST");
                editType(0);
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
            case 0: //if its 0, which means selecting ROOM
                headerText.setText("Rooms");
                //cant make a item/strg if you are selecting rooms
                itembtn.setVisibility(View.GONE);
                strgbtn.setVisibility(View.GONE);
                rmbtn.setVisibility(View.VISIBLE);
                break;
            case 1: //if its 1, which means selecting STORAGE
                headerText.setText("Storages");
                break;
            case 2: //if its 2, which means selecting ITEMS
                headerText.setText("Items");
                break;
            default:
                headerText.setText("Objecttype not Found");
        }

    }

    /**
     * sets everything up for making something anew (-1) editiing something (1) or simply viewing (0);
     * @param x  = what kind of stuff you want
     */
    public void editType(int x) {
        edittype = x;
        if( x == 0) {
            ViewSwitcher btnswitcher = (ViewSwitcher) findViewById(R.id.buttonSwitcher);
            btnswitcher.showNext(); //or switcher.showPrevious();
            ViewSwitcher dscrswitcher = (ViewSwitcher) findViewById(R.id.descriptionSwitcher);
            dscrswitcher.showNext(); //or switcher.showPrevious();
            ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.titleSwitcher);
            switcher.showNext(); //or switcher.showPrevious();
            itembtn.setEnabled(true);
            strgbtn.setEnabled(true);
            rmbtn.setEnabled(true);
        }
        else {
            itembtn.setEnabled(false);
            strgbtn.setEnabled(false);
            rmbtn.setEnabled(false);
            ViewSwitcher btnswitcher = (ViewSwitcher) findViewById(R.id.buttonSwitcher);
            btnswitcher.showNext(); //or switcher.showPrevious();
            ViewSwitcher dscrswitcher = (ViewSwitcher) findViewById(R.id.descriptionSwitcher);
            dscrswitcher.showNext(); //or switcher.showPrevious();
            ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.titleSwitcher);
            switcher.showNext(); //or switcher.showPrevious();
        }
    }
}
