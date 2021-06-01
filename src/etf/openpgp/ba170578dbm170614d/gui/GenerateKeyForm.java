package etf.openpgp.ba170578dbm170614d.gui;

import etf.openpgp.ba170578dbm170614d.pgp.GenerateKeys;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateKeyForm {
    private JTextField nameTextField;
    private JButton submitButton;
    private JTextField emailTextField;
    private JComboBox asymetricAlgorithms;
    private JTable KeyTable;
    private JButton deleteKeyButton;
    private JPanel generateKeyFormPanel;
    private JButton BackButton;
    private JFrame frame;

    private static final String regex = "^(.+)@(.+)$";

    boolean ValidEmail(String email){
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()) return true;
        else return false;
    }

    void initComponents(JFrame MainFrame){
        frame = new JFrame("Generate Key Form");
        frame.setContentPane(generateKeyFormPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        asymetricAlgorithms.addItem("ElGamal 1024");
        asymetricAlgorithms.addItem("ElGamal 2048");
        asymetricAlgorithms.addItem("ElGamal 4096");

        asymetricAlgorithms.setSelectedIndex(0);

        frame.setVisible(true);



        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ValidEmail(emailTextField.getText()) && nameTextField.getText() != null){
                    PasswordForm pf= new PasswordForm();
                    GenerateKeys generateKeys = new GenerateKeys();
                    generateKeys.generateKeys();
                }else{
                    JOptionPane.showMessageDialog(frame, "Something wrong, check your email or name.");
                }
            }
        });

        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainFrame.setVisible(true);
            }
        });
    }

    GenerateKeyForm(JFrame frame) {
        initComponents(frame);
    }

}
