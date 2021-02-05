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
    JTextField textField1;
    JTextField textField2;
    private Project project;

    public SwaggerHelperSettingUI(Project project) {
        this.project = project;
        initParam(project);
    }

    public void initParam(Project project) {
        SwaggerHelperConfig setting = SwaggerHelperConfig.getInstance(project);
        textField1.setText(String.join(";", setting.interestingException));
        textField2.setText(setting.springRootConfigurationClassName);
    }

}
