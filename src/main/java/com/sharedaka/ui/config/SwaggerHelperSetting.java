package com.sharedaka.ui.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.sharedaka.config.SwaggerHelperConfig;
import com.sharedaka.core.SwaggerHelperApplicationManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SwaggerHelperSetting implements SearchableConfigurable {

    private SwaggerHelperSettingUI swaggerHelperSettingUI;
    private String interestingExceptionStr;
    private Project project;

    public SwaggerHelperSetting(Project project) {
        this.project = project;
        SwaggerHelperApplicationManager.getInstance(project).setSwaggerHelperSetting(this);
    }

    @NotNull
    @Override
    public String getId() {
        return "Swagger-Helper";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return getId();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (null == this.swaggerHelperSettingUI) {
            this.swaggerHelperSettingUI = new SwaggerHelperSettingUI(this.project);
        }
        return this.swaggerHelperSettingUI.mainPanel;
    }

    @Override
    public boolean isModified() {
        String old = String.join(";", SwaggerHelperConfig.getInstance(project).interestingException);
        return !old.equals(interestingExceptionStr);
    }

    @Override
    public void apply() throws ConfigurationException {
        String[] exceptions = interestingExceptionStr.split(";");
        SwaggerHelperConfig.getInstance(project).interestingException.clear();
        SwaggerHelperConfig.getInstance(project).interestingException.addAll(Arrays.stream(exceptions).filter((str) -> !"".equals(str)).collect(Collectors.toList()));
    }

    public String getInterestingExceptionStr() {
        return interestingExceptionStr;
    }

    public void setInterestingExceptionStr(final String newStr) {
        interestingExceptionStr = newStr;
    }
}
