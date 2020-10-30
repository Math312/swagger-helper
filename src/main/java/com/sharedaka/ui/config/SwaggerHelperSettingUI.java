package com.sharedaka.ui.config;

import com.intellij.openapi.project.Project;
import com.sharedaka.config.SwaggerHelperConfig;
import com.sharedaka.core.SwaggerHelperApplicationManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class SwaggerHelperSettingUI {
    public JPanel mainPanel;
    private JTextField textField1;
    private Project project;

    public SwaggerHelperSettingUI(Project project) {
        this.project = project;
        Document document = textField1.getDocument();
        initParam(project);
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwaggerHelperApplicationManager.getInstance(project).getSwaggerHelperSetting().setInterestingExceptionStr(textField1.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwaggerHelperApplicationManager.getInstance(project).getSwaggerHelperSetting().setInterestingExceptionStr(textField1.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SwaggerHelperApplicationManager.getInstance(project).getSwaggerHelperSetting().setInterestingExceptionStr(textField1.getText());
            }
        });
    }

    public void initParam(Project project) {
        SwaggerHelperSetting setting = SwaggerHelperApplicationManager.getInstance(project).getSwaggerHelperSetting();
        if (setting.getInterestingExceptionStr() == null || setting.getInterestingExceptionStr().length() == 0) {
            SwaggerHelperConfig config = SwaggerHelperConfig.getInstance(this.project);
            setting.setInterestingExceptionStr(String.join(";", config.interestingException));
        }
        textField1.setText(setting.getInterestingExceptionStr());
    }

}
