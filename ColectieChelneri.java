package proiect;

import javax.swing.*;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ColectieChelneri {
    private static ColectieChelneri instance;
    private LinkedHashMap<ArrayList<String>, Integer> chelneri;
    private ArrayList<String> helper;
    private String line;
    private int k, cnt, number, value=0;
    private ArrayList<ArrayList<String>> comenzi;
    private double pretFinal;

    private File file;
    private FileWriter fileWriter;

    private BufferedReader bf;

    private ColectieChelneri() {
        comenzi=new ArrayList<>();
        chelneri = new LinkedHashMap<>();
        helper = new ArrayList<>();
        try {
            bf = new BufferedReader(new FileReader("Angajati.txt"));
            int k = 1, cnt = 0;
            while ((line = bf.readLine()) != null) {
                if (cnt == 3 && helper.get(2).equals("Chelner")) {
                    helper.add(0, Integer.toString(k));
                    chelneri.put(new ArrayList<>(helper), 0);
                    cnt = 0;
                    k++;
                    helper.clear();
                } else if (cnt == 3 && helper.get(2).equals("Admin")) {
                    helper.clear();
                    cnt = 0;
                }
                helper.add(line);
                cnt++;
            }
            if (cnt == 3 && helper.get(2).equals("Chelner")) {
                helper.add(0, Integer.toString(k));
                chelneri.put(new ArrayList<>(helper), 0);
                cnt = 0;
                k++;
                helper.clear();
            }
            bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void mutareComanda(String ID, String masa) throws IOException {
        file=new File("Comenzi.txt");
        fileWriter=new FileWriter(file, true);
        fileWriter.write(masa);
        fileWriter.write("\n");
        for (int i=0; i<comenzi.size(); i++){
            ArrayList<String> through = comenzi.get(i);
            if (through.get(0).equals(ID)) {
                for (int j = 1; j < through.size(); j++) {
                    fileWriter.write(through.get(j));
                    fileWriter.write(" ");
                }
                fileWriter.write("\n");
                break;
            }
        }
        fileWriter.close();
    }

    public void stergereProduse(String ID) throws IOException {
        int cnt=1;
        StringBuffer bf=new StringBuffer();
        for (int i=0; i<comenzi.size(); i++){
            ArrayList<String> helper=comenzi.get(i);
            if (helper.get(0).equals(ID)) {
                for (int j = 1; j < helper.size(); j+=3){
                    bf.append(cnt+".");
                    bf.append(helper.get(j));
                    bf.append("\n");
                    cnt++;
                }
                String result = JOptionPane.showInputDialog(null, bf.toString(), "Stergere produse", JOptionPane.QUESTION_MESSAGE);
                String[] produseSterse = result.split(" ");
                cnt=1;
                int h=0;
                for (int j=1; j<helper.size(); j+=3){
                    if (Integer.parseInt(produseSterse[h])==cnt){
                        helper.remove(j);
                        helper.remove(j);
                        helper.remove(j);
                        h++;
                    }
                    cnt++;
                }
                file=new File("Comenzi.txt");
                fileWriter=new FileWriter(file);
                for (i=0; i<comenzi.size(); i++) {
                    helper = comenzi.get(i);
                    fileWriter.write(helper.get(0));
                    fileWriter.write("\n");
                    for (int j=1; j<helper.size(); j++){
                        fileWriter.write(helper.get(j));
                        fileWriter.write(" ");
                    }
                    fileWriter.write("\n");
                }
                fileWriter.close();
                break;
            }
        }
    }

    public void citireComanda() throws IOException {
        comenzi.clear();
        int cnt=1;
        String idMasa=null;
        bf=new BufferedReader(new FileReader("Comenzi.txt"));
        while ((line= bf.readLine())!=null){
            if (cnt==1)
                idMasa=line;
            else {
                String[] elements = line.split(" ");
                ArrayList<String> row = new ArrayList<>();
                for (String element : elements) {
                    row.add(element);
                }
                row.add(0,idMasa);
                comenzi.add(row);
                cnt=0;
            }
            cnt++;
        }
        bf.close();
    }

    public void achitareComanda(String ID) throws IOException {
        file=new File("Comenzi.txt");
        fileWriter=new FileWriter(file);
        for (int i=0; i<comenzi.size(); i++){
            ArrayList<String> helper=comenzi.get(i);
            if (helper.get(0).equals(ID)==false) {
                for (int j = 0; j < helper.size(); j++) {
                    if (j == 0) {
                        fileWriter.write(helper.get(j));
                        fileWriter.write("\n");
                    } else {
                        fileWriter.write(helper.get(j));
                        fileWriter.write(" ");
                    }
                }
                fileWriter.write("\n");
            } else if (helper.get(0).equals(ID)==true){
                pretFinal = 0;
                StringBuilder sb=new StringBuilder();
                for (int j=1; j<helper.size(); j++)
                    if (j%3==0){
                        pretFinal+=Double.parseDouble(helper.get(j));
                        sb.append("\t");
                        sb.append(helper.get(j));
                        sb.append("\n");
                    } else {
                        sb.append(helper.get(j));
                        sb.append("\t");
                        if (j%2==0)
                            sb.append("x");
                    }

                JOptionPane.showMessageDialog(null, sb.toString() + "\nTOTAL LEI \t" + pretFinal, "Bon Fiscal", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        fileWriter.close();
    }

    public static ColectieChelneri getInstance() {
        if (instance==null)
            instance=new ColectieChelneri();
        return instance;
    }

    public void findWord(String ID, boolean state){
        for (Map.Entry<ArrayList<String>, Integer> entry : chelneri.entrySet()) {
            ArrayList<String> key = entry.getKey();
            if (key.contains(ID)) {
                if (entry.getValue() == 3 && state == false)
                    JOptionPane.showMessageDialog(null, "Chelnerul este full!");
                if (entry.getValue() > 3 && state==false) {
                    JOptionPane.showMessageDialog(null, "Chelnerul este full!");
                    value=4;
                    entry.setValue(4);
                }
                else if (entry.getValue() == 4 && state == true){
                    value = entry.getValue() -2;
                    entry.setValue(value);
                } else {
                    if (state == false) {
                        value = entry.getValue() + 1;
                        entry.setValue(value);
                    } else if (state== true){
                        value = entry.getValue() -1;
                        entry.setValue(value);
                    }
                }
                number=value;
                break;
            }
        }
    }

    public int getNUMBER(){
        return number;
    }


    public ArrayList<ArrayList<String>> getComenzi(){
        return comenzi;
    }

    public int getSize(){
        int size = chelneri.size();
        return size;
    }
}

