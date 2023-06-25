package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AdaugareProdus extends JPanel implements ActionListener, ItemListener {
    private int SCREEN_WIDTH = 1000;
    private int SCREEN_HEIGHT = 500;

    private GridBagLayout gbl;
    private GridBagConstraints gbc;

    private Tabel tabel;
    private JScrollPane scrollPane;

    private File file;
    private FileWriter fileWriter;
    private String[] pathFiles = new String[10];
    private String selectedItem;
    static int k = 0;

    private JPanel eastPanel;
    private JPanel northPanel;
    private JPanel westPanel;
    private JPanel southPanel;

    private JButton backButton;
    private JButton adaugaButton;
    private JComboBox<String> categorieBox;
    private JLabel denumireLabel;
    private JLabel cantitateLabel;
    private JLabel unitateLabel;
    private JLabel pretLabel;
    private JLabel titleLabel;
    private JLabel indicatieLabel;
    private JTextField denumireField;
    private JTextField cantitateField;
    private JTextField unitateField;
    private JTextField pretField;

    private CardLayout cardLayout;
    private ArrayList<String> etichete;
    private BufferedReader bufferedReader;

    public AdaugareProdus() {
        etichete=new ArrayList<>();
        try{
            String line=null;
            bufferedReader=new BufferedReader(new FileReader("Categorii.txt"));
            while ((line=bufferedReader.readLine())!=null){
                etichete.add(line);
            }
            bufferedReader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        this.setLayout(new BorderLayout());

        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();


        eastPanel = new JPanel();
        eastPanel.setLayout(gbl);
        northPanel = new JPanel();
        northPanel.setLayout(gbl);
        westPanel = new JPanel();
        westPanel.setLayout(gbl);
        southPanel = new JPanel();
        southPanel.setLayout(gbl);

        categorieBox = new JComboBox<>(etichete.toArray(new String[0]));
        denumireField = new JTextField(15);
        cantitateField = new JTextField(15);
        unitateField = new JTextField(15);
        pretField = new JTextField(15);
        setButtons();


        gbc.insets = new Insets(25, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.CENTER;
        northPanel.add(titleLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.CENTER;
        northPanel.add(categorieBox, gbc);

        gbc.insets = new Insets(0, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        westPanel.add(denumireLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        westPanel.add(denumireField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        westPanel.add(cantitateLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        westPanel.add(cantitateField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        westPanel.add(unitateLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        westPanel.add(unitateField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        westPanel.add(pretLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        westPanel.add(pretField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        westPanel.add(adaugaButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        southPanel.add(backButton, gbc);


        renderNull();


        cardLayout = new CardLayout();
        backButton.addActionListener(this);
        adaugaButton.addActionListener(this);

        categorieBox.addItemListener(this);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(westPanel, BorderLayout.WEST);
        this.add(southPanel, BorderLayout.SOUTH);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(ERestaurant.getInstance().getContentPane(), "Catalog");
            ERestaurant.getInstance().setSize(new Dimension(600, 500));
            ERestaurant.getInstance().setLocationRelativeTo(null);
            renderNull();
            denumireField.setText("");
            cantitateField.setText("");
            unitateField.setText("");
            pretField.setText("");
            eastPanel.removeAll();
        } else if (e.getSource() == adaugaButton) {
            try {
                int i;
                for (i = 0; i < k; i++)
                    if (pathFiles[i].contains(selectedItem))
                        break;
                FileWriter write = new FileWriter(pathFiles[i], true);
                String denumire, cantitate, unitate, pret;
                denumire = denumireField.getText();
                cantitate = cantitateField.getText();
                unitate = unitateField.getText();
                pret = pretField.getText();
                if (denumire.isEmpty() || cantitate.isEmpty() || unitate.isEmpty() || pret.isEmpty())
                    JOptionPane.showMessageDialog(null, "Introduceti corespunzator datele!");
                else {
                    write.write(denumire);
                    write.write("\n");
                    write.write(cantitate);
                    write.write("\n");
                    write.write(unitate);
                    write.write("\n");
                    write.write(pret);
                    write.write("\n");
                    write.close();
                    JOptionPane.showMessageDialog(null, "Datele au fost memorate!");
                    tableDisplay();
                }
                denumireField.setText("");
                cantitateField.setText("");
                unitateField.setText("");
                pretField.setText("");
            } catch (Exception exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(null, "Nu ati selectat categoria!");
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            selectedItem = (String) e.getItem();
            if (new File(selectedItem + ".txt").exists()) {
                System.out.println("File found: " + new File(selectedItem + ".txt").getAbsolutePath());
                int ok = 0;
                for (int i = 0; i < k && ok == 0; i++) {
                    if (pathFiles[i].equals((String) (new File(selectedItem + ".txt").getAbsolutePath())))
                        ok = 1;
                }
                if (ok == 0)
                    pathFiles[k++] = new File(selectedItem + ".txt").getAbsolutePath();
                try {
                    tableDisplay();
                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                }
            } else {
                try {
                    file = new File(selectedItem + ".txt");
                    fileWriter = new FileWriter(file);
                    System.out.println("New file is ready to set: " + file.getAbsolutePath());
                    pathFiles[k] = file.getAbsolutePath();
                    k++;
                    tableDisplay();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void setButtons() {
        backButton = new JButton("Back");
        adaugaButton = new JButton("Add");
        denumireLabel = new JLabel("Denumire");
        cantitateLabel = new JLabel("Cantitate");
        unitateLabel = new JLabel("Unitate");
        pretLabel = new JLabel("Pret");
        titleLabel = new JLabel("Alegeti categoria");
        indicatieLabel = new JLabel("Adaugati un produs");
    }

    public void renderNull() {
        categorieBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    value = "Select...";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        categorieBox.setSelectedItem(null);
    }

    public int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public void tableDisplay() throws FileNotFoundException {
        Tabel tabel = new Tabel();
        int cnt = 0,i,index;
        for (i = 0; i < k; i++)
            if (pathFiles[i].contains(selectedItem))
                break;
        File file = new File(pathFiles[i]);
        Scanner scanner = new Scanner(file);
        ArrayList<String> array = new ArrayList<>();
        tabel.addColumn("ID");
        tabel.addColumn("Denumire");
        tabel.addColumn("Cantitate");
        tabel.addColumn("Unitate");
        tabel.addColumn("Pret");
        cnt=1;
        index=1;
        if (file.length() > 0) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                array.add(line);
                cnt++;
                if (cnt == tabel.getColumnCount()) {
                    String[] newRow=new String[tabel.getColumnCount()];
                    newRow[0]=Integer.toString(index);
                    for (i=1; i<tabel.getColumnCount(); i++)
                        newRow[i]=array.get(i-1);
                    tabel.addRow(newRow);
                    cnt = 1;
                    array.clear();
                    index++;
                }
            }
        }
        array.clear();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        scrollPane = new JScrollPane(tabel);
        tabel.setPreferredScrollableViewportSize(new Dimension(350, 150));
        eastPanel.removeAll();
        eastPanel.add(scrollPane, gbc);
        this.add(eastPanel, BorderLayout.EAST);
        this.revalidate();
        this.repaint();
    }
}

