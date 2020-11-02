package com.sharedaka.config;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.model.utils.SpringModelUtils;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.sharedaka.core.SwaggerHelperApplicationManager;
import com.sharedaka.ui.config.SwaggerHelperSetting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@State(
        name = "SwaggerHelperSettings",
        storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class SwaggerHelperConfig implements PersistentStateComponent<SwaggerHelperConfig.SwaggerHelperConfigEntity> {

    private Project project;

    public SwaggerHelperConfig(Project project) {
        this.project = project;
    }

    public List<String> interestingException = new LinkedList<>();

    public String springRootConfigurationClassName;

    public static SwaggerHelperConfig getInstance(Project project) {
        return project.getService(SwaggerHelperConfig.class);
    }

    @Nullable
    @Override
    public SwaggerHelperConfigEntity getState() {
        SwaggerHelperConfigEntity configEntity = new SwaggerHelperConfigEntity();
        configEntity.interestingExceptions = String.join(";", this.interestingException);
        configEntity.springConfigurationClass = this.springRootConfigurationClassName;
        return configEntity;
    }

    @Override
    public void loadState(@NotNull SwaggerHelperConfigEntity state) {
        SwaggerHelperConfigEntity entity = new SwaggerHelperConfigEntity();
        XmlSerializerUtil.copyBean(state, entity);
        this.interestingException.addAll(Arrays.asList(state.interestingExceptions.split(";")));
        this.springRootConfigurationClassName = state.springConfigurationClass;
        SwaggerHelperSetting setting = SwaggerHelperApplicationManager.getInstance(project).getSwaggerHelperSetting();
        if (state.springConfigurationClass != null) {
            SwaggerHelperApplicationManager.getInstance(project).setCommonSpringModel(SpringModelUtils.getInstance().getSpringModel(JavaPsiFacade.getInstance(project).findClass(springRootConfigurationClassName, GlobalSearchScope.projectScope(project))));
        }
        if (setting != null) {
            setting.setInterestingExceptionStr(state.interestingExceptions);
            setting.setSpringRootConfigurationClassName(state.springConfigurationClass);
        }
    }

    public static class SwaggerHelperConfigEntity {
        public String interestingExceptions;

        public String springConfigurationClass;
    }
}
