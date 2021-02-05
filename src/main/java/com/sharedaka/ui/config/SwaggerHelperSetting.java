package com.sharedaka.ui.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.model.utils.SpringModelUtils;
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
    private Project project;

    public SwaggerHelperSetting(Project project) {
        this.project = project;
    }

    public static SwaggerHelperSetting getInstance(Project project) {
        return project.getComponent(SwaggerHelperSetting.class);
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
        SwaggerHelperConfig config = SwaggerHelperConfig.getInstance(project);
        String interestingExceptionStr = String.join(";", config.interestingException);
        String springRootConfigurationClassName = config.springRootConfigurationClassName;
        return checkInterestingExceptions(interestingExceptionStr)
                || checkSpringRootConfigurationClassName(springRootConfigurationClassName);
    }

    private boolean checkInterestingExceptions(String interestingExceptionStr) {
        if (interestingExceptionStr == null) {
            return true;
        }
        return !swaggerHelperSettingUI.textField1.getText().equals(interestingExceptionStr);
    }

    private boolean checkSpringRootConfigurationClassName(String springRootConfigurationClassName) {
        if (springRootConfigurationClassName == null) {
            return true;
        }
        return !swaggerHelperSettingUI.textField2.getText().equals(springRootConfigurationClassName);
    }

    @Override
    public void apply() throws ConfigurationException {
        String[] exceptions = swaggerHelperSettingUI.textField1.getText().split(";");
        SwaggerHelperConfig.getInstance(project).interestingException.clear();
        SwaggerHelperConfig.getInstance(project).interestingException.addAll(Arrays.stream(exceptions).filter((str) -> !"".equals(str)).collect(Collectors.toList()));
        SwaggerHelperConfig.getInstance(project).springRootConfigurationClassName = swaggerHelperSettingUI.textField2.getText();
        SwaggerHelperApplicationManager.getInstance(project).setCommonSpringModel(SpringModelUtils.getInstance().getSpringModel(JavaPsiFacade.getInstance(project).findClass(swaggerHelperSettingUI.textField2.getText(), GlobalSearchScope.projectScope(project))));
    }
}
