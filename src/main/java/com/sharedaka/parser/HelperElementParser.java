package com.sharedaka.parser;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiNewExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.sharedaka.entity.ErrorCodes;
import com.sharedaka.utils.BasicTypeUtil;
import com.sharedaka.utils.PsiElementUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class HelperElementParser extends JavaElementVisitor {

    protected HashSet<PsiElement> psiElements = new HashSet<>();

    @Override
    public void visitMethod(PsiMethod method) {
        super.visitMethod(method);
    }

    @Override
    public void visitCallExpression(PsiCallExpression callExpression) {
        if (!(PsiElementUtil.isComplexCall(callExpression) || PsiElementUtil.isPipeline(callExpression))) {
            PsiMethod psiMethod = callExpression.resolveMethod();
            if (psiMethod != null) {
                psiMethod.accept(this);
            }

        }
        super.visitCallExpression(callExpression);
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
