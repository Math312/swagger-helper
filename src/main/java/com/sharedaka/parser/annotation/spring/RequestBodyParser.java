package com.sharedaka.parser.annotation.spring;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.sharedaka.entity.annotation.spring.RequestBodyEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.PsiAnnotationUtil;

import java.util.Map;

/**
 * @author math312
 */
public class RequestBodyParser extends AbstractAnnotationParser {

    private static final String SWAGGER_REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody";

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return SWAGGER_REQUEST_BODY.equals(psiAnnotation.getQualifiedName());
    }

    @Override
    protected Object mapToAnnotationEntity(Map<String, PsiAnnotationMemberValue> attributeMap) {
        RequestBodyEntity requestBody = new RequestBodyEntity();
        requestBody.setRequired(PsiAnnotationUtil.parseBooleanAttribute(attributeMap.get("required")));
        return requestBody;
    }
}
