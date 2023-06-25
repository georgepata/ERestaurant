package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ModificarePanel extends JPanel {
    private final int SCREEN_WIDTH=500;
    private final int SCREEN_HEIGHT=400;
    private BorderLayout borderLayout;

    private JTable tabelComanda;
    private JScrollPane jScrollPane;

    private GridBagLayout gridBagLayout;
    private GridBagConstraints gbc=new GridBagConstraints();
    private CardLayout cardLayout;

    private JPanel centerPanel;
    private JPanel southPanel;

    private String ID;

    private JButton saveButton;
    private JButton backButton;

    private File file;
    private FileWriter fileWriter;
    private ArrayList<ArrayList<String>> comenzi = ColectieChelneri.getInstance().getComenzi();

    public ModificarePanel(String ID){
        cardLayout=new CardLayout();
        borderLayout=new BorderLayout();
        gridBagLayout=new GridBagLayout();
        this.setLayout(borderLayout);
        this.ID=ID;
        centerPanel=new JPanel();
        centerPanel.setLayout(gridBagLayout);
        southPanel=new JPanel();
        southPanel.setLayout(gridBagLayout);
        backButton=new JButton("Back");
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.anchor=GridBagConstraints.LINE_START;
        gbc.insets=new Insets(0,20,20,0);
        backButton.addActionListener(new actionImplement());
        southPanel.add(backButton,gbc);

        saveButton=new JButton("Save");
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=1;
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.insets=new Insets(0,0,20,0);
        saveButton.addActionListener(new actionImplement());


        try{
            tableDisplay(ID);
        }catch (Exception e){
            e.printStackTrace();
        }
        southPanel.add(saveButton, gbc);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);
    }


    public void tableDisplay(String ID) throws IOException {
        String line=null;
        String[] forRow;
        tabelComanda=new Tabel();
        ((Tabel) tabelComanda).addColumn("Denumire");
        ((Tabel) tabelComanda).addColumn("Numar produse");
        ((Tabel) tabelComanda).addColumn("Pret");
        ((Tabel) tabelComanda).setEnabled(true);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.weightx=1;
        jScrollPane=new JScrollPane(tabelComanda);
        jScrollPane.setPreferredSize(new Dimension(400,200));
        centerPanel.add(jScrollPane, gbc);
        for (int i=0; i<comenzi.size(); i++){
            ArrayList<String> helper=comenzi.get(i);
            if (helper.get(0).equals(ID)){
                for (int j=1; j<helper.size(); j+=3) {
                    forRow = new String[]{helper.get(j), helper.get(j + 1), helper.get(j + 2)};
                    ((Tabel) tabelComanda).addRow(forRow);
                }
            }
        }
    }

    private void scriereFisier() throws IOException {
        file=new File("Comenzi.txt");
        fileWriter=new FileWriter(file);
        for (int i=0; i<comenzi.size(); i++){
            ArrayList<String>helper=comenzi.get(i);
            fileWriter.write(helper.get(0));
            fileWriter.write("\n");
            for (int j=1; j<helper.size(); j++){
                fileWriter.write(helper.get(j));
                fileWriter.write(" ");
            }
            fileWriter.write("\n");
        }
        fileWriter.close();
    }

    public int getSCREEN_WIDTH(){
        return SCREEN_WIDTH;
    }
    public int getSCREEN_HEIGHT(){
        return SCREEN_HEIGHT;
    }

    private class actionImplement implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == saveButton) {
                for (int i = 0; i < comenzi.size(); i++) {
                    ArrayList<String> helper = comenzi.get(i);
                    int row = 0;
                    if (helper.get(0).equals(ID)) {
                        for (int j = 2; j < helper.size(); j += 3) {
                            helper.set(j, (String) tabelComanda.getValueAt(row, 1));
                            double z = Double.parseDouble((String) tabelComanda.getValueAt(row, 1)) * Double.parseDouble((String) tabelComanda.getValueAt(row, 2));
                            tabelComanda.setValueAt(z, row, 2);
                            helper.set(j+1, Double.toString(z));
                            row++;
                        }
                        break;
                    }
                }
            
                JOptionPane.showMessageDialog(null, "Modificarile au fost salvate!");
                try {
                    scriereFisier();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (e.getSource() == backButton){
                cardLayout=(CardLayout) getParent().getLayout();
                cardLayout.show(ERestaurant.getInstance().getContentPane(), "Comenzi");
            }
        }
    }
}
