package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class VizualizareCatalog extends JPanel implements ActionListener, ItemListener {
    private int SCREEN_WIDTH = 500;
    private int SCREEN_HEIGHT = 700;

    private BorderLayout borderLayout;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gbc;
    private CardLayout cardLayout;

    private JComboBox<String> categorieBox;
    private String selectedItem;
    private String[] pathFiles = new String[10];
    static int k=0;

    private JPanel southPanel;
    private JPanel centerPanel;
    private JPanel northPanel;

    private JButton backButton;
    private JButton printButton;

    private ArrayList<String> etichete;
    private BufferedReader bufferedReader;

    public VizualizareCatalog() {
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

        cardLayout = new CardLayout();
        gridBagLayout = new GridBagLayout();
        gbc = new GridBagConstraints();
        borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        southPanel = new JPanel();
        southPanel.setLayout(gridBagLayout);
        centerPanel = new JPanel();
        centerPanel.setLayout(gridBagLayout);
        northPanel = new JPanel();
        northPanel.setLayout(gridBagLayout);

        categorieBox = new JComboBox<>(etichete.toArray(new String[0]));
        categorieBox.setMaximumSize(new Dimension(150, 100));
        gbc.insets = new Insets(50, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        northPanel.add(categorieBox, gbc);
        renderNull();

        backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 20, 20, 0);
        southPanel.add(backButton, gbc);

        printButton=new JButton("Print");
        categorieBox.addItemListener(this);
        backButton.addActionListener(this);
        printButton.addActionListener(this);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    public int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(ERestaurant.getInstance().getContentPane(), "Catalog");
            ERestaurant.getInstance().setSize(new Dimension(600, 500));
            ERestaurant.getInstance().setLocationRelativeTo(null);
            centerPanel.removeAll();
            renderNull();
        } else if (e.getSource() == printButton) {
            printRecord(centerPanel);
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

            }
        }
    }

    private void printRecord(JPanel myPanel){
        PrinterJob printerJob=PrinterJob.getPrinterJob();
        printerJob.setJobName("Print Catalog");
        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex>0)
                    return Printable.NO_SUCH_PAGE;
                Graphics2D graphics2D= (Graphics2D) graphics;
                graphics2D.translate(pageFormat.getImageableX()*2, pageFormat.getImageableY()*2);
                graphics2D.scale(0.5,0.5);
                myPanel.paint(graphics2D);
                return Printable.PAGE_EXISTS;
            }
        });
        boolean returnResult=printerJob.printDialog();
        if (returnResult)
            try{
                printerJob.print();
            } catch (PrinterException printerException){
                JOptionPane.showMessageDialog(this, "Print error: " + printerException.getMessage());
            }
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
                    String[] newRow={Integer.toString(index) ,array.get(0), array.get(1), array.get(2), array.get(3)};
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
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.weightx=1;
        gbc.gridwidth=1;
        JScrollPane scrollPane = new JScrollPane(tabel);
        tabel.setPreferredScrollableViewportSize(new Dimension(350, 150));
        centerPanel.removeAll();
        centerPanel.add(scrollPane, gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.anchor=GridBagConstraints.CENTER;
        centerPanel.add(printButton, gbc);
        this.add(centerPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}
