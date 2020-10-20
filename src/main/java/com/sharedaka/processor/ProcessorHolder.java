package com.sharedaka.processor;

import com.sharedaka.processor.business.SwaggerApiControllerProcessor;
import com.sharedaka.processor.business.SwaggerApiMethodProcessor;
import com.sharedaka.processor.business.SwaggerApiModelProcessor;

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
