package com.sharedaka.entity;

import java.util.Map;

public class ErrorCodes {

    private String className;

    private String qualifiedName;

    private Map<String, ErrorCodeEntity> errorCodes;


    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, ErrorCodeEntity> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Map<String, ErrorCodeEntity> errorCodes) {
        this.errorCodes = errorCodes;
    }

    public ErrorCodeEntity getErrorCodeEntityByName(String name) {
        if (errorCodes == null) {
            throw new IllegalArgumentException();
        }
        String errorCodeName = name;
        if (name.startsWith(className) || name.startsWith(qualifiedName)) {
            errorCodeName = name.substring(name.lastIndexOf(".") + 1);
        }
        return errorCodes.get(errorCodeName);
    }

}
