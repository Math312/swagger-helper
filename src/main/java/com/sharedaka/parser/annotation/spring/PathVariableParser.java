package com.sharedaka.parser.annotation.spring;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;
import com.sharedaka.entity.annotation.spring.PathVariableEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.StringUtil;

import java.util.Objects;

import static com.sharedaka.constant.spring.SpringMvcAnnotations.PATH_VARIABLE_ANNOTATION_NAME;

public class PathVariableParser extends AbstractAnnotationParser {

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return PATH_VARIABLE_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName());
    }

    @Override
    public Object doParse(PsiAnnotation psiAnnotation) {
        PathVariableEntity requestHeader = new PathVariableEntity();
        PsiNameValuePair[] attributes = psiAnnotation.getParameterList().getAttributes();
        for (PsiNameValuePair attribute : attributes) {
            PsiAnnotationMemberValue value = attribute.getValue();
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
                case "required": {
                    if (value != null) {
                        requestHeader.setRequired(Boolean.parseBoolean(value.getText()));
                    }
                    break;
                }
            }
        }
        return requestHeader;
    }
}
