package com.sharedaka.parser;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiNewExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.model.utils.SpringModelUtils;
import com.sharedaka.config.SwaggerHelperConfig;
import com.sharedaka.core.SwaggerHelperApplicationManager;
import com.sharedaka.entity.ErrorCodeEntity;
import com.sharedaka.entity.ErrorCodes;
import com.sharedaka.utils.BasicTypeUtil;

import java.util.HashMap;
import java.util.Map;

public class CodeExceptionParser extends ExceptionElementParser {

    Map<String, ErrorCodeEntity> result = new HashMap<>();

    public Map<String, ErrorCodeEntity> generate(PsiMethod psiMethod) {
        psiMethod.accept(this);
        return result;
    }

    @Override
    public boolean interestingException(Project project, PsiType exceptionType) {
        for (String interestingException : SwaggerHelperConfig.getInstance(project).interestingException) {
            if (exceptionType.getCanonicalText().equals(interestingException)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void processException(PsiThrowStatement statement) {
        for (PsiElement psiElement : statement.getChildren()) {
            if (psiElement instanceof PsiNewExpressionImpl) {
                PsiExpressionList argumentList = ((PsiNewExpressionImpl) psiElement).getArgumentList();
                if (argumentList != null) {
                    for (PsiExpression psiExpression : argumentList.getExpressions()) {
                        if (!BasicTypeUtil.isBasicType(psiExpression.getType().getCanonicalText())) {
                            PsiClass errorCodeEnum = JavaPsiFacade.getInstance(psiElement.getProject()).findClass(psiExpression.getType().getCanonicalText(), GlobalSearchScope.projectScope(psiElement.getProject()));
                            if (ParserHolder.getErrorCodeParser().support(errorCodeEnum)) {
                                ErrorCodes errorCodes = (ErrorCodes) ParserHolder.getErrorCodeParser().parse(errorCodeEnum);
                                result.put(psiExpression.getText(), errorCodes.getErrorCodeEntityByName(psiExpression.getText()));
                            }
                        }
                    }
                }
            }
        }
    }
}
