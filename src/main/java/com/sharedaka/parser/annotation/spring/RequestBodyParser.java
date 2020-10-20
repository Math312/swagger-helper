package com.sharedaka.parser.annotation.spring;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;
import com.sharedaka.entity.annotation.spring.RequestBodyEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;

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
    public Object doParse(PsiAnnotation psiAnnotation) {
        RequestBodyEntity requestBody = new RequestBodyEntity();
        PsiNameValuePair[] attributes = psiAnnotation.getParameterList().getAttributes();
        for (PsiNameValuePair attribute : attributes) {
            PsiAnnotationMemberValue value = attribute.getValue();
            if ("required".equals(attribute.getName())) {
                if (value != null) {
                    requestBody.setRequired(Boolean.parseBoolean(value.getText()));
                }
            }
        }
        return requestBody;
    }
}
