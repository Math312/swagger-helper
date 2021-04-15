package com.sharedaka.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.sharedaka.dispatcher.DispatcherHolder;
import org.jetbrains.annotations.NotNull;

public class SwaggerAnnotationGenerateAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent anActionEvent) {
        anActionEvent.getPresentation().setEnabledAndVisible(DispatcherHolder.getSwaggerApiControllerProcessor().support(anActionEvent));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        DispatcherHolder.getSwaggerApiControllerProcessor().dispatcher(anActionEvent);
    }


}
