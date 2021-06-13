package etf.openpgp.ba170578dbm170614d.gui;

import etf.openpgp.ba170578dbm170614d.pgp.EncryptMessage;
import etf.openpgp.ba170578dbm170614d.pgp.GenerateKeys;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.security.*;
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
    private JTextField passwordField;

    private JFrame frame;

    /**
     *
     * @param MainFrame
     */
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

        passwordField.setEnabled(false);

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
                    privateKeySignatureBox.setEnabled(true);
                    passwordField.setEnabled(true);
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

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PGPPublicKey publicEncryptKey = null;

                if(encriptionCheckBox.isSelected()) {
                    PGPPublicKeyRing nextPublicKeyRing = null;
                    for (Iterator<PGPPublicKeyRing> iterator = GenerateKeys.pgpPublicKeyRing.iterator(); iterator.hasNext(); ) {
                        nextPublicKeyRing = iterator.next();
                        if ((nextPublicKeyRing.getPublicKey().getKeyID() + "").equals(publicKeyEncryptionBox.getSelectedItem())) {
                            break;
                        }
                    }

                    for (Iterator<PGPPublicKey> iterator = nextPublicKeyRing.getPublicKeys(); iterator.hasNext(); ) {
                        publicEncryptKey = iterator.next();
                        if (publicEncryptKey.isEncryptionKey()) {
                            System.out.println("ENKRIPTOVANI");
                            break;
                        } else {
                            System.out.println("NIJE ENKRIPTOVANI");
                        }
                    }
                }

                String name = "";
                PGPSecretKey secretSignedKey = null;
                PGPPrivateKey privateKey = null;

                if(signatureCheckBox.isSelected()){
                    PGPSecretKeyRing next = null;
                    PGPSecretKey secretKey = null;

                    for (Iterator<PGPSecretKeyRing> iterator = GenerateKeys.pgpSecretKeyRing.iterator(); iterator.hasNext();) {
                        next = iterator.next();
                        if ((next.getSecretKey().getKeyID() + "").equals(privateKeySignatureBox.getSelectedItem())) {
                            break;
                        }

                    }

                    for (Iterator<PGPSecretKey> iterator = next.getSecretKeys(); iterator.hasNext(); ) {
                        secretSignedKey = iterator.next();
                        if (secretSignedKey.isSigningKey()) {
                            System.out.println("SIGNING KEY");
                            break;
                        } else {
                            System.out.println("NIJE SIGNINGKEY");
                        }
                    }

                    secretKey = next.getSecretKey();


                    Iterator<String> it = secretKey.getPublicKey().getUserIDs();
                    name += it.next();


                    //private key

                    PBESecretKeyDecryptor secretKeyDecryptor = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(passwordField.getText().toCharArray());
                    try {
                        privateKey = secretKey.extractPrivateKey(secretKeyDecryptor);

                    } catch (PGPException ex) {
                        JOptionPane.showMessageDialog(frame, "Wrong password.");
                    }


                }

                int selectedIndex = symmetricAlgorithmBox.getSelectedIndex();
                int symmetricAlg = 0;

                switch (selectedIndex) {
                    case 0:
                        symmetricAlg = 0;
                        break;
                    case 1:
                        symmetricAlg = 1;
                }

                if(messageTextArea.getText().equals("")){
                    JOptionPane.showMessageDialog(frame, "Message field is empty.");
                }else if(encriptionCheckBox.isSelected() && !signatureCheckBox.isSelected()){
                    if(EncryptMessage.EncryptMessage(messageTextArea.getText(), publicEncryptKey, conversionCheckBox.isSelected(), compressionCheckBox.isSelected(), symmetricAlg)){
                        JOptionPane.showMessageDialog(frame, "The message was successfully encrypted");
                    }else{
                        JOptionPane.showMessageDialog(frame, "An error has occurred.");
                    }
                }else if(signatureCheckBox.isSelected()){
                    if(EncryptMessage.SignEncryptMessage(messageTextArea.getText(), name, publicEncryptKey, secretSignedKey, privateKey, conversionCheckBox.isSelected(), compressionCheckBox.isSelected(), encriptionCheckBox.isSelected(), symmetricAlg)){
                        JOptionPane.showMessageDialog(frame, "The message was successfully encrypted");
                    }else{
                        JOptionPane.showMessageDialog(frame, "An error has occurred.");
                    }
                }else{
                    JOptionPane.showMessageDialog(frame, "Error.");
                }

            }
        });

    }


    /**
     *
     * @param MainFrame
     */
    public SendMessageFrom(JFrame MainFrame){
        initComponent(MainFrame);
    }

}
