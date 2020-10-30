package com.sharedaka.core;

import com.intellij.openapi.project.Project;
import com.sharedaka.ui.config.SwaggerHelperSetting;

public class SwaggerHelperApplicationManager {

    private Project project;

    private SwaggerHelperSetting swaggerHelperSetting;

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

}
