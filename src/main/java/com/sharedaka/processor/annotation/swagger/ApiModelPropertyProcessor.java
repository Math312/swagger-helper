package com.sharedaka.processor.annotation.swagger;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.javadoc.PsiDocComment;
import com.sharedaka.entity.annotation.swagger.ApiModelPropertyEntity;
import com.sharedaka.utils.StringUtil;

public class ApiModelPropertyProcessor {

    private static String API_MODEL_PROPERTY_FORMAT = "@ApiModelProperty(value = \"%s\", required = %s";

    public static ApiModelPropertyEntity createByPsiField(PsiField psiField) {
        ApiModelPropertyEntity apiModelProperty = new ApiModelPropertyEntity();
        PsiDocComment psiDocDocument = psiField.getDocComment();
        if (psiDocDocument != null) {
            StringBuilder sb = new StringBuilder();
            for (PsiElement psiElement : psiDocDocument.getDescriptionElements()) {
                sb.append(psiElement.getText());
            }
            apiModelProperty.setValue(StringUtil.removeSpace(sb.toString()));
        } else {
            apiModelProperty.setValue("");
        }

        apiModelProperty.setRequired(true);
        return apiModelProperty;
    }

    public static void mergeApiAnnotation(ApiModelPropertyEntity source, ApiModelPropertyEntity target) {
        if (source.getValue() != null) {
            target.setValue(source.getValue());
        }
        if (source.getAccess() != null) {
            target.setAccess(source.getAccess());
        }
        if (source.getAllowableValues() != null) {
            target.setAllowableValues(source.getAllowableValues());
        }
        if (source.getAllowEmptyValue() != null) {
            target.setAllowEmptyValue(source.getAllowEmptyValue());
        }
        if (source.getHidden() != null) {
            target.setHidden(source.getHidden());
        }
        if (source.getPosition() != null) {
            target.setPosition(source.getPosition());
        }
        if (source.getName() != null) {
            target.setName(source.getName());
        }
        if (source.getNotes() != null) {
            target.setNotes(source.getNotes());
        }
        if (source.getDataType() != null) {
            target.setDataType(source.getDataType());
        }
        if (source.getRequired() != null) {
            target.setRequired(source.getRequired());
        }
        if (source.getReadOnly() != null) {
            target.setReadOnly(source.getReadOnly());
        }
        if (source.getReference() != null) {
            target.setReference(source.getReference());
        }
    }

    public static String createAnnotationString(ApiModelPropertyEntity apiEntity) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(API_MODEL_PROPERTY_FORMAT, apiEntity.getValue(), apiEntity.getRequired()));
        if (apiEntity.getName() != null) {
            sb.append(String.format(",name = \"%s\"", apiEntity.getName()));
        }
        if (apiEntity.getDataType() != null) {
            sb.append(String.format(",dataType = %s", apiEntity.getDataType()));
        }
        if (apiEntity.getAllowableValues() != null) {
            sb.append(String.format(",allowableValues = \"%s\"", apiEntity.getAllowableValues()));
        }
        if (apiEntity.getPosition() != null) {
            sb.append(String.format(",position = %s", apiEntity.getPosition()));
        }
        if (apiEntity.getHidden() != null) {
            sb.append(String.format(",hidden = %s", apiEntity.getHidden()));
        }
        if (apiEntity.getAccess() != null) {
            sb.append(String.format(",access = \"%s\"", apiEntity.getAccess()));
        }
        if (apiEntity.getNotes() != null) {
            sb.append(String.format(",notes = \"%s\"", apiEntity.getNotes()));
        }
        if (apiEntity.getReference() != null) {
            sb.append(String.format(",reference = \"%s\"", apiEntity.getReference()));
        }
        if (apiEntity.getAllowEmptyValue() != null) {
            sb.append(String.format(",allowEmptyValue = %s", apiEntity.getAllowEmptyValue()));
        }
        if (apiEntity.getReadOnly() != null) {
            sb.append(String.format(",required = %s", apiEntity.getReadOnly()));
        }
        sb.append(")");
        return sb.toString();
    }
}
