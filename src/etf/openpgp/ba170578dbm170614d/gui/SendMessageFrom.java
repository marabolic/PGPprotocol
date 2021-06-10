package etf.openpgp.ba170578dbm170614d.gui;

import etf.openpgp.ba170578dbm170614d.pgp.GenerateKeys;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;

public class SendMessageFrom {
    private JCheckBox encriptionCheckBox;
    private JCheckBox signatureCheckBox;
    private JCheckBox compressionCheckBox;
    private JCheckBox conversionCheckBox;
    private JPanel sendMessageFormPanel;
    private JList publicKeyList;
    private JComboBox privateKeySignatureBox;
    private JComboBox publicKeyEncryptionBox;
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

        publicKeyEncryptionBox.removeAll();
        for (Iterator<PGPPublicKeyRing> iterator = GenerateKeys.pgpPublicKeyRing.iterator(); iterator.hasNext(); ) {
            PGPPublicKeyRing next = iterator.next();
            publicKeyEncryptionBox.addItem(next.getPublicKey().getKeyID() + "");
            System.out.println(next.getPublicKey().getKeyID() + "PUBLIC PUBLIC");
        }

        publicKeyEncryptionBox.setEnabled(false);


        privateKeySignatureBox.removeAll();
        for (Iterator<PGPSecretKeyRing> iterator = GenerateKeys.pgpSecretKeyRing.iterator(); iterator.hasNext();) {
            PGPSecretKeyRing next = iterator.next();
            privateKeySignatureBox.addItem(next.getSecretKey().getKeyID() + "");
            System.out.println(next.getSecretKey().getKeyID() + "SECRET SECRET");
        }

        privateKeySignatureBox.setEnabled(false);


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
                    //PasswordForm pf = new PasswordForm(frame);
                    String password = JOptionPane.showInputDialog(frame,"Password");
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
                    publicKeyEncryptionBox.setEnabled(true);
                }else{
                    symmetricAlgorithmBox.setEnabled(false);
                    publicKeyEncryptionBox.setEnabled(false);
                }
            }
        });
    }

    public SendMessageFrom(JFrame MainFrame){
        initComponent(MainFrame);
    }
}
