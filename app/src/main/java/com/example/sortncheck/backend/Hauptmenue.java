package com.example.sortncheck.backend;

import android.os.Environment;

import java.io.*;
import java.io.IOException;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.*;
import java.util.Set;
import java.util.TreeMap;

/**
 * Die oberste Schicht. Alles befindet sich hier drinnen.
 * @author Sebastian Zettl
 * @version 2020-25-04
 */
public class Hauptmenue {

    private Map<Long ,Raum> raume; // Map wo die id die auf den Raum verweist
    private long rID; // naechst freie Raum id
    private long lID; // naechst freie Lager id
    private long oID; // naechst freie Objekt id
    private Set<Long> tempRID; // temporaer freie id
    private Set<Long> tempLID; // temporaer freie id
    private Set<Long> tempOID; // temporaer freie id

    /**
     * Erstellt ein neues Hauptmenue mmit allen notwendigen Dateien.
     * Falls einen Dateie schon existiert werden diese geladen.
     */
    public Hauptmenue() {
        if (this.createPath()) {
            raume = new TreeMap<>();
            rID = 0;
            lID = 0;
            oID = 0;
            tempRID = new TreeSet<>();
            tempLID = new TreeSet<>();
            tempOID = new TreeSet<>();
            this.saveMenue();
        }
        else {
            this.load();
        }
    }

    /**
     * Holt eine freie Id fuer einen Raum.
     * Wird fuer addMMethoden benutzt
     * @return Eine freie id
     */
    public long getFreeRaumID() {
        if (tempRID.size() == 0) {
            long id = rID;
            rID++;
            return id;
        }
        else {
            long id = tempRID.iterator().next();
            this.removeRID();
            return id;
        }
    }

    /**
     * Holt eine freie Id fuer ein Lager.
     * Wird fuer addMMethoden benutzt
     * @return Eine freie id
     */
    public long getFreeLagerID() {
        if (tempLID.size() == 0) {
            long id = lID;
            lID++;
            return id;
        }
        else {
            long id = tempLID.iterator().next();
            this.removeLID();
            return id;
        }
    }

    /**
     * Holt eine freie Id fuer ein Objekt.
     * Wird fuer addMMethoden benutzt
     * @return Eine freie id
     */
    public long getFreeObjektID() {
        if (tempOID.size() == 0) {
            long id = oID;
            oID++;
            return id;
        }
        else {
            long id = tempOID.iterator().next();
            this.removeOID();
            return id;
        }
    }

    /**
     * Fuegt eine temporaer freie Raum Id hinzu
     * @param id eine temporaer freie id
     */
    public void addRID(long id) {
        tempRID.add(id);
    }

    /**
     * Fuegt eine temporaer freie Lager Id hinzu
     * @param id eine temporaer freie id
     */
    public void addLID(long id) {
        tempLID.add(id);
    }

    /**
     * Fuegt eine temporaer freie Objekt Id hinzu
     * @param id eine temporaer freie id
     */
    public void addOID(long id) {
        tempOID.add(id);
    }

    /**
     * Loescht die erste temporaer freie Raum id
     */
    public void removeRID() {
        tempRID.remove(tempRID.iterator().next());
    }

    /**
     * Loescht die erste temporaer freie Lager id
     */
    public void removeLID() {
        tempLID.remove(tempLID.iterator().next());
    }

    /**
     * Loescht die erste temporaer freie Objekt id
     */
    public void removeOID() {
        tempOID.remove(tempOID.iterator().next());
    }

    /**
     * Fuegt einen neune Raum mit einer eindeutigen id hinzu.
     * @param name Der volle Name des Raumes
     * @param displayName Kuerzel des namens
     */
    public void addRaum(String name, String displayName) {
        long id = this.getFreeRaumID();
        Raum r = new Raum(id, name, displayName, this);
        this.saveNewRaum(r);
        raume.put(id, r);
    }

    /**
     * Nur fuer load Methode. Fuegt einen bestehenden Raum zum Manue hinzu
     * @param raum Ein Raum mit einer Id
     */
    public void addRaum(Raum raum) {
        raume.put(raum.getId(), raum);
    }

