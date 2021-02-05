package com.sharedaka.ui.sequence;

import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTabbedPane;

import javax.swing.*;
import java.awt.*;

public class SwaggerMethodPanel extends JPanel {

    public SwaggerMethodPanel() {
        Box box = Box.createVerticalBox();
        this.add(box);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.setBackground(JBColor.WHITE);


        Box titleBar = Box.createHorizontalBox();
        titleBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(titleBar);
        JLabel titleLabel = new JLabel("Post");
        titleLabel.setBackground(Gray._247);
        titleLabel.setForeground(Gray._94);
        titleLabel.setOpaque(true);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setFont(new Font("Simsun", Font.BOLD, 13));
        titleBar.add(titleLabel);
        box.add(Box.createRigidArea(new Dimension(0,10)));
        Box urlBar = Box.createHorizontalBox();
        urlBar.setAlignmentX(LEFT_ALIGNMENT);
        box.add(urlBar);
        JLabel httpMethodLabel = new JLabel("Post");
        httpMethodLabel.setBackground(Gray._247);
        httpMethodLabel.setForeground(Gray._94);
        httpMethodLabel.setBorder(BorderFactory.createLineBorder(Gray._221));
        httpMethodLabel.setFont(new Font("Simsun", Font.BOLD, 15));
        httpMethodLabel.setMinimumSize(new Dimension(80,35));
        httpMethodLabel.setMaximumSize(new Dimension(80,35));
        JTextArea urlTextArea = new JTextArea(3,50);
        urlTextArea.setAutoscrolls(true);
        urlTextArea.setLineWrap(true);
        urlTextArea.setMinimumSize(new Dimension(350,30));
        urlTextArea.setMaximumSize(new Dimension(350,30));
        urlTextArea.setBackground(Gray._247);
        JButton commitButton = new JButton("Send");
        commitButton.setMaximumSize(new Dimension(80,30));
        commitButton.setMinimumSize(new Dimension(80,30));
        commitButton.setBorder(BorderFactory.createLineBorder(JBColor.LIGHT_GRAY, 1));
        commitButton.setSize(80, 30);
        urlBar.add(httpMethodLabel);
        urlBar.add(urlTextArea);
        urlBar.add(commitButton);

        Box requestsBox = Box.createHorizontalBox();
        box.add(requestsBox);
        requestsBox.setAlignmentX(LEFT_ALIGNMENT);
        JBTabbedPane requestContentPane = new JBTabbedPane();
        requestsBox.add(requestContentPane);
        requestContentPane.addTab("Headers", new RequestParamPanel());
        requestContentPane.addTab("Param",new RequestParamPanel());
        requestContentPane.addTab("body", new RequestBodyPanel());

        box.add(Box.createRigidArea(new Dimension(0,10)));


        Box responseBox = Box.createVerticalBox();
        box.add(responseBox);
        responseBox.setAlignmentX(LEFT_ALIGNMENT);
        JBTabbedPane responseContentPane = new JBTabbedPane();
        responseBox.add(responseContentPane);
        responseContentPane.addTab("Headers", new ResponseHeaderPanel());
        responseContentPane.addTab("body", new RequestBodyPanel());

    }
}
