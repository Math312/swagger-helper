package com.sharedaka.parser.annotation.spring;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.sharedaka.entity.annotation.spring.RequestParamEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.PsiAnnotationUtil;

import java.util.Map;

/**
 * @author math312
 */
public class RequestParamParser extends AbstractAnnotationParser {

    private static final String SWAGGER_REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";

    public boolean support(PsiAnnotation swaggerRequestHeader) {
        return SWAGGER_REQUEST_PARAM.equals(swaggerRequestHeader.getQualifiedName());
    }

    @Override
    protected Object mapToAnnotationEntity(Map<String, PsiAnnotationMemberValue> attributeMap) {
        RequestParamEntity requestParam = new RequestParamEntity();
        requestParam.setName(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("name")));
        requestParam.setValue(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("value")));
        requestParam.setDefaultValue(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("defaultValue")));
        requestParam.setRequired(PsiAnnotationUtil.parseBooleanAttribute(attributeMap.get("required")));
        return requestParam;
    }
}
