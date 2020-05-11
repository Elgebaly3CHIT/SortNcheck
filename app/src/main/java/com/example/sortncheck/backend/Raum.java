package com.example.sortncheck.backend;

import android.os.Environment;

import java.io.*;
import java.util.*;

/**
 * Ein einzelner Raum wie eine Kueche z.B.
 * Es koennen sich Objekte und Lager hier drinnen befinde.
 * @author Sebastian Zettl
 * @version 2020-25-04
 */
public class Raum {

    private long id;
    private String name;
    private String displayName;
    private Map<Long ,Lagermoeglichkeit> lagermoeglichkeiten = new TreeMap<>();
    private Map<Long ,Objekt> objekte = new TreeMap<>();
    private Hauptmenue menue;

    /**
     * Ein Konstruktor fuer einen Raum
     * @param id Eine eindeutige ID
     * @param name Der volle Name des Raumes
     * @param displayName Ein Kuerzel des Names
     * @param menue Das menue in dem sich der Raum befindet
     */
    public Raum(long id, String name, String displayName, Hauptmenue menue) {
        this.setId(id);
        this.setName(name);
        this.setDisplayName(displayName);
        this.menue = menue;
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
        if (name.length() < 41) {
            this.name = name;
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if (displayName.length() < 7 && !this.raumDisplayNameExists(displayName)) {
            this.displayName = displayName;
        }
    }

    public Map<Long, Lagermoeglichkeit> getLagermoeglichkeiten() {
        return lagermoeglichkeiten;
    }

    public Map<Long, Objekt> getObjekte() {
        return objekte;
    }

    /**
     * Checkt ob der DisplayName schon fuer einen anderen Raum existiert.
     * Ruft die Methode im Meneu auf.
     * @param displayName Der displayName der ueberprueft werden soll
     * @return true, wenn er schon existiert, ansonsten false
     */
    public boolean raumDisplayNameExists(String displayName) {
        return this.menue.raumDisplayNameExists(displayName);
    }

    /**
     * Checkt ob der DisplayName schon fuer ein Lager in diesem Raum existiert
     * @param displayName Der displayName der ueberprueft werden soll
     * @return true, wenn er schon existiert, ansonsten false
     */
    public boolean lagerDisplayNameExists(String displayName) {
        for (Lagermoeglichkeit l: this.lagermoeglichkeiten.values()) {
            if (l.getDisplayName().equals(displayName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checkt ob der DisplayName schon fuer ein Objekt in diesem Raum existiert
     * @param displayName Der displayName der ueberprueft werden soll
     * @return true, wenn er schon existiert, ansonsten false
     */
    public boolean objektDisplayNameExists(String displayName) {
        for (Objekt o: this.objekte.values()) {
            if (o.getDisplayName().equals(displayName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Holt eine freie Id fuer ein Lager.
     * Wird fuer addMMethoden benutzt
     * @return Eine freie id
     */
    public long getFreeLagerID() {
        return menue.getFreeLagerID();
    }

    /**
     * Holt eine freie Id fuer ein Objekt.
     * Wird fuer addMMethoden benutzt
     * @return Eine freie id
     */
    public long getFreeObjektID() {
        return menue.getFreeObjektID();
    }

    /**
     * Fuegt eine temporaer freie Raum Id hinzu.
     * Wird fuer addMMethoden benutzt
     * @param id eine temporaer freie id
     */
    public void addRID(long id) {
        menue.addRID(id);
    }

    /**
     * Fuegt eine temporaer freie Lager Id hinzu
     * @param id eine temporaer freie id
     */
    public void addLID(long id) {
        menue.addLID(id);
    }

    /**
     * Fuegt eine temporaer freie Objekt Id hinzu
     * @param id eine temporaer freie id
     */
    public void addOID(long id) {
        menue.addOID(id);
    }

    /**
     * Holt das Mennue Objekt
     * @return Das Hauptmenue Objekt
     */
    public Hauptmenue getMenue() {
        return this.menue;
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
        this.menue.saveMenue();
        objekte.put(id, o);
    }

    /**
     * Nur fuer load Methode. Fuegt ein bestehendes Objekt zum Raum hinzu
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
        this.saveNewLager(l);
        this.menue.saveMenue();
        lagermoeglichkeiten.put(id, l);
    }

    /**
     * Nur fuer load Methode. Fuegt ein bestehendes Lager zum Raum hinzu
     * @param lager Ein Lager mit einer Id
     */
    public void addLager(Lagermoeglichkeit lager) {
        lagermoeglichkeiten.put(lager.getId(), lager);
    }

    /**
     * Holt das Lager mit der uebergebenen id. Durchsucht auch alle Sub Lager.
     * Falls nichts gefunden wird, wird null zurueckgegeben.
     * @param id Id des Lagers
     * @return Das Lager, falls nichts gefunden wurde, null.
     */
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
     * Speichert ein neues Lager.
     * @param lager Das Lager das gespeichert werden soll.
     */
    public void saveNewLager(Lagermoeglichkeit lager) {
        File container = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Lager.csv");

        try {
            FileWriter fstream = new FileWriter(container, true);
            BufferedWriter out = new BufferedWriter(fstream);
            String s = lager.getId()+";"+lager.getName()+";"+lager.getDisplayName()+";"+lager.getBeschreibung()+";"+lager.getParentRaumID()+";"+lager.getParentLagerID();
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
     * Speichert ein neues Objekt.
     * @param objekt Das Objekt das gespeichert werden soll.
     */
    public void saveNewObjekt(Objekt objekt) {
        File container = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Objekt.csv");

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

    public void editLager(long id, String name, String displayName, String beschreibung) {
        List<String> lines = new ArrayList<String>();
        String line = null;
        try {
            File f1 = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Lager.csv");
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

    public void editObject(long id, String name, String displayName, String beschreibung) {
        List<String> lines = new ArrayList<String>();
        String line = null;
        try {
            File f1 = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Objekt.csv");
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
     * Loescht diesen Raum und alle Werte die sich darin befiden.
     * Fuegt die id des Raumes zu tempId hinzu. Speichert den Stand des Menues.
     */
    public void delete() {
        for (Objekt o: objekte.values()) {
            o.delete();
        }
        objekte = null;
        for (Lagermoeglichkeit l: lagermoeglichkeiten.values()) {
            l.delete();
        }
        this.addRID(id);
        this.getMenue().saveMenue();
        lagermoeglichkeiten = null;
        name = null;
        displayName = null;
        id = -1;
    }
}
