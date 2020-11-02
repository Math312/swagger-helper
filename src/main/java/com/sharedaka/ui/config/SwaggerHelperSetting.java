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
    private String interestingExceptionStr;
    private String springRootConfigurationClassName;
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
        return checkInterestingExceptions() || checkSpringRootConfigurationClassName();
    }

    private boolean checkInterestingExceptions() {
        if (interestingExceptionStr == null) {
            return true;
        }
        SwaggerHelperConfig config = SwaggerHelperConfig.getInstance(project);
        if (config.interestingException == null) {
            return true;
        }
        String old = String.join(";", config.interestingException);
        return !old.equals(interestingExceptionStr);
    }

    private boolean checkSpringRootConfigurationClassName() {
        if (springRootConfigurationClassName == null) {
            return true;
        }
        SwaggerHelperConfig config = SwaggerHelperConfig.getInstance(project);
        if (config.springRootConfigurationClassName == null) {
            return true;
        }
        return !config.springRootConfigurationClassName.equals(springRootConfigurationClassName);
    }

    @Override
    public void apply() throws ConfigurationException {
        String[] exceptions = interestingExceptionStr.split(";");
        SwaggerHelperConfig.getInstance(project).interestingException.clear();
        SwaggerHelperConfig.getInstance(project).interestingException.addAll(Arrays.stream(exceptions).filter((str) -> !"".equals(str)).collect(Collectors.toList()));
        SwaggerHelperConfig.getInstance(project).springRootConfigurationClassName = this.springRootConfigurationClassName;
        SwaggerHelperApplicationManager.getInstance(project).setCommonSpringModel(SpringModelUtils.getInstance().getSpringModel(JavaPsiFacade.getInstance(project).findClass(springRootConfigurationClassName, GlobalSearchScope.projectScope(project))));
    }

    public String getInterestingExceptionStr() {
        return interestingExceptionStr;
    }

    public void setInterestingExceptionStr(final String newStr) {
        interestingExceptionStr = newStr;
    }

    public String getSpringRootConfigurationClassName() {
        return springRootConfigurationClassName;
    }

    public void setSpringRootConfigurationClassName(String springRootConfigurationClassName) {
        this.springRootConfigurationClassName = springRootConfigurationClassName;
    }
}
