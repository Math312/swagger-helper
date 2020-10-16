package com.sharedaka.parser.annotation.swagger;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiElement;
import com.sharedaka.entity.annotation.swagger.ApiImplicitParamEntity;
import com.sharedaka.entity.annotation.swagger.ApiImplicitParamsEntity;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.parser.annotation.AnnotationParserHolder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME;
import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME;

public class ApiImplicitParamsParser extends AbstractAnnotationParser {

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return psiAnnotation.hasQualifiedName(SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME);
    }

    @Override
    public Object doParse(PsiAnnotation psiAnnotation) {
        ApiImplicitParamsEntity apiImplicitParamsEntity = new ApiImplicitParamsEntity();
        PsiAnnotationMemberValue apiImplicitParams = psiAnnotation.findAttributeValue("value");
        if (apiImplicitParams != null) {
            List<PsiElement> apiImplicitParamList = Arrays.stream(apiImplicitParams.getChildren())
                    .filter((apiImplicitParam) -> apiImplicitParam instanceof PsiAnnotation && ((PsiAnnotation) apiImplicitParam).hasQualifiedName(SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME))
                    .collect(Collectors.toList());
            ApiImplicitParamParser apiImplicitParamParser = (ApiImplicitParamParser) AnnotationParserHolder.getAnnotationProcessor(SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME);
            List<ApiImplicitParamEntity> apiImplicitParamEntities = new LinkedList<>();
            for (PsiElement psiElement : apiImplicitParamList) {
                apiImplicitParamEntities.add((ApiImplicitParamEntity) apiImplicitParamParser.doParse((PsiAnnotation) psiElement));
            }
            apiImplicitParamsEntity.setValue(apiImplicitParamEntities);
        }
        return apiImplicitParamsEntity;
    }
}
