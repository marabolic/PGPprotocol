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
                if (checkPassword()){
                    JOptionPane.showMessageDialog(frame, "Your keys are generated.");
                    frame.dispose();
                }else{
                    JOptionPane.showMessageDialog(frame, "Wrong password");
                }
            }
        });
    }

    public PasswordForm(){

        initComponents();
    }

    public boolean checkPassword(){
        String pass = passwordField.getPassword().toString();
        if (password == null){
            createPassword();
            return true;
        }

        boolean equals = pass.equals(password.toString());
        return equals;
    }

    public void createPassword(){
        password = passwordField.getPassword();
    }

    public char[] getPassword(){
        return password;
    }

}
