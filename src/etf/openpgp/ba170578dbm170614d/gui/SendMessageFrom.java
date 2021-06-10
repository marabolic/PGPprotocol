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
    private JPanel sendMessageFormPanel;
    private JList publicKeyList;
    private JComboBox privateKeySignatureBox;
    private JComboBox symmetricAlgorithmBox;
    private JButton sendButton;
    private JTextArea messageTextArea;
    private JButton backButton;
    private JFrame frame;

    void initComponent(JFrame MainFrame){
        frame = new JFrame("Generate Key Form");
        frame.setContentPane(sendMessageFormPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        privateKeySignatureBox.addItem("Test 1");
        privateKeySignatureBox.addItem("Test 2");

        privateKeySignatureBox.setEnabled(false);

        DefaultListModel listModel = new DefaultListModel();
        listModel.addElement("test 1");
        listModel.addElement("test 2");
        listModel.addElement("test 3");

        publicKeyList.setModel(listModel);

        publicKeyList.setEnabled(false);

        symmetricAlgorithmBox.addItem("3DES + EDE");
        symmetricAlgorithmBox.addItem("AES 128 bits");

        symmetricAlgorithmBox.setSelectedIndex(0);

        symmetricAlgorithmBox.setEnabled(false);


        frame.setVisible(true);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Message sent successfully.");
                frame.dispose();
                MainFrame.setVisible(true);
            }
        });

        backButton.addActionListener(new ActionListener() {
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
                    PasswordForm pf = new PasswordForm(frame);
                    //TODO: password checking
                    if (true){
                        privateKeySignatureBox.setEnabled(true);
                    }

                }else{
                    privateKeySignatureBox.setEnabled(false);
                }
            }
        });

        encriptionCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(encriptionCheckBox.isSelected()){
                    symmetricAlgorithmBox.setEnabled(true);
                    publicKeyList.setEnabled(true);
                }else{
                    symmetricAlgorithmBox.setEnabled(false);
                    publicKeyList.setEnabled(false);
                }
            }
        });
    }

    public SendMessageFrom(JFrame MainFrame){
        initComponent(MainFrame);
    }
}
