package com.sharedaka.dispatcher;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;
import com.sharedaka.processor.ProcessorHolder;
import com.sharedaka.processor.business.SwaggerApiControllerProcessor;
import com.sharedaka.processor.business.SwaggerApiMethodProcessor;
import com.sharedaka.processor.business.SwaggerApiModelProcessor;

public class SwaggerActionDispatcher {

    public boolean support(AnActionEvent actionEvent) {
        if (actionEvent == null) {
            return false;
        }
        Project project = actionEvent.getProject();
        if (project != null) {
            Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
            if (editor != null) {
                PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
                if (psiFile instanceof PsiJavaFile) {
                    String selectedText = editor.getSelectionModel().getSelectedText();
                    for (PsiElement psiElement : psiFile.getChildren()) {
                        if (psiElement instanceof PsiClass) {
                            PsiClass psiClass = (PsiClass) psiElement;

                            SwaggerApiModelProcessor swaggerApiModelProcessor = ProcessorHolder.getSwaggerApiModelProcessor();
                            if (swaggerApiModelProcessor.support(psiClass)) {
                                return true;
                            }

                            if (selectedText != null) {
                                {
                                    if (selectedText.equals(((PsiClass) psiElement).getName())) {
                                        SwaggerApiControllerProcessor swaggerApiControllerProcessor = ProcessorHolder.getSwaggerApiControllerProcessor();
                                        if (swaggerApiControllerProcessor.support(psiClass)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                            PsiMethod[] psiMethods = ((PsiClass) psiElement).getMethods();
                            for (PsiMethod psiMethod : psiMethods) {
                                if (psiMethod.getName().equals(selectedText)) {
                                    SwaggerApiMethodProcessor swaggerApiMethodProcessor = ProcessorHolder.getSwaggerApiMethodProcessor();
                                    if (swaggerApiMethodProcessor.support(psiClass, psiMethod)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void dispatcher(AnActionEvent actionEvent) {
        Project project = actionEvent.getProject();
        if (project != null) {
            Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
            if (editor != null) {
                PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
                // todo 细化选择控制
                if (psiFile != null) {
                    doDispatcher(psiFile, editor);
                }
            }
        }
    }

    private void doDispatcher(PsiFile psiFile, Editor editor) {
        String selectedText = editor.getSelectionModel().getSelectedText();
        for (PsiElement psiElement : psiFile.getChildren()) {
            if (psiElement instanceof PsiClass) {
                PsiClass psiClass = (PsiClass) psiElement;
                WriteCommandAction.runWriteCommandAction(psiFile.getProject(), () -> {
                            SwaggerApiModelProcessor swaggerApiModelProcessor = ProcessorHolder.getSwaggerApiModelProcessor();
                            if (swaggerApiModelProcessor.support(psiClass)) {
                                swaggerApiModelProcessor.process((PsiClass) psiElement);
                            }
                        }
                );
                if (selectedText != null) {
                    {
                        if (selectedText.equals(((PsiClass) psiElement).getName())) {
                            WriteCommandAction.runWriteCommandAction(psiFile.getProject(), () -> {
                                SwaggerApiControllerProcessor swaggerApiControllerProcessor = ProcessorHolder.getSwaggerApiControllerProcessor();
                                if (swaggerApiControllerProcessor.support(psiClass)) {
                                    swaggerApiControllerProcessor.process((PsiClass) psiElement);
                                }
                                SwaggerApiModelProcessor swaggerApiModelProcessor = ProcessorHolder.getSwaggerApiModelProcessor();
                                if (swaggerApiModelProcessor.support(psiClass)) {
                                    swaggerApiModelProcessor.process((PsiClass) psiElement);
                                }
                            });
                        }
                        PsiMethod[] psiMethods = ((PsiClass) psiElement).getMethods();
                        for (PsiMethod psiMethod : psiMethods) {
                            if (psiMethod.getName().equals(selectedText)) {
                                WriteCommandAction.runWriteCommandAction(psiFile.getProject(), () -> {
                                    SwaggerApiMethodProcessor swaggerApiMethodProcessor = ProcessorHolder.getSwaggerApiMethodProcessor();
                                    if (swaggerApiMethodProcessor.support(psiClass, psiMethod)) {
                                        swaggerApiMethodProcessor.process(psiMethod);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }

    }
}
