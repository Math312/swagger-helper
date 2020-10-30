package com.sharedaka.entity.annotation.swagger;

import java.util.List;

public class ApiImplicitParamsEntity {

    public List<ApiImplicitParamEntity> getValue() {
        return value;
    }

    public void setValue(List<ApiImplicitParamEntity> value) {
        this.value = value;
    }

    private List<ApiImplicitParamEntity> value;

}
