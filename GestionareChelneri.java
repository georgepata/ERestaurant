package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GestionareChelneri extends JPanel {
    private static int SCREEN_WIDTH = 800;
    private static int SCREEN_HEIGHT = 600;
    private int cnt, index;

    private LoginInfo admin;

    private String pathFile, starePanel;

    private BorderLayout borderLayout;
    private CardLayout cardLayout;
    private GridBagLayout gridBagLayout;
    GridBagConstraints gbc = new GridBagConstraints();

    private JLabel idLabel;
    private JTextField idTextField;
    private JLabel titleLabel;
    private JLabel numeLabel;
    private JLabel prenumeLabel;
    private JLabel functieLabel;
    private JTextField numeTextField;
    private JTextField prenumeTextField;
    private JTextField functieTextField;

    private JButton backButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton saveButton;

    private JTable table;
    private JScrollPane jScrollPane;

    private JPanel northPanel;
    private JPanel westPanel;
    private JPanel southPanel;
    private JPanel eastPanel;

    private File file;
    private FileWriter fileWriter;

    public GestionareChelneri() {
        admin = new LoginInfo();
        gridBagLayout = new GridBagLayout();
        borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        northPanel = new JPanel();
        northPanel.setLayout(gridBagLayout);
        titleLabel = new JLabel("Gestionati angajatii");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(40, 0, 0, 0);
        northPanel.add(titleLabel, gbc);

        backButton = new JButton("Back");
        addButton=new JButton("Add");
        editButton=new JButton("Edit");
        deleteButton=new JButton("Delete");
        saveButton=new JButton("Save");

        southPanel = new JPanel();
        southPanel.setLayout(gridBagLayout);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 20,20, 0);
        southPanel.add(backButton, gbc);

        westPanel = new JPanel();
        westPanel.setLayout(gridBagLayout);
        table = new Tabel();
        verificareFisier();

        eastPanel=new JPanel();
        eastPanel.setLayout(gridBagLayout);
        numeLabel=new JLabel("Nume:");
        prenumeLabel=new JLabel("Prenume:");
        functieLabel=new JLabel("Functie:");
        numeTextField=new JTextField(15);
        prenumeTextField=new JTextField(15);
        functieTextField=new JTextField(15);


        backButton.addActionListener(new buttonImplement());
        addButton.addActionListener(new buttonImplement());
        editButton.addActionListener(new buttonImplement());
        deleteButton.addActionListener(new buttonImplement());
        saveButton.addActionListener(new buttonImplement());

        this.add(northPanel, BorderLayout.NORTH);
        this.add(westPanel, BorderLayout.WEST);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(eastPanel, BorderLayout.EAST);
    }

    public int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public void verificareFisier() {
        if (new File("Angajati.txt").exists()) {
            System.out.println("File found: " + (new File("Angajati.txt").getAbsolutePath()));
            pathFile = (new File("Angajati.txt").getAbsolutePath());
        } else {
            try {
                file = new File("Angajati.txt");
                fileWriter = new FileWriter(file);
                System.out.println("New file is ready to set: " + file.getAbsolutePath());
                pathFile = file.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            displayTable();
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void displayTable() throws FileNotFoundException {
        table=new Tabel();
        ArrayList<String> array = new ArrayList<>();
        File file = new File(pathFile);
        Scanner scanner = new Scanner(file);
        ((Tabel) table).addColumn("ID");
        ((Tabel) table).addColumn("Nume");
        ((Tabel) table).addColumn("Prenume");
        ((Tabel) table).addColumn("Functie");
        cnt = 1;
        index = 1;
        if (file.length() > 0) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                array.add(line);
                cnt++;
                if (cnt == table.getColumnCount()) {
                    String[] newRow = new String[table.getColumnCount()];
                    newRow[0] = Integer.toString(index);
                    for (int i = 1; i < table.getColumnCount(); i++)
                        newRow[i] = array.get(i - 1);
                    ((Tabel) table).addRow(newRow);
                    admin.addToChelneri(newRow);
                    cnt = 1;
                    array.clear();
                    index++;
                }
            }
        }
        array.clear();
        index--;
        jScrollPane=new JScrollPane(table);
        gbc=new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        table.setPreferredScrollableViewportSize(new Dimension(300, 250));
        westPanel.removeAll();
        westPanel.add(jScrollPane, gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=1;
        westPanel.add(addButton, gbc);

        gbc.gridx=1;
        gbc.gridy=1;
        gbc.gridwidth=1;
        westPanel.add(deleteButton, gbc);

        gbc.gridx=2;
        gbc.gridy=1;
        gbc.gridwidth=1;
        westPanel.add(editButton, gbc);
        this.revalidate();
        this.repaint();
    }


    private class buttonImplement implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == backButton) {
                cardLayout = (CardLayout) getParent().getLayout();
                cardLayout.show(ERestaurant.getInstance().getContentPane(), "Staff");
                ERestaurant.getInstance().setSize(new Dimension(ERestaurant.getInstance().getSCREEN_WIDTH(), ERestaurant.getInstance().getSCREEN_HEIGHT()));
                ERestaurant.getInstance().setLocationRelativeTo(null);
                eastPanel.removeAll();
            } else if (e.getSource() == addButton){
                eastPanel.removeAll();
                addDesign();
                starePanel=addButton.getText();
            } else if (e.getSource() == deleteButton){
                eastPanel.removeAll();
                deleteDesign();
                starePanel=deleteButton.getText();
            } else if (e.getSource() == editButton){
                eastPanel.removeAll();
                editDesign();
                starePanel=editButton.getText();
            } else if (e.getSource() == saveButton){
                if (starePanel.equals("Add")) {
                    if (numeTextField.getText().isEmpty() || prenumeTextField.getText().isEmpty() || functieTextField.getText().isEmpty())
                        JOptionPane.showMessageDialog(null, "Introduceti corespunzator datele");
                    else if (starePanel.equals("Add")) {
                        try {
                            fileWriter = new FileWriter(pathFile, true);
                            fileWriter.write(numeTextField.getText());
                            fileWriter.write("\n");
                            fileWriter.write(prenumeTextField.getText());
                            fileWriter.write("\n");
                            fileWriter.write(functieTextField.getText());
                            fileWriter.write("\n");
                            fileWriter.close();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(null, "Angajatul a fost adaugat!");
                        eastPanel.removeAll();
                        westPanel.removeAll();
                        admin.getChelneri().removeAll(admin.getChelneri());
                        try {
                            displayTable();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                    numeTextField.setText("");
                    prenumeTextField.setText("");
                    functieTextField.setText("");
                } else if (starePanel.equals("Delete")){
                    admin.cautareChelner(Integer.parseInt(idTextField.getText()));
                    admin.displayChelner();
                    file=new File(pathFile);
                    try {
                        fileWriter=new FileWriter(file);
                        for (String[]z : admin.getChelneri()){
                            for (int i=1; i<z.length; i++) {
                                fileWriter.write(z[i]);
                                fileWriter.write("\n");
                            }
                        }
                        fileWriter.close();
                        admin.getChelneri().removeAll(admin.getChelneri());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        displayTable();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    eastPanel.removeAll();
                    revalidate();
                    repaint();
                } else if (starePanel.equals(editButton.getText())){
                    if (numeTextField.getText().equals("") || prenumeTextField.getText().equals("") || functieTextField.getText().equals(""))
                        JOptionPane.showMessageDialog(null, "Introduceti corespunzator datele!");
                    else {
                        String[] array = {numeTextField.getText(), prenumeTextField.getText(), functieTextField.getText()};
                        admin.modifyChelner(array, Integer.parseInt(idTextField.getText()));
                        file = new File(pathFile);
                        try {
                            fileWriter = new FileWriter(file);
                            for (String[] z : admin.getChelneri()) {
                                for (int i = 1; i < z.length; i++) {
                                    fileWriter.write(z[i]);
                                    fileWriter.write("\n");
                                }
                            }
                            fileWriter.close();
                            admin.getChelneri().removeAll(admin.getChelneri());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        try {
                            displayTable();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        eastPanel.removeAll();
                        revalidate();
                        repaint();
                    }
                    numeTextField.setText("");
                    prenumeTextField.setText("");
                    functieTextField.setText("");
                }
            }
        }
    }
    public void addDesign(){
        JLabel titleAdd=new JLabel("Adaugati datele angajatului");
        gbc.insets=new Insets(0,0,20,10);
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        eastPanel.add(titleAdd, gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(numeLabel, gbc);
        gbc.gridx=1;
        gbc.gridy=1;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(numeTextField, gbc);
        gbc.gridx=0;
        gbc.gridy=2;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(prenumeLabel, gbc);
        gbc.gridx=1;
        gbc.gridy=2;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(prenumeTextField, gbc);
        gbc.gridx=0;
        gbc.gridy=3;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(functieLabel, gbc);
        gbc.gridx=1;
        gbc.gridy=3;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(functieTextField, gbc);
        gbc.gridx=0;
        gbc.gridy=4;
        gbc.gridwidth=2;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        eastPanel.add(saveButton, gbc);
        add(eastPanel, BorderLayout.EAST);
        revalidate();;
        repaint();
    }
    public void deleteDesign(){
        JLabel titleAdd=new JLabel("Stergeti datele angajatului");
        idLabel=new JLabel("ID:");
        idTextField=new JTextField(10);
        gbc.insets=new Insets(0,0,20,10);
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        eastPanel.add(titleAdd, gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=1;
        eastPanel.add(idLabel, gbc);
        gbc.gridx=1;
        gbc.gridy=1;
        gbc.gridwidth=1;
        eastPanel.add(idTextField, gbc);
        gbc.gridx=0;
        gbc.gridy=2;
        gbc.gridwidth=2;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        eastPanel.add(saveButton, gbc);

        eastPanel.add(saveButton, gbc);
        add(eastPanel, BorderLayout.EAST);
        revalidate();;
        repaint();
    }
    public void editDesign(){
        idTextField=new JTextField();
        JLabel titleAdd=new JLabel("Editati datele angajatului");
        gbc.insets=new Insets(0,0,20,10);
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        eastPanel.add(titleAdd, gbc);
        gbc.gridx=1;
        gbc.gridy=0;
        gbc.gridwidth=1;
        eastPanel.add(idTextField, gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(numeLabel, gbc);
        gbc.gridx=1;
        gbc.gridy=1;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(numeTextField, gbc);
        gbc.gridx=0;
        gbc.gridy=2;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(prenumeLabel, gbc);
        gbc.gridx=1;
        gbc.gridy=2;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(prenumeTextField, gbc);
        gbc.gridx=0;
        gbc.gridy=3;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(functieLabel, gbc);
        gbc.gridx=1;
        gbc.gridy=3;
        gbc.weightx=1;
        gbc.gridwidth=1;
        eastPanel.add(functieTextField, gbc);
        gbc.gridx=0;
        gbc.gridy=4;
        gbc.gridwidth=2;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        eastPanel.add(saveButton, gbc);
        add(eastPanel, BorderLayout.EAST);
        revalidate();;
        repaint();
    }
}
