package proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutentificareAdmin extends JPanel implements ActionListener {
    LoginInfo data;

    private CardLayout cardLayout;

    private AdaugareProdus adaugarePanel;
    private StergereProdus stergereProdus;

    private JLabel title;
    private JLabel idLabel;
    private JLabel passwordLabel;
    private JTextField idTextField;
    private JPasswordField passwordTextField;
    private JButton loginButton;
    private JButton backButton;

    AutentificareAdmin(){
        this.setSize(new Dimension(ERestaurant.getInstance().getSCREEN_WIDTH(),ERestaurant.getInstance().getSCREEN_HEIGHT()));
        loginButton=new JButton("Log in");
        backButton=new JButton("Back");

        this.setLayout(null);

        idLabel=new JLabel("ID: ");
        passwordLabel=new JLabel("Password: ");
        idTextField=new JTextField();
        passwordTextField=new JPasswordField();
        title=new JLabel("Administrator");

        idLabel.setBounds(60,100, 75,25);
        passwordLabel.setBounds(20, 150, 75,25);
        idTextField.setBounds(100, 100, 200,35);
        passwordTextField.setBounds(100, 150, 200,35);
        title.setBounds(130, 20, 100,25);
        backButton.setBounds(140,250, 70,25);
        loginButton.setBounds(140, 210, 70,25);

        this.add(idLabel);
        this.add(passwordLabel);
        this.add(idTextField);
        this.add(passwordTextField);
        this.add(title);


        cardLayout=new CardLayout();
        loginButton.addActionListener(this);
        backButton.addActionListener(this);

        this.add(loginButton);
        this.add(backButton);
        data=new LoginInfo();
        adaugarePanel = new AdaugareProdus();
        stergereProdus=new StergereProdus();
    }
    public String getId(){
        return data.getId();
    }
    public String getPassword(){
        return data.getPASSWORD();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton){
            cardLayout=(CardLayout) getParent().getLayout();
            cardLayout.show(ERestaurant.getInstance().getContentPane(), "Catalog");
            ERestaurant.getInstance().setSize(new Dimension(Catalog.getInstance().getSCREEN_WIDTH(), Catalog.getInstance().getSCREEN_WIDTH()));
            ERestaurant.getInstance().setLocationRelativeTo(null);
            idTextField.setText("");
            passwordTextField.setText("");
        } else if (e.getSource() == loginButton){
            cardLayout= (CardLayout) getParent().getLayout();
            ERestaurant.getInstance().getContentPane().add(adaugarePanel, "Adauga");
            ERestaurant.getInstance().getContentPane().add(stergereProdus, "Monitorizare");
            char[] password=passwordTextField.getPassword();
            String switcherPassword=new String(password);
            if ((data.getId().equals(idTextField.getText())) && (data.getPASSWORD().equals(switcherPassword)) == true) {
                if (Catalog.getInstance().getStarePanel() == "Adaugare") {
                    cardLayout.show(ERestaurant.getInstance().getContentPane(), "Adauga");
                    ERestaurant.getInstance().setSize(new Dimension(adaugarePanel.getSCREEN_WIDTH(),adaugarePanel.getSCREEN_HEIGHT()));
                    ERestaurant.getInstance().setLocationRelativeTo(null);
                }
                else if (Catalog.getInstance().getStarePanel() == "Monitorizare") {
                    cardLayout.show(ERestaurant.getInstance().getContentPane(), "Monitorizare");
                    ERestaurant.getInstance().setSize(new Dimension(adaugarePanel.getSCREEN_WIDTH(),adaugarePanel.getSCREEN_HEIGHT()));
                    ERestaurant.getInstance().setLocationRelativeTo(null);
                }
            } else JOptionPane.showMessageDialog(null, "Invalid data");
            idTextField.setText("");
            passwordTextField.setText("");
        }
    }

    public JButton getLoginButton(){
        loginButton.removeActionListener(this);
        return loginButton;
    }
    public JButton getBackButton(){
        backButton.removeActionListener(this);
        return backButton;
    }
    public JTextField getIdTextField(){
        return idTextField;
    }
    public JTextField getPasswordTextField(){
        return passwordTextField;
    }
}
