package com.sharedaka.core;

import com.intellij.openapi.project.Project;
import com.intellij.spring.CommonSpringModel;
import com.intellij.spring.contexts.model.SpringModel;
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
        return commonSpringModel;
    }

    public void setCommonSpringModel(CommonSpringModel commonSpringModel) {
        this.commonSpringModel = commonSpringModel;
    }
}
