package etf.openpgp.ba170578dbm170614d.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordForm {
    private JPasswordField PasswordField;
    private JButton PasswordButton;
    private JPanel PasswordPanel;
    private JFrame frame;

    private char[] password;

    void initComponents(){
        frame = new JFrame("Password");
        frame.setContentPane(PasswordPanel);
        frame.pack();
        frame.setVisible(true);

        PasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                password = PasswordField.getPassword();
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
}
