package etf.openpgp.ba170578dbm170614d.gui;

import sun.applet.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReceiveMessageForm {
    private JTextField BrowseTextField;
    private JButton BrowseButton;
    private JLabel RecieveMessageLabel;
    private JButton decryptionButton;
    private JPanel RecieveMessageFormPanel;
    private JButton BackButton;
    private JFrame frame;

    void initComponents(JFrame MainFrame){
        frame = new JFrame("Recieve Message Form");
        frame.setContentPane(RecieveMessageFormPanel);
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

        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainFrame.setVisible(true);
            }
        });
    }

    ReceiveMessageForm(JFrame MainFrame){
        initComponents(MainFrame);
    }
}
