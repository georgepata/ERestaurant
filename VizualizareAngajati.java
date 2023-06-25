package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class VizualizareAngajati extends JPanel {
    CardLayout cardLayout;
    File file;
    Scanner scanner;
    JTable table;
    JScrollPane jScrollPane;
    GridBagLayout gridBagLayout =new GridBagLayout();
    GridBagConstraints gbc=new GridBagConstraints();
    JPanel centerPanel;
    JPanel southPanel;
    JButton backButton;
    public VizualizareAngajati(){
        cardLayout = new CardLayout();
        backButton=new JButton("Back");
        this.setLayout(new BorderLayout());
        centerPanel=new JPanel();
        centerPanel.setLayout(gridBagLayout);
        southPanel=new JPanel();
        southPanel.setLayout(gridBagLayout);
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.weightx=1;
        gbc.anchor=GridBagConstraints.LINE_START;
        gbc.insets=new Insets(0,10,10,0);
        southPanel.add(backButton, gbc);
        try{
            displayTable();
        } catch (Exception e){
            e.printStackTrace();
        }
        backButton.addActionListener(new buttonImplement());
        this.add(centerPanel);
        this.add(southPanel, BorderLayout.SOUTH);
    }
    public void displayTable() throws FileNotFoundException {
        file=new File("Angajati.txt");
        table=new JTable();
        ArrayList<String> array = new ArrayList<>();
        table=new Tabel();
        scanner = new Scanner(file);
        ((Tabel) table).addColumn("ID");
        ((Tabel) table).addColumn("Nume");
        ((Tabel) table).addColumn("Prenume");
        ((Tabel) table).addColumn("Functie");
        int cnt = 1;
        int index = 1;
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
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        table.setPreferredScrollableViewportSize(new Dimension(300, 250));
        centerPanel.removeAll();
        centerPanel.add(jScrollPane, gbc);
        this.revalidate();
        this.repaint();
    }
    private class buttonImplement implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == backButton)
                cardLayout=(CardLayout) getParent().getLayout();
                cardLayout.show(ERestaurant.getInstance().getContentPane(), "Staff");
                ERestaurant.getInstance().setSize(new Dimension(ERestaurant.getInstance().getSCREEN_WIDTH(), ERestaurant.getInstance().getSCREEN_HEIGHT()));
                ERestaurant.getInstance().setLocationRelativeTo(null);
        }
    }
}
