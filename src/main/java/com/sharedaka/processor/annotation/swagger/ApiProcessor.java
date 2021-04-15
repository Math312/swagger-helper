package com.sharedaka.processor.annotation.swagger;

import com.intellij.psi.PsiClass;
import com.sharedaka.entity.annotation.swagger.ApiEntity;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ApiProcessor {

    private static String API_FORMAT = "@Api(value = \"%s\", description = \"%s\"";

    public static ApiEntity createByPsiClass(PsiClass psiClass) {
        ApiEntity apiEntity = new ApiEntity();
        apiEntity.setValue("");
        apiEntity.setDescription("");
        return apiEntity;
    }

    public static void mergeApiAnnotation(ApiEntity source, ApiEntity target) {
        if (source.getValue() != null) {
            target.setValue(source.getValue());
        }
        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }
        if (source.getConsumes() != null) {
            target.setConsumes(source.getConsumes());
        }
        if (source.getBasePath() != null) {
            target.setBasePath(source.getBasePath());
        }
        if (source.getHidden() != null) {
            target.setHidden(source.getHidden());
        }
        if (source.getPosition() != null) {
            target.setPosition(source.getPosition());
        }
        if (source.getProduces() != null) {
            target.setProduces(source.getProduces());
        }
        if (source.getProtocols() != null) {
            target.setProtocols(source.getProtocols());
        }
        if (source.getTags() != null) {
            target.setTags(source.getTags());
        }
    }

    public static String createAnnotationString(ApiEntity apiEntity) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(API_FORMAT, apiEntity.getValue(), apiEntity.getDescription()));
        if (apiEntity.getProtocols() != null) {
            sb.append(String.format(",protocols = \"%s\"", apiEntity.getProtocols()));
        }
        if (apiEntity.getProduces() != null) {
            sb.append(String.format(",produces = \"%s\"", apiEntity.getProduces()));
        }
        if (apiEntity.getPosition() != null) {
            sb.append(String.format(",position = %s", apiEntity.getPosition()));
        }
        if (apiEntity.getHidden() != null) {
            sb.append(String.format(",hidden = %s", apiEntity.getHidden()));
        }
        if (apiEntity.getProtocols() != null) {
            sb.append(String.format(",protocols = %s", apiEntity.getProtocols()));
        }
        if (apiEntity.getBasePath() != null) {
            sb.append(String.format(",basePath = \"%s\"", apiEntity.getBasePath()));
        }
        if (apiEntity.getConsumes() != null) {
            sb.append(String.format(",consumes = \"%s\"", apiEntity.getConsumes()));
        }
        if (apiEntity.getTags() != null) {
            String[] tags = new String[apiEntity.getTags().length];
            for (int i = 0; i < tags.length; i++) {
                tags[i] = "\"" + apiEntity.getTags()[i] + "\"";
            }
            sb.append(Arrays.stream(tags).collect(Collectors.joining(",", ",tags = {", "}")));
        }
        sb.append(")");
        return sb.toString();
    }


}
