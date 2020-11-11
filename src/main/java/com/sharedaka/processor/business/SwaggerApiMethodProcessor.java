package com.sharedaka.processor.business;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.sharedaka.constant.HttpMethods;
import com.sharedaka.entity.ErrorCodeEntity;
import com.sharedaka.entity.annotation.spring.*;
import com.sharedaka.entity.annotation.swagger.ApiImplicitParamEntity;
import com.sharedaka.entity.annotation.swagger.ApiImplicitParamsEntity;
import com.sharedaka.entity.annotation.swagger.ApiOperationEntity;
import com.sharedaka.parser.CodeExceptionParser;
import com.sharedaka.parser.ParserHolder;
import com.sharedaka.utils.BasicTypeUtil;
import com.sharedaka.utils.PsiAnnotationUtil;
import com.sharedaka.utils.PsiElementUtil;
import com.sharedaka.utils.PsiTypeUtil;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.sharedaka.constant.JavaConstants.CLASS_SUFFIX;
import static com.sharedaka.constant.JavaConstants.FILE_CLASS_NAME;
import static com.sharedaka.constant.spring.SpringMvcAnnotations.*;
import static com.sharedaka.constant.swagger.SwaggerAnnotations.*;

public class SwaggerApiMethodProcessor implements MethodSupportable {

    private final Set<String> interestingAnnotation;

    private final String API_OPERATION_FORMAT = "@ApiOperation(value = \"%s\", notes = \"%s\",httpMethod = \"%s\"";

    private final String API_RESPONSE_FORMAT = "@ApiResponse(code = %d, message = \"%s\")";

    public SwaggerApiMethodProcessor() {
        this.interestingAnnotation = new HashSet<>(5);
        this.interestingAnnotation.add(DELETE_MAPPING_ANNOTATION_NAME);
        this.interestingAnnotation.add(GET_MAPPING_ANNOTATION_NAME);
        this.interestingAnnotation.add(REQUEST_MAPPING_ANNOTATION_NAME);
        this.interestingAnnotation.add(POST_MAPPING_ANNOTATION_NAME);
        this.interestingAnnotation.add(PUT_MAPPING_ANNOTATION_NAME);
    }

    @Override
    public boolean support(PsiClass psiClass, PsiMethod psiMethod) {
        if (PsiElementUtil.getAnnotation(psiClass, REST_CONTROLLER_ANNOTATION_NAME) != null || PsiElementUtil.getAnnotation(psiClass, CONTROLLER_ANNOTATION_NAME) != null) {
            PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();
            Set<String> annotationNames = Arrays.stream(annotations).map(PsiAnnotation::getQualifiedName).collect(Collectors.toSet());
            annotationNames.retainAll(this.interestingAnnotation);
            if (annotationNames.size() == 1) {
                PsiAnnotation psiAnnotation = psiMethod.getModifierList().findAnnotation(annotationNames.iterator().next());
                if (REQUEST_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName())) {
                    RequestMappingEntity requestMapping = (RequestMappingEntity) ParserHolder.getAnnotationProcessor(REQUEST_MAPPING_ANNOTATION_NAME).parse(psiAnnotation);
                    return Arrays.stream(requestMapping.getMethod()).collect(Collectors.toSet()).size() == 1;
                }
            } else {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public void process(PsiMethod psiMethod) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiMethod.getProject());
        processApiOperation(elementFactory, psiMethod);
        processApiImplicitParams(elementFactory, psiMethod);
        processApiResponses(elementFactory, psiMethod);
    }

    private void processApiResponses(PsiElementFactory elementFactory, PsiMethod psiMethod) {
        CodeExceptionParser codeExceptionParser = new CodeExceptionParser();
        Map<String, ErrorCodeEntity> result = codeExceptionParser.generate(psiMethod);
        writeApiResponseToFile(result, elementFactory, psiMethod);
        PsiElementUtil.importPackage(elementFactory, psiMethod.getContainingFile(), psiMethod.getProject(), "ApiResponse");
    }

