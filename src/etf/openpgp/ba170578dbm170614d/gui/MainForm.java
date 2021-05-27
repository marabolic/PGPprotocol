package etf.openpgp.ba170578dbm170614d.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private JButton GenerateKeyFormButton;
    private JPanel MainPanel;
    private JButton importExportButton;
    private JButton showRingButton;
    private JButton sendButton;
    private JButton recieveButton;

    public MainForm() {
        GenerateKeyFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainPanel.setVisible(false);
                new GenerateKeyForm();
            }
        });


    }
}
