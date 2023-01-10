package org.example;

import database.JDBCHelper;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Jan Rau
 */
public class GUIDatabase extends JFrame {

    private JScrollPane scrollPane1;
    private JTable table1;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBoxJoins;
    private JButton buttonAll;

    private JButton buttonJoins;
    private JLabel icon;

    private JTextField textField;

    public GUIDatabase() {
        initComponents();
    }

    String[] tables = {"Spender", "Empfanger", "Organ", "Gewebeprobe", "Allergie", "Medikation", "Krankheit",
            "Land", "Bundesland", "Stadt", "Labor", "Adresse", "Transplantationszentrum"};

    String[] joins = {"STA", "ETA", "SK","EK","SASBL","EASBL","COMPATABILITY"};

    Connection con = JDBCHelper.getConnection();

    private void initComponents() {
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        comboBox1 = new JComboBox<>(tables);
        comboBoxJoins = new JComboBox<>(joins);
        buttonJoins = new JButton();
        buttonAll = new JButton();
        icon = new JLabel();
        textField = new JTextField("Organ_ID");
        //======== this ========
        setMaximumSize(new Dimension(1400, 700));
        setMinimumSize(new Dimension(1400, 700));
        setPreferredSize(new Dimension(1400, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setIconImage(new ImageIcon("dblogo (1).png").getImage());
        icon.setIcon(new ImageIcon("dblogo (1).png"));


        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(table1);
        }

        table1.setEnabled(false);

        add(icon);
        icon.setBounds(1250, 55, 400, 800);

        // ==== TextField ====

        add(textField);
        textField.setBounds(465, 595, 120, 40);

        //Schreibt Organ_ID in das Textfeld, wenn es nicht im Fokus ist
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals("Organ_ID")) {
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText("Organ_ID");
                }
            }
        });

        // ==== ComboBox ====
        add(scrollPane1);
        scrollPane1.setBounds(210, 60, 1000, 475);
        add(comboBox1);
        comboBox1.setBounds(15, 60, 120, 40);

        comboBoxJoins.setRenderer(new ComboBoxRenderer());
        add(comboBoxJoins);
        comboBoxJoins.setBounds(15, 200, 120, 40);

        //---- buttons ----
        buttonAll.setText("Anzeigen");
        buttonAll.setBackground(new Color(211, 211, 211));
        add(buttonAll);
        buttonAll.setBounds(215, 595, 120, 40);

        buttonJoins.setText("Join");
        buttonJoins.setBackground(new Color(211, 211, 211));
        add(buttonJoins);
        buttonJoins.setBounds(340, 595, 120, 40);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for (int i = 0; i < getComponentCount(); i++) {
                Rectangle bounds = getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            setMinimumSize(preferredSize);
            setPreferredSize(preferredSize);
        }

        buttonAll.addActionListener(e -> {
            //Shows all data from selected Table
            String selectedItem = Objects.requireNonNull(comboBox1.getSelectedItem()).toString().toLowerCase(Locale.ROOT);
            String sql = " SELECT * FROM " + selectedItem;
            selects(sql);
        });


        buttonJoins.addActionListener(e -> {
            // Gibt Spender im Transplantationszentrum mit Adresse aus
            if (Objects.equals(comboBoxJoins.getSelectedItem(), "STA")) {
                String sql = "SELECT Spender.PATIENT_ID,Spender.NACHNAME,Spender.VORNAME,T.T_ID,T.NAME,T.TELEFONNUMMER,A2.STRASSENAME,A2.HAUSNUMMER FROM SPENDER " +
                        "INNER JOIN TRANSPLANTATIONSZENTRUM T on T.T_ID = SPENDER.T_ID " +
                        "INNER JOIN ADRESSE A2 on A2.ADRESSE_ID = T.ADRESSE_ID";
                selects(sql);
            }
            // Gibt Empfaenger im Transplantationszentrum mit Adresse aus
            if (Objects.equals(comboBoxJoins.getSelectedItem(), "ETA")) {
                String sql = "SELECT Empfanger.PATIENT_ID,Empfanger.NACHNAME,Empfanger.VORNAME,T.T_ID,T.NAME,T.TELEFONNUMMER,A2.STRASSENAME,A2.HAUSNUMMER FROM Empfanger " +
                        "INNER JOIN TRANSPLANTATIONSZENTRUM T on T.T_ID = Empfanger.T_ID " +
                        "INNER JOIN ADRESSE A2 on A2.ADRESSE_ID = T.ADRESSE_ID";
                selects(sql);
            }

            if(Objects.equals(comboBoxJoins.getSelectedItem(), "SK")){
                String sql = "SELECT DISTINCT S.VORNAME,S.NACHNAME,S.\"ALTER\",B.DAUER,K.KRANKHEITSBEZEICHNUNG FROM SPENDER S " +
                        "INNER JOIN BETRIFFT2 B on S.PATIENT_ID = B.PATIENT_ID INNER JOIN KRANKHEIT K " +
                        "on B.K_ID = K.K_ID ORDER BY S.NACHNAME";
                selects(sql);
            }

            if(Objects.equals(comboBoxJoins.getSelectedItem(), "EK")){
                String sql = "SELECT DISTINCT E.VORNAME,E.NACHNAME,E.\"ALTER\",B.DAUER,K.KRANKHEITSBEZEICHNUNG FROM EMPFANGER E " +
                        "INNER JOIN BETRIFFT B on E.PATIENT_ID = B.PATIENT_ID INNER JOIN KRANKHEIT K " +
                        "on B.K_ID = K.K_ID ORDER BY E.NACHNAME";
                selects(sql);
            }

            if(Objects.equals(comboBoxJoins.getSelectedItem(), "SASBL")){
                String sql = "SELECT S.VORNAME, S.NACHNAME, A2.STRASSENAME,A2.HAUSNUMMER,S2.STADTNAME,S2.PLZ,B.BUNDESLANDNAME,L2.LAND_NAME,L2.KUERZEL FROM SPENDER S " +
                        "INNER JOIN ADRESSE A2 on S.ADRESSE_ID = A2.ADRESSE_ID " +
                        "INNER JOIN STADT S2 on A2.STADT_ID = S2.STADT_ID " +
                        "INNER JOIN LIEGT_IN LI on S2.STADT_ID = LI.STADT_ID INNER JOIN BUNDESLAND B on LI.STAAT_ID = B.STAAT_ID " +
                        "INNER JOIN LAND L2 on B.LAND_ID = L2.LAND_ID";
                selects(sql);
            }

            if(Objects.equals(comboBoxJoins.getSelectedItem(), "EASBL")){
                String sql = "SELECT E.VORNAME, E.NACHNAME, A2.STRASSENAME,A2.HAUSNUMMER,S2.STADTNAME,S2.PLZ,B.BUNDESLANDNAME,L2.LAND_NAME,L2.KUERZEL FROM EMPFANGER E " +
                        "INNER JOIN ADRESSE A2 on E.ADRESSE_ID = A2.ADRESSE_ID " +
                        "INNER JOIN STADT S2 on A2.STADT_ID = S2.STADT_ID " +
                        "INNER JOIN LIEGT_IN LI on S2.STADT_ID = LI.STADT_ID " +
                        "INNER JOIN BUNDESLAND B on LI.STAAT_ID = B.STAAT_ID INNER JOIN LAND L2 on B.LAND_ID = L2.LAND_ID";
                selects(sql);
            }


            // Guckt nach passenden Organen von Spender fuer Empfaenger
            if (Objects.equals(comboBoxJoins.getSelectedItem(), "COMPATABILITY")) {
                String id = textField.getText();
                if (!id.equals("Organ_ID")) {
                    String sql = "SELECT S.PATIENT_ID, S.VORNAME,S.NACHNAME,O.ORGANBEZEICHNUNG,E.PATIENT_ID," +
                            "E.VORNAME,E.NACHNAME,E.\"ALTER\",E.PRIORITAET FROM SPENDER S " +
                            "INNER JOIN ORGAN O on S.PATIENT_ID = O.PATIENT_ID INNER JOIN GEWEBEPROBE G on O.GEWEBEPROBE_ID = G.GEWEBEPROBE_ID " +
                            "INNER JOIN EMPFANGER E on E.GEWEBETYP = G.ERGEBNIS AND E.ORGANTYP = O.ORGANBEZEICHNUNG AND E.BLUTGRUPPE = S.BLUTGRUPPE WHERE ORGAN_ID = " + id +
                            " ORDER BY E.PRIORITAET, E.\"ALTER\" ASC";
                    selects(sql);
                }
            }
        });
    }

    /**
     * Executes Select Commands and formats JTable
     *
     * @param sql Select Command
     */
    public void selects(String sql) {
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String[] columns = new String[columnCount];


            for (int i = 0; i < columnCount; i++) {
                columns[i] = rsmd.getColumnName(i + 1);
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setRowCount(0);

            model.setColumnIdentifiers(columns);


            while (rs.next()) {
                String[] data = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    data[i] = rs.getString(i + 1);
                }
                model.addRow(data);
            }

            table1.setModel(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}

