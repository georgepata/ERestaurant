package proiect;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;


public class AdministrareComenzi extends JPanel {
    private final int SCREEN_WIDTH = 500;
    private final int SCREEN_HEIGHT=500;
    private int row=0;
    private final String[] etichete = {"Masa 1", "Masa 2", "Masa 3", "Masa 4", "Masa 5", "Masa 6", "Masa 7", "Masa 8", "Masa 9", "Masa 10"};


    private LinkedHashMap<String, Boolean> mese;
    private JTable table;
    private JScrollPane jScrollPane;

    private GridBagLayout gridBagLayout;
    private GridBagConstraints gbc=new GridBagConstraints();
    private CardLayout cardLayout;
    private BorderLayout borderLayout;


    private JPanel southPanel;
    private JPanel centerPanel;
    private JPanel northPanel;

    private JLabel titleLabel;

    private Comanda comanda;

    private JButton backButton;
    private JButton modificareButton;
    private JButton stergereButton;
    private JButton mutareButton;

    private ColectieChelneri chelneri = ColectieChelneri.getInstance();
    private ModificarePanel modificarePanel;

    private File file;
    private FileWriter fileWriter;

    public AdministrareComenzi(){
        borderLayout=new BorderLayout();
        this.setLayout(borderLayout);

        cardLayout=new CardLayout();
        gridBagLayout=new GridBagLayout();

        southPanel=new JPanel();
        southPanel.setLayout(gridBagLayout);
        northPanel=new JPanel();
        northPanel.setLayout(gridBagLayout);
        northPanelDesign();

        centerPanel=new JPanel();
        centerPanel.setLayout(gridBagLayout);
        mese=new LinkedHashMap<>();
        for (int i=0; i<etichete.length; i++)
            mese.put(etichete[i], false);
        displayTable();
        ((Tabel) table).addMouseListener(new mouseAction());


        backButton=new JButton("Back");
        backButton.addActionListener(new buttonImplement());
        backButtonDesign();

        modificareButton=new JButton("Modificare comanda");
        stergereButton=new JButton("Stergere produse");
        mutareButton=new JButton("Mutare comanda");

        modificareButton.addActionListener(new buttonImplement());
        stergereButton.addActionListener(new buttonImplement());
        mutareButton.addActionListener(new buttonImplement());

        this.add(southPanel, BorderLayout.SOUTH);
        this.add(centerPanel);
        this.add(northPanel, BorderLayout.NORTH);
    }

