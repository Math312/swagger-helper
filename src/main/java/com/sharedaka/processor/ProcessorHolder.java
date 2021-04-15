package com.sharedaka.processor;

import com.sharedaka.processor.business.SwaggerApiControllerProcessor;
import com.sharedaka.processor.business.SwaggerApiMethodProcessor;
import com.sharedaka.processor.business.SwaggerApiModelProcessor;

/**
 * Swagger Helper现提供三个功能，其中两个与类相关，一个与方法相关，三个功能对应三个处理器
 * 1. Class相关：
 *     - 实体类生成@ApiModel相关注解   - SwaggerApiModelProcessor
 *     - Controller类生成@Api相关注解 - SwaggerApiControllerProcessor
 * 2. Method相关：
 *     - 生成对应Controller注释       - SwaggerApiMethodProcessor
 * 该类负责对处理器进行管理
 *
 * */
public class ProcessorHolder {

    public static SwaggerApiControllerProcessor swaggerApiControllerProcessor;

    public static final String swaggerApiControllerProcessorLock = "";


    public static SwaggerApiMethodProcessor swaggerApiMethodProcessor;

    public static final String swaggerApiMethodProcessorLock = "";

    private static SwaggerApiModelProcessor swaggerApiModelProcessor;

    public static final String swaggerApiModelProcessorLock = "";

    public static SwaggerApiControllerProcessor getSwaggerApiControllerProcessor() {
        if (swaggerApiControllerProcessor != null) {
            return swaggerApiControllerProcessor;
        } else {
            synchronized (swaggerApiControllerProcessorLock) {
                if (swaggerApiControllerProcessor == null) {
                    swaggerApiControllerProcessor = new SwaggerApiControllerProcessor();
                }
            }
            return swaggerApiControllerProcessor;
        }
    }

    public static SwaggerApiMethodProcessor getSwaggerApiMethodProcessor() {
        if (swaggerApiMethodProcessor != null) {
            return swaggerApiMethodProcessor;
        } else {
            synchronized (swaggerApiMethodProcessorLock) {
                if (swaggerApiMethodProcessor == null) {
                    swaggerApiMethodProcessor = new SwaggerApiMethodProcessor();
                }
            }
            return swaggerApiMethodProcessor;
        }
    }

    public static SwaggerApiModelProcessor getSwaggerApiModelProcessor() {
        if (swaggerApiModelProcessor != null) {
            return swaggerApiModelProcessor;
        } else {
            synchronized (swaggerApiModelProcessorLock) {
                if (swaggerApiModelProcessor == null) {
                    swaggerApiModelProcessor = new SwaggerApiModelProcessor();
                }
            }
            return swaggerApiModelProcessor;
        }
    }
}
