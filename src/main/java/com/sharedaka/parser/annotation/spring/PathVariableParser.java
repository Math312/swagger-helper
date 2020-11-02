package com.sharedaka.parser.annotation.spring;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.sharedaka.entity.annotation.spring.PathVariableEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.PsiAnnotationUtil;

import java.util.Map;

import static com.sharedaka.constant.spring.SpringMvcAnnotations.PATH_VARIABLE_ANNOTATION_NAME;

public class PathVariableParser extends AbstractAnnotationParser {

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return PATH_VARIABLE_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName());
    }

    @Override
    protected Object mapToAnnotationEntity(Map<String, PsiAnnotationMemberValue> attributeMap) {
        PathVariableEntity pathVariableEntity = new PathVariableEntity();
        pathVariableEntity.setValue(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("value")));
        pathVariableEntity.setName(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("name")));
        pathVariableEntity.setRequired(PsiAnnotationUtil.parseBooleanAttribute(attributeMap.get("required")));
        return pathVariableEntity;
    }
}
