package com.sharedaka.parser.annotation.swagger;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;
import com.sharedaka.constant.swagger.SwaggerAnnotations;
import com.sharedaka.entity.annotation.swagger.ApiModelEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.PsiAnnotationUtil;

import java.util.HashMap;
import java.util.Map;

public class ApiModelParser extends AbstractAnnotationParser {

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return psiAnnotation.hasQualifiedName(SwaggerAnnotations.SWAGGER_API_MODEL);
    }

    @Override
    protected Object doParse(PsiAnnotation psiAnnotation) {
        PsiNameValuePair[] attributes = psiAnnotation.getParameterList().getAttributes();
        Map<String, PsiAnnotationMemberValue> attributeMap = new HashMap<>();
        for (PsiNameValuePair attribute : attributes) {
            if (attribute.getValue() != null) {
                attributeMap.put(attribute.getAttributeName(), attribute.getValue());
            }
        }
        return mapToAnnotationEntity(attributeMap);
    }

    private Object mapToAnnotationEntity(Map<String, PsiAnnotationMemberValue> attributeMap) {
        ApiModelEntity apiModelProperty = new ApiModelEntity();
        apiModelProperty.setValue(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("value")));
        apiModelProperty.setDescription(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("description")));
        apiModelProperty.setReference(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("reference")));
        apiModelProperty.setParent(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("parent")));
        apiModelProperty.setDiscriminator(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("discriminator")));
        apiModelProperty.setSubTypes(PsiAnnotationUtil.parseStringArrayAttribute(attributeMap.get("subTypes")));
        return apiModelProperty;
    }
}