    private void writeApiResponseToFile(Map<String, ErrorCodeEntity> errorCodeEntityMap, PsiElementFactory psiElementFactory, PsiMethod psiMethod) {
        List<String> apiResponseList = new LinkedList<>();
        for (Map.Entry<String, ErrorCodeEntity> entityEntry : errorCodeEntityMap.entrySet()) {
            apiResponseList.add(String.format(API_RESPONSE_FORMAT, entityEntry.getValue().getCode(), entityEntry.getValue().getMessage()));
        }
        String apiResponses = "@ApiResponses({})";
        if (apiResponseList.size() > 0) {
            apiResponses = apiResponseList.stream().collect(Collectors.joining(",\n", "@ApiResponses({\n", "\n})"));
        }
        PsiAnnotationUtil.writeAnnotation(psiElementFactory, "ApiResponses", "io.swagger.annotations.ApiResponses", apiResponses, psiMethod);
    }

    private void processApiImplicitParams(PsiElementFactory psiElementFactory, PsiMethod psiMethod) {
        PsiAnnotation apiImplicitParams = psiMethod.getModifierList().findAnnotation(SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME);
        PsiAnnotation apiImplicitParam = psiMethod.getModifierList().findAnnotation(SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME);
        Map<String, ApiImplicitParamEntity> existedApiImplicitParams = readAlreadyExistsAnnotation(apiImplicitParams, apiImplicitParam);
        PsiParameter[] psiParameters = psiMethod.getParameterList().getParameters();
        Map<String, ApiImplicitParamEntity> generatedImplicitParams = processMethodParam(psiMethod.getProject(), psiParameters);
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
            ApiImplicitParamEntity apiImplicitParamEntity = (ApiImplicitParamEntity) ParserHolder.getAnnotationProcessor(SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME).parse(apiImplicitParam);
            if (apiImplicitParamEntity.getName() != null) {
                result.put(apiImplicitParamEntity.getName(), apiImplicitParamEntity);
            }
        }
        if (apiImplicitParams != null) {
            ApiImplicitParamsEntity apiImplicitParamsEntity = (ApiImplicitParamsEntity) ParserHolder.getAnnotationProcessor(SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME).parse(apiImplicitParams);
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
        PsiAnnotation apiOperationExist = psiMethod.getModifierList().findAnnotation(SWAGGER_API_OPERATION_ANNOTATION_NAME);
        ApiOperationEntity apiOperationEntity = createApiOperation(psiMethod);
        if (apiOperationExist != null) {
            ApiOperationEntity apiOperationEntityExisted = (ApiOperationEntity) ParserHolder.getAnnotationProcessor(SWAGGER_API_OPERATION_ANNOTATION_NAME).parse(apiOperationExist);
            mergeApiOperation(apiOperationEntityExisted, apiOperationEntity);
        }
        String apiOperationAnnotationText = createApiOperationStr(apiOperationEntity);
        PsiAnnotationUtil.writeAnnotation(psiElementFactory, "ApiOperation", "io.swagger.annotations.ApiOperation", apiOperationAnnotationText, psiMethod);
    }

