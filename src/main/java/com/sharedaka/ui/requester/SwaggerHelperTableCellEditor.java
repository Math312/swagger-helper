package com.sharedaka.ui.requester;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwaggerHelperTableCellEditor extends DefaultCellEditor {


    public SwaggerHelperTableCellEditor() {
        // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。
        super(new JTextField());

        // 设置点击几次激活编辑。
        this.setClickCountToStart(1);
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        delegate.setValue(value);
        if (value instanceof String) {
            JButton button = new JButton((String) value);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingCanceled();
                    if (table.getRowCount() > 1 && row != table.getRowCount() - 1) {
                        DefaultTableModel defaultTableModel = (DefaultTableModel) table.getModel();
                        defaultTableModel.removeRow(row);
                    }
                }
            });
            return (Component) button;
        } else {
            return null;
        }

    }

//    /**
//     * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。
//     */
//    @Override
//    public Object getCellEditorValue() {
//        return
//    }
}
