package com.sharedaka.ui.requester;

import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Vector;

public class RequestParamPanel extends JPanel {

    private Vector<String> columnNames = new Vector<>();

    private Vector<Vector<Object>> dataVector = new Vector<>();

    private JTable jTable;

    public RequestParamPanel() {
        initColumnNames();
        initData();
        this.setMaximumSize(new Dimension(500, 200));
        TableModel tableModel = new DefaultTableModel(this.dataVector, columnNames);
        JTable jTable = new JTable(tableModel);
        jTable.getColumnModel().getColumn(3).setCellEditor(new SwaggerHelperTableCellEditor());
        jTable.getColumnModel().getColumn(3).setCellRenderer(new SwaggerHelperTableCellRenderer());
        jTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        jTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (dataVector.size() == e.getLastRow() + 1 && (TableModelEvent.UPDATE == e.getType())) {
                    dataVector.add(getEmptyRowData());
                }
            }
        });
        JScrollPane scrollPane = new JBScrollPane(jTable);
        scrollPane.setMaximumSize(new Dimension(500, 200));
        this.add(scrollPane);
    }

    private void initColumnNames() {
        Vector<String> defaultColumnNames = new Vector<>();
        defaultColumnNames.add("KEY");
        defaultColumnNames.add("VALUE");
        defaultColumnNames.add("DESCRIPTION");
        defaultColumnNames.add("Operation");
        columnNames = defaultColumnNames;
    }

    private void initData() {
        dataVector.add(getEmptyRowData());
    }

    private Vector<Object> getEmptyRowData() {
        Vector<Object> emptyTableData = new Vector<>();
        emptyTableData.add("");
        emptyTableData.add("");
        emptyTableData.add("");
//        SwaggerHelperButton swaggerHelperButton = new SwaggerHelperButton("删除");
        emptyTableData.add("删除");
        return emptyTableData;
    }

    private boolean isEmptyLine(Vector<Object> vector) {
        for (Object o : vector) {
            if (o != null) {
                if (o instanceof String) {
                    if (((String) o).length() > 0) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }
}
