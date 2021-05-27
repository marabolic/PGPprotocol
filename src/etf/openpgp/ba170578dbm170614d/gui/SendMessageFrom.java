package etf.openpgp.ba170578dbm170614d.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SendMessageFrom {
    private JCheckBox encriptionCheckBox;
    private JCheckBox signatureCheckBox;
    private JCheckBox compressionCheckBox;
    private JCheckBox conversionCheckBox;
    private JPanel SendMessageFormPanel;
    private JList PublicKeyList;
    private JComboBox PrivateKeySignatureBox;
    private JComboBox SymmetricAlgorithmBox;
    private JButton sendButton;
    private JTextArea MessageTextArea;
    private JButton BackButton;
    private JPasswordField PasswordField;
    private JFrame frame;

    void initComponent(JFrame MainFrame){
        frame = new JFrame("Generate Key Form");
        frame.setContentPane(SendMessageFormPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        PrivateKeySignatureBox.addItem("Test 1");
        PrivateKeySignatureBox.addItem("Test 2");

        PrivateKeySignatureBox.setEnabled(false);

        DefaultListModel listModel = new DefaultListModel();
        listModel.addElement("test 1");
        listModel.addElement("test 2");
        listModel.addElement("test 3");

        PublicKeyList.setModel(listModel);

        PublicKeyList.setEnabled(false);

        SymmetricAlgorithmBox.addItem("3DES + EDE");
        SymmetricAlgorithmBox.addItem("AES 128 bita");

        SymmetricAlgorithmBox.setSelectedIndex(0);

        SymmetricAlgorithmBox.setEnabled(false);

        PasswordField.setEnabled(false);

        frame.setVisible(true);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Message sent successfully.");
                frame.dispose();
                MainFrame.setVisible(true);
            }
        });

        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainFrame.setVisible(true);
            }
        });

        signatureCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(signatureCheckBox.isSelected()){
                    PrivateKeySignatureBox.setEnabled(true);
                    PasswordField.setEnabled(true);
                }else{
                    PrivateKeySignatureBox.setEnabled(false);
                    PasswordField.setEnabled(false);
                }
            }
        });

        encriptionCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(encriptionCheckBox.isSelected()){
                    SymmetricAlgorithmBox.setEnabled(true);
                    PublicKeyList.setEnabled(true);
                }else{
                    SymmetricAlgorithmBox.setEnabled(false);
                    PublicKeyList.setEnabled(false);
                }
            }
        });
    }

    public SendMessageFrom(JFrame MainFrame){
        initComponent(MainFrame);
    }
}
