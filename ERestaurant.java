package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ERestaurant extends JFrame implements ActionListener {
    private static ERestaurant instance;

    private final int SCREEN_WIDTH=350;
    private final int SCREEN_HEIGHT=350;

    private CardLayout cardLayout;

    private Catalog catalog;

    private JButton buttonCatalog;
    private JButton buttonStaff;
    private JButton buttonComenzi;

    private JPanel menuPanel;
    private JPanel catalogPanel;
    private JPanel staffPanel;
    private JPanel comenziPanel;

    private BufferedWriter bufferedWriter;
    private File file;

    public ERestaurant() {
        file=new File("Comenzi.txt");
        try{
            bufferedWriter=new BufferedWriter(new FileWriter(file));
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        this.setTitle("Restaurant");
        instance=this;

        menuPanel=new JPanel();
        catalogPanel=new Catalog();
        staffPanel=new Staff();
        comenziPanel=new AdministrareComenzi();

        buttonCatalog = new JButton("Catalog");
        buttonStaff = new JButton("Staff");
        buttonComenzi = new JButton("Administrare Comenzi");

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(buttonCatalog);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(buttonStaff);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(buttonComenzi);
        menuPanel.add(Box.createVerticalGlue());

        this.fixButtons();


        this.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);

        cardLayout=new CardLayout();
        this.getContentPane().setLayout(cardLayout);
        this.getContentPane().add(menuPanel, "Menu");
        this.getContentPane().add(catalogPanel, "Catalog");
        this.getContentPane().add(staffPanel, "Staff");
        this.getContentPane().add(comenziPanel, "Comenzi");
        cardLayout.show(getContentPane(), "Menu");

        buttonCatalog.addActionListener(this);
        buttonStaff.addActionListener(this);
        buttonComenzi.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonCatalog){
            cardLayout.show(getContentPane(), "Catalog");
            pack();
            setLocationRelativeTo(null);
        } else if (e.getSource() == buttonStaff) {
            cardLayout.show(getContentPane(), "Staff");
            this.setSize(new Dimension(((Staff) staffPanel).getScreenWidth(), ((Staff) staffPanel).getScreenHeight()));
            setLocationRelativeTo(null);
        } else if (e.getSource() == buttonComenzi){
            cardLayout.show(getContentPane(), "Comenzi");
            this.setSize(new Dimension(((AdministrareComenzi) comenziPanel).getSCREEN_WIDTH(), ((AdministrareComenzi) comenziPanel).getSCREEN_HEIGHT()));
            setLocationRelativeTo(null);
        }
    }

    private void fixButtons(){
        buttonCatalog.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonStaff.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonComenzi.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    public static ERestaurant getInstance(){
        return instance;
    }
    public int getSCREEN_WIDTH(){
        return SCREEN_WIDTH;
    }
    public int getSCREEN_HEIGHT(){
        return SCREEN_HEIGHT;
    }
}
