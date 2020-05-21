package com.example.sortncheck.backend;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Ein einzelnes Lager wie ein Schrank z.B.
 * Es koennen sich Objekte und weitere Lager hier drinnen befinde.
 * @author Sebastian Zettl
 * @version 2020-25-04
 */
public class Lagermoeglichkeit implements Parcelable {

    private long id;
    private String name;
    private String displayName;
    private String beschreibung;
    private Map<Long ,Objekt> objekte = new TreeMap<>();
    private Map<Long ,Lagermoeglichkeit> lagermoeglichkeiten = new TreeMap<>();
    private Lagermoeglichkeit lager;
    private Raum raum;

    /**
     * Ein Konstruktor fuer ein Lager wenn es isch in einem weiteren Lager befindet
     * @param id Eine eindeutige ID
     * @param name Der volle Name des Lagers
     * @param displayName Ein Kuerzel des Names
     * @param beschreibung Eine Becshreibung des Lagers
     * @param lager Das lager in dem sich dieses Lager befindet
     */
    public Lagermoeglichkeit(long id, String name, String displayName, String beschreibung, Lagermoeglichkeit lager) {
        this.setLager(lager);
        this.setId(id);
        this.setName(name);
        this.setDisplayName(displayName);
        this.setBeschreibung(beschreibung);

    }

    /**
     * Ein Konstruktor fuer ein Lager wenn es sich in einem Raum befindet
     * @param id Eine eindeutige ID
     * @param name Der volle Name des Lagers
     * @param displayName Ein Kuerzel des Names
     * @param beschreibung Eine Becshreibung des Lagers
     * @param raum Der raum in dem sich dieses Lager befindet
     */
    public Lagermoeglichkeit(long id, String name, String displayName, String beschreibung, Raum raum) {
        this.setRaum(raum);
        this.setId(id);
        this.setName(name);
        this.setDisplayName(displayName);
        this.setBeschreibung(beschreibung);

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.length() < 41) {
            this.name = name;
        }
        else {
            throw new IllegalArgumentException("Name ist zu lang");
        }
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        if (beschreibung != null && beschreibung.length() < 501) {
            this.beschreibung = beschreibung;
        } else {
            throw new IllegalArgumentException("Beschreibung ist zu lang oder null");
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if (displayName.length() < 11 && !this.lagerDisplayNameExists(displayName)) {
            this.displayName = displayName;
        }
        else {
            throw new IllegalArgumentException("Display Name ist zu lang, existiert schon oder ist null");
        }
    }

    /**
     * Get all Items in this Lagermöglichkeit
     * @return all Items
     */
    public Map<Long, Objekt> getObjekte() {
        return objekte;
    }

    /**
     * get all Lagermoglichkeiten in this Lagermoglichkeit
     * @return all Lagermoglichkeiten
     */
    public Map<Long, Lagermoeglichkeit> getLagermoeglichkeiten() {
        return lagermoeglichkeiten;
    }

    public Raum getRaum() {
        return raum;
    }

    public void setRaum(Raum raum) {
        this.raum = raum;
    }

    public Lagermoeglichkeit getLager() {
        return lager;
    }

    public void setLager(Lagermoeglichkeit lager) {
        this.lager = lager;
    }

    public boolean lagerDisplayNameCheck(String displayName) {
        for (Lagermoeglichkeit l: this.lagermoeglichkeiten.values()) {
            if (l.getDisplayName().equals(displayName)) {
                return true;
            }
        }
        return false;
    }

    public boolean lagerDisplayNameExists(String displayName) {
        if (displayName == null) {
            return true;
        }
        if (this.raum != null) {
            return this.raum.lagerDisplayNameExists(displayName);
        }
        else {
            return this.lager.lagerDisplayNameCheck(displayName);
        }
    }

    public boolean objektDisplayNameExists(String displayName) {
        for (Objekt o: this.objekte.values()) {
            if (o.getDisplayName().equals(displayName)) {
                return true;
            }
        }
        return false;
    }

    public long getParentRaumID() {
        if (this.raum == null) {
            return -1;
        }
        else {
            return this.raum.getId();
        }
    }

    public long getParentLagerID() {
        if (this.lager == null) {
            return -1;
        }
        else {
            return this.lager.getId();
        }
    }

    public Hauptmenue getMenue() {
        if (this.raum != null) {
            return this.raum.getMenue();
        }
        else {
            return this.lager.getMenue();
        }
    }

    /**
     * Holt eine freie Id fuer ein Lager.
     * Wird fuer addMMethoden benutzt
     * @return Eine freie id
     */
    public long getFreeLagerID() {
        if (raum != null) {
            return raum.getFreeLagerID();
        }
        else {
            return lager.getFreeLagerID();
        }
    }

