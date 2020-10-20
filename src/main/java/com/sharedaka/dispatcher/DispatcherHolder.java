package com.sharedaka.dispatcher;

public class DispatcherHolder {

    private static SwaggerActionDispatcher swaggerActionDispatcher;

    private static String swaggerActionDispatcherLock = "";

    public static SwaggerActionDispatcher getSwaggerApiControllerProcessor() {
        if (swaggerActionDispatcher != null) {
            return swaggerActionDispatcher;
        } else {
            synchronized (swaggerActionDispatcherLock) {
                if (swaggerActionDispatcher == null) {
                    swaggerActionDispatcher = new SwaggerActionDispatcher();
                }
            }
            return swaggerActionDispatcher;
        }
    }
}
