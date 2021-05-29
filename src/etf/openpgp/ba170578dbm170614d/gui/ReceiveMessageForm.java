package etf.openpgp.ba170578dbm170614d.gui;



import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReceiveMessageForm {
    private JTextField browseTextField;
    private JButton browseButton;
    private JLabel recieveMessageLabel;
    private JButton decryptionButton;
    private JPanel recieveMessageFormPanel;
    private JButton backButton;
    private JFrame frame;

    void initComponents(JFrame MainFrame){
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

        backButton.addActionListener(new ActionListener() {
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
