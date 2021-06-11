package etf.openpgp.ba170578dbm170614d.gui;

import etf.openpgp.ba170578dbm170614d.pgp.GenerateKeys;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class ImportExport {
    private JButton importPublicKeyButton;
    private JPanel importExport;
    private JButton exportPublicKeyButton;
    private JComboBox publicKeyComboBox;
    private JComboBox secretKeyComboBox;
    private JButton exportSecretKeyButton;
    private JButton backButton;
    private JButton importSecretKeyButton;
    private JFrame frame;
    private JFileChooser importKeys;

    public void init(JFrame MainFrame){
        frame = new JFrame("Import/Export");
        frame.setContentPane(importExport);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        if(GenerateKeys.pgpSecretKeyRing != null){
            exportSecretKeysInComboBox();
            System.out.println("NIJE PRAZAN SECRET");
        }else{
            System.out.println("PRAZAN SECRET");
        }
        if(GenerateKeys.pgpPublicKeyRing != null){
            exportPublicKeysInComboBox();
            System.out.println("NIJE PRAZAN PUBLIC");
        }else{
            System.out.println("PRAZAN PUBLIC");
        }



        exportPublicKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportPublicKey();
            }
        });

        exportSecretKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportSecretKey();
            }
        });

        importPublicKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importPublicKey();
            }
        });
        importSecretKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importSecretKey();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainFrame.setVisible(true);
            }
        });
    }

    public ImportExport(JFrame MainFrame) {
        init(MainFrame);
    }

    private void exportPublicKeysInComboBox() {
        publicKeyComboBox.removeAll();
        for (Iterator<PGPPublicKeyRing> iterator = GenerateKeys.pgpPublicKeyRing.iterator(); iterator.hasNext(); ) {
            PGPPublicKeyRing next = iterator.next();
            publicKeyComboBox.addItem(next.getPublicKey().getKeyID() + "");
            System.out.println(next.getPublicKey().getKeyID() + "PUBLIC PUBLIC");
        }
    }

    private void exportSecretKeysInComboBox(){
        secretKeyComboBox.removeAll();
        for (Iterator<PGPSecretKeyRing> iterator = GenerateKeys.pgpSecretKeyRing.iterator(); iterator.hasNext();) {
            PGPSecretKeyRing next = iterator.next();
            secretKeyComboBox.addItem(next.getSecretKey().getKeyID() + "");
            System.out.println(next.getSecretKey().getKeyID() + "SECRET SECRET");
        }
    }

    private void exportPublicKey() {
        ArmoredOutputStream exportPublicKeyFile = null;

        String exportPublicKeyString = publicKeyComboBox.getSelectedItem().toString();


        PGPPublicKeyRing next = null;

        for (Iterator<PGPPublicKeyRing> iterator = GenerateKeys.pgpPublicKeyRing.iterator(); iterator.hasNext();) {
            next = iterator.next();
            if ((next.getPublicKey().getKeyID() + "").equals(exportPublicKeyString)) {
                break;
            }
        }

        try {
            exportPublicKeyFile = new ArmoredOutputStream(new FileOutputStream("ExportedPublicKey.asc"));
            next.encode(exportPublicKeyFile);
            exportPublicKeyFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                exportPublicKeyFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(frame, "You exported public key.");
    }

    private void exportSecretKey() {
        ArmoredOutputStream exportSecretKeyFile = null;

        String exportSecretKeyString = secretKeyComboBox.getSelectedItem().toString();

        PGPSecretKeyRing next = null;

        for (Iterator<PGPSecretKeyRing> iterator = GenerateKeys.pgpSecretKeyRing.iterator(); iterator.hasNext();) {
            next = iterator.next();
            if ((next.getSecretKey().getKeyID() + "").equals(exportSecretKeyString)) {
                break;
            }

        }
        try {
            exportSecretKeyFile = new ArmoredOutputStream(new FileOutputStream("ExportedSecretKey.asc"));
            next.encode(exportSecretKeyFile);
            exportSecretKeyFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                exportSecretKeyFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(frame, "You exported private key.");
    }

    private void importPublicKey() {
        PGPPublicKey importedPublicKey = null;
        importKeys = new javax.swing.JFileChooser();

        int returnVal = importKeys.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = importKeys.getSelectedFile();
            try {
                PGPPublicKeyRing importPublicKeyRing = new PGPPublicKeyRing(PGPUtil.getDecoderStream(new FileInputStream(file.getAbsolutePath())), new BcKeyFingerprintCalculator());
                importedPublicKey = importPublicKeyRing.getPublicKey();

                if(GenerateKeys.pgpPublicKeyRing == null){
                    GenerateKeys.pgpPublicKeyRing = new ArrayList<>();
                }
                GenerateKeys.pgpPublicKeyRing.add(importPublicKeyRing);

                System.out.println((file.getAbsolutePath()));
            } catch (Exception ex) {
                System.out.println("problem accessing file" + file.getAbsolutePath());
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
        if(importedPublicKey != null) {
            JOptionPane.showMessageDialog(frame, "You imported public key : " + importedPublicKey.toString());
        }else{
            JOptionPane.showMessageDialog(frame, "Error");
        }
    }


    private void importSecretKey() {
        PGPSecretKey importedSecretKey = null;
        importKeys = new javax.swing.JFileChooser();

        int returnVal = importKeys.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = importKeys.getSelectedFile();
            try {
                PGPSecretKeyRing importSecretKeyRing = new PGPSecretKeyRing(PGPUtil.getDecoderStream(new FileInputStream(file.getAbsolutePath())), new BcKeyFingerprintCalculator());
                importedSecretKey = importSecretKeyRing.getSecretKey();

                if(GenerateKeys.pgpSecretKeyRing == null){
                    GenerateKeys.pgpSecretKeyRing = new ArrayList<>();
                }
                GenerateKeys.pgpSecretKeyRing.add(importSecretKeyRing);

                System.out.println((file.getAbsolutePath()));
            } catch (Exception ex) {
                System.out.println("problem accessing file" + file.getAbsolutePath());
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
        if(importedSecretKey != null) {
            JOptionPane.showMessageDialog(frame, "You imported secret key : " + importedSecretKey.toString());
        }else{
            JOptionPane.showMessageDialog(frame, "Error");
        }
    }

}
