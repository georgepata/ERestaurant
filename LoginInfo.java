package proiect;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class LoginInfo {
    private static String ID;
    private static String PASSWORD;
    private ArrayList<String[]> chelneri;
    private BufferedReader reader;
    LoginInfo(){
        try{
            reader=new BufferedReader(new FileReader("AdminData.txt"));
            ID=reader.readLine();
            PASSWORD=reader.readLine();
            reader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        chelneri=new ArrayList<>();
    }
    public String getId(){
        return ID;
    }
    public String getPASSWORD() {
        return PASSWORD;
    }
    public ArrayList<String[]> getChelneri(){
        return chelneri;
    }
    public void addToChelneri(String[] array){
        chelneri.add(array);
    }
    public void cautareChelner(int number){
        int cnt=0;
        if (number==0 || number > chelneri.size()) {
            JOptionPane.showMessageDialog(null, "Introduceti un ID corespunzator!");
        } else {
            chelneri.remove(number - 1);
            JOptionPane.showMessageDialog(null,"Angajatul a fost sters!");
        }
    }
    public void modifyChelner(String[] array, int number){
        if (number==0 || number > chelneri.size()) {
            JOptionPane.showMessageDialog(null, "Introduceti un ID corespunzator!");
        }
        else {
            String[] retrievedArray = chelneri.get(number - 1);
            for (int i = 1; i < retrievedArray.length; i++)
                retrievedArray[i] = array[i - 1];
            displayChelner();
            JOptionPane.showMessageDialog(null, "Datele angajatului au fost editate!");
        }
    }
    public void displayChelner(){
        for (String[] z : chelneri) {
            for (String nm : z) {
                System.out.print(nm+" ");
            }
            System.out.println();
        }
    }
}
