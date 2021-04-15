package com.sharedaka.utils;

import com.intellij.psi.*;

public class PsiAnnotationUtil {

    public static String getAttributeStringValue(PsiAnnotation psiAnnotation, String attributeName) {
        PsiAnnotationMemberValue attributeValue = psiAnnotation.findDeclaredAttributeValue(attributeName);
        if (attributeValue == null) {
            return null;
        } else {
            return attributeValue.getText();
        }
    }

    public static void writeAnnotation(PsiElementFactory elementFactory, String name, String qualifiedName, String annotationText, PsiModifierListOwner psiModifierListOwner) {
        PsiAnnotation psiAnnotationDeclare = elementFactory.createAnnotationFromText(annotationText, psiModifierListOwner);
        final PsiNameValuePair[] attributes = psiAnnotationDeclare.getParameterList().getAttributes();
        if (psiModifierListOwner.getModifierList() != null) {
            PsiAnnotation existAnnotation = psiModifierListOwner.getModifierList().findAnnotation(qualifiedName);
            if (existAnnotation != null) {
                existAnnotation.delete();
            }
        }
        PsiElementUtil.importPackage(elementFactory, psiModifierListOwner.getContainingFile(), psiModifierListOwner.getProject(), name);
        PsiAnnotation psiAnnotation = psiModifierListOwner.getModifierList().addAnnotation(name);
        for (PsiNameValuePair pair : attributes) {
            psiAnnotation.setDeclaredAttributeValue(pair.getName(), pair.getValue());
        }
    }

    public static String parseStringAttribute(PsiAnnotationMemberValue value) {
        if (value == null) {
            return null;
        }
        return StringUtil.removeHeadAndTailQuotationMarks(value.getText());
    }

    public static String[] parseStringArrayAttribute(PsiAnnotationMemberValue value) {
        if (value == null) {
            return null;
        }
        String valueStr = StringUtil.removeSpace(value.getText());
        valueStr = valueStr.substring(1, valueStr.length() - 1);
        if (valueStr.equals("")) {
            return new String[]{};
        }
        String[] valueArr = valueStr.split(",");
        for (int i = 0; i < valueArr.length; i++) {
            valueArr[i] = StringUtil.removeHeadAndTailQuotationMarks(valueArr[i]);
        }
        return valueArr;
    }

    public static Boolean parseBooleanAttribute(PsiAnnotationMemberValue value) {
        if (value == null) {
            return null;
        }
        String strValue = StringUtil.removeHeadAndTailQuotationMarks(value.getText());
        if (strValue.equals("true")) {
            return true;
        } else if (strValue.equals("false")) {
            return false;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Integer parseIntegerAttribute(PsiAnnotationMemberValue value) {
        if (value == null) {
            return null;
        }
        String strValue = StringUtil.removeHeadAndTailQuotationMarks(value.getText());
        return Integer.parseInt(strValue);
    }
}
