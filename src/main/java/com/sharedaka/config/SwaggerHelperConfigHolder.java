package com.sharedaka.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.sharedaka.ui.config.SwaggerHelperSetting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@State(
        name = "SwaggerHelperSettings",
        storages = @Storage("swagger-helper.xml"))
public class SwaggerHelperConfigHolder implements PersistentStateComponent<SwaggerHelperConfig> {

    public static List<String> interestingException = new LinkedList<>();


    @Nullable
    @Override
    public SwaggerHelperConfig getState() {
        StringBuilder sb = new StringBuilder();
        for (String exception: SwaggerHelperConfigHolder.interestingException) {
            sb.append(exception).append(";");
        }
        SwaggerHelperConfig swaggerHelperConfig = new SwaggerHelperConfig();
        swaggerHelperConfig.setInterestingExceptions(sb.toString());
        return swaggerHelperConfig;
    }

    @Override
    public void loadState(@NotNull SwaggerHelperConfig state) {
        String[] strings = state.getInterestingExceptions().split(";");
        interestingException.addAll(Arrays.asList(strings));
        SwaggerHelperSetting.setInterestingExceptionStr(state.getInterestingExceptions());
    }
}
