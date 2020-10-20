package com.sharedaka.parser.annotation.swagger;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;
import com.sharedaka.constant.swagger.SwaggerAnnotations;
import com.sharedaka.entity.annotation.swagger.ApiEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.PsiAnnotationUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ApiParser extends AbstractAnnotationParser {

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return Objects.equals(psiAnnotation.getQualifiedName(), SwaggerAnnotations.SWAGGER_API_ANNOTATION_NAME);
    }

    @Override
    protected Object doParse(PsiAnnotation psiAnnotation) {
        PsiNameValuePair[] attributes = psiAnnotation.getParameterList().getAttributes();
        Map<String, PsiAnnotationMemberValue> attributeMap = new HashMap<>();
        for (PsiNameValuePair attribute : attributes) {
            if (attribute.getValue() != null) {
                attributeMap.put(attribute.getName(), attribute.getValue());
            }
        }
        return mapToAnnotationEntity(attributeMap);
    }

    private Object mapToAnnotationEntity(Map<String, PsiAnnotationMemberValue> attributeMap) {
        ApiEntity result = new ApiEntity();
        result.setValue(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("value")));
        result.setDescription(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("description")));
        result.setBasePath(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("basePath")));
        result.setProduces(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("produces")));
        result.setConsumes(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("consumes")));
        result.setProtocols(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("protocols")));
        result.setHidden(PsiAnnotationUtil.parseBooleanAttribute(attributeMap.get("hidden")));
        result.setTags(PsiAnnotationUtil.parseStringArrayAttribute(attributeMap.get("tags")));
        return result;
    }
}