    private class buttonImplement implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == backButton){
                cardLayout=(CardLayout) getParent().getLayout();
                cardLayout.show(ERestaurant.getInstance().getContentPane(), "Menu");
                ERestaurant.getInstance().setSize(new Dimension(ERestaurant.getInstance().getSCREEN_WIDTH(), ERestaurant.getInstance().getSCREEN_HEIGHT()));
                ERestaurant.getInstance().setLocationRelativeTo(null);
            } else if (e.getSource() == modificareButton){
                int row=table.getSelectedRow()+1;
                modificarePanel=new ModificarePanel(Integer.toString(row));
                ERestaurant.getInstance().getContentPane().add(modificarePanel, "ModificareComenzi");
                cardLayout.show(ERestaurant.getInstance().getContentPane(), "ModificareComenzi");
            } else if (e.getSource() == stergereButton) {
                int row=table.getSelectedRow()+1;
                try {
                    chelneri.stergereProduse(Integer.toString(row));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (e.getSource() == mutareButton){
                try{
                    String result = JOptionPane.showInputDialog(null, "La ce masa doriti sa mutati comanda: ");
                    table.setValueAt(true, Integer.parseInt(result)-1,1);
                    Random random=new Random();
                    String chelnerRow = (String) table.getValueAt(table.getSelectedRow(), 2);
                    chelneri.findWord(chelnerRow, false);
                    if (chelneri.getNUMBER() >3){
                        int valoare=random.nextInt(chelneri.getSize())+1;
                        chelneri.findWord(Integer.toString(valoare), false);
                        while (chelneri.getNUMBER() > 3){
                            valoare=random.nextInt(chelneri.getSize())+1;
                            chelneri.findWord(Integer.toString(valoare), false);
                        }
                        table.setValueAt(Integer.toString(valoare), Integer.parseInt(result)-1, 2);
                    } else table.setValueAt(chelnerRow, Integer.parseInt(result)-1, 2);
                    centerPanel.revalidate();
                    centerPanel.repaint();
                    chelneri.mutareComanda(Integer.toString((table.getSelectedRow())+1), result);
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"Masa nu exista!");
                }
            }
        }
    }

    public int getSCREEN_WIDTH(){
        return SCREEN_WIDTH;
    }
    public int getSCREEN_HEIGHT(){
        return SCREEN_HEIGHT;
    }
    public void backButtonDesign(){
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.weightx=1;
        gbc.anchor=GridBagConstraints.LINE_START;
        gbc.insets=new Insets(0,20,20,0);
        southPanel.add(backButton, gbc);
    }
    public void northPanelDesign (){
        titleLabel=new JLabel("Rezervare");
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.weightx=1;
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.insets=new Insets(50,0,0,0);
        northPanel.add(titleLabel, gbc);
    }

    public void displayTable(){
        table=new Tabel();
        ((Tabel) table).setEnabled(true);
        ((Tabel) table).setDefaultEditor(Object.class, null);
        ((Tabel) table).addColumn("ID");
        ((Tabel) table).addColumn("Stare");
        ((Tabel) table).addColumn("ID Chelner");
        for (String key : mese.keySet())
            ((Tabel) table).addRow(key, mese.get(key));
        table.setFillsViewportHeight(true);
        gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.weightx=1;
        gbc.anchor=GridBagConstraints.CENTER;
        jScrollPane=new JScrollPane(table);
        jScrollPane.setPreferredSize(new Dimension(300, 200));
        centerPanel.removeAll();
        centerPanel.add(jScrollPane,gbc);
        this.revalidate();
        this.repaint();
    }
    private class mouseAction implements MouseListener{
        public void mouseClicked(MouseEvent e){
            JTable target;
            try {
                chelneri.citireComanda();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (e.getClickCount() == 2){
                try{
                    target = (JTable) e.getSource();
                    row=target.getSelectedRow();
                    if (table.getValueAt(row, 1).equals(false)){
                        String result=JOptionPane.showInputDialog("Introduceti ID-ul chelnerului");
                        if (Integer.parseInt(result) <=0 || Integer.parseInt(result) > chelneri.getSize())
                            JOptionPane.showMessageDialog(null, "Nu exista chelnerul cu ID-ul "+result);
                        else {
                            comanda=new Comanda();
                            row++;
                            chelneri.findWord(result, false);
                            if (chelneri.getNUMBER() <=3) {
                                fileComenzi(Integer.toString(row));
                                ((Tabel) table).setValueAt(result, row-1, 2);
                                cardLayout = (CardLayout) getParent().getLayout();
                                ERestaurant.getInstance().getContentPane().add(comanda, "Comanda");
                                cardLayout.show(ERestaurant.getInstance().getContentPane(), "Comanda");
                                table.setValueAt(true, table.getSelectedRow(), 1);
                                ERestaurant.getInstance().setSize(new Dimension(comanda.getSCREEN_WIDTH(), comanda.getSCREEN_HEIGHT()));
                                ERestaurant.getInstance().setLocationRelativeTo(null);
                            }
                        }
                    } else if (table.getValueAt(row,1).equals(true)){
                        int result = JOptionPane.showConfirmDialog(null, "Comanda este achitata?", "Plata", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            String chelnerRow = (String) target.getValueAt(target.getSelectedRow(), 2);
                            chelneri.findWord(chelnerRow, true);
                            table.setValueAt(false, row, 1);
                            table.setValueAt(null, row, 2);
                            centerPanel.remove(modificareButton);
                            centerPanel.remove(stergereButton);
                            centerPanel.remove(mutareButton);
                            centerPanel.revalidate();
                            centerPanel.repaint();
                            try {
                                chelneri.achitareComanda(Integer.toString(target.getSelectedRow()+1));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    chelneri.afisare();
                } catch (Exception e1){
                    JOptionPane.showMessageDialog(null, "Introduceti o valoare corespunzatoare!");
                }
            } else if (e.getClickCount() == 1){
                target = (JTable) e.getSource();
                row=target.getSelectedRow();
                if (table.getValueAt(row, 1).equals(true)){
                    row++;
                    gbc.gridwidth=1;
                    gbc.anchor=GridBagConstraints.CENTER;
                    gbc.insets=new Insets(10,0,0,0);
                    gbc.gridx=0;
                    gbc.gridy=1;
                    centerPanel.add(modificareButton, gbc);
                    gbc.gridx=0;
                    gbc.gridy=2;
                    centerPanel.add(stergereButton, gbc);
                    gbc.gridx=0;
                    gbc.gridy=3;
                    centerPanel.add(mutareButton, gbc);
                    centerPanel.revalidate();
                    centerPanel.repaint();
                } else if (table.getValueAt(row, 1).equals(false)){
                    centerPanel.remove(modificareButton);
                    centerPanel.remove(stergereButton);
                    centerPanel.remove(mutareButton);
                    centerPanel.revalidate();
                    centerPanel.repaint();
                }
            }
            try {
                chelneri.citireComanda();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            chelneri.afisare();
            chelneri.afisareComenzi();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    public void fileComenzi(String id){
        if (new File("Comenzi.txt").exists()){
            try{
                fileWriter=new FileWriter(new File("Comenzi.txt").getAbsoluteFile(), true);
                fileWriter.write(id);
                fileWriter.write("\n");
                fileWriter.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            try{
                file=new File("Comenzi.txt");
                fileWriter=new FileWriter(file);
                fileWriter.write(id);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
