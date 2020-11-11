package com.sharedaka.parser;

import com.intellij.psi.*;
import com.intellij.psi.util.MethodSignature;
import com.intellij.spring.model.SpringBeanPointer;
import com.intellij.spring.model.SpringModelSearchParameters;
import com.intellij.spring.model.utils.SpringModelSearchers;
import com.sharedaka.core.SwaggerHelperApplicationManager;
import com.sharedaka.utils.PsiElementUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class HelperElementParser extends JavaElementVisitor {

    protected HashSet<PsiElement> psiElements = new HashSet<>();

    @Override
    public void visitMethod(PsiMethod method) {
        super.visitMethod(method);
    }

    @Override
    public void visitCallExpression(PsiCallExpression callExpression) {
        PsiMethod psiMethod = callExpression.resolveMethod();
        if (psiMethod != null) {
            if (PsiElementUtil.isAbstractMethod(psiMethod)) {
                PsiMethod resolvedMethod = findCallMethodInSpringContext(psiMethod);
                if (resolvedMethod != null) {
                    resolvedMethod.accept(this);
                }
            } else {
                psiMethod.accept(this);
            }
        }
    }

    public PsiMethod findCallMethodInSpringContext(PsiMethod psiMethod) {
        PsiClass abstractClass = psiMethod.getContainingClass();
        if (abstractClass != null) {
            MethodSignature methodSignature = psiMethod.getSignature(PsiSubstitutor.EMPTY);
            Collection<SpringBeanPointer> springBeanPointerList = SpringModelSearchers.findBeans(SwaggerHelperApplicationManager.getInstance(psiMethod.getProject()).getCommonSpringModel(), SpringModelSearchParameters.byClass(abstractClass));
            if (springBeanPointerList.size() == 1) {
                for (SpringBeanPointer springBeanPointer : springBeanPointerList) {
                    PsiClass psiClass = springBeanPointer.getBeanClass();
                    PsiMethod[] psiMethods = psiClass.getMethods();
                    for (PsiMethod methodImpl : psiMethods) {
                        if (methodImpl.getHierarchicalMethodSignature().equals(methodSignature)) {
                            return methodImpl;
                        }
                    }
                }
            }
        }
        return null;
    }


    @Override
    public void visitClass(PsiClass aClass) {
        super.visitClass(aClass);
    }

    @Override
    public void visitElement(@NotNull PsiElement element) {

        if (!psiElements.contains(element)) {
            psiElements.add(element);
            element.acceptChildren(this);
        }
    }


}
