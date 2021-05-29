package etf.openpgp.ba170578dbm170614d.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private JButton generateKeyFormButton;
    private JPanel mainPanel;
    private JButton importExportButton;
    private JButton showRingButton;
    private JButton sendButton;
    private JButton receiveButton;
    public JFrame frame;

    public MainForm() {
        frame = new JFrame("PGP");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        mainPanel.setVisible(true);

        generateKeyFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new GenerateKeyForm(frame);
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new SendMessageFrom(frame);
            }
        });
        receiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new ReceiveMessageForm(frame);
            }
        });
    }

    public static void main(String[] args) {
        MainForm mf = new MainForm();
    }

    public void setVisibleMainForm(){
        frame.setVisible(true);
    }
}
