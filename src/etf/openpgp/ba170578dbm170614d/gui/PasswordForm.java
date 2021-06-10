package etf.openpgp.ba170578dbm170614d.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordForm {
    private JPasswordField passwordField;
    private JButton passwordButton;
    private JPanel passwordPanel;
    private JDialog frame;

    private char[] password;

    void initComponents(Frame owner){
        frame = new JDialog(owner, "Password", true);
        frame.setContentPane(passwordPanel);
        frame.pack();
        frame.setVisible(true);

        passwordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: set modal
                createPassword();
                if (password.length > 0){
                    JOptionPane.showMessageDialog(frame, "Your keys are generated.");
                    frame.dispose();

                }else{
                    JOptionPane.showMessageDialog(frame, "Wrong password");
                }
            }
        });
    }

    public PasswordForm(JFrame owner){
        initComponents(owner);
    }

    public static boolean checkPassword(String existingPassword, String [] typedInPassword){
        if (existingPassword.equals(typedInPassword)){
            return true;
        }
        return false;
    }

    public void createPassword(){
        password = passwordField.getPassword();
    }

    public char[] getPassword(){
        return password;
    }

}
