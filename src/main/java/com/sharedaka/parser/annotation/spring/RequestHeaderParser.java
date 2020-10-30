package com.sharedaka.parser.annotation.spring;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;
import com.sharedaka.entity.annotation.spring.RequestHeaderEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.StringUtil;

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
    public Object doParse(PsiAnnotation psiAnnotation) {
        RequestHeaderEntity requestHeader = new RequestHeaderEntity();
        PsiNameValuePair[] attributes = psiAnnotation.getParameterList().getAttributes();
        for (PsiNameValuePair attribute : attributes) {
            PsiAnnotationMemberValue value = attribute.getValue();
            if (attribute.getName() == null || attribute.getName().length() == 0) {
                if (value != null) {
                    requestHeader.setValue(StringUtil.removeHeadAndTailQuotationMarks(value.getText()));
                }
            } else {
                switch (Objects.requireNonNull(attribute.getName())) {
                    case "value": {
                        if (value != null) {
                            requestHeader.setValue(StringUtil.removeHeadAndTailQuotationMarks(value.getText()));
                        }
                        break;
                    }
                    case "name": {
                        if (value != null) {
                            requestHeader.setName(StringUtil.removeHeadAndTailQuotationMarks(value.getText()));
                        }
                        break;
                    }
                    case "defaultValue": {
                        if (value != null) {
                            requestHeader.setDefaultValue(StringUtil.removeHeadAndTailQuotationMarks(value.getText()));
                        }
                        break;
                    }
                    case "required": {
                        if (value != null) {
                            requestHeader.setRequired(Boolean.parseBoolean(value.getText()));
                        }
                        break;
                    }
                }
            }
        }
        return requestHeader;
    }
}
