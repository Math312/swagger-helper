package com.sharedaka.ui.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.sharedaka.config.SwaggerHelperConfigHolder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SwaggerHelperSetting implements SearchableConfigurable {

    private SwaggerHelperSettingUI swaggerHelperSettingUI;
    private static String interestingExceptionStr;

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
        if (null == swaggerHelperSettingUI) {
            this.swaggerHelperSettingUI = new SwaggerHelperSettingUI();
        }
        return swaggerHelperSettingUI.mainPanel;
    }

    @Override
    public boolean isModified() {
        String old = SwaggerHelperConfigHolder.interestingException.stream().collect(Collectors.joining(";"));
        return !old.equals(interestingExceptionStr);
    }

    @Override
    public void apply() throws ConfigurationException {
        String[] exceptions = interestingExceptionStr.split(";");
        SwaggerHelperConfigHolder.interestingException.clear();
        SwaggerHelperConfigHolder.interestingException.addAll(Arrays.asList(exceptions));
    }

    public static String getInterestingExceptionStr() {
        return interestingExceptionStr;
    }

    public static void setInterestingExceptionStr(final String newStr) {
        interestingExceptionStr = newStr;
    }
}
