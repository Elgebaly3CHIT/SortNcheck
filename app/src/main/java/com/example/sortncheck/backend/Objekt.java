package com.example.sortncheck.backend;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Ein einzelnes Objekt wie ein Stift z.B.
 * @author Sebastian Zettl
 * @version 2020-25-04
 */
public class Objekt implements Parcelable {

    private long id; // eindeuitige id des Objektes
    private String name;
    private String displayName;
    private String beschreibung;
    private Raum raum; // Raum in dem sich das Objekt befindet, null wenn es in einem lager ist
    private Lagermoeglichkeit lager; // Lager in dem sich das Objekt befindet, null wenn es in einem Raum ist

    /**
     * Ein Konstruktor fuer ein Objekz wenn es sich in einem Lager befindet
     * @param id Eine eindeutige ID
     * @param name Der volle Name des Lagers
     * @param displayName Ein Kuerzel des Names
     * @param beschreibung Eine Becshreibung des Lagers
     * @param lager Das lager in dem sich dieses Objekt befindet
     */
    public Objekt(long id, String name, String displayName, String beschreibung, Lagermoeglichkeit lager) {
        this.setId(id);
        this.setName(name);
        this.setDisplayName(displayName);
        this.setBeschreibung(beschreibung);
        this.setLager(lager);
    }

    /**
     * Ein Konstruktor fuer ein Objekz wenn es sich in einem Raum befindet
     * @param id Eine eindeutige ID
     * @param name Der volle Name des Lagers
     * @param displayName Ein Kuerzel des Names
     * @param beschreibung Eine Becshreibung des Lagers
     * @param raum Der raum in dem sich dieses Objekt befindet
     */
    public Objekt(long id, String name, String displayName, String beschreibung, Raum raum) {
        this.setId(id);
        this.setName(name);
        this.setDisplayName(displayName);
        this.setBeschreibung(beschreibung);
        this.setRaum(raum);
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
            throw new IllegalArgumentException("Name ist zu lang oder null");
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
        if (displayName.length() < 7 && !this.objektDisplayNameExists(displayName)) {
            this.displayName = displayName;
        }
        else {
            throw new IllegalArgumentException("Display Name ist zu lang, existiert schon oder ist null");
        }
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

    /**
     * Checkt ob der DisplayName im Lager oder Raum schon existiert.
     * @param displayName Der DisplayName der gecheckt werden soll
     * @return true wenn er existiert, ansosnten false.
     */
    public boolean objektDisplayNameExists(String displayName) {
        if (displayName == null) {
            return true;
        }
        if (this.raum != null) {
            return this.raum.objektDisplayNameExists(displayName);
        }
        else {
            return this.lager.objektDisplayNameExists(displayName);
        }
    }

    /**
     * Holt die Id des Raumes in dem sich das Objekt befidnet
     * @return die Id des Raumes in dem sich as Objekt  befindet
     */
    public long getParentRaumID() {
        if (this.raum == null) {
            return -1;
        }
        else {
            return this.raum.getId();
        }
    }

    /**
     * Holt die Id des Lagers in dem sich das Objekt befidnet
     * @return die Id des Lagers in dem sich as Objekt  befindet
     */
    public long getParentLagerID() {
        if (this.lager == null) {
            return -1;
        }
        else {
            return this.lager.getId();
        }
    }

    /**
     * Holt das Hauptmenue Objekt.
     * @return Das Hauptmenue Objekt
     */
    public Hauptmenue getMenue() {
        if (this.raum != null) {
            return this.raum.getMenue();
        }
        else {
            return this.lager.getMenue();
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

    /**
     * Loescht dieses Objekt und alle Werte die sich darin befiden.
     * Fuegt die id des Objektes zu tempId hinzu. Speichert den Stand des Menues.
     */
    public void delete() {
        this.addOID(id);
        this.getMenue().saveMenue();
        name = null;
        displayName = null;
        id = -1;
        beschreibung = null;
        lager = null;
        raum = null;
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
    public static final Parcelable.Creator<Objekt> CREATOR = new Parcelable.Creator<Objekt>() {
        public Objekt createFromParcel(Parcel in) {
            return new Objekt(in);
        }

        public Objekt[] newArray(int size) {
            return new Objekt[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Objekt(Parcel in) {
        mData = in.readInt();
    }
}
