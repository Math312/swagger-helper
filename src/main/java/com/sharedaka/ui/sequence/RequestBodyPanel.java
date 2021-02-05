package com.sharedaka.ui.sequence;

import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

public class RequestBodyPanel extends JPanel {

    public RequestBodyPanel() {
        this.setMaximumSize(new Dimension(500, 200));
        JTextArea bodyArea = new JTextArea(14, 50);
        bodyArea.setMaximumSize(new Dimension(500, 200));
        JScrollPane jScrollPane = new JBScrollPane(bodyArea);
        jScrollPane.setMaximumSize(new Dimension(500, 200));
        this.add(jScrollPane);
    }
}
