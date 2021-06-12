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

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] message = messageTextArea.getText().toCharArray();
                byte [] messageInBytes = new byte[message.length];
                for (int i = 0; i < message.length; i++){
                    messageInBytes[i] = (byte) message[i];
                }
                if (message != null){
                    if (signatureCheckBox.isSelected()){
                        try {
                            int index = privateKeySignatureBox.getSelectedIndex();
                            //TODO: find selected private key for signature initialization
                            int i = 0;
                            PGPSecretKeyRing temp = null;
                            for (Iterator<PGPSecretKeyRing> iterator = GenerateKeys.pgpSecretKeyRing.iterator(); iterator.hasNext(); ) {
                                temp = iterator.next();
                                if (i == index){
                                    break;
                                }
                                i++;
                            }
                            //TODO: sign

                            Signature signature = Signature.getInstance("DSA", "BC");
                            PBESecretKeyDecryptor decryptor = new BcPBESecretKeyDecryptorBuilder(
                                    new BcPGPDigestCalculatorProvider()).build(passwordField.getText().toCharArray());

                            signature.initSign((PrivateKey) temp.getSecretKey().extractPrivateKey(decryptor), new SecureRandom());
                            signature.update(messageInBytes);
                            signature.sign();
                        } catch (NoSuchAlgorithmException ex) {
                            ex.printStackTrace();
                        } catch (NoSuchProviderException ex) {
                            ex.printStackTrace();
                        } catch (SignatureException ex) {
                            ex.printStackTrace();
                        } catch (PGPException ex) {
                            ex.printStackTrace();
                        } catch (InvalidKeyException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (encriptionCheckBox.isSelected()){

                    }
                    if(compressionCheckBox.isSelected()){

                    }
                    if(conversionCheckBox.isSelected()){

                    }
                }

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
                if(encriptionCheckBox.isSelected() && !signatureCheckBox.isSelected()){
                    PGPPublicKeyRing nextPublic = null;
                    for (Iterator<PGPPublicKeyRing> iterator = GenerateKeys.pgpPublicKeyRing.iterator(); iterator.hasNext();) {
                        nextPublic = iterator.next();
                        if ((nextPublic.getPublicKey().getKeyID() + "").equals(publicKeyEncryptionBox.getSelectedItem())) {
                            break;
                        }
                    }

                    int selectedIndex = symmetricAlgorithmBox.getSelectedIndex();
                    int symmetricAlg = 0;

                    switch (selectedIndex){
                        case 0: symmetricAlg = 0; break;
                        case 1: symmetricAlg = 1;
                    }

                    if(messageTextArea.getText().equals("")){
                        JOptionPane.showMessageDialog(frame, "Message field is empty.");
                    }else{
                        if(EncryptMessage.EncryptMessage(messageTextArea.getText(), nextPublic.getPublicKey(), conversionCheckBox.isSelected(), compressionCheckBox.isSelected(), symmetricAlg)){
                            JOptionPane.showMessageDialog(frame, "The message was successfully encrypted");
                        }else{
                            JOptionPane.showMessageDialog(frame, "An error has occurred.");
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(frame, "Error.");
                }


            }
        });

    }

    public SendMessageFrom(JFrame MainFrame){
        initComponent(MainFrame);
    }

}
