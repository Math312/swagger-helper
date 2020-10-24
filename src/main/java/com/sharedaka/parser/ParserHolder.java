package com.sharedaka.parser;

import com.sharedaka.parser.annotation.AbstractAnnotationParser;
import com.sharedaka.parser.annotation.spring.*;
import com.sharedaka.parser.annotation.swagger.*;
import com.sharedaka.parser.enums.ErrorCodeParser;

import java.util.HashMap;

import static com.sharedaka.constant.spring.SpringMvcAnnotations.*;
import static com.sharedaka.constant.swagger.SwaggerAnnotations.*;

/**
 * @author math312
 */
public class ParserHolder {

    private final static HashMap<String, AbstractAnnotationParser> annotationProcessors;

    private final static ErrorCodeParser errorCodeParser;

    static {
        annotationProcessors = new HashMap<>();
        annotationProcessors.put(REQUEST_HEADER_ANNOTATION_NAME, new RequestHeaderParser());
        annotationProcessors.put(REQUEST_PARAM_ANNOTATION_NAME, new RequestParamParser());
        annotationProcessors.put(REQUEST_BODY_ANNOTATION_NAME, new RequestBodyParser());
        annotationProcessors.put(PATH_VARIABLE_ANNOTATION_NAME, new PathVariableParser());
        annotationProcessors.put(REQUEST_MAPPING_ANNOTATION_NAME, new RequestMappingParser());
        annotationProcessors.put(SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME, new ApiImplicitParamParser());
        annotationProcessors.put(SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME, new ApiImplicitParamsParser());
        annotationProcessors.put(SWAGGER_API_OPERATION_ANNOTATION_NAME, new ApiOperationParser());
        annotationProcessors.put(SWAGGER_API_ANNOTATION_NAME, new ApiParser());
        annotationProcessors.put(SWAGGER_API_MODEL_PROPERTY, new ApiModelPropertyParser());
        annotationProcessors.put(SWAGGER_API_MODEL, new ApiModelParser());

        errorCodeParser = new ErrorCodeParser();
    }

    public static AbstractAnnotationParser getAnnotationProcessor(String key) {
        return annotationProcessors.get(key);
    }

    public static ErrorCodeParser getErrorCodeParser() {
        return errorCodeParser;
    }

}