    /**
     * Holt eine freie Id fuer ein Objekt.
     * Wird fuer addMMethoden benutzt
     * @return Eine freie id
     */
    public long getFreeObjektID() {
        if (raum != null) {
            return raum.getFreeObjektID();
        }
        else {
            return lager.getFreeObjektID();
        }
    }

    /**
     * Fuegt eine temporaer freie Lager Id hinzu
     * @param id eine temporaer freie id
     */
    public void addLID(long id) {
        if (raum == null) {
            lager.addLID(id);
        }
        else {
            raum.addLID(id);
        }
    }

    /**
     * Fuegt eine temporaer freie Objekt Id hinzu
     * @param id eine temporaer freie id
     */
    public void addOID(long id) {
        if (raum == null) {
            lager.addOID(id);
        }
        else {
            raum.addOID(id);
        }
    }

    public Object getParentType() {
        if (this.raum != null) {
            return this.raum.getClass();
        }
        else {
            return this.lager.getClass();
        }
    }

    public long getParentId() {
        if (this.raum != null) {
            return this.raum.getId();
        }
        else {
            return this.lager.getId();
        }
    }

    /**
     * Fuegt ein neues Objekt mit generirter id in das Lager ein
     * @param name Name des Objektes
     * @param displayName Kuerzel des Names
     * @param beschreibung Eine Beschreibung des Objektes
     */
    public void addObjekt(String name, String displayName, String beschreibung) {
        long id = this.getFreeObjektID();
        Objekt o = new Objekt(id, name, displayName, beschreibung, this);
        this.saveNewObjekt(o);
        this.getMenue().saveMenue();
        objekte.put(id, o);
    }

    /**
     * Nur fuer load Methode. Fuegt ein bestehendes Objekt zum Lager hinzu
     * @param objekt Ein Objekt mit einer Id
     */
    public void addObjekt(Objekt objekt) {
        objekte.put(objekt.getId(), objekt);
    }

    /**
     * Fuegt ein neues Lager mit generirter id in das Lager ein
     * @param name Name des Lagers
     * @param displayName Kuerzel des Names
     * @param beschreibung Eine Beschreibung des Lagers
     */
    public void addLager(String name, String displayName, String beschreibung) {
        long id = this.getFreeLagerID();
        Lagermoeglichkeit l = new Lagermoeglichkeit(id, name, displayName, beschreibung, this);
       // Log.i("Lagername",l.getDisplayName());
        this.saveNewLager(l);
        this.getMenue().saveMenue();
        lagermoeglichkeiten.put(id, l);
    }

    /**
     * Nur fuer load Methode. Fuegt ein bestehendes Lager zum Raum hinzu
     * @param lager Ein Lager mit einer Id
     */
    public void addLager(Lagermoeglichkeit lager) {
        lagermoeglichkeiten.put(lager.getId(), lager);
    }

    public Lagermoeglichkeit getLager(long id) {
        if (this.lagermoeglichkeiten.containsKey(id)) {
            return this.lagermoeglichkeiten.get(id);
        }
        for (Lagermoeglichkeit l: this.lagermoeglichkeiten.values()) {
            Lagermoeglichkeit l2 = l.getLager(id);
            if (l2 != null) {
                return l2;
            }
        }
        return null;
    }

