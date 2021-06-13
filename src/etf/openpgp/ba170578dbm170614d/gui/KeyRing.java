package etf.openpgp.ba170578dbm170614d.gui;

import etf.openpgp.ba170578dbm170614d.pgp.GenerateKeys;
import org.bouncycastle.openpgp.PGPPublicKeyRing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class KeyRing {
    private JPanel keyRing;
    private JTable keyTable;
    private JScrollPane scrollPane;
    private JButton backButton;
    private JFrame frame;


    /**
     *
     * @param mainFrame
     */
    private void init(JFrame mainFrame){
        frame = new JFrame("KeyRing");
        frame.setContentPane(keyRing);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Email");
        model.addColumn("KeyID");

        if(GenerateKeys.pgpPublicKeyRing != null) {
            for (Iterator<PGPPublicKeyRing> iterator = GenerateKeys.pgpPublicKeyRing.iterator(); iterator.hasNext(); ) {
                PGPPublicKeyRing next = iterator.next();

                String email = "";
                Iterator<?> mail = next.getPublicKey().getUserIDs();

                while (mail.hasNext()) {
                    email += (String) mail.next();
                }

                Object[] row = {email, next.getPublicKey().getKeyID() + ""};
                model.addRow(row);
                System.out.println("Proso public");
            }
        }

        keyTable.setModel(model);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                mainFrame.setVisible(true);
            }
        });
    }


    /**
     *
     * @param mainFrame
     */
    public KeyRing(JFrame mainFrame){
        init(mainFrame);
    }

}
