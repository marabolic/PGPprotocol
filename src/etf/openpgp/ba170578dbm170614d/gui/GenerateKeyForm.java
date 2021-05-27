package etf.openpgp.ba170578dbm170614d.gui;

import javax.swing.*;
import java.awt.event.ItemListener;

public class GenerateKeyForm {
    private JTextField nameTextField;
    private JButton submitButton;
    private JTextField emailTextField;
    private JComboBox asymetricAlgorithms;



    void initComponents(){
        String [] algrorithms = {"DSA", "El Gamal"};
        asymetricAlgorithms = new JComboBox(algrorithms);

    }

    GenerateKeyForm() {
        initComponents();
    }


    public static void main(String[] args) {
        GenerateKeyForm gk = new GenerateKeyForm();

    }


}
