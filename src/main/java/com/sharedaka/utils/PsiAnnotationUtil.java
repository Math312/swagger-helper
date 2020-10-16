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
        PsiAnnotation existAnnotation = psiModifierListOwner.getModifierList().findAnnotation(qualifiedName);
        if (existAnnotation != null) {
            existAnnotation.delete();
        }

        PsiElementUtil.importPackage(elementFactory, psiModifierListOwner.getContainingFile(), psiModifierListOwner.getProject(), name);
        PsiAnnotation psiAnnotation = psiModifierListOwner.getModifierList().addAnnotation(name);
        for (PsiNameValuePair pair : attributes) {
            psiAnnotation.setDeclaredAttributeValue(pair.getName(), pair.getValue());
        }
    }
}
