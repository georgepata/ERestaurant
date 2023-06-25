package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Staff extends JPanel {
    private static int SCREEN_WIDTH=400;
    private static int SCREEN_HEIGHT=400;

    private JLabel jTitle;

    private String[] items = {"Admin", "Chelner"};
    private CardLayout cardLayout;
    private GridBagLayout gridBagLayout;
    private JComboBox<String> categoryBox;
    private AutentificareAdmin autentificareAdmin;

    private JPanel northPanel;
    private JPanel centerPanel;
    private JPanel southPanel;

    private JPanel gestionareChelneri;
    private VizualizareAngajati vizualizareAngajati;

    private JButton loginButton;
    private JButton backButton;
    private JButton intoarcereButton;

    public Staff(){
        vizualizareAngajati=new VizualizareAngajati();
        cardLayout=new CardLayout();
        this.setLayout(new BorderLayout());

        gestionareChelneri=new GestionareChelneri();

        gridBagLayout=new GridBagLayout();
        centerPanel=new JPanel();
        centerPanel.setLayout(gridBagLayout);
        categoryBox=new JComboBox<>(items);
        renderNull();

        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1;
        gbc.anchor=GridBagConstraints.CENTER;
        centerPanel.add(categoryBox, gbc);

        intoarcereButton=new JButton("Back");
        southPanel=new JPanel();
        southPanel.setLayout(gridBagLayout);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1;
        gbc.anchor=GridBagConstraints.LINE_START;
        gbc.insets=new Insets(0,20,20,0);
        southPanel.add(intoarcereButton, gbc);

        jTitle=new JLabel("Alegeti functia");
        northPanel=new JPanel();
        northPanel.setLayout(gridBagLayout);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1;
        gbc.insets=new Insets(40, 0, 0 ,0);
        gbc.anchor=GridBagConstraints.CENTER;
        northPanel.add(jTitle, gbc);

        autentificareAdmin=new AutentificareAdmin();


        categoryBox.addItemListener(new itemListener());
        loginButton=autentificareAdmin.getLoginButton();
        backButton = autentificareAdmin.getBackButton();
        loginButton.addActionListener(new buttonImplement());
        backButton.addActionListener(new buttonImplement());
        intoarcereButton.addActionListener(new buttonImplement());

        this.add(centerPanel);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(northPanel, BorderLayout.NORTH);
    }
    private class itemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e){
            if (e.getStateChange() == ItemEvent.SELECTED){
                if (e.getItem().equals("Admin")){
                    cardLayout=(CardLayout) getParent().getLayout(); 
                    ERestaurant.getInstance().getContentPane().add(autentificareAdmin, "Autentificare");
                    cardLayout.show(ERestaurant.getInstance().getContentPane(), "Autentificare");
                    ERestaurant.getInstance().setSize(new Dimension(ERestaurant.getInstance().getSCREEN_WIDTH(), ERestaurant.getInstance().getSCREEN_HEIGHT()));
                    ERestaurant.getInstance().setLocationRelativeTo(null);
                } else if (e.getItem().equals("Chelner")){
                    vizualizareAngajati=new VizualizareAngajati();
                    cardLayout=(CardLayout) getParent().getLayout();
                    ERestaurant.getInstance().getContentPane().add(vizualizareAngajati, "Vizualizare");
                    cardLayout.show(ERestaurant.getInstance().getContentPane(), "Vizualizare");
                    renderNull();
                }
            }
        }
    }

    public int getScreenWidth(){
        return SCREEN_WIDTH;
    }

    public int getScreenHeight(){
        return SCREEN_HEIGHT;
    }

    public void renderNull(){
        categoryBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    value = "Select...";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        categoryBox.setSelectedItem(null);
    }

    private class buttonImplement implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == loginButton){
                if (autentificareAdmin.getIdTextField().getText().equals(autentificareAdmin.getId()) && autentificareAdmin.getPasswordTextField().getText().equals(autentificareAdmin.getPassword())){
                    cardLayout = (CardLayout) getParent().getLayout();
                    ERestaurant.getInstance().getContentPane().add(gestionareChelneri, "Gestionare");
                    cardLayout.show(ERestaurant.getInstance().getContentPane(), "Gestionare");
                    ERestaurant.getInstance().setSize(new Dimension(((GestionareChelneri) gestionareChelneri).getScreenWidth(), ((GestionareChelneri) gestionareChelneri).getScreenHeight()));
                    ERestaurant.getInstance().setLocationRelativeTo(null);
                } else
                    JOptionPane.showMessageDialog(null, "Invalid data");
                autentificareAdmin.getIdTextField().setText("");
                autentificareAdmin.getPasswordTextField().setText("");
            } else if (e.getSource() == backButton){
                cardLayout = (CardLayout) getParent().getLayout();
                cardLayout.show(ERestaurant.getInstance().getContentPane(), "Staff");
                ERestaurant.getInstance().setSize(new Dimension(ERestaurant.getInstance().getSCREEN_WIDTH(), ERestaurant.getInstance().getSCREEN_HEIGHT()));
                ERestaurant.getInstance().setLocationRelativeTo(null);
                autentificareAdmin.getIdTextField().setText("");
                autentificareAdmin.getPasswordTextField().setText("");
            } else if (e.getSource() == intoarcereButton){
                cardLayout = (CardLayout) getParent().getLayout();
                cardLayout.show(ERestaurant.getInstance().getContentPane(), "Menu");
                ERestaurant.getInstance().setSize(new Dimension(ERestaurant.getInstance().getSCREEN_WIDTH(), ERestaurant.getInstance().getSCREEN_HEIGHT()));
                ERestaurant.getInstance().setLocationRelativeTo(null);
            }
            renderNull();
        }
    }
}
