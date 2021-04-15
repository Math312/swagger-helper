package com.sharedaka.parser.annotation.swagger;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.sharedaka.constant.swagger.SwaggerAnnotations;
import com.sharedaka.entity.annotation.swagger.ApiModelPropertyEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.PsiAnnotationUtil;

import java.util.Map;
import java.util.Objects;

public class ApiModelPropertyParser extends AbstractAnnotationParser {

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return Objects.equals(psiAnnotation.getQualifiedName(), SwaggerAnnotations.SWAGGER_API_MODEL_PROPERTY);
    }

    @Override
    public Object mapToAnnotationEntity(Map<String, PsiAnnotationMemberValue> attributeMap) {
        ApiModelPropertyEntity apiModelProperty = new ApiModelPropertyEntity();
        apiModelProperty.setValue(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("value")));
        apiModelProperty.setName(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("name")));
        apiModelProperty.setReference(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("reference")));
        apiModelProperty.setReadOnly(PsiAnnotationUtil.parseBooleanAttribute(attributeMap.get("readOnly")));
        apiModelProperty.setDataType(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("dataType")));
        apiModelProperty.setRequired(PsiAnnotationUtil.parseBooleanAttribute(attributeMap.get("required")));
        apiModelProperty.setNotes(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("notes")));
        apiModelProperty.setAllowEmptyValue(PsiAnnotationUtil.parseBooleanAttribute(attributeMap.get("allowEmptyValue")));
        apiModelProperty.setAllowableValues(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("allowableValues")));
        apiModelProperty.setPosition(PsiAnnotationUtil.parseIntegerAttribute(attributeMap.get("position")));
        apiModelProperty.setAccess(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("access")));
        apiModelProperty.setHidden(PsiAnnotationUtil.parseBooleanAttribute(attributeMap.get("hidden")));
        return apiModelProperty;
    }
}
