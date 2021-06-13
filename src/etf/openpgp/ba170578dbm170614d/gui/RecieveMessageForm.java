package etf.openpgp.ba170578dbm170614d.gui;



import etf.openpgp.ba170578dbm170614d.pgp.GenerateKeys;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class RecieveMessageForm {
    private JTextField browseTextField;
    private JLabel recieveMessageLabel;
    private JButton decryptionButton;
    private JPanel recieveMessageFormPanel;
    private JButton backButton;
    private JButton findFileButton;
    private JFrame frame;
    private JFileChooser importEncryptedMessage;


    /**
     *
     * @param MainFrame
     */
    public void initComponents(JFrame MainFrame){
        frame = new JFrame("Recieve Message Form");
        frame.setContentPane(recieveMessageFormPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        decryptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Your message is : ...");
                frame.dispose();
                MainFrame.setVisible(true);
            }
        });

        findFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = importEncryptedMessage();
                if(path.contains(".pgp")){
                    browseTextField.setText(path);
                    browseTextField.repaint();
                }else{
                    JOptionPane.showMessageDialog(frame, "Wrong file type.");
                }
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


    /**
     *
     * @param MainFrame
     */
    public RecieveMessageForm(JFrame MainFrame){
        initComponents(MainFrame);
    }


    /**
     *
     * @return
     */
    private String importEncryptedMessage() {
        importEncryptedMessage = new javax.swing.JFileChooser();

        int returnVal = importEncryptedMessage.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = importEncryptedMessage.getSelectedFile();
                return file.getAbsolutePath().toString();
        } else {
            System.out.println("File access cancelled by user.");
        }
        return "";
    }
}
