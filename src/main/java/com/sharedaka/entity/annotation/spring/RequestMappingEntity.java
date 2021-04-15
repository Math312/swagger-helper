package com.sharedaka.entity.annotation.spring;

public class RequestMappingEntity {

    private String name = "";

    private String[] value = new String[0];

    private String[] path = new String[0];

    /**
     * RequestMethod.GET,RequestMethod.DELETE,RequestMethod.POST,RequestMethod.PUT
     * */
    private String[] method = new String[0];

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public String[] getMethod() {
        return method;
    }

    public void setMethod(String[] method) {
        this.method = method;
    }
}
