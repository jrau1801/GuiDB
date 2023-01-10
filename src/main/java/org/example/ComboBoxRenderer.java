package org.example;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

public class ComboBoxRenderer extends BasicComboBoxRenderer {

    String[] tooltips = {
            "Spender-Transplantationszentrum-Adresse",
            "Empfaenger-Transplantationszentrum-Adresse",
            "Spender-Krankheit",
            "Empfaenger-Krankheit",
            "Spender-Adresse-Stadt-Bundesland-Land",
            "Empfaenger-Adresse-Stadt-Bundesland-Land",
            "Organkompatibilitaet"
    };

    /**
     * shows tooltips for hovered option
     * @param list The JList we're painting.
     * @param value The value returned by list.getModel().getElementAt(index).
     * @param index The cells index.
     * @param isSelected True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            if (-1 < index) {
                list.setToolTipText(tooltips[index]);
            }
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setFont(list.getFont());
        setText((value == null) ? "" : value.toString());
        return this;
    }
}
