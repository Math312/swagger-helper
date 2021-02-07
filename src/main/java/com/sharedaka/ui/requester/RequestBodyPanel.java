package com.sharedaka.ui.requester;

import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

public class RequestBodyPanel extends JPanel {

    private JTextArea bodyArea;

    public RequestBodyPanel() {
        this.setMaximumSize(new Dimension(500, 200));
        JTextArea bodyArea = new JTextArea(14, 50);
        bodyArea.setMaximumSize(new Dimension(500, 200));
        bodyArea.setAutoscrolls(true);
        bodyArea.setLineWrap(true);
        this.bodyArea = bodyArea;
        JScrollPane jScrollPane = new JBScrollPane(bodyArea);
        jScrollPane.setMaximumSize(new Dimension(500, 200));
        this.add(jScrollPane);
    }

    public void setBody(String data) {
        this.bodyArea.setText(data);
    }
}
