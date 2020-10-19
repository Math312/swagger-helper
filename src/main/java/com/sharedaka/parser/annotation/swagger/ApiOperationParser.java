package com.sharedaka.parser.annotation.swagger;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiNameValuePair;
import com.sharedaka.entity.annotation.swagger.ApiOperationEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_API_OPERATION_ANNOTATION_NAME;

public class ApiOperationParser extends AbstractAnnotationParser {

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return psiAnnotation.hasQualifiedName(SWAGGER_API_OPERATION_ANNOTATION_NAME);
    }

    @Override
    protected Object doParse(PsiAnnotation psiAnnotation) {
        PsiNameValuePair[] attributes = psiAnnotation.getParameterList().getAttributes();
        Map<String, PsiAnnotationMemberValue> attributeMap = new HashMap<>();
        for (PsiNameValuePair attribute : attributes) {
            if (attribute.getValue() != null) {
                attributeMap.put(attribute.getAttributeName(), attribute.getValue());
            }
        }
        return mapToAnnotationEntity(attributeMap);
    }

    private Object mapToAnnotationEntity(Map<String, PsiAnnotationMemberValue> attributeMap) {
        ApiOperationEntity apiOperationEntity = new ApiOperationEntity();
        if (attributeMap.containsKey("value")) {
            apiOperationEntity.setValue(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("value").getText()));
        }
        if (attributeMap.containsKey("notes")) {
            apiOperationEntity.setNotes(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("notes").getText()));
        }
        if (attributeMap.containsKey("response")) {
            apiOperationEntity.setResponse(attributeMap.get("response").getText());
        }
        if (attributeMap.containsKey("httpMethod")) {
            apiOperationEntity.setHttpMethod(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("httpMethod").getText()));
        }
        if (attributeMap.containsKey("consumes")) {
            apiOperationEntity.setConsumes(StringUtil.removeHeadAndTailQuotationMarks(attributeMap.get("consumes").getText()));
        }
        if (attributeMap.containsKey("tags")) {
            String content = attributeMap.get("tags").getText();
            content = StringUtil.removeSpace(content);
            content = content.substring(1, content.length() - 1);
            if (content.equals("")) {
                apiOperationEntity.setTags(new String[]{});
            } else {
                String[] contents = content.split(",");
                for (int i = 0; i < contents.length; i++) {
                    contents[i] = StringUtil.removeHeadAndTailQuotationMarks(contents[i]);
                }
                apiOperationEntity.setTags(contents);
            }
        }
        return apiOperationEntity;
    }
}
