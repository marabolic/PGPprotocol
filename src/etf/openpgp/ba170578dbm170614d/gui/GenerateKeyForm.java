package etf.openpgp.ba170578dbm170614d.gui;

import etf.openpgp.ba170578dbm170614d.pgp.GenerateKeys;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateKeyForm {
    private JTextField nameTextField;
    private JButton submitButton;
    private JTextField emailTextField;
    private JComboBox asymetricAlgorithms;
    private JButton deleteKeyButton;
    private JPanel generateKeyFormPanel;
    private JButton BackButton;
    private JComboBox publicKeyComboBox;
    private JComboBox secretKeyComboBox;
    private JLabel PublicKey;
    private JLabel secretKey;
    private JComboBox dsaAsymetricComboBox;
    private JFrame frame;

    private static final String regex = "^(.+)@(.+)$";

    /**
     *
     * @param email
     * @return
     */
    boolean ValidEmail(String email){
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()) return true;
        else return false;
    }

    /**
     *
     * @param MainFrame
     */
    void initComponents(JFrame MainFrame){
        frame = new JFrame("Generate Key Form");
        frame.setContentPane(generateKeyFormPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        asymetricAlgorithms.addItem("ElGamal 1024");
        asymetricAlgorithms.addItem("ElGamal 2048");
        asymetricAlgorithms.addItem("ElGamal 4096");

        dsaAsymetricComboBox.addItem("DSA 1024");
        dsaAsymetricComboBox.addItem("DSA 2048");

        asymetricAlgorithms.setSelectedIndex(0);
        overviewKey();

        frame.setVisible(true);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ValidEmail(emailTextField.getText()) && nameTextField.getText() != null){
                    //PasswordForm pf= new PasswordForm(frame);
                    String password = JOptionPane.showInputDialog(frame,"Password");
                    GenerateKeys generateKeys = new GenerateKeys();
                    int selectedIndex = asymetricAlgorithms.getSelectedIndex();
                    int selectedIndexDsaComboBox = dsaAsymetricComboBox.getSelectedIndex();

                    int elgParam = 1024;
                    int dsaParam = 1024;

                    switch (selectedIndex){
                        case 0: elgParam = 1024; break;
                        case 1: elgParam = 2048; break;
                        case 2: elgParam = 4096;
                    }

                    switch (selectedIndexDsaComboBox){
                        case 0: dsaParam = 1024; break;
                        case 1: dsaParam = 2048;
                    }

                    System.out.println("email: " + emailTextField.getText());
                    System.out.println("pass: " + password);
                    generateKeys.generateKeys(dsaParam, elgParam, emailTextField.getText(), password.toCharArray());


                    System.out.println(GenerateKeys.pgpPublicKeyRing.size() + "");

                    overviewKey();
                    publicKeyComboBox.revalidate();
                    secretKeyComboBox.revalidate();
                    publicKeyComboBox.repaint();
                    secretKeyComboBox.repaint();

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

        deleteKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] publicKey = publicKeyComboBox.getSelectedItem().toString().split(" ");
                String[] secretKey = secretKeyComboBox.getSelectedItem().toString().split(" ");

                System.out.println(publicKey[1]);
                System.out.println(secretKey[1]);

                if(publicKey[1].equals(secretKey[1])){
                    String password = JOptionPane.showInputDialog(frame,"Password");
                    if(deleteKeys(secretKey[1], password)){
                        publicKeyComboBox.removeItem(publicKey[0]+ " " +publicKey[1]);
                        secretKeyComboBox.removeItem(secretKey[0] + " " + secretKey[1]);

                        publicKeyComboBox.revalidate();
                        secretKeyComboBox.revalidate();
                        publicKeyComboBox.repaint();
                        secretKeyComboBox.repaint();
                    }else{
                        JOptionPane.showMessageDialog(frame, "Wrong password.");
                    }
                }else{
                    JOptionPane.showMessageDialog(frame, "Something wrong, private and secret key id isnt equal.");
                }
            }
        });
    }

    /**
     *
     * @param frame
     */
    GenerateKeyForm(JFrame frame) {
        initComponents(frame);
    }

    /**
     *
     */
    private void overviewKey(){
        publicKeyComboBox.removeAll();
        secretKeyComboBox.removeAll();
        System.out.println("USAO SAM U OVER");

        if(GenerateKeys.pgpPublicKeyRing != null){
            for (Iterator<PGPPublicKeyRing> iterator = GenerateKeys.pgpPublicKeyRing.iterator(); iterator.hasNext();) {
                PGPPublicKeyRing next = iterator.next();

                String email = "";
                Iterator<?> mail = next.getPublicKey().getUserIDs();

                while (mail.hasNext()) {
                    email += (String) mail.next();
                }

                publicKeyComboBox.addItem(email + " " + next.getPublicKey().getKeyID());
                System.out.println("Proso public");
            }

            for (Iterator<PGPSecretKeyRing> iterator = GenerateKeys.pgpSecretKeyRing.iterator(); iterator.hasNext();) {
                PGPSecretKeyRing next = iterator.next();

                String email = "";
                Iterator<?> mail = next.getPublicKey().getUserIDs();

                while (mail.hasNext()) {
                    email += (String) mail.next();
                }

                secretKeyComboBox.addItem(email + " " + next.getSecretKey().getKeyID());
                System.out.println("Proso secret");
            }
        }

    }

    /**
     *
     * @param keyID
     * @param password
     * @return
     */
    private boolean deleteKeys(String keyID,String password){
        PGPSecretKeyRing next = null;
        for (Iterator<PGPSecretKeyRing> iterator = GenerateKeys.pgpSecretKeyRing.iterator(); iterator.hasNext();) {
            next = iterator.next();
            if ((next.getSecretKey().getKeyID() + "").equals(keyID)) {
                break;
            }
        }

        PBESecretKeyDecryptor dec = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(password.toCharArray());

        try {
            if(dec == null){
                return false;
            }else if(next.getSecretKey().extractPrivateKey(dec) != null){
                System.out.println("SIFRA DOBRA");
                System.out.println("PRIVATNI OBRISAN");
                GenerateKeys.pgpSecretKeyRing.remove(next);

                PGPPublicKeyRing nextPublic = null;
                for (Iterator<PGPPublicKeyRing> iterator = GenerateKeys.pgpPublicKeyRing.iterator(); iterator.hasNext();) {
                    nextPublic = iterator.next();
                    if ((nextPublic.getPublicKey().getKeyID() + "").equals(keyID)) {
                        break;
                    }

                }

                GenerateKeys.pgpPublicKeyRing.remove(nextPublic);
                System.out.println("JAVNI OBRISAN");
                return true;
            }else{
                System.out.println("SIFRA NIJE DOBRA");
            }

        } catch (PGPException e) {
            e.printStackTrace();
        }
        return false;
    }

}
