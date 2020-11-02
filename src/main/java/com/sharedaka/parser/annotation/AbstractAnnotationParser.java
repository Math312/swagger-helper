package com.sharedaka.parser.annotation;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author math312
 */
public abstract class AbstractAnnotationParser {

    public abstract boolean support(PsiAnnotation psiAnnotation);

    public Object parse(PsiAnnotation psiAnnotation) {
        if (support(psiAnnotation)) {
            return doParse(psiAnnotation);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Object doParse(PsiAnnotation psiAnnotation) {
        Map<String, PsiAnnotationMemberValue> attributeMap = parseAnnotationAttributeToMap(psiAnnotation);
        return mapToAnnotationEntity(attributeMap);
    }

    protected Map<String, PsiAnnotationMemberValue> parseAnnotationAttributeToMap(PsiAnnotation psiAnnotation) {
        PsiNameValuePair[] attributes = psiAnnotation.getParameterList().getAttributes();
        Map<String, PsiAnnotationMemberValue> attributeMap = new HashMap<>();
        for (PsiNameValuePair attribute : attributes) {
            if (attribute.getValue() != null) {
                if (attribute.getName() == null || attribute.getName().length() == 0) {
                    attributeMap.put("value", attribute.getValue());
                } else {
                    attributeMap.put(attribute.getName(), attribute.getValue());
                }
            }
        }
        return attributeMap;
    }

    protected Object mapToAnnotationEntity(Map<String, PsiAnnotationMemberValue> attributeMap) {
        return null;
    }
}
