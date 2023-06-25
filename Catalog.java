package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Catalog extends JPanel implements ActionListener {

    private AutentificareAdmin autentificareAdmin;
    private static Catalog instance;

    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 500;
    private String starePanel;

    private CardLayout cardLayout;

    private VizualizareCatalog vizualizarePanel;

    private JButton buttonAdaugare;
    private JButton buttonMonitorizare;
    private JButton buttonVizualizare;
    private JButton buttonInapoi;


    public Catalog() {
        instance=this;

        buttonAdaugare = new JButton();
        buttonMonitorizare = new JButton();
        buttonVizualizare = new JButton();
        buttonInapoi = new JButton();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalGlue());
        this.add(buttonAdaugare);
        this.add(Box.createVerticalGlue());
        this.add(buttonMonitorizare);
        this.add(Box.createVerticalGlue());
        this.add(buttonVizualizare);
        this.add(Box.createVerticalGlue());
        this.add(buttonInapoi);
        this.add(Box.createVerticalGlue());

        vizualizarePanel=new VizualizareCatalog();

        this.setButtonNames();
        this.allignButton();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        cardLayout=new CardLayout();
        autentificareAdmin=new AutentificareAdmin();
        buttonInapoi.addActionListener(this);
        buttonAdaugare.addActionListener(this);
        buttonMonitorizare.addActionListener(this);
        buttonVizualizare.addActionListener(this);
    }

    private void setButtonNames(){
        buttonAdaugare.setText("Adauga produs");
        buttonMonitorizare.setText("Monitorizare produs");
        buttonVizualizare.setText("Vizualizeaza catalogul");
        buttonInapoi.setText("Inapoi");
    }
    private void allignButton(){
        buttonAdaugare.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonMonitorizare.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonVizualizare.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonInapoi.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    public static Catalog getInstance(){
        return instance;
    }
    public int getSCREEN_WIDTH(){
        return SCREEN_WIDTH;
    }
    public int getSCREEN_HEIGHT(){
        return SCREEN_HEIGHT;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonInapoi){
            cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(ERestaurant.getInstance().getContentPane(), "Menu");
            ERestaurant.getInstance().setSize(new Dimension(ERestaurant.getInstance().getSCREEN_WIDTH(), ERestaurant.getInstance().getSCREEN_HEIGHT()));
            ERestaurant.getInstance().setLocationRelativeTo(null);
        } else if (e.getSource() == buttonAdaugare){
            starePanel = "Adaugare";
            ERestaurant.getInstance().getContentPane().add(autentificareAdmin, "Autentificare");
            cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(ERestaurant.getInstance().getContentPane(), "Autentificare");
            ERestaurant.getInstance().setSize(new Dimension(ERestaurant.getInstance().getSCREEN_WIDTH(), ERestaurant.getInstance().getSCREEN_HEIGHT()));
            ERestaurant.getInstance().setLocationRelativeTo(null);
        } else if (e.getSource() == buttonMonitorizare){
            starePanel = "Monitorizare";
            ERestaurant.getInstance().getContentPane().add(autentificareAdmin, "Autentificare");
            cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(ERestaurant.getInstance().getContentPane(), "Autentificare");
            ERestaurant.getInstance().setSize(new Dimension(ERestaurant.getInstance().getSCREEN_WIDTH(), ERestaurant.getInstance().getSCREEN_HEIGHT()));
            ERestaurant.getInstance().setLocationRelativeTo(null);
        } else if (e.getSource() == buttonVizualizare){
            ERestaurant.getInstance().getContentPane().add(vizualizarePanel, "Vizualizare");
            cardLayout=(CardLayout) getParent().getLayout();
            cardLayout.show(ERestaurant.getInstance().getContentPane(), "Vizualizare");
            ERestaurant.getInstance().setSize(new Dimension(vizualizarePanel.getSCREEN_WIDTH(), vizualizarePanel.getSCREEN_HEIGHT()));
            ERestaurant.getInstance().setLocationRelativeTo(null);
        }
    }
    public String getStarePanel(){
        return starePanel;
    }
}
