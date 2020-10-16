package com.sharedaka.parser.annotation;

import com.sharedaka.parser.annotation.spring.*;
import com.sharedaka.parser.annotation.swagger.ApiImplicitParamParser;
import com.sharedaka.parser.annotation.swagger.ApiImplicitParamsParser;

import java.util.HashMap;

import static com.sharedaka.constant.spring.SpringMvcAnnotations.*;
import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME;
import static com.sharedaka.constant.swagger.SwaggerAnnotations.SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME;

/**
 * @author math312
 */
public class AnnotationParserHolder {

    private final static HashMap<String, AbstractAnnotationParser> annotationProcessors;

    static {
        annotationProcessors = new HashMap<>();
        annotationProcessors.put(REQUEST_HEADER_ANNOTATION_NAME, new RequestHeaderParser());
        annotationProcessors.put(REQUEST_PARAM_ANNOTATION_NAME, new RequestParamParser());
        annotationProcessors.put(REQUEST_BODY_ANNOTATION_NAME, new RequestBodyParser());
        annotationProcessors.put(PATH_VARIABLE_ANNOTATION_NAME, new PathVariableParser());
        annotationProcessors.put(REQUEST_MAPPING_ANNOTATION_NAME, new RequestMappingParser());
        annotationProcessors.put(SWAGGER_IMPLICIT_PARAM_ANNOTATION_NAME, new ApiImplicitParamParser());
        annotationProcessors.put(SWAGGER_IMPLICIT_PARAMS_ANNOTATION_NAME, new ApiImplicitParamsParser());
    }

    public static AbstractAnnotationParser getAnnotationProcessor(String key) {
        return annotationProcessors.get(key);
    }

}
