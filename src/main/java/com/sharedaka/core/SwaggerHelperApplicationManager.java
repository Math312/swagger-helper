package com.sharedaka.core;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.CommonSpringModel;
import com.intellij.spring.model.utils.SpringModelUtils;
import com.sharedaka.config.SwaggerHelperConfig;
import com.sharedaka.ui.config.SwaggerHelperSetting;

public class SwaggerHelperApplicationManager {

    private Project project;

    private SwaggerHelperSetting swaggerHelperSetting;

    private CommonSpringModel commonSpringModel;

    public SwaggerHelperApplicationManager(Project project) {
        this.project = project;
    }

    public static SwaggerHelperApplicationManager getInstance(Project project) {
        return project.getService(SwaggerHelperApplicationManager.class);
    }

    public SwaggerHelperSetting getSwaggerHelperSetting() {
        return swaggerHelperSetting;
    }

    public void setSwaggerHelperSetting(SwaggerHelperSetting swaggerHelperSetting) {
        this.swaggerHelperSetting = swaggerHelperSetting;
    }

    public CommonSpringModel getCommonSpringModel() {
        SwaggerHelperConfig config = SwaggerHelperConfig.getInstance(project);
        if (commonSpringModel == null) {
            synchronized (this) {
                if (commonSpringModel == null) {
                    this.commonSpringModel = SpringModelUtils.getInstance().getSpringModel(JavaPsiFacade.getInstance(project).findClass(config.springRootConfigurationClassName, GlobalSearchScope.projectScope(project)));
                }
            }
        }
        return commonSpringModel;
    }

    public void setCommonSpringModel(CommonSpringModel commonSpringModel) {
        this.commonSpringModel = commonSpringModel;
    }
}
