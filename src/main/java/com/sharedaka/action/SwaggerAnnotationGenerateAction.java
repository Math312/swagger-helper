package com.sharedaka.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiUtilBase;
import com.sharedaka.utils.GeneratorUtils;
import org.jetbrains.annotations.NotNull;

public class SwaggerAnnotationGenerateAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project != null) {
            Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
            if (editor != null) {
                PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
                anActionEvent.getPresentation().setEnabledAndVisible(psiFile instanceof PsiJavaFile);
            }
        }
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project != null) {
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
            Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
            if (editor != null) {
                PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
                String selectionText = editor.getSelectionModel().getSelectedText();
                new GeneratorUtils(project, psiFile, elementFactory, selectionText).doGenerate();
            }
        }
    }


}