    /**
     * Holt eine freie Id fuer ein Lager
     * @return Eine freie id
     */
    public boolean raumDisplayNameExists(String displayName) {
        for (Raum r: this.raume.values()) {
            if (r.getDisplayName().equals(displayName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sucht alle Objekte die mit dem regex uebereintimmen.
     * @param regex Der String nach dem gesucht werden soll
     * @return Ein Set welches alle uebereinstimmenden Objekte enthaelt
     */
    public Set<Raum> suchenRaum(String regex) {
        Set<Raum> such = new HashSet<>();
        for (Raum r: raume.values()) {
            if (r.getName().indexOf(regex) > 0) {
                such.add(r);
            }
        }
        return such;
    }

    /**
     * Sucht alle Objekte die mit dem regex uebereintimmen
     * @param regex Der String nach dem gesucht werden soll
     * @return Ein Set welches alle uebereinstimmenden Objekte enthaelt
     */
    public Set<Lagermoeglichkeit> suchenLager(String regex) {
        Set<Lagermoeglichkeit> such = new HashSet<>();
        for (Raum r: raume.values()) {
            for (Lagermoeglichkeit l: r.getLagermoeglichkeiten().values()) {
                if (l.getName().indexOf(regex) > 0) {
                    such.add(l);
                }
            }
            for (Lagermoeglichkeit l: r.getLagermoeglichkeiten().values()) {
                for (Objekt o: l.getObjekte().values()) {
                    if (o.getName().indexOf(regex) > 0) {
                        such.add(o);
                    }
                }
            }
        }
        return such;
    }

    /**
     * Sucht alle Objekte die mit dem regex uebereintimmen
     * @param regex Der String nach dem gesucht werden soll
     * @return Ein Set welches alle uebereinstimmenden Objekte enthaelt
     */
    public Set<Objekt> suchenObjekt(String regex) {
        Set<Objekt> such = new HashSet<>();
        for (Raum r: raume.values()) {
            for (Objekt o: r.getObjekte().values()) {
                if (o.getName().indexOf(regex) > 0) {
                    such.add(o);
                }
            }
            for (Lagermoeglichkeit l: r.getLagermoeglichkeiten().values()) {
                for (Objekt o: l.getObjekte().values()) {
                    if (o.getName().indexOf(regex) > 0) {
                        such.add(o);
                    }
                }
            }
        }
        return such;
    }

    /**
     * Holt den Raum der an diser id steht
     * @param id Die id des Raumes
     * @return den Raum, falls er nicht existiert null
     */
    public Raum getRaum(long id) {
        if (this.raume.containsKey(id)) {
            return this.raume.get(id);
        }
        return null;
    }

    /**
     * Holt das Lager das an diser id steht
     * @param id Die id des Lagers
     * @return das Lager, falls es nicht existiert null
     */
    public Lagermoeglichkeit getLager(long id) {
        for (Raum r: this.raume.values()) {
            if (r.getLagermoeglichkeiten().containsKey(id)) {
                return r.getLagermoeglichkeiten().get(id);
            }
        }

        return null;
    }

    /**
     * Wird beim start der App ausgefuehrt. Erzeugt alles aus vier CSV Dateien.
     */
    public void load() {
        File filename = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Menue.csv");

        try {
            Scanner s = new Scanner(new BufferedReader(new FileReader(filename)));
            s.useDelimiter("\n");
            String[] a = s.next().split(";");
            this.rID = Long.parseLong(a[0]);
            this.lID = Long.parseLong(a[1]);
            this.oID = Long.parseLong(a[2]);

            String[] sa = a[3].split(",");
            this.tempRID.clear();
            for (int i = 0; i < sa.length; i++) {
                this.tempRID.add(Long.parseLong(sa[i]));
            }
            sa = a[4].split(",");
            this.tempLID.clear();
            for (int i = 0; i < sa.length; i++) {
                this.tempLID.add(Long.parseLong(sa[i]));
            }
            sa = a[5].split(",");
            this.tempOID.clear();
            for (int i = 0; i < sa.length; i++) {
                this.tempOID.add(Long.parseLong(sa[i]));
            }
            s.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        filename = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Raum.csv");

        try {
            Scanner s = new Scanner(new BufferedReader(new FileReader(filename)));
            s.useDelimiter("\n");
            while(s.hasNext()) {
                String[] a = s.next().split(";");
                Raum r = new Raum(Long.parseLong(a[0]), a[1], a[2], this);
                this.addRaum(r);
            }
            s.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        filename = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Lager.csv");

        try {
            Scanner s = new Scanner(new BufferedReader(new FileReader(filename)));
            s.useDelimiter("\n");
            List<String[]> la = new LinkedList<>();
            while(s.hasNext()) {
                String[] a = s.next().split(";");
                Lagermoeglichkeit l;
                if (a[5] != "-1") {
                    l = new Lagermoeglichkeit(Long.parseLong(a[0]), a[1], a[2], a[3], this.getRaum(Long.parseLong(a[5])));
                    this.getRaum(Long.parseLong(a[5])).addLager(l);
                }
                else {
                    la.add(a);
                }
            }
            s.close();
            for (int i = 0; i < la.size(); i++) {
                String[] a = la.get(i);
                Lagermoeglichkeit l;
                if (this.getLager(Long.parseLong(a[4])) != null) {
                    l = new Lagermoeglichkeit(Long.parseLong(a[0]), a[1], a[2], a[3], this.getLager(Long.parseLong(a[4])));
                    this.getLager(Long.parseLong(a[5])).addLager(l);
                }
                else {
                    la.add(a);
                    la.remove(i);
                    i--;
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        filename = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Objekt.csv");

        try {
            Scanner s = new Scanner(new BufferedReader(new FileReader(filename)));
            s.useDelimiter("\n");
            List<String[]> ob = new LinkedList<>();
            while(s.hasNext()) {
                String[] a = s.next().split(";");
                Objekt o;
                if (a[5] != "-1") {
                    o = new Objekt(Long.parseLong(a[0]), a[1], a[2], a[3], this.getRaum(Long.parseLong(a[5])));
                    this.getRaum(Long.parseLong(a[5])).addObjekt(o);
                }
                else {
                    ob.add(a);
                }
            }
            s.close();
            for (int i = 0; i < ob.size(); i++) {
                String[] a = ob.get(i);
                Objekt o;
                if (this.getLager(Long.parseLong(a[4])) != null) {
                    o = new Objekt(Long.parseLong(a[0]), a[1], a[2], a[3], this.getLager(Long.parseLong(a[4])));
                    this.getLager(Long.parseLong(a[5])).addObjekt(o);
                }
                else {
                    ob.add(a);
                    ob.remove(i);
                    i--;
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Erzeugt beim ersten aufruf der App alle notwendigen Dateien.
     * @return True wenn die Dateien erstellt wurden, false wenn sie schon existieren.
     */
    public boolean createPath() {
        File f = new File(Environment.getExternalStorageDirectory(), "SortNCheck");
        if (f.isDirectory()) {
            return false;
        }
        f.mkdir();
        File menue = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Menue.csv");
        File room = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Raum.csv");
        File container = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Lager.csv");
        File object = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Objekt.csv");
        try {
            menue.createNewFile();
            room.createNewFile();
            container.createNewFile();
            object.createNewFile();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * Editiert einen bestehenden Raum, mit den uebergebennén Parametern.
     * @param id Id des Raumes der editiert wird, kann nicht editiert werden.
     * @param name Name des Raumes
     * @param displayName DisplayName des Raumes
     */
    public void editRaum(long id, String name, String displayName) {
        List<String> lines = new ArrayList<String>();
        String line = null;
        try {
            File f1 = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Raum.csv");
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                String[] a = line.split(";");
                if (Long.parseLong(a[0]) == id) {
                    a[1] = name;
                    a[2] = displayName;
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
     * Speichert den derzeitigen Stand des Menues.
     */
    public void saveMenue() {
        File container = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Menue.csv");

        try {
            FileWriter fstream = new FileWriter(container);
            BufferedWriter out = new BufferedWriter(fstream);
            String s = this.rID+";"+this.lID+";"+this.oID;

            StringBuilder sb = new StringBuilder();
            for (Iterator i = tempRID.iterator(); i.hasNext();) {
                sb.append(i.next());
                if (i.hasNext()) {
                    sb.append(",");
                }
            }

            s += ";"+sb.toString();

            sb = new StringBuilder();
            for (Iterator i = tempLID.iterator(); i.hasNext();) {
                sb.append(i.next());
                if (i.hasNext()) {
                    sb.append(",");
                }
            }

            s += ";"+sb.toString();

            sb = new StringBuilder();
            for (Iterator i = tempOID.iterator(); i.hasNext();) {
                sb.append(i.next());
                if (i.hasNext()) {
                    sb.append(",");
                }
            }
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
     * Speichert einen neuen Raum.
     * @param raum Der Raum der gespeichert werden soll.
     */
    public void saveNewRaum(Raum raum) {
        File container = new File(Environment.getExternalStorageDirectory(), "SortNCheck/Raum.csv");

        try {
            FileWriter fstream = new FileWriter(container, true);
            BufferedWriter out = new BufferedWriter(fstream);
            String s = raum.getId()+";"+raum.getName()+";"+raum.getDisplayName();
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
}
