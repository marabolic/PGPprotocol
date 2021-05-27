package etf.openpgp.ba170578dbm170614d.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateKeyForm {
    private JTextField nameTextField;
    private JButton submitButton;
    private JTextField emailTextField;
    private JComboBox asymetricAlgorithms;
    private JTable KeyTable;
    private JButton deleteKeyButton;
    private JPanel GenerateKeyFormPanel;

    private static final String regex = "^(.+)@(.+)$";

    boolean ValidEmail(String email){
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()) return true;
        else return false;
    }

    void initComponents(){
        String [] algrorithms = {"DSA 1024", "DSA 2048", "El Gamal 1024", "El Gamal 2048", "El Gamal 4096"};
        asymetricAlgorithms = new JComboBox(algrorithms);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ValidEmail(emailTextField.getText()) && nameTextField.getText() != null){
                    new PasswordForm();
                }
            }
        });
    }

    GenerateKeyForm() {
        initComponents();


    }


    public static void main(String[] args) {
        GenerateKeyForm gk = new GenerateKeyForm();

    }


}
