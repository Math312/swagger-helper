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
import com.sharedaka.utils.PsiElementUtil;

/**
 * Swagger-Helper由一个按钮作为统一入口
 * 根据用户光标所在位置进行进行不同的处理
 * 因此需要一个分发器进行功能判断
 * 该类负责分发功能
 *
 * @author math312
 */
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
                    int startOffset = editor.getSelectionModel().getSelectionStart();
                    PsiElement selectedElement = PsiUtilBase.getElementAtOffset(psiFile, startOffset);

                    if (selectedElement instanceof PsiMethod || PsiElementUtil.getPsiMethod(selectedElement) != null) {
                        PsiMethod psiMethod = null;
                        if (selectedElement instanceof PsiMethod) {
                            psiMethod = (PsiMethod) selectedElement;
                        } else {
                            psiMethod = PsiElementUtil.getPsiMethod(selectedElement);
                        }
                        return ProcessorHolder.getSwaggerApiMethodProcessor().support(PsiElementUtil.getPsiCLass(psiMethod), psiMethod);
                    } else if (selectedElement instanceof PsiClass || PsiElementUtil.getPsiCLass(selectedElement) != null) {
                        PsiClass psiClass = null;
                        if (selectedElement instanceof PsiClass) {
                            psiClass = (PsiClass) selectedElement;
                        } else {
                            psiClass = PsiElementUtil.getPsiCLass(selectedElement);
                        }
                        return ProcessorHolder.getSwaggerApiControllerProcessor().support(psiClass) || ProcessorHolder.getSwaggerApiModelProcessor().support(psiClass);
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
        int startOffset = editor.getSelectionModel().getSelectionStart();
        PsiElement selectedElement = PsiUtilBase.getElementAtOffset(psiFile, startOffset);

        if (selectedElement instanceof PsiMethod || PsiElementUtil.getPsiMethod(selectedElement) != null) {
            PsiMethod psiMethod = null;
            if (selectedElement instanceof PsiMethod) {
                psiMethod = (PsiMethod) selectedElement;
            } else {
                psiMethod = PsiElementUtil.getPsiMethod(selectedElement);
            }
            PsiMethod finalPsiMethod = psiMethod;
            WriteCommandAction.runWriteCommandAction(psiFile.getProject(), () -> {
                SwaggerApiMethodProcessor swaggerApiMethodProcessor = ProcessorHolder.getSwaggerApiMethodProcessor();
                if (swaggerApiMethodProcessor.support(PsiElementUtil.getPsiCLass(finalPsiMethod), finalPsiMethod)) {
                    swaggerApiMethodProcessor.process(finalPsiMethod);
                }
            });
        } else if (selectedElement instanceof PsiClass || PsiElementUtil.getPsiCLass(selectedElement) != null) {
            PsiClass psiClass = null;
            if (selectedElement instanceof PsiClass) {
                psiClass = (PsiClass) selectedElement;
            } else {
                psiClass = PsiElementUtil.getPsiCLass(selectedElement);
            }
            PsiClass finalPsiClass = psiClass;
            WriteCommandAction.runWriteCommandAction(psiFile.getProject(), () -> {
                SwaggerApiControllerProcessor swaggerApiControllerProcessor = ProcessorHolder.getSwaggerApiControllerProcessor();
                if (swaggerApiControllerProcessor.support(finalPsiClass)) {
                    swaggerApiControllerProcessor.process(finalPsiClass);
                }
                SwaggerApiModelProcessor swaggerApiModelProcessor = ProcessorHolder.getSwaggerApiModelProcessor();
                if (swaggerApiModelProcessor.support(finalPsiClass)) {
                    swaggerApiModelProcessor.process(finalPsiClass);
                }
            });
        }

    }

}

