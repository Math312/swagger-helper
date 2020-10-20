package com.sharedaka.processor.business;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.sharedaka.constant.spring.SpringMvcAnnotations;
import com.sharedaka.constant.swagger.SwaggerAnnotations;
import com.sharedaka.entity.annotation.swagger.ApiModelEntity;
import com.sharedaka.entity.annotation.swagger.ApiModelPropertyEntity;
import com.sharedaka.parser.annotation.AnnotationParserHolder;
import com.sharedaka.processor.annotation.swagger.ApiModelProcessor;
import com.sharedaka.processor.annotation.swagger.ApiModelPropertyProcessor;
import com.sharedaka.utils.PsiAnnotationUtil;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SwaggerApiModelProcessor implements ClassSupportable {
    @Override
    public boolean support(PsiClass psiClass) {
        Set<String> annotationName = Arrays.stream(psiClass.getAnnotations()).map(PsiAnnotation::getQualifiedName).collect(Collectors.toSet());
        return !psiClass.isEnum() && !psiClass.isInterface() && !annotationName.contains(SpringMvcAnnotations.SERVICE_ANNOTATION_NAME) && !annotationName.contains(SpringMvcAnnotations.COMPONENT_ANNOTATION_NAME) && !annotationName.contains(SpringMvcAnnotations.CONFIGURATION_ANNOTATION_NAME) && !annotationName.contains(SpringMvcAnnotations.CONTROLLER_ANNOTATION_NAME) && !annotationName.contains(SpringMvcAnnotations.REST_CONTROLLER_ANNOTATION_NAME);

    }

    @Override
    public void process(PsiClass psiClass) {
        Project project = psiClass.getProject();
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        processApiModel(elementFactory, psiClass);
        processApiModelProperty(elementFactory, psiClass);
    }

    private void processApiModel(PsiElementFactory elementFactory, PsiClass psiClass) {
        PsiAnnotation existedApiModel = psiClass.getAnnotation(SwaggerAnnotations.SWAGGER_API_MODEL);
        ApiModelEntity apiModel = ApiModelProcessor.createByPsiClass(psiClass);
        if (existedApiModel != null) {
            ApiModelEntity existedApiModelEntity = (ApiModelEntity) AnnotationParserHolder.getAnnotationProcessor(SwaggerAnnotations.SWAGGER_API_MODEL).parse(existedApiModel);
            ApiModelProcessor.mergeApiAnnotation(existedApiModelEntity, apiModel);
        }
        PsiAnnotationUtil.writeAnnotation(elementFactory, "ApiModel", SwaggerAnnotations.SWAGGER_API_MODEL, ApiModelProcessor.createAnnotationString(apiModel), psiClass);
    }

    private void processApiModelProperty(PsiElementFactory elementFactory, PsiClass psiClass) {
        PsiField[] psiFields = psiClass.getFields();
        for (PsiField psiField : psiFields) {
            PsiAnnotation apiModelPropertyAnnotation = psiField.getAnnotation(SwaggerAnnotations.SWAGGER_API_MODEL_PROPERTY);
            ApiModelPropertyEntity apiModelProperty = ApiModelPropertyProcessor.createByPsiField(psiField);
            if (apiModelPropertyAnnotation != null) {
                ApiModelPropertyEntity existedApiModelProperty = (ApiModelPropertyEntity) AnnotationParserHolder.getAnnotationProcessor(SwaggerAnnotations.SWAGGER_API_MODEL_PROPERTY).parse(apiModelPropertyAnnotation);
                ApiModelPropertyProcessor.mergeApiAnnotation(existedApiModelProperty, apiModelProperty);
            }
            PsiAnnotationUtil.writeAnnotation(elementFactory, "ApiModelProperty", SwaggerAnnotations.SWAGGER_API_MODEL_PROPERTY, ApiModelPropertyProcessor.createAnnotationString(apiModelProperty), psiField);
        }
    }
}
