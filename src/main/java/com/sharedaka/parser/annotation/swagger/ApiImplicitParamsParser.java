package com.sharedaka.parser.annotation.swagger;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiElement;
import com.sharedaka.entity.annotation.swagger.ApiImplicitParamEntity;
import com.sharedaka.entity.annotation.swagger.ApiImplicitParamsEntity;
import com.sharedaka.parser.ParserHolder;
import com.sharedaka.parser.annotation.AbstractAnnotationParser;

import java.util.*;
import java.util.stream.Collectors;

import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME;
import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME;

public class ApiImplicitParamsParser extends AbstractAnnotationParser {

    @Override
    public boolean support(PsiAnnotation psiAnnotation) {
        return SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName());
    }

    @Override
    public Object doParse(PsiAnnotation psiAnnotation) {
        ApiImplicitParamsEntity apiImplicitParamsEntity = new ApiImplicitParamsEntity();
        PsiAnnotationMemberValue apiImplicitParams = psiAnnotation.findAttributeValue("value");
        if (apiImplicitParams != null) {
            List<PsiElement> apiImplicitParamList = Arrays.stream(apiImplicitParams.getChildren())
                    .filter((apiImplicitParam) -> apiImplicitParam instanceof PsiAnnotation && Objects.equals(((PsiAnnotation) apiImplicitParam).getQualifiedName(), SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME))
                    .collect(Collectors.toList());
            ApiImplicitParamParser apiImplicitParamParser = (ApiImplicitParamParser) ParserHolder.getAnnotationProcessor(SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME);
            List<ApiImplicitParamEntity> apiImplicitParamEntities = new LinkedList<>();
            for (PsiElement psiElement : apiImplicitParamList) {
                apiImplicitParamEntities.add((ApiImplicitParamEntity) apiImplicitParamParser.doParse((PsiAnnotation) psiElement));
            }
            apiImplicitParamsEntity.setValue(apiImplicitParamEntities);
        }
        return apiImplicitParamsEntity;
    }

}
