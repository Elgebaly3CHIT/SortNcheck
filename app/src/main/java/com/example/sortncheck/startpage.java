package com.example.sortncheck;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Map;

/**
 * Die java für die Room/Storage/Item Page. java und xml need renaming, or dont idrc
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
    public Button enterbtn;
    public Button editbtn;
    public Button createItm;
    public Button createStrg;
    public Button createRoom;
    public Button saveButton;
    public ImageButton deleteButton;

    public Long currentSelectedObjekt;
    public Long currentInsideID;
    public int edit_type = 0;
    public int object_type;
    public long currentSelectionId;
    public int selection_type;
    public Raum currentInsideRaum;
    public Lagermoeglichkeit currentInsideStrg;
    public Raum currentSelectionRaum;
    public Lagermoeglichkeit currentSelectionStrg;
    public Objekt currentSelectionItem;
    public Hauptmenue overhauptmenue;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overhauptmenue = new Hauptmenue();
        
        setContentView(R.layout.activity_startpage);
        //From here on out you can work with views


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
         enterbtn = findViewById(R.id.enter);
        editbtn = findViewById(R.id.edit);
        deleteButton = findViewById(R.id.deletebtn);

        enterbtn.setEnabled(false);
        editbtn.setEnabled(false);
        //setting up page, according to objecttype, and the room its inside in
        Bundle b = getIntent().getExtras();
        if (b != null) {
            object_type = b.getInt("objectType"); // the type of object its currently inside
            currentInsideID = b.getLong("Id");
        }
        setToObject(object_type);
        //Updates the buttons once
        update();

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

        //entering a thing
        enterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(startpage.this, startpage.class);
                Bundle b = new Bundle();
                b.putInt("objectType", selection_type); // 0 = Room, 1 = Storage, 2 = Item  puts in the selected object type and id fot you to know where you are
                b.putLong("Id",currentSelectionId);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
        /**
         * Item Button
         */
        itembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editType(-1);
                selection_type = 2;
            }
        });

        /**
         * New Storage button
         */
        strgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editType(-1);
                selection_type = 1;
            }
        });
        // new room button
        rmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editType(-1);
                selection_type = 0;
            }
        });
        /**
         * what is currently being saved? 0 = room etc.
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(edit_type == -1) {
                    if(selection_type == 0) newRoom()
                            ;
                    if(selection_type == 1) newStorage()
                        ;
                    if(selection_type == 2) newItem()
                        ;
                }
                else {
                    if(selection_type == 1) editRoom()
                            ;
                    if(selection_type == 2) editStorage()
                            ;
                    if(selection_type == 3) editItem()
                            ;
                }
            }
        });
        /**
         * delete button, deletes currently selected thing
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(selection_type == 1) deleteRoom()
                        ;
                if(selection_type == 2) deleteStorage()
                        ;
                if(selection_type == 3) deleteItem()
                        ;
            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                editType(1);

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
            case 0: //if its 0, which means inside Hauptmenu
                headerText.setText("Sortncheck");
                //cant make a item/strg if you are selecting rooms
                itembtn.setVisibility(View.GONE);
                strgbtn.setVisibility(View.GONE);
                rmbtn.setVisibility(View.VISIBLE);
                break;
            case 1: //if its 1, which means inside  Room

                currentInsideRaum = overhauptmenue.getRaum(currentInsideID);
                headerText.setText(""+currentInsideRaum.getName());
                itembtn.setVisibility(View.VISIBLE);
                strgbtn.setVisibility(View.VISIBLE);
                rmbtn.setVisibility(View.GONE);
                break;
            case 2: //if its 2, which means inside  Storage
                currentInsideStrg = overhauptmenue.getLager(currentInsideID);
                headerText.setText(""+currentInsideStrg.getName());
                itembtn.setVisibility(View.VISIBLE);
                strgbtn.setVisibility(View.VISIBLE);
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
        edit_type = x;
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

    /**
     * Add new room, and updates the buttons
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void newRoom() {
        buttonarea.removeAllViews();
        String name = (String) titleEdit.getText().toString();
        String displayName = (String) displayNameEdit.getText().toString();
        String description = (String) descriptionEdit.getText().toString();
        addObject(name , description,  displayName,0);
        update();
        editType(0);

    }
    /**
     * Add new storage, and updates the buttons
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void newStorage() {
        buttonarea.removeAllViews();
        String name = (String) titleEdit.getText().toString();
        String displayName = (String) displayNameEdit.getText().toString();
        String description = (String) descriptionEdit.getText().toString();
        addObject(name, description , displayName,1);
        update();
        editType(0);

    }
    /**
     * Add new item, and updates the buttons
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void newItem() {
        buttonarea.removeAllViews();
        String name = (String) titleEdit.getText().toString();
        String displayName = (String) displayNameEdit.getText().toString();
        String description = (String) descriptionEdit.getText().toString();
        addObject(name, description , displayName,2);
        update();
        editType(0);

    }

    /**
     * Room is selected
     * @param raum = raum that got selected
     */
    public void select(Raum raum) {
        selection_type = 1;
        currentSelectionId = raum.getId();
        currentSelectionRaum = raum;
        titleView.setText(currentSelectionRaum.getName());
        descriptionView.setText(currentSelectionRaum.getBeschreibung());
    }
    /**
     * Storage is selected
     * @param strg = strg that got selected
     */
    public void select(Lagermoeglichkeit strg) {
        selection_type = 2;
        currentSelectionId = strg.getId();
        currentSelectionStrg = strg;
        titleView.setText(""+currentSelectionStrg.getName());
        descriptionView.setText(currentSelectionStrg.getBeschreibung());

        enterbtn.setVisibility(View.VISIBLE);
    }
    /**
     * Item is selected
     * @param item = Item that got selected
     */
    public void select(Objekt item) {
        selection_type = 3;
        currentSelectionId = item.getId();
        currentSelectionItem = item;
        enterbtn.setVisibility(View.GONE);
        titleView.setText(""+currentSelectionItem.getName());
        descriptionView.setText(item.getBeschreibung());
    }

    /**
     * Function that updates buttons if its selecting rooms (Its inside Hauptmenu)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateButtonsHaupt() {

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
                    select(x);

                    enterbtn.setEnabled(true);
                    editbtn.setEnabled(true);
                }
            });
            buttonarea.addView(btn1);
        }
    }

    /**
     * Updates all the objects inside of a room
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateButtonsRoom() {
        Map <Long ,Lagermoeglichkeit> strgs = currentInsideRaum.getLagermoeglichkeiten();
        for (Map.Entry<Long, Lagermoeglichkeit> entry : strgs.entrySet()) {
            final Lagermoeglichkeit x = entry.getValue();
            Button btn1 = new Button(this);
            btn1.setBackgroundResource( R.drawable.funbtn);
            btn1.setText(entry.getValue().getDisplayName());
            btn1.setId(Math.toIntExact(entry.getKey()));
            TextViewCompat.setAutoSizeTextTypeWithDefaults(btn1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            btn1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
               public void onClick(View v) {
                    select(x);

                    enterbtn.setEnabled(true);
                    editbtn.setEnabled(true);
                }
            });
            buttonarea.addView(btn1);
        }
        Map <Long ,Objekt> items = currentInsideRaum.getObjekte();
        for (Map.Entry<Long, Objekt> entry : items.entrySet()) {
            final Objekt x = entry.getValue();
            Button btn1 = new Button(this);
            btn1.setBackgroundResource( R.drawable.funbtn);
            btn1.setText(entry.getValue().getDisplayName());
            btn1.setId(Math.toIntExact(entry.getKey()));
            TextViewCompat.setAutoSizeTextTypeWithDefaults(btn1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            btn1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
               public void onClick(View v) {
                    select(x);

                    enterbtn.setEnabled(true);
                    editbtn.setEnabled(true);
                }
            });
            buttonarea.addView(btn1);
        }
    }

    /**
     * Function that updates buttons to show all objects in storage
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateButtonsStrg() {
        Map <Long ,Lagermoeglichkeit> strgs = currentInsideStrg.getLagermoeglichkeiten();
        for (Map.Entry<Long, Lagermoeglichkeit> entry : strgs.entrySet()) {
            final Lagermoeglichkeit x = entry.getValue();
            Button btn1 = new Button(this);
            btn1.setBackgroundResource( R.drawable.funbtn);
            btn1.setText(entry.getValue().getDisplayName());
            btn1.setId(Math.toIntExact(entry.getKey()));
            TextViewCompat.setAutoSizeTextTypeWithDefaults(btn1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            btn1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
               public void onClick(View v) {
                    select(x);

                    enterbtn.setEnabled(true);
                    editbtn.setEnabled(true);
                }
            });
            buttonarea.addView(btn1);
        }
        Map <Long ,Objekt> items = currentInsideStrg.getObjekte();
        for (Map.Entry<Long, Objekt> entry : items.entrySet()) {
            final Objekt x = entry.getValue();
            Button btn1 = new Button(this);
            btn1.setBackgroundResource( R.drawable.funbtn);
            btn1.setText(entry.getValue().getDisplayName());
            btn1.setId(Math.toIntExact(entry.getKey()));
            TextViewCompat.setAutoSizeTextTypeWithDefaults(btn1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            btn1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
               public void onClick(View v) {
                    select(x);

                    enterbtn.setEnabled(true);
                    editbtn.setEnabled(true);
                }
            });
            buttonarea.addView(btn1);
        }
    }

    /**
     * choose between update kind, so that you dont look for items in a hauptmenue etc
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update() {

        switch(object_type) {
            case 0:
                updateButtonsHaupt();
                break;
            case 1:
                updateButtonsRoom();
                break;
            case 2:
                updateButtonsStrg();
                break;
        }
    }

    /**
     * Choose where new Object ( Im sure there's a more efficient way to do this. Too bad!)
     * @param name name of new object
     * @param description descrtiption of new object
     * @param displayname displayname of new object
     * @param type = type of object to add 0 = add room, 1 = add Storage 2 = add Item
     */
    public void addObject(String name,  String displayname,String description,int type) {
        switch(object_type) {
            // if you add inside hauptmenu
            case 0:
                switch(type) {
                    case 0:
                        overhauptmenue.addRaum(name,description,displayname);
                        break;
                    default: //TODO: Write exception for voodoo shit
                        break;
                }
                break;
                // if you add inside room
            case 1:
                switch(type) {
                    case 1:
                        currentInsideRaum.addLager(name,description,displayname);
                        break;
                    case 2:
                        currentInsideRaum.addObjekt(name,description,displayname);
                        break;
                    default: //TODO: Write exception for voodoo shit
                        break;
                }
                break;
                // if add inside storage
            case 2:
                switch(type) {
                    case 1:
                        currentInsideStrg.addLager(name,description,displayname);
                        break;
                    case 2:
                        currentInsideStrg.addObjekt(name,description,displayname);
                        break;
                    default: //TODO: Write exception for voodoo shit
                        break;
                }
                break;
            default :
                //TODO: vodoo
                break;
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteRoom() {
        Button badbutton = findViewById(Math.toIntExact(currentSelectionRaum.getId()));
        badbutton.setSelected(false);
        badbutton.setVisibility(View.GONE);
        currentSelectionRaum.delete();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteStorage() {
        Button badbutton = findViewById(Math.toIntExact(currentSelectionStrg.getId()));
        badbutton.setSelected(false);
        badbutton.setVisibility(View.GONE);
        currentSelectionStrg.delete();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteItem() {
        Button badbutton = findViewById(Math.toIntExact(currentSelectionItem.getId()));
        badbutton.setSelected(false);
        badbutton.setVisibility(View.GONE);
        currentSelectionItem.delete();
    }

    /**
     * Edits Room, and reruns the Entire thing. Im sure its very resource intensive. Too bad!
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void editRoom() {
        String name = (String) titleEdit.getText().toString();
        String displayName = (String) displayNameEdit.getText().toString();
        String description = (String) descriptionEdit.getText().toString();
        currentSelectionRaum.editRaum(name , displayName, description  );
        editType(0);
        Intent intent = new Intent(startpage.this, startpage.class);
        Bundle b = new Bundle();
        b.putInt("objectType", object_type); // 0 = Room, 1 = Storage, 2 = Item  puts in the selected object type and id fot you to know where you are
        b.putLong("Id",currentInsideID);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    /**
     * Edits a storage
     */
    public void editStorage() {
        String name = (String) titleEdit.getText().toString();
        String displayName = (String) displayNameEdit.getText().toString();
        String description = (String) descriptionEdit.getText().toString();
        currentSelectionStrg.editLager(name , displayName, description  );
        editType(0);/*
        Intent intent = new Intent(startpage.this, startpage.class);
        Bundle b = new Bundle();
        b.putInt("objectType", object_type); // 0 = Room, 1 = Storage, 2 = Item  puts in the selected object type and id fot you to know where you are
        b.putLong("Id",currentInsideID);
        intent.putExtras(b);
        startActivity(intent);
        finish();*/
    }
    public void editItem() {
        String name = (String) titleEdit.getText().toString();
        String displayName = (String) displayNameEdit.getText().toString();
        String description = (String) descriptionEdit.getText().toString();
        currentSelectionItem.editObject(name,displayName,description);
        editType(0);/*
        Intent intent = new Intent(startpage.this, startpage.class);
        Bundle b = new Bundle();
        b.putInt("objectType", object_type); // 0 = Room, 1 = Storage, 2 = Item  puts in the selected object type and id fot you to know where you are
        b.putLong("Id",currentInsideID);
        intent.putExtras(b);
        startActivity(intent);
        finish();*/
    }
}
