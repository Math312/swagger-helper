package com.sharedaka.parser.enums;

import com.intellij.psi.*;
import com.sharedaka.entity.ErrorCodeEntity;
import com.sharedaka.entity.ErrorCodes;
import com.sharedaka.utils.StringUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ErrorCodeParser extends AbstractEnumParser {


    @Override
    public boolean support(PsiClass psiClass) {
        return super.support(psiClass);
    }

    private boolean checkConstructor(PsiClass psiClass) {
        PsiMethod[] constructors = psiClass.getConstructors();
        if (constructors.length != 1) {
            return false;
        }
        PsiMethod constructor = constructors[0];
        PsiParameter[] parameters = constructor.getParameterList().getParameters();
        if (parameters.length != 2) {
            return false;
        }
        Set<String> typeSet = new HashSet<String>();
        for (PsiParameter parameter : parameters) {
            typeSet.add(parameter.getType().getCanonicalText());
        }
        return typeSet.contains("java.lang.String") && (typeSet.contains("java.lang.Integer") || typeSet.contains("int"));
    }

    @Override
    protected Object doParse(PsiClass psiClass) {
        ErrorCodes errorCodes = new ErrorCodes();
        errorCodes.setClassName(psiClass.getName());
        errorCodes.setQualifiedName(psiClass.getQualifiedName());
        HashMap<String, ErrorCodeEntity> errorCodesMap = new HashMap<>();
        for (PsiElement psiElement : psiClass.getChildren()) {
            if (psiElement instanceof PsiEnumConstant) {
                PsiEnumConstant enumConstant = (PsiEnumConstant) psiElement;
                ErrorCodeEntity errorCode = processEnumConstant(enumConstant);
                if (errorCode != null) {
                    errorCodesMap.put(errorCode.getName(), errorCode);
                }
            }
        }
        errorCodes.setErrorCodes(errorCodesMap);
        return errorCodes;
    }

    public ErrorCodeEntity processEnumConstant(PsiEnumConstant enumConstant) {
        PsiExpressionList argumentList = enumConstant.getArgumentList();
        if (argumentList == null) {
            return null;
        }

        String name = enumConstant.getName();
        Integer code = null;
        String message = null;
        for (PsiExpression argument : argumentList.getExpressions()) {
            String className = argument.getType().getCanonicalText();
            if ("int".equals(className) || "java.lang.Integer".equals(className)) {
                code = Integer.parseInt(argument.getText());
            } else {
                message = StringUtil.removeHeadAndTailQuotationMarks(argument.getText());
            }

        }
        return new ErrorCodeEntity(name, code, message);
    }
}
