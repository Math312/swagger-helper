package com.sharedaka.processor.annotation.swagger;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.sharedaka.entity.annotation.swagger.ApiModelEntity;
import com.sharedaka.utils.StringUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ApiModelProcessor {

    private static String API_MODEL_FORMAT = "@ApiModel(value = \"%s\", description = \"%s\"";

    public static ApiModelEntity createByPsiClass(PsiClass psiClass) {
        ApiModelEntity apiModel = new ApiModelEntity();
        PsiDocComment psiDocDocument = psiClass.getDocComment();
        if (psiDocDocument != null) {
            StringBuilder sb = new StringBuilder();
            for (PsiElement psiElement : psiDocDocument.getDescriptionElements()) {
                sb.append(psiElement.getText());
            }
            apiModel.setDescription(StringUtil.removeSpace(sb.toString()));
        } else {
            apiModel.setDescription("");
        }

        apiModel.setValue(psiClass.getQualifiedName());
        return apiModel;
    }

    public static void mergeApiAnnotation(ApiModelEntity source, ApiModelEntity target) {
        if (source.getValue() != null) {
            target.setValue(source.getValue());
        }
        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }
        if (source.getDiscriminator() != null) {
            target.setDiscriminator(source.getDiscriminator());
        }
        if (source.getParent() != null) {
            target.setParent(source.getParent());
        }
        if (source.getSubTypes() != null) {
            target.setSubTypes(source.getSubTypes());
        }
        if (source.getReference() != null) {
            target.setReference(source.getReference());
        }
    }

    public static String createAnnotationString(ApiModelEntity apiEntity) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(API_MODEL_FORMAT, apiEntity.getValue(), apiEntity.getDescription()));
        if (apiEntity.getDiscriminator() != null) {
            sb.append(String.format(",discriminator = \"%s\"", apiEntity.getDiscriminator()));
        }
        if (apiEntity.getParent() != null) {
            sb.append(String.format(",parent = %s", apiEntity.getParent()));
        }
        if (apiEntity.getReference() != null) {
            sb.append(String.format(",reference = \"%s\"", apiEntity.getReference()));
        }
        if (apiEntity.getSubTypes() != null) {
            String[] subTypes = new String[apiEntity.getSubTypes().length];
            for (int i = 0; i < subTypes.length; i++) {
                subTypes[i] = "\"" + apiEntity.getSubTypes()[i] + "\"";
            }
            sb.append(Arrays.stream(subTypes).collect(Collectors.joining(",", ",subTypes = {", "}")));
        }
        sb.append(")");
        return sb.toString();
    }

}
