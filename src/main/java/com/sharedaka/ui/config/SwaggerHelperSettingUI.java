package com.sharedaka.ui.config;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwaggerHelperSettingUI {
    public JPanel mainPanel;
    private JTextField textField1;
    private JButton checkButton;


    public SwaggerHelperSettingUI() {
        Document document = textField1.getDocument();
        textField1.setText(SwaggerHelperSetting.getInterestingExceptionStr());
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwaggerHelperSetting.setInterestingExceptionStr(textField1.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwaggerHelperSetting.setInterestingExceptionStr(textField1.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SwaggerHelperSetting.setInterestingExceptionStr(textField1.getText());
            }
        });
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

}
