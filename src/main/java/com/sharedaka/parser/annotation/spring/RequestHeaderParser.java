package com.sharedaka.parser.annotation.spring;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.sharedaka.entity.annotation.spring.RequestHeaderEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.PsiAnnotationUtil;

import java.util.Map;
import java.util.Objects;

import static com.sharedaka.constant.spring.SpringMvcAnnotations.REQUEST_HEADER_ANNOTATION_NAME;

/**
 * @author math312
 */
public class RequestHeaderParser extends AbstractAnnotationParser {

    private static final String SWAGGER_REQUEST_HEADER = REQUEST_HEADER_ANNOTATION_NAME;

    public boolean support(PsiAnnotation swaggerRequestHeader) {
        return Objects.equals(swaggerRequestHeader.getQualifiedName(), SWAGGER_REQUEST_HEADER);
    }

    @Override
    protected Object mapToAnnotationEntity(Map<String, PsiAnnotationMemberValue> attributeMap) {
        RequestHeaderEntity requestHeader = new RequestHeaderEntity();
        requestHeader.setValue(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("value")));
        requestHeader.setName(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("name")));
        requestHeader.setDefaultValue(PsiAnnotationUtil.parseStringAttribute(attributeMap.get("defaultValue")));
        requestHeader.setRequired(PsiAnnotationUtil.parseBooleanAttribute(attributeMap.get("required")));
        return requestHeader;
    }
}
