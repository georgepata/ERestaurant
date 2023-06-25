package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.Expression;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

  public class StergereProdus extends JPanel implements ItemListener, ActionListener {
    private ArrayList<String> etichete;
    private BufferedReader bufferedReader;
    private String[] pathFile=new String[10];
    private String selectedItem, verificare;
    private ArrayList<String[]> produs;

    File file;
    Scanner scanner;

    private int k=0;
    private int index, number;

    private JTable table;
    private JScrollPane jScrollPane;

    private BorderLayout borderLayout;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gbc;
    private CardLayout cardLayout;

    private JPanel northPanel;
    private JPanel centerPanel;
    private JPanel southPanel;
    private JPanel eastPanel;

    private JButton backButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton saveButton;

    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel indicatie;
    private JTextField idField;
    private JComboBox<String> categorieBox;

  private JLabel denumireLabel;
  private JLabel cantitateLabel;
  private JLabel unitateLabel;
  private JLabel pretLabel;
  private JTextField denumireField;
  private JTextField cantitateField;
  private JTextField unitateField;
  private JTextField pretField;

    public StergereProdus(){
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

        borderLayout=new BorderLayout();
        this.setLayout(borderLayout);
        gridBagLayout=new GridBagLayout();
        gbc=new GridBagConstraints();

        categorieBox=new JComboBox<>(etichete.toArray(new String[0]));
        idLabel=new JLabel("ID:");
        idField=new JTextField(10);

        backButton=new JButton("Back");
        deleteButton=new JButton("Delete");
        editButton=new JButton("Edit");
        saveButton=new JButton("Save");

        northPanel=new JPanel();
        northPanel.setLayout(gridBagLayout);
        southPanel=new JPanel();
        southPanel.setLayout(gridBagLayout);
        centerPanel=new JPanel();
        centerPanel.setLayout(gridBagLayout);
        eastPanel=new JPanel();
        eastPanel.setLayout(gridBagLayout);


        gbc.weightx=1;
        gbc.fill=GridBagConstraints.CENTER;
        gbc.insets=new Insets(20,0,0,0);

        gbc.gridx=0;
        gbc.gridy=0;
        titleLabel=new JLabel("Delete item");
        northPanel.add(titleLabel, gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        northPanel.add(categorieBox, gbc);


        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.weightx=1;
        gbc.anchor=GridBagConstraints.WEST;
        gbc.insets=new Insets(0,20,20,0);
        southPanel.add(backButton, gbc);

        renderNull();
        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);


        categorieBox.addItemListener(this);
        cardLayout=new CardLayout();
        backButton.addActionListener(this);
        deleteButton.addActionListener(this);
        editButton.addActionListener(this);
        saveButton.addActionListener(this);
    }



    public void renderNull(){
        categorieBox.setRenderer(new DefaultListCellRenderer(){
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

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED){
            selectedItem=(String) e.getItem();
            if (new File(selectedItem+".txt").exists()){
                int ok = 0;
                System.out.println(new File(selectedItem + ".txt").getAbsolutePath());
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton){
            cardLayout= (CardLayout) getParent().getLayout();
            cardLayout.show(ERestaurant.getInstance().getContentPane(), "Catalog");
            ERestaurant.getInstance().setSize(new Dimension(Catalog.getInstance().getSCREEN_WIDTH(), Catalog.getInstance().getSCREEN_HEIGHT()));
            ERestaurant.getInstance().setLocationRelativeTo(null);
            renderNull();
            centerPanel.removeAll();
        } else if (e.getSource() == deleteButton){
            String verificare=idField.getText();
            int number;
            if (verificare.isEmpty()==true || Integer.parseInt(verificare) == 0) {
                JOptionPane.showMessageDialog(null, "Introduceti un ID corespunzator!");
            }
            else if (verificare.isEmpty()==false){
                number=Integer.parseInt(idField.getText());
                if (number>index){
                    JOptionPane.showMessageDialog(null, "Introduceti un ID mai mic sau egal decat "+index);
                } else {
                    int cnt=0, ok=0, j=0;
                    for (String[] z : produs){
                        j=0;
                        for (String mn : z)
                            if (mn.equals(Integer.toString(number)) && j==0) {
                                ok=1;
                                break;
                            } else j=1;
                        if (ok==1) break;
                        cnt++;
                    }
                    produs.remove(cnt);
                    JOptionPane.showMessageDialog(null, "Produsul cu ID-ul " +number+ " a fost sters!");
                    try {
                        addToFile();
                    } catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
                eastPanel.removeAll();
                this.revalidate();
                this.repaint();
            }
        } else if (e.getSource() == editButton){
            eastPanel.removeAll();
            verificare=idField.getText();
            if (verificare.isEmpty()==true || Integer.parseInt(verificare) == 0) {
                JOptionPane.showMessageDialog(null, "Introduceti un ID corespunzator!");
            }
            else if (verificare.isEmpty()==false) {
                number = Integer.parseInt(idField.getText());
                if (number > index) {
                    JOptionPane.showMessageDialog(null, "Introduceti un ID mai mic sau egal decat " + index);
                } else {
                    denumireLabel=new JLabel("Denumire");
                    denumireField=new JTextField(10);
                    cantitateLabel=new JLabel("Cantitate");
                    cantitateField=new JTextField(10);
                    unitateLabel=new JLabel("Unitate");
                    unitateField=new JTextField(10);
                    pretLabel=new JLabel("Pret");
                    pretField=new JTextField(10);
                    indicatie=new JLabel("Editati produsul cu ID-ul "+Integer.toString(number));
                    gbc.insets=new Insets(0,0,20,15);
                    gbc.gridx=0;
                    gbc.gridy=0;
                    gbc.gridwidth=2;
                    gbc.fill=GridBagConstraints.HORIZONTAL;
                    eastPanel.add(indicatie, gbc);
                    gbc.gridwidth=1;
                    gbc.gridx=0;
                    gbc.gridy=1;
                    eastPanel.add(denumireLabel, gbc);
                    gbc.gridx=1;
                    gbc.gridy=1;
                    eastPanel.add(denumireField, gbc);
                    gbc.gridx=0;
                    gbc.gridy=2;
                    eastPanel.add(cantitateLabel, gbc);
                    gbc.gridx=1;
                    gbc.gridy=2;
                    eastPanel.add(cantitateField, gbc);
                    gbc.gridx=0;
                    gbc.gridy=3;
                    eastPanel.add(unitateLabel, gbc);
                    gbc.gridx=1;
                    gbc.gridy=3;
                    eastPanel.add(unitateField, gbc);
                    gbc.gridx=0;
                    gbc.gridy=4;
                    eastPanel.add(pretLabel, gbc);
                    gbc.gridx=1;
                    gbc.gridy=4;
                    eastPanel.add(pretField, gbc);
                    gbc.gridx=0;
                    gbc.gridy=5;
                    gbc.anchor=GridBagConstraints.CENTER;
                    gbc.fill=GridBagConstraints.HORIZONTAL;
                    gbc.gridwidth=2;
                    eastPanel.add(saveButton, gbc);
                    this.add(eastPanel, BorderLayout.EAST);
                    this.revalidate();
                    this.repaint();
                }
            }
        } else if (e.getSource() == saveButton){
            int i,ok=0, cnt=1;
            if (denumireField.getText().isEmpty() || cantitateField.getText().isEmpty() || unitateField.getText().isEmpty() || pretField.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "Completati spatiile libere");
            else {
                String[] ay = {Integer.toString(number), denumireField.getText(), cantitateField.getText(), unitateField.getText(), pretField.getText()};
                for (String[] z : produs) {
                    i = 0;
                    for (String nm : z) {
                        if (i == 0 && Integer.toString(number).equals(nm)) {
                            ok = 1;
                            break;
                        }
                    }
                    if (ok == 1) break;
                    cnt++;
                }
                produs.set(cnt - 1, ay);
                JOptionPane.showMessageDialog(null, "Produsul a fost modificat!");
                eastPanel.removeAll();
                try {
                    addToFile();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        idField.setText("");
    }



    public void tableDisplay() throws FileNotFoundException {
        gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor=GridBagConstraints.CENTER;
        table.setPreferredScrollableViewportSize(new Dimension(350, 150));
        centerPanel.removeAll();
        centerPanel.add(jScrollPane, gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        gbc.anchor=GridBagConstraints.CENTER;
        centerPanel.add(idLabel, gbc);

        gbc.gridx=0;
        gbc.gridy=2;
        gbc.anchor=GridBagConstraints.CENTER;
        centerPanel.add(idField, gbc);

        gbc.gridx=0;
        gbc.gridy=3;
        gbc.anchor=GridBagConstraints.CENTER;
        centerPanel.add(deleteButton, gbc);
        gbc.gridx=0;
        gbc.gridy=4;
        gbc.anchor=GridBagConstraints.CENTER;
        centerPanel.add(editButton, gbc);
        this.revalidate();
        this.repaint();
    }

    public void addToFile() throws IOException {
        File file= new File(selectedItem+".txt");
        FileWriter w=new FileWriter(file.getAbsoluteFile());
        for (String[] z: produs){
            int i=0;
            for(String nm : z) {
                if (i != 0) {
                    w.write(nm);
                    w.write("\n");
                }
                i=1;
            }
        }
        w.close();
        refreshTable();
    }

    public void refreshTable() throws FileNotFoundException {
        String path = null;
        table=new Tabel();
        for (int i=0; i<k; i++)
            if (pathFile[i].contains(selectedItem))
                path = pathFile[i];
        File file=new File(path);
        Scanner scanner=new Scanner(file);
        ((Tabel) table).addColumn("ID");
        ((Tabel) table).addColumn("Denumire");
        ((Tabel) table).addColumn("Cantiate");
        ((Tabel) table).addColumn("Unitate");
        ((Tabel) table).addColumn("Pret");
        ArrayList<String> array = new ArrayList<>();
        produs= new ArrayList<>();
        int cnt=1;
        index=1;
        if (file.length() > 0)
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                array.add(line);
                cnt++;
                if (cnt == table.getColumnCount()) {
                    String[] newRow=new String[table.getColumnCount()];
                    newRow[0]=Integer.toString(index);
                    for (int i=1; i<table.getColumnCount(); i++)
                        newRow[i]=array.get(i-1);
                    ((Tabel) table).addRow(newRow);
                    produs.add(newRow);
                    cnt = 1;
                    array.clear();
                    index++;
                }
            }
        array.clear();
        index--;
        jScrollPane=new JScrollPane(table);
        gbc.gridwidth=1;
        gbc.insets=new Insets(0,0,10,0);
        gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor=GridBagConstraints.CENTER;
        table.setPreferredScrollableViewportSize(new Dimension(350, 150));
        centerPanel.removeAll();
        centerPanel.add(jScrollPane, gbc);
        tableDisplay();
        this.revalidate();
        this.repaint();
    }
}
