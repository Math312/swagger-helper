package com.sharedaka.parser.annotation.swagger;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;
import com.sharedaka.entity.annotation.swagger.ApiImplicitParamEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME;

public class ApiImplicitParamParser extends AbstractAnnotationParser {

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName());
    }

    @Override
    public Object doParse(PsiAnnotation psiAnnotation) {
        PsiNameValuePair[] attributes = psiAnnotation.getParameterList().getAttributes();
        Map<String, PsiAnnotationMemberValue> attributeMap = new HashMap<>();
        for (PsiNameValuePair attribute : attributes) {
            if (attribute.getValue() != null) {
                attributeMap.put(attribute.getName(), attribute.getValue());
            }
        }
        return mapToAnnotationEntity(attributeMap);
    }

    private ApiImplicitParamEntity mapToAnnotationEntity(Map<String, PsiAnnotationMemberValue> attributeMap) {
        ApiImplicitParamEntity apiImplicitParamEntity = new ApiImplicitParamEntity();
        if (attributeMap.containsKey("name")) {
            apiImplicitParamEntity.setName(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("name").getText()));
        }
        if (attributeMap.containsKey("value")) {
            apiImplicitParamEntity.setValue(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("value").getText()));
        }
        if (attributeMap.containsKey("defaultValue")) {
            apiImplicitParamEntity.setDefaultValue(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("defaultValue").getText()));
        }
        if (attributeMap.containsKey("required")) {
            apiImplicitParamEntity.setRequired(Boolean.parseBoolean(attributeMap.get("required").getText()));
        }
        if (attributeMap.containsKey("dataType")) {
            apiImplicitParamEntity.setDataType(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("dataType").getText()));
        }
        if (attributeMap.containsKey("dataTypeClass")) {
            apiImplicitParamEntity.setDataTypeClass(attributeMap.get("dataTypeClass").getText());
        }
        if (attributeMap.containsKey("paramType")) {
            apiImplicitParamEntity.setParamType(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("paramType").getText()));
        }
        if (attributeMap.containsKey("allowableValues")) {
            apiImplicitParamEntity.setAllowableValues(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("allowableValues").getText()));
        }
        if (attributeMap.containsKey("allowMultiple")) {
            apiImplicitParamEntity.setAllowMultiple(Boolean.parseBoolean(attributeMap.get("allowMultiple").getText()));
        }
        if (attributeMap.containsKey("access")) {
            apiImplicitParamEntity.setAccess(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("access").getText()));
        }
        if (attributeMap.containsKey("type")) {
            apiImplicitParamEntity.setType(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("type").getText()));
        }
        if (attributeMap.containsKey("format")) {
            apiImplicitParamEntity.setFormat(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("format").getText()));
        }
        if (attributeMap.containsKey("allowEmptyValue")) {
            apiImplicitParamEntity.setAllowEmptyValue(Boolean.parseBoolean(attributeMap.get("allowEmptyValue").getText()));
        }
        if (attributeMap.containsKey("readOnly")) {
            apiImplicitParamEntity.setReadOnly(Boolean.parseBoolean(attributeMap.get("readOnly").getText()));
        }
        if (attributeMap.containsKey("collectionFormat")) {
            apiImplicitParamEntity.setCollectionFormat(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("collectionFormat").getText()));
        }
        return apiImplicitParamEntity;
    }
}
