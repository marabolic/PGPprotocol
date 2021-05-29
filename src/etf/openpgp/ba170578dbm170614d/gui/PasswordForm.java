package etf.openpgp.ba170578dbm170614d.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordForm {
    private JPasswordField passwordField;
    private JButton passwordButton;
    private JPanel passwordPanel;
    private JFrame frame;

    private char[] password;

    void initComponents(){
        frame = new JFrame("Password");
        frame.setContentPane(passwordPanel);
        frame.pack();
        frame.setVisible(true);

        passwordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                password = passwordField.getPassword();
                if(password.length != 0){
                    JOptionPane.showMessageDialog(frame, "Your keys are generated.");
                    frame.dispose();
                }else{
                    JOptionPane.showMessageDialog(frame, "Enter the password please.");
                }
            }
        });
    }

    public PasswordForm(){

        initComponents();
    }

    public boolean checkPassword(){
        String pass = passwordField.getPassword().toString();
        //TODO: send to Aurhentication to check
        return pass == "123";
    }

}