    /**
     * Speichert ein komplett neues Lager im CSV File ab
     * @param lager Das Lager das gespeichert werden soll
     */
    public void saveNewLager(Lagermoeglichkeit lager) {
        Log.i("SAVE","SAVE MEEEEEEEE");
        File container = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SortNCheck/Lager.csv");

        try {
            FileWriter fstream = new FileWriter(container, true);
            BufferedWriter out = new BufferedWriter(fstream);
            String s = lager.getId()+";"+lager.getName()+";"+lager.getDisplayName()+";"+lager.getBeschreibung()+";"+lager.getParentRaumID()+";"+lager.getParentLagerID();

            Log.i("Object",s);
            out.write(s);
            out.newLine();

            //close buffer writer
            out.flush();
            out.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Speichert ein komplett neues Objekt im CSV File ab
     * @param objekt Das Objket das gespeichert werden soll
     */
    public void saveNewObjekt(Objekt objekt) {
        File container = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SortNCheck/Objekt.csv");

        try {
            FileWriter fstream = new FileWriter(container, true);
            BufferedWriter out = new BufferedWriter(fstream);
            String s = objekt.getId()+";"+objekt.getName()+";"+objekt.getDisplayName()+";"+objekt.getBeschreibung()+";"+objekt.getParentRaumID()+";"+objekt.getParentLagerID();
            out.write(s);
            out.newLine();

            //close buffer writer
            out.flush();
            out.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Editiert ein bestehendes Lager, mit den uebergebennén Parametern.
     * @param id Die id des Lagers. Kann nicht editiert werden.
     * @param name Name des Lagers
     * @param displayName DisplayName des Lagers
     * @param beschreibung Becshreibung des Lagers
     */
    public void editLager(long id, String name, String displayName, String beschreibung) {
        List<String> lines = new ArrayList<String>();
        String line = null;
        try {
            File f1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SortNCheck/Lager.csv");
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                String[] a = line.split(";");
                if (Long.parseLong(a[0]) == id) {
                    a[1] = name;
                    a[2] = displayName;
                    a[3] = beschreibung;
                    line = "";
                    for (int i = 0; i < a.length; i++) {
                        line += a[i];
                        if (i+1 != a.length) {
                            line += ";";
                        }
                    }
                }

                lines.add(line);
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for(String s : lines) {
                out.write(s);
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Editiert ein bestehendes Objekt, mit den uebergebennén Parametern.
     * @param id Die id des Lagers. Kann nicht editiert werden.
     * @param name Name des Objekts
     * @param displayName DisplayName des Objektes
     * @param beschreibung Becshreibung des Objektes
     */
    public void editObject(long id, String name, String displayName, String beschreibung) {
        List<String> lines = new ArrayList<String>();
        String line = null;
        try {
            File f1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SortNCheck/Objekt.csv");
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                String[] a = line.split(";");
                if (Long.parseLong(a[0]) == id) {
                    a[1] = name;
                    a[2] = displayName;
                    a[3] = beschreibung;
                    line = "";
                    for (int i = 0; i < a.length; i++) {
                        line += a[i];
                        if (i+1 != a.length) {
                            line += ";";
                        }
                    }
                }

                lines.add(line);
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for(String s : lines) {
                out.write(s);
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Set<Lagermoeglichkeit> suchenLager(String regex) {
        Set<Lagermoeglichkeit> lager = new TreeSet<>();
        for(Lagermoeglichkeit l: this.lagermoeglichkeiten.values()) {
            if (l.getLager()==null) {
                if(l.getName().indexOf(regex) > 0) {
                    lager.add(l);
                }
            }
            else {
                lager.addAll(l.suchenLager(regex));
            }
        }
        return lager;
    }

    public Set<Objekt> suchenObjekt(String regex) {
        Set<Objekt> lager = new TreeSet<>();
        for (Objekt o: this.objekte.values()) {
            if(o.getName().indexOf(regex) > 0) {
                lager.add(o);
            }
        }
        for(Lagermoeglichkeit l: this.lagermoeglichkeiten.values()) {
            if (l.getLager()!=null) {
                if(l.getName().indexOf(regex) > 0) {
                    lager.addAll(l.suchenObjekt(regex));
                }
            }
        }
        return lager;
    }

    public void deleteLager(long id) {
        this.lagermoeglichkeiten.remove(id);
    }

    public void deleteObject(long id) {
        this.objekte.remove(id);
    }

    /**
     * Loescht dieses Lager und alle Werte die sich darin befiden.
     * Fuegt die id des Lagers zu tempId hinzu. Speichert den Stand des Menues.
     */
    public void delete() {
        for (Objekt o: objekte.values()) {
            o.delete();
        }
        objekte = null;
        for (Lagermoeglichkeit l: lagermoeglichkeiten.values()) {
            l.delete();
        }
        this.addLID(id);
        this.getMenue().saveMenue();
        List<String> lines = new ArrayList<String>();
        String line = null;
        try {
            File f1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SortNCheck/Lager.csv");
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                String[] a = line.split(";");
                if (Long.parseLong(a[0]) != id) {
                    lines.add(line);
                }
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for(String s : lines) {
                out.write(s);
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (raum != null) {
            raum.deleteLager(id);
        }
        else {
            lager.deleteLager(id);
        }
        lagermoeglichkeiten = null;
        name = null;
        displayName = null;
        id = -1;
        beschreibung = null;
        lager = null;
        raum = null;
        System.gc();
    }
    private int mData;

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Lagermoeglichkeit> CREATOR = new Parcelable.Creator<Lagermoeglichkeit>() {
        public Lagermoeglichkeit createFromParcel(Parcel in) {
            return new Lagermoeglichkeit(in);
        }

        public Lagermoeglichkeit[] newArray(int size) {
            return new Lagermoeglichkeit[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Lagermoeglichkeit(Parcel in) {
        mData = in.readInt();
    }
}
