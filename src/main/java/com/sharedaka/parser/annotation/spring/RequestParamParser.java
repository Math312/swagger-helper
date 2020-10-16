package com.sharedaka.parser.annotation.spring;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;
import com.sharedaka.entity.annotation.spring.RequestParamEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.StringUtil;

/**
 * @author math312
 */
public class RequestParamParser extends AbstractAnnotationParser {

    private static final String SWAGGER_REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";

    public boolean support(PsiAnnotation swaggerRequestHeader) {
        return swaggerRequestHeader.hasQualifiedName(SWAGGER_REQUEST_PARAM);
    }

    /**
     * 不想用反射
     */
    @Override
    public Object doParse(PsiAnnotation psiAnnotation) {
        RequestParamEntity requestParam = new RequestParamEntity();
        PsiNameValuePair[] attributes = psiAnnotation.getParameterList().getAttributes();
        for (PsiNameValuePair attribute : attributes) {
            PsiAnnotationMemberValue value = attribute.getValue();
            switch (attribute.getAttributeName()) {
                case "value": {
                    if (value != null) {
                        requestParam.setValue(StringUtil.removeHeadAndTailQuotationMarks(value.getText()));
                    }
                    break;
                }
                case "name": {
                    if (value != null) {
                        requestParam.setName(StringUtil.removeHeadAndTailQuotationMarks(value.getText()));
                    }
                    break;
                }
                case "defaultValue": {
                    if (value != null) {
                        requestParam.setDefaultValue(StringUtil.removeHeadAndTailQuotationMarks(value.getText()));
                    }
                    break;
                }
                case "required": {
                    if (value != null) {
                        requestParam.setRequired(Boolean.parseBoolean(value.getText()));
                    }
                    break;
                }
            }
        }
        return requestParam;
    }
}
