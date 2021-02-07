package com.sharedaka.ui.requester;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author shaoyanli
 */
public class SwaggerHelperTableCellRenderer implements TableCellRenderer {

    public SwaggerHelperTableCellRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                   int column) {

        if (value instanceof String) {
            JButton jButton = new JButton((String) value);
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (table.getRowCount() > 1 && row != table.getRowCount() - 1) {
                        DefaultTableModel defaultTableModel = (DefaultTableModel) table.getModel();
                        defaultTableModel.removeRow(row);
                    }

                }
            });
            return jButton;
        } else {
            return null;
        }
    }

}