package etf.openpgp.ba170578dbm170614d.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JFrame frame;

    void initComponent(JFrame MainFrame){
        frame = new JFrame("Generate Key Form");
        frame.setContentPane(SendMessageFormPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        SymmetricAlgorithmBox.addItem("3DES + EDE");
        SymmetricAlgorithmBox.addItem("AES 128 bita");

        SymmetricAlgorithmBox.setSelectedIndex(0);

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
    }

    public SendMessageFrom(JFrame MainFrame){
        initComponent(MainFrame);
    }
}
