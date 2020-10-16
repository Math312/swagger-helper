package com.sharedaka.processor.business;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.sharedaka.constant.HttpMethods;
import com.sharedaka.entity.annotation.spring.*;
import com.sharedaka.entity.annotation.swagger.ApiImplicitParamEntity;
import com.sharedaka.entity.annotation.swagger.ApiImplicitParamsEntity;
import com.sharedaka.parser.annotation.AnnotationParserHolder;
import com.sharedaka.utils.BasicTypeUtil;
import com.sharedaka.utils.PsiAnnotationUtil;
import com.sharedaka.utils.PsiElementUtil;
import com.sharedaka.utils.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.sharedaka.constant.JavaConstants.*;
import static com.sharedaka.constant.spring.SpringMvcAnnotations.*;
import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME;
import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME;

public class SwaggerApiMethodProcessor implements MethodSupportable {

    private final Set<String> interestingAnnotation;

    private final String API_OPERATION_FORMAT = "@ApiOperation(value = \"%s\", notes = \"%s\",httpMethod = \"%s\", response = %s)";

    public SwaggerApiMethodProcessor() {
        this.interestingAnnotation = new HashSet<>(5);
        this.interestingAnnotation.add(DELETE_MAPPING_ANNOTATION_NAME);
        this.interestingAnnotation.add(GET_MAPPING_ANNOTATION_NAME);
        this.interestingAnnotation.add(REQUEST_MAPPING_ANNOTATION_NAME);
        this.interestingAnnotation.add(POST_MAPPING_ANNOTATION_NAME);
        this.interestingAnnotation.add(PUT_MAPPING_ANNOTATION_NAME);
    }

    @Override
    public boolean support(PsiMethod psiMethod) {
        PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();
        Set<String> annotationNames = Arrays.stream(annotations).map(PsiAnnotation::getQualifiedName).collect(Collectors.toSet());
        annotationNames.retainAll(this.interestingAnnotation);
        if (annotationNames.size() == 1) {
            PsiAnnotation psiAnnotation = psiMethod.getModifierList().findAnnotation(annotationNames.iterator().next());
            if (psiAnnotation.hasQualifiedName(REQUEST_MAPPING_ANNOTATION_NAME)) {
                RequestMappingEntity requestMapping = (RequestMappingEntity) AnnotationParserHolder.getAnnotationProcessor(REQUEST_MAPPING_ANNOTATION_NAME).parse(psiAnnotation);
                return Arrays.stream(requestMapping.getMethod()).collect(Collectors.toSet()).size() == 1;
            }
        } else {
            return false;
        }
        return true;
    }

