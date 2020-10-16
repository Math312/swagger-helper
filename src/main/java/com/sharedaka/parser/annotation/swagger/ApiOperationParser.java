package com.sharedaka.parser.annotation.swagger;

import com.intellij.psi.PsiAnnotation;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;

import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_API_OPERATION_ANNOTATION_NAME;

public class ApiOperationParser extends AbstractAnnotationParser {

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return psiAnnotation.hasQualifiedName(SWAGGER_API_OPERATION_ANNOTATION_NAME);
    }

    @Override
    protected Object doParse(PsiAnnotation psiAnnotation) {
        return null;
    }
}
