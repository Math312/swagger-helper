package com.sharedaka.ui.sequence;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class RequestParamPanel extends JPanel {

    private final String[] colNames = {"KEY","VALUE","DESCRIPTION"};

    private String[][] data = new String[][]{{"1","2","3"}};

    public RequestParamPanel() {
        this.setMaximumSize(new Dimension(500,200));

        TableModel tableModel = new DefaultTableModel(data,colNames);
        JTable jTable = new JTable(tableModel);
        jTable.setPreferredScrollableViewportSize(new Dimension(500,200));
        JScrollPane scrollPane = new JBScrollPane(jTable);
        scrollPane.setMaximumSize(new Dimension(500, 200));
        this.add(scrollPane);
    }
}
