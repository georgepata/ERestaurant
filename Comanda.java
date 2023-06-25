package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Comanda extends JPanel {
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT= 500;

    private JPanel southPanel;
    private JPanel westPanel;
    private JPanel eastPanel;

    private JButton comandaButton;
    private JButton saveButton;

    private JComboBox<String> categorieBox;
    private String[] pathFile=new String[10];
    private String selectedItem;
    private int k=0, index;
    private double pretFinal=0;
    private ArrayList<String[]> produs;
    private ArrayList<String[]> listaComenzi;

    String id, cantitate;

    private JTable tabelCategory;
    private JTable tabelComanda;
    private JScrollPane comandaScrollPane=new JScrollPane();
    private JScrollPane jScrollPane;

    private JLabel idLabel;
    private JLabel cantitateLabel;
    private JTextField idField;
    private JTextField cantitateField;

    private JLabel pretLabel;
    private JLabel comandaLabel;

    private CardLayout cardLayout;
    private BorderLayout borderLayout;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gbc =new GridBagConstraints();

    private File file;
    private FileWriter fileWriter;

    private ArrayList<String> etichete;
    private BufferedReader bufferedReader;

    public Comanda(){
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

        listaComenzi=new ArrayList<>();
        this.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        borderLayout=new BorderLayout();
        this.setLayout(borderLayout);
        cardLayout=new CardLayout();
        gridBagLayout=new GridBagLayout();

        southPanel=new JPanel();
        southPanel.setLayout(gridBagLayout);
        comandaButton=new JButton("Comanda");
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.insets=new Insets(0,0,20,0);
        southPanel.add(comandaButton, gbc);

        westPanel=new JPanel();
        westPanel.setLayout(gridBagLayout);

        categorieBox=new JComboBox<>(etichete.toArray(new String[0]));
        categorieBox.addItemListener(new itemlistener());
        renderNull();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.insets=new Insets(0, 50, 0 ,0);
        gbc.anchor=GridBagConstraints.CENTER;
        westPanel.add(categorieBox, gbc);

        eastPanel = new JPanel();
        eastPanel.setLayout(gridBagLayout);
        comandaLabel = new JLabel("Comanda");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill=GridBagConstraints.NONE;
        gbc.insets=new Insets(0,0,20,0);
        eastPanel.add(comandaLabel, gbc);

        pretLabel = new JLabel("Pret: " + pretFinal + " lei");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill=GridBagConstraints.NONE;
        eastPanel.add(pretLabel, gbc);

        designProduct();
        comandaTabel();

        comandaButton.addActionListener(new actionImplement());
        saveButton.addActionListener(new actionImplement());

        this.add(westPanel, BorderLayout.WEST);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(eastPanel, BorderLayout.EAST);
    }
    private class actionImplement implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == saveButton){
                id = idField.getText();
                cantitate = cantitateField.getText();
                if (idField.getText().equals("") || cantitateField.getText().equals(""))
                    JOptionPane.showMessageDialog(null, "Introduceti valori corespunzatoare");
                else if (Integer.parseInt(idField.getText()) >tabelCategory.getRowCount() || Integer.parseInt(idField.getText()) == 0)
                    JOptionPane.showMessageDialog(null, "Nu exista ID-ul " + idField.getText());
                else comandaTabel();
                idField.setText("");
                cantitateField.setText("");
            } else if (e.getSource() == comandaButton){
                if (((Tabel) tabelComanda).isJTableEmpty() == false){
                    JOptionPane.showMessageDialog(null, "Comanda a fost inregistrata!");
                    String[] z;
                    try{
                        file=new File("Comenzi.txt");
                        fileWriter =new FileWriter(file, true);
                        for (int i=0; i<listaComenzi.size(); i++){
                            z=listaComenzi.get(i);
                            for (int j=0; j<z.length; j++) {
                                fileWriter.write(z[j]);
                                fileWriter.write(" ");
                            }
                        }
                        fileWriter.write("\n");
                        fileWriter.close();
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                    renderNull();
                    cardLayout= (CardLayout) getParent().getLayout();
                    cardLayout.show(ERestaurant.getInstance().getContentPane(), "Comenzi");
                    ERestaurant.getInstance().setSize(new Dimension(SCREEN_WIDTH-300, SCREEN_HEIGHT));
                    ERestaurant.getInstance().setLocationRelativeTo(null);
                } else JOptionPane.showMessageDialog(null, "Nu ati introdus niciun produs");
            }
        }
    }

    private class itemlistener implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED){
                selectedItem=(String) e.getItem();
                if (new File(selectedItem+".txt").exists()){
                    int ok = 0;
                    for (int i = 0; i < k && ok == 0; i++) {
                        if (pathFile[i].equals((String) (new File(selectedItem + ".txt").getAbsolutePath())))
                            ok = 1;
                    }
                    if (ok == 0)
                        pathFile[k++] = new File(selectedItem + ".txt").getAbsolutePath();
                    try{
                        refreshTable();
                    } catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            }
        }
    }
    public void refreshTable() throws FileNotFoundException {
        String path = null;
        tabelCategory=new Tabel();
        for (int i=0; i<k; i++)
            if (pathFile[i].contains(selectedItem))
                path = pathFile[i];
        File file=new File(path);
        Scanner scanner=new Scanner(file);
        ((Tabel) tabelCategory).addColumn("ID");
        ((Tabel) tabelCategory).addColumn("Denumire");
        ((Tabel) tabelCategory).addColumn("Cantiate");
        ((Tabel) tabelCategory).addColumn("Unitate");
        ((Tabel) tabelCategory).addColumn("Pret");
        ArrayList<String> array = new ArrayList<>();
        produs= new ArrayList<>();
        int cnt=1;
        index=1;
        if (file.length() > 0)
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                array.add(line);
                cnt++;
                if (cnt == tabelCategory.getColumnCount()) {
                    String[] newRow=new String[tabelCategory.getColumnCount()];
                    newRow[0]=Integer.toString(index);
                    for (int i=1; i<tabelCategory.getColumnCount(); i++)
                        newRow[i]=array.get(i-1);
                    ((Tabel) tabelCategory).addRow(newRow);
                    produs.add(newRow);
                    cnt = 1;
                    array.clear();
                    index++;
                }
            }
        array.clear();
        index--;
        jScrollPane=new JScrollPane(tabelCategory);
        gbc.gridwidth=1;
        gbc.insets=new Insets(0,0,10,0);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.anchor=GridBagConstraints.CENTER;
        tabelCategory.setPreferredScrollableViewportSize(new Dimension(450, 250));
        westPanel.removeAll();
        westPanel.add(jScrollPane, gbc);
        categorieBox.addItemListener(new itemlistener());
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.anchor=GridBagConstraints.CENTER;
        westPanel.add(categorieBox, gbc);
        this.revalidate();
        this.repaint();
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
    public int getSCREEN_WIDTH(){
        return SCREEN_WIDTH;
    }
    public int getSCREEN_HEIGHT(){
        return SCREEN_HEIGHT;
    }


    public void comandaTabel(){
        tabelComanda=new Tabel();
        ((Tabel) tabelComanda).addColumn("Denumire");
        ((Tabel) tabelComanda).addColumn("Numar produse");
        ((Tabel) tabelComanda).addColumn("Pret");
        if (!idField.getText().equals("") && !cantitateField.getText().equals("")){
            String[] helper = produs.get(Integer.parseInt(idField.getText())-1);
            double pretProdus = Double.parseDouble(helper[helper.length-1]) * Integer.parseInt(cantitateField.getText());
            pretFinal+=pretProdus;
            String[] rowProduct = {helper[1], cantitateField.getText(), Double.toString(pretProdus)};
            listaComenzi.add(rowProduct);
            for (int i=0; i<listaComenzi.size(); i++)
                ((Tabel) tabelComanda).addRow(listaComenzi.get(i));
        }
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=2;
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        eastPanel.remove(comandaScrollPane);
        eastPanel.remove(pretLabel);
        comandaScrollPane=new JScrollPane(tabelComanda);
        comandaScrollPane.setPreferredSize(new Dimension(300,150));
        eastPanel.add(comandaScrollPane, gbc);
        pretLabel.setText("Pret: " + pretFinal + " lei");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill=GridBagConstraints.NONE;
        eastPanel.add(pretLabel, gbc);
        eastPanel.revalidate();
        eastPanel.repaint();
    }
    public void designProduct(){
        idLabel = new JLabel("ID: ");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        eastPanel.add(idLabel, gbc);

        idField = new JTextField(10);
        idField.setText("");
        gbc.gridx=1;
        gbc.gridy=3;
        gbc.gridwidth=1;
        eastPanel.add(idField, gbc);

        cantitateLabel = new JLabel("Numar: ");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        eastPanel.add(cantitateLabel, gbc);

        cantitateField = new JTextField(10);
        cantitateField.setText("");
        gbc.gridx=1;
        gbc.gridy=4;
        gbc.gridwidth=1;
        eastPanel.add(cantitateField, gbc);

        saveButton=new JButton("Save");
        gbc.gridx=0;
        gbc.gridy=5;
        gbc.gridwidth=2;
        gbc.fill=GridBagConstraints.NONE;
        eastPanel.add(saveButton, gbc);
    }

    public double getPretFinal(){
        return pretFinal;
    }
}
