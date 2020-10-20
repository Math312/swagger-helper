package com.sharedaka.processor.business;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.sharedaka.constant.spring.SpringMvcAnnotations;
import com.sharedaka.constant.swagger.SwaggerAnnotations;
import com.sharedaka.entity.annotation.swagger.ApiEntity;
import com.sharedaka.parser.annotation.AnnotationParserHolder;
import com.sharedaka.processor.annotation.swagger.ApiProcessor;
import com.sharedaka.utils.PsiAnnotationUtil;

public class SwaggerApiControllerProcessor implements ClassSupportable {

    @Override
    public boolean support(PsiClass psiClass) {
        if (psiClass == null) {
            return false;
        }
        return psiClass.getAnnotation(SpringMvcAnnotations.REST_CONTROLLER_ANNOTATION_NAME) != null || psiClass.getAnnotation(SpringMvcAnnotations.CONTROLLER_ANNOTATION_NAME) != null;
    }

    @Override
    public void process(PsiClass psiClass) {
        Project project = psiClass.getProject();
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        processSwaggerApiAnnotation(elementFactory, psiClass);
    }

    private static void processSwaggerApiAnnotation(PsiElementFactory elementFactory, PsiClass psiClass) {
        PsiAnnotation psiAnnotation = psiClass.getAnnotation(SwaggerAnnotations.SWAGGER_API_ANNOTATION_NAME);
        ApiEntity apiAnnotation = ApiProcessor.createByPsiClass(psiClass);
        if (psiAnnotation != null) {
            ApiEntity existedApiAnnotation = (ApiEntity) AnnotationParserHolder.getAnnotationProcessor(SwaggerAnnotations.SWAGGER_API_ANNOTATION_NAME).parse(psiAnnotation);
            ApiProcessor.mergeApiAnnotation(existedApiAnnotation, apiAnnotation);
        }
        String apiAnnotationStr = ApiProcessor.createAnnotationString(apiAnnotation);
        PsiAnnotationUtil.writeAnnotation(elementFactory, "Api", "io.swagger.annotations.Api", apiAnnotationStr, psiClass);
    }
}