    private String createApiOperationStr(ApiOperationEntity apiOperationEntity) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(API_OPERATION_FORMAT, apiOperationEntity.getValue(), apiOperationEntity.getNotes(), apiOperationEntity.getHttpMethod()));
        if (apiOperationEntity.getConsumes() != null) {
            sb.append(String.format(", consumes= \"%s\" ", apiOperationEntity.getConsumes()));
        }
        if (apiOperationEntity.getTags() != null) {
            String[] tags = new String[apiOperationEntity.getTags().length];
            for (int i = 0; i < tags.length; i++) {
                tags[i] = "\"" + apiOperationEntity.getTags()[i] + "\"";
            }
            sb.append(Arrays.stream(tags).collect(Collectors.joining(",", ",tags = {", "}")));
        }
        return sb.toString();
    }

    private void mergeApiOperation(ApiOperationEntity apiOperationEntityExisted, ApiOperationEntity apiOperationEntity) {
        if (apiOperationEntityExisted.getConsumes() != null) {
            apiOperationEntity.setConsumes(apiOperationEntityExisted.getConsumes());
        }
        if (apiOperationEntityExisted.getHttpMethod() != null) {
            apiOperationEntity.setHttpMethod(apiOperationEntityExisted.getHttpMethod());
        }
        if (apiOperationEntityExisted.getNotes() != null) {
            apiOperationEntity.setNotes(apiOperationEntityExisted.getNotes());
        }
        if (apiOperationEntityExisted.getValue() != null) {
            apiOperationEntity.setValue(apiOperationEntityExisted.getValue());
        }
        if (apiOperationEntityExisted.getResponse() != null) {
            apiOperationEntity.setResponse(apiOperationEntityExisted.getResponse());
        }
        if (apiOperationEntityExisted.getTags() != null) {
            apiOperationEntity.setTags(apiOperationEntityExisted.getTags());
        }
    }

    // todo 处理泛型类型
    private ApiOperationEntity createApiOperation(PsiMethod psiMethod) {
        ApiOperationEntity apiOperationEntity = new ApiOperationEntity();
        String returnTypeName = psiMethod.getReturnType().getCanonicalText() + ".class";
        apiOperationEntity.setResponse(returnTypeName);
        PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();
        PsiAnnotation springMvcMappingAnnotation = chooseSpringMvcMappingAnnotation(annotations);
        String httpMethod = getHttpMethod(springMvcMappingAnnotation);
        apiOperationEntity.setHttpMethod(httpMethod);
        apiOperationEntity.setNotes("");
        apiOperationEntity.setValue("");
        return apiOperationEntity;
    }


    private PsiAnnotation chooseSpringMvcMappingAnnotation(PsiAnnotation[] psiAnnotations) {
        for (PsiAnnotation psiAnnotation : psiAnnotations) {
            if (DELETE_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName()) ||
                    GET_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName()) ||
                    PUT_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName()) ||
                    POST_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName()) ||
                    REQUEST_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName())) {
                return psiAnnotation;
            }
        }
        return null;
    }

    private String getHttpMethod(PsiAnnotation psiAnnotation) {
        if (DELETE_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName())) {
            return HttpMethods.DELETE;
        }
        if (GET_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName())) {
            return HttpMethods.GET;
        }
        if (PUT_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName())) {
            return HttpMethods.PUT;
        }
        if (POST_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName())) {
            return HttpMethods.POST;
        }
        if (REQUEST_MAPPING_ANNOTATION_NAME.equals(psiAnnotation.getQualifiedName())) {
            RequestMappingEntity requestMapping = (RequestMappingEntity) ParserHolder.getAnnotationProcessor(REQUEST_MAPPING_ANNOTATION_NAME).parse(psiAnnotation);
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
            return psiType.getPresentableText();
        }
    }

    // 需要重构
    private Map<String, ApiImplicitParamEntity> processMethodParam(Project project, PsiParameter[] psiParameters) {
        Map<String, ApiImplicitParamEntity> apiImplicitParamEntityMap = new LinkedHashMap<>();
        for (PsiParameter psiParameter : psiParameters) {
            PsiType psiType = psiParameter.getType();
            String dataTypeClass = PsiTypeUtil.getReturnType(psiType) + CLASS_SUFFIX;
            String dataType = getDataTypeByPsiType(psiType);
            String name = psiParameter.getName();
            if (psiType.getPresentableText().equals("HttpServletRequest")) {
                continue;
            }
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
                        RequestHeaderEntity requestHeader = (RequestHeaderEntity) ParserHolder.getAnnotationProcessor(REQUEST_HEADER_ANNOTATION_NAME).parse(psiAnnotation);
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
                        RequestParamEntity requestParam = (RequestParamEntity) ParserHolder.getAnnotationProcessor(REQUEST_PARAM_ANNOTATION_NAME).parse(psiAnnotation);
                        if (null != requestParam.getRequired()) {
                            required = requestParam.getRequired();
                        }
                        if (null != requestParam.getValue()) {
                            name = requestParam.getValue();
                        }
                        if (null != requestParam.getName()) {
                            name = requestParam.getName();
                        }
                        defaultValue = requestParam.getDefaultValue();
                        break;
                    case PATH_VARIABLE_ANNOTATION_NAME:
                        paramType = "path";
                        PathVariableEntity pathVariable = (PathVariableEntity) ParserHolder.getAnnotationProcessor(PATH_VARIABLE_ANNOTATION_NAME).parse(psiAnnotation);
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
                        RequestBodyEntity requestBody = (RequestBodyEntity) ParserHolder.getAnnotationProcessor(REQUEST_BODY_ANNOTATION_NAME).parse(psiAnnotation);
                        if (null != requestBody.getRequired()) {
                            required = requestBody.getRequired();
                        }
                        dataType = psiType.getPresentableText();
                        break;
                    default:
                        break;
                }
            }
            PsiClass paramClass = PsiTypeUtil.getPsiClass(project, psiType);
            if ("query".equals(paramType) && paramClass != null && paramClass.isEnum()) {
                dataType = "string";
            } else {
                if ("query".equals(paramType) && !BasicTypeUtil.isBasicType(psiType.getCanonicalText())) {
                    continue;
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
