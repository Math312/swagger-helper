package com.sharedaka.parser.annotation.spring;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;
import com.sharedaka.entity.annotation.spring.RequestMappingEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.StringUtil;

import java.util.Objects;

import static com.sharedaka.constant.spring.SpringMvcAnnotations.REQUEST_MAPPING_ANNOTATION_NAME;

public class RequestMappingParser extends AbstractAnnotationParser {

    private final String HTTP_METHOD_ARRAY_REGEX = "\\{(RequestMethod.GET|RequestMethod.POST|RequestMethod.PUT|RequestMethod.DELETE|GET|PUT|POST|DELETE,)*(RequestMethod.GET|RequestMethod.POST|RequestMethod.PUT|RequestMethod.DELETE|GET|PUT|POST|DELETE)?\\}";

    private final String HTTP_METHOD_REGEX = "(GET|PUT|POST|DELETE|RequestMethod.GET|RequestMethod.PUT|RequestMethod.POST|RequestMethod.DELETE)";

    private final String HTTP_PATH_ARRAY_REGEX = "\\{(\"\\w*\",)*(\"\\w*\")?\\}";

    private final String HTTP_PATH_REGEX = "\"\\w*\"";


    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return REQUEST_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName());
    }

    @Override
    public Object doParse(PsiAnnotation psiAnnotation) {
        RequestMappingEntity result = new RequestMappingEntity();
        PsiNameValuePair[] attributes = psiAnnotation.getParameterList().getAttributes();
        for (PsiNameValuePair attribute : attributes) {
            PsiAnnotationMemberValue attributeValue = attribute.getValue();
            if (attributeValue != null) {
                if (attribute.getName() == null || attribute.getName().length() == 0) {
                    String paths = StringUtil.removeSpace(attribute.getText());
                    result.setValue(parsePathAttribute(paths));
                } else {
                    switch (Objects.requireNonNull(attribute.getName())) {
                        case "name": {
                            result.setName(StringUtil.removeHeadAndTailQuotationMarks(attribute.getValue().getText()));
                            break;
                        }
                        case "method": {
                            String httpMethods = StringUtil.removeSpace(attributeValue.getText());
                            result.setMethod(parseMethodAttribute(httpMethods));
                            break;
                        }
                        case "value": {
                            String paths = StringUtil.removeSpace(attribute.getText());
                            result.setValue(parsePathAttribute(paths));
                            break;
                        }
                        case "path": {
                            String paths = StringUtil.removeSpace(attribute.getText());
                            result.setPath(parsePathAttribute(paths));
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    public String[] parsePathAttribute(String pathAttribute) {
        if (pathAttribute.matches(HTTP_PATH_ARRAY_REGEX)) {
            String[] paths = pathAttribute.substring(1, pathAttribute.length() - 1).split(",");
            for (int i = 0; i < paths.length; i++) {
                paths[i] = StringUtil.removeHeadAndTailQuotationMarks(paths[i]);
            }
            return paths;
        } else if (pathAttribute.matches(HTTP_PATH_REGEX)) {
            return new String[]{StringUtil.removeHeadAndTailQuotationMarks(pathAttribute)};
        } else {
            return new String[]{""};
        }
    }

    public String[] parseMethodAttribute(String methodAttribute) {
        if (methodAttribute.matches(HTTP_METHOD_ARRAY_REGEX)) {
            String[] methods = methodAttribute.substring(1, methodAttribute.length() - 1).split(",");
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].length() <= 5) {
                    methods[i] = "RequestMethod." + methods[i];
                }
            }
            return methods;
        } else if (methodAttribute.matches(HTTP_METHOD_REGEX)) {
            if (methodAttribute.length() <= 5) {
                return new String[]{"RequestMethod." + methodAttribute};
            } else {
                return new String[]{methodAttribute};
            }
        } else {
            return new String[]{"RequestMethod.GET"};
        }

    }
}