    public void process(Project project, PsiMethod psiMethod) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        processApiOperation(elementFactory, psiMethod);
        processApiImplicitParams(elementFactory, psiMethod);
    }

    private void processApiImplicitParams(PsiElementFactory psiElementFactory, PsiMethod psiMethod) {
        PsiAnnotation apiImplicitParams = psiMethod.getModifierList().findAnnotation(SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME);
        PsiAnnotation apiImplicitParam = psiMethod.getModifierList().findAnnotation(SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME);
        Map<String, ApiImplicitParamEntity> existedApiImplicitParams = readAlreadyExistsAnnotation(apiImplicitParams, apiImplicitParam);
        PsiParameter[] psiParameters = psiMethod.getParameterList().getParameters();
        Map<String, ApiImplicitParamEntity> generatedImplicitParams = processMethodParam(psiParameters);
        mergeImplicitParams(existedApiImplicitParams, generatedImplicitParams);
        writeApiImplicitParamsToFile(generatedImplicitParams, psiElementFactory, psiMethod);
        PsiElementUtil.importPackage(psiElementFactory, psiMethod.getContainingFile(), psiMethod.getProject(), "ApiImplicitParam");
    }

    private void writeApiImplicitParamsToFile(Map<String, ApiImplicitParamEntity> generatedImplicitParams, PsiElementFactory psiElementFactory, PsiMethod psiMethod) {
        List<String> apiImplicitParamList = new LinkedList<>();
        for (Map.Entry<String, ApiImplicitParamEntity> entityEntry : generatedImplicitParams.entrySet()) {
            apiImplicitParamList.add(generateImplicitParamString(entityEntry.getValue()));
        }
        String apiImplicitParams = "@ApiImplicitParams({})";
        if (apiImplicitParamList.size() > 0) {
            apiImplicitParams = apiImplicitParamList.stream().collect(Collectors.joining(",\n", "@ApiImplicitParams({\n", "\n})"));
        }
        PsiAnnotationUtil.writeAnnotation(psiElementFactory, "ApiImplicitParams", "io.swagger.annotations.ApiImplicitParams", apiImplicitParams, psiMethod);
    }

    private void mergeImplicitParams(Map<String, ApiImplicitParamEntity> existedApiImplicitParams, Map<String, ApiImplicitParamEntity> generatedImplicitParams) {
        for (Map.Entry<String, ApiImplicitParamEntity> entityEntry : generatedImplicitParams.entrySet()) {
            if (existedApiImplicitParams.containsKey(entityEntry.getKey())) {
                ApiImplicitParamEntity apiImplicitParamEntity = existedApiImplicitParams.get(entityEntry.getKey());
                coverAttributes(apiImplicitParamEntity, entityEntry.getValue());
            }
        }
    }

    private Map<String, ApiImplicitParamEntity> readAlreadyExistsAnnotation(PsiAnnotation apiImplicitParams, PsiAnnotation apiImplicitParam) {
        Map<String, ApiImplicitParamEntity> result = new HashMap<>();
        if (apiImplicitParam != null) {
            ApiImplicitParamEntity apiImplicitParamEntity = (ApiImplicitParamEntity) AnnotationParserHolder.getAnnotationProcessor(SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME).parse(apiImplicitParam);
            if (apiImplicitParamEntity.getName() != null) {
                result.put(apiImplicitParamEntity.getName(), apiImplicitParamEntity);
            }
        }
        if (apiImplicitParams != null) {
            ApiImplicitParamsEntity apiImplicitParamsEntity = (ApiImplicitParamsEntity) AnnotationParserHolder.getAnnotationProcessor(SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME).parse(apiImplicitParams);
            if (apiImplicitParamsEntity.getValue() != null) {
                for (ApiImplicitParamEntity apiImplicitParamEntity : apiImplicitParamsEntity.getValue()) {
                    if (apiImplicitParamEntity.getName() != null) {
                        result.put(apiImplicitParamEntity.getName(), apiImplicitParamEntity);
                    }
                }
            }
        }
        return result;
    }

    private void processApiOperation(PsiElementFactory psiElementFactory, PsiMethod psiMethod) {
        PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();
        PsiAnnotation springMvcMappingAnnotation = chooseSpringMvcMappingAnnotation(annotations);
        String httpMethod = getHttpMethod(springMvcMappingAnnotation);
        PsiAnnotation apiOperationExist = psiMethod.getModifierList().findAnnotation("io.swagger.annotations.ApiOperation");
        String apiOperationAttrValue = "";
        String apiOperationAttrNotes = "";
        if (apiOperationExist != null) {
            apiOperationAttrValue = PsiAnnotationUtil.getAttributeStringValue(apiOperationExist, "value");
            if (apiOperationAttrValue == null) {
                apiOperationAttrValue = "";
            } else {
                apiOperationAttrValue = StringUtil.removeHeadAndTailQuotationMarks(apiOperationAttrValue);
            }
            apiOperationAttrNotes = PsiAnnotationUtil.getAttributeStringValue(apiOperationExist, "notes");
            if (apiOperationAttrNotes == null) {
                apiOperationAttrNotes = "";
            } else {
                apiOperationAttrNotes = StringUtil.removeHeadAndTailQuotationMarks(apiOperationAttrNotes);
            }
        }
        PsiType returnType = psiMethod.getReturnType();
        String returnTypeStr = VOID_CLASS;
        if (returnType != null) {
            returnTypeStr = returnType.getCanonicalText() + CLASS_SUFFIX;
        }
        String apiOperationAnnotationText = String.format(API_OPERATION_FORMAT, apiOperationAttrValue, apiOperationAttrNotes, httpMethod, returnTypeStr);
        PsiAnnotationUtil.writeAnnotation(psiElementFactory, "ApiOperation", "io.swagger.annotations.ApiOperation", apiOperationAnnotationText, psiMethod);
    }


    private PsiAnnotation chooseSpringMvcMappingAnnotation(PsiAnnotation[] psiAnnotations) {
        for (PsiAnnotation psiAnnotation : psiAnnotations) {
            if (psiAnnotation.hasQualifiedName(DELETE_MAPPING_ANNOTATION_NAME) ||
                    psiAnnotation.hasQualifiedName(GET_MAPPING_ANNOTATION_NAME) ||
                    psiAnnotation.hasQualifiedName(PUT_MAPPING_ANNOTATION_NAME) ||
                    psiAnnotation.hasQualifiedName(POST_MAPPING_ANNOTATION_NAME) ||
                    psiAnnotation.hasQualifiedName(REQUEST_MAPPING_ANNOTATION_NAME)) {
                return psiAnnotation;
            }
        }
        return null;
    }

    private String getHttpMethod(PsiAnnotation psiAnnotation) {
        if (psiAnnotation.hasQualifiedName(DELETE_MAPPING_ANNOTATION_NAME)) {
            return HttpMethods.DELETE;
        }
        if (psiAnnotation.hasQualifiedName(GET_MAPPING_ANNOTATION_NAME)) {
            return HttpMethods.GET;
        }
        if (psiAnnotation.hasQualifiedName(PUT_MAPPING_ANNOTATION_NAME)) {
            return HttpMethods.PUT;
        }
        if (psiAnnotation.hasQualifiedName(POST_MAPPING_ANNOTATION_NAME)) {
            return HttpMethods.POST;
        }
        if (psiAnnotation.hasQualifiedName(REQUEST_MAPPING_ANNOTATION_NAME)) {
            RequestMappingEntity requestMapping = (RequestMappingEntity) AnnotationParserHolder.getAnnotationProcessor(REQUEST_MAPPING_ANNOTATION_NAME).parse(psiAnnotation);
            if (requestMapping.getMethod().length == 0) {
                return HttpMethods.GET;
            } else {
                return requestMapping.getMethod()[0].replaceAll("RequestMethod.", "");
            }
        }
        return HttpMethods.GET;
    }

    public String getDataTypeByPsiType(PsiType psiType) {
        String canonicalName = psiType.getCanonicalText();
        if (canonicalName.equals(MULTIPART_FILE_CLASS_NAME) || canonicalName.equals(FILE_CLASS_NAME)) {
            return "file";
        }
        Set<String> superTypeNames = Arrays.stream(psiType.getSuperTypes()).map(PsiType::getCanonicalText).collect(Collectors.toSet());
        if (superTypeNames.contains(MULTIPART_FILE_CLASS_NAME) || superTypeNames.contains(FILE_CLASS_NAME)) {
            return "file";
        }
        String dataType = BasicTypeUtil.getDataTypeByCanonicalName(canonicalName);
        if (dataType != null) {
            return dataType;
        } else {
            return "object";
        }
    }

    private Map<String, ApiImplicitParamEntity> processMethodParam(PsiParameter[] psiParameters) {
        Map<String, ApiImplicitParamEntity> apiImplicitParamEntityMap = new LinkedHashMap<>();
        for (PsiParameter psiParameter : psiParameters) {
            PsiType psiType = psiParameter.getType();
            String dataTypeClass = psiType.getCanonicalText() + CLASS_SUFFIX;
            String dataType = getDataTypeByPsiType(psiType);
            String name = psiParameter.getName();
            String paramType = "query";
            String defaultValue = null;
            String value = "";
            boolean required = true;
            if (Objects.equals(dataType, "file")) {
                paramType = "form";
            }
            for (PsiAnnotation psiAnnotation : psiParameter.getAnnotations()) {
                if (StringUtils.isEmpty(psiAnnotation.getQualifiedName())) {
                    break;
                }
                switch (psiAnnotation.getQualifiedName()) {
                    case REQUEST_HEADER_ANNOTATION_NAME:
                        paramType = "header";
                        RequestHeaderEntity requestHeader = (RequestHeaderEntity) AnnotationParserHolder.getAnnotationProcessor(REQUEST_HEADER_ANNOTATION_NAME).parse(psiAnnotation);
                        if (null != requestHeader.getRequired()) {
                            required = requestHeader.getRequired();
                        }
                        if (null != requestHeader.getValue()) {
                            name = requestHeader.getValue();
                        }
                        if (null != requestHeader.getName()) {
                            name = requestHeader.getName();
                        }
                        defaultValue = requestHeader.getDefaultValue();
                        break;
                    case REQUEST_PARAM_ANNOTATION_NAME:
                        paramType = "query";
                        RequestParamEntity requestParam = (RequestParamEntity) AnnotationParserHolder.getAnnotationProcessor(REQUEST_PARAM_ANNOTATION_NAME).parse(psiAnnotation);
                        if (null != requestParam.getRequired()) {
                            required = requestParam.getRequired();
                        }
                        if (null != requestParam.getValue()) {
                            value = requestParam.getValue();
                        }
                        if (null != requestParam.getName()) {
                            name = requestParam.getName();
                        }
                        defaultValue = requestParam.getDefaultValue();
                        break;
                    case PATH_VARIABLE_ANNOTATION_NAME:
                        paramType = "path";
                        PathVariableEntity pathVariable = (PathVariableEntity) AnnotationParserHolder.getAnnotationProcessor(PATH_VARIABLE_ANNOTATION_NAME).parse(psiAnnotation);
                        if (null != pathVariable.getRequired()) {
                            required = pathVariable.getRequired();
                        }
                        if (null != pathVariable.getValue()) {
                            name = pathVariable.getValue();
                        }
                        if (null != pathVariable.getName()) {
                            name = pathVariable.getName();
                        }
                        break;
                    case REQUEST_BODY_ANNOTATION_NAME:
                        paramType = "body";
                        RequestBodyEntity requestBody = (RequestBodyEntity) AnnotationParserHolder.getAnnotationProcessor(REQUEST_BODY_ANNOTATION_NAME).parse(psiAnnotation);
                        if (null != requestBody.getRequired()) {
                            required = requestBody.getRequired();
                        }
                        break;
                    default:
                        break;
                }
            }
            ApiImplicitParamEntity apiImplicitParamEntity = new ApiImplicitParamEntity();
            apiImplicitParamEntity.setName(name);
            apiImplicitParamEntity.setRequired(required);
            apiImplicitParamEntity.setValue(value);
            apiImplicitParamEntity.setDataType(dataType);
            apiImplicitParamEntity.setDataTypeClass(dataTypeClass);
            apiImplicitParamEntity.setParamType(paramType);
            apiImplicitParamEntity.setDefaultValue(defaultValue);
            apiImplicitParamEntityMap.put(name, apiImplicitParamEntity);
        }
        return apiImplicitParamEntityMap;
    }


    private void coverAttributes(ApiImplicitParamEntity from, ApiImplicitParamEntity to) {
        if (from.getName() != null) {
            to.setName(from.getName());
        }
        if (from.getValue() != null) {
            to.setValue(from.getValue());
        }
        if (from.getDefaultValue() != null) {
            to.setDefaultValue(from.getDefaultValue());
        }
        if (from.getAllowableValues() != null) {
            to.setAllowableValues(from.getAllowableValues());
        }
        if (from.getRequired() != null) {
            to.setRequired(from.getRequired());
        }
        if (from.getAllowMultiple() != null) {
            to.setAllowMultiple(from.getAllowMultiple());
        }
        if (from.getAccess() != null) {
            to.setAccess(from.getAccess());
        }
        if (from.getDataType() != null) {
            to.setDataType(from.getDataType());
        }
        if (from.getDataTypeClass() != null) {
            to.setDataTypeClass(from.getDataTypeClass());
        }
        if (from.getParamType() != null) {
            to.setParamType(from.getParamType());
        }
        if (from.getType() != null) {
            to.setType(from.getType());
        }
        if (from.getFormat() != null) {
            to.setFormat(from.getFormat());
        }
        if (from.getAllowEmptyValue() != null) {
            to.setAllowEmptyValue(from.getAllowEmptyValue());
        }
        if (from.getReadOnly() != null) {
            to.setReadOnly(from.getReadOnly());
        }
        if (from.getCollectionFormat() != null) {
            to.setCollectionFormat(from.getCollectionFormat());
        }
    }


    private String generateImplicitParamString(ApiImplicitParamEntity from) {
        StringBuilder sb = new StringBuilder();
        String format = "@ApiImplicitParam(value = \"%s\", name = \"%s\", required = %s, paramType = \"%s\", dataType = \"%s\", dataTypeClass = %s";
        String init = String.format(format, from.getValue(), from.getName(), from.getRequired(), from.getParamType(), from.getDataType(), from.getDataTypeClass());
        sb.append(init);
        if (from.getDefaultValue() != null) {
            sb.append(String.format(", defaultValue = \"%s\"", from.getDefaultValue()));
        }
        if (from.getAllowableValues() != null) {
            sb.append(String.format(", allowableValues = \"%s\"", from.getAllowableValues()));
        }
        if (from.getAllowMultiple() != null) {
            sb.append(String.format(", allowableValues = \"%s\"", from.getAllowableValues()));
        }
        if (from.getAccess() != null) {
            sb.append(String.format(", access = \"%s\"", from.getAccess()));
        }
        if (from.getType() != null) {
            sb.append(String.format(", type = \"%s\"", from.getType()));
        }
        if (from.getFormat() != null) {
            sb.append(String.format(", format = \"%s\"", from.getFormat()));
        }
        if (from.getAllowEmptyValue() != null) {
            sb.append(String.format(", allowEmptyValue = \"%s\"", from.getAllowEmptyValue()));
        }
        if (from.getReadOnly() != null) {
            sb.append(String.format(", readOnly = \"%s\"", from.getReadOnly()));
        }
        if (from.getCollectionFormat() != null) {
            sb.append(String.format(", collectionFormat = \"%s\"", from.getCollectionFormat()));
        }
        sb.append(")");
        return sb.toString();
    }

}
