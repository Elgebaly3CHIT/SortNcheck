package com.example.sortncheck;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.sortncheck.backend.Hauptmenue;
import com.example.sortncheck.backend.Lagermoeglichkeit;
import com.example.sortncheck.backend.Objekt;
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
    public TextView displayNameEdit;

    public  ConstraintLayout infoBox;

    public Button strgbtn;
    public Button itembtn;
    public Button rmbtn;

    LinearLayout buttonarea;

    public Button createItm;
    public Button createStrg;
    public Button createRoom;
    public Button saveButton;

    public Map<Button,Raum> roombuttons;
    public Map<Button,Lagermoeglichkeit> storagebuttons;
    public Map<Button, Objekt> itembuttons;

    public Long currentSelectedObjekt;
    public Long currentInside;
    public int edittype = 0;
    public int objecttype;

    public Hauptmenue overhauptmenue;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overhauptmenue = new Hauptmenue();

       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  <-- design choice, if you want full screen or the thing with the notifications and clock and stuff*/
        setContentView(R.layout.activity_startpage);
        //start of the "fun" stuff
        //variable Declaration
        overhauptmenue = new Hauptmenue(); // alle objekte sind hier
        headerText = (TextView) findViewById(R.id.typeofObject); // The thing that says in what type of object you are
        infoBox = (ConstraintLayout) findViewById(R.id.infoBox); // the box with info of currently selected object
        final Button oval = findViewById(R.id.oval); //oval "button", only used once
        strgbtn = findViewById(R.id.strgbtn); // storage button, to make a new storage
        itembtn = findViewById(R.id.itembtn); // item button, to make a new item
        rmbtn = findViewById(R.id.rmbtn); // room button, to make a new room
        buttonarea = (LinearLayout) findViewById(R.id.buttonarea); //area where the buttons are (scrollable)
        // views for Object title and description
        descriptionView = (TextView) findViewById(R.id.description);
         titleView = (TextView) findViewById(R.id.objecttitle);
        // edits for Object title and description
        descriptionEdit = (TextView) findViewById(R.id.enterdescription);
         titleEdit = (TextView) findViewById(R.id.titleenter);
        saveButton = findViewById(R.id.save); // button that saves changes
        displayNameEdit = findViewById(R.id.enterDisplayname); // space to enter displayname
        createItm = findViewById(R.id.itembtn); // Button to make new Item

        //setting up page, according to objecttype, and the room its inside in
        Bundle b = getIntent().getExtras();
         objecttype = -1; // or other values
        if (b != null) {
            objecttype = b.getInt("objectType"); // the type of object its currently inside
            currentInside = b.getLong("insideId"); //the Id of object its currently inside
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                newRoom();
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
                itembtn.setVisibility(View.VISIBLE);
                strgbtn.setVisibility(View.VISIBLE);
                rmbtn.setVisibility(View.GONE);
                break;
            case 2: //if its 2, which means selecting ITEMS
                headerText.setText("Items");
                itembtn.setVisibility(View.VISIBLE);
                strgbtn.setVisibility(View.VISIBLE);
                descriptionView.setVisibility(View.VISIBLE);
                descriptionEdit.setVisibility(View.VISIBLE);
                rmbtn.setVisibility(View.GONE);
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void newRoom() {
        buttonarea.removeAllViews();
        String name = (String) titleEdit.getText().toString();
        String displayName = (String) displayNameEdit.getText().toString();
        overhauptmenue.addRaum(name , displayName);
        updateButtonsRaum();
        editType(0);

    }
    public void selectRaum(Raum raum) {
        titleView.setText(raum.getName());
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateButtonsRaum() {

        Map <Long ,Raum> raume = overhauptmenue.getRaume();
        for (Map.Entry<Long, Raum> entry : raume.entrySet()) {
            final Raum x = entry.getValue();
            Button btn1 = new Button(this);
            btn1.setBackgroundResource( R.drawable.funbtn);
            btn1.setText(entry.getValue().getDisplayName());
            btn1.setId(Math.toIntExact(entry.getKey()));
            TextViewCompat.setAutoSizeTextTypeWithDefaults(btn1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            btn1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectRaum(x);
                }
            });
            buttonarea.addView(btn1);
        }
    }
}
