# READEME

## 使用方法
1. 安装插件
2. 选中类名或方法名 -> generate code(Command + N or alt + insert) -> generate swagger annotation 

目前已知最低兼容版本为2019.1

## 注解支持

1. ApiImplicitParam:
    - value: 默认为空字符串
    - name： 默认为变量名
    - required： 默认为true
    - paramType:
        - body
            - RequestBody: 
                1. 基础类型：不生成注解
                2. 复合类型：生成注解，dataType：类名，dataTypeClass：全限定类名.class
        - query
            - RequestParam:
                1. 基础类型：生成注解，dataType：类名，dataTypeClass：全限定类名.class
                2. 复合类型：不生成注解
        - path
            - PathVariable:
                1. 基础类型：生成注解，dataType：类名，dataTypeClass：全限定类名.class
                2. 复合类型：不生成注解
        - header
            - RequestHeader:
                1. 基础类型：生成注解，dataType：类名，dataTypeClass：全限定类名.class
                2. 复合类型：不生成注解
                
2. ApiOperation:
    - httpMethod: 基于RequestMapping\GetMapping\DeleteMapping\PostMapping\PutMapping生成
    - value：暂时为空字符串
    - notes：暂时为空字符串
    
3. Api:
    - value: 空字符串
    - description：空字符串

4. ApiModel
    - value: 类名
    - description： 读注解
5. ApiModelProperty:
    - value: 读取注解
    - required：true
6. ApiResponse
    
    Swagger-Helper支持根据异常以及状态码，生成ApiResponse注解。使用该功能需要满足如下条件：
    1. 状态码需使用异常抛出的方式处理。例如使用Spring MVC 的统一异常处理，抛出异常，使用Spring MVC支持将异常转化为code、message，例如：
        ```java
       BusinessException
        ```
       使用ExceptionHandler拦截BusinessException进行处理。
    2. code、message需通过枚举标注。例如：
        ```java
        public enum ErrorCode {
            SUCCESSFUL(0,"Successfully");
            int code;
            
            int message;
            
            public ErrorCode(int code, int message) {
                this.code  = code;
                this.message = message;
            }
        }      
        ```
       抛出异常时需将对应的ErrorCode作为构造参数传入，例如：
       ```java
        throw new BusinessException(ErrorCode.SUCCESSFUL);
        ```
       此时如果Swagger-Helper检测到上面的异常抛出语句，会生成@ApiResponse(code=0,message="Successfully")注解。
       
        对于抛出的Exception，可在Preferences->Swagger-Helper配置项中进行配置。
        
        注意，目前该功能还不支持Spring的Bean注入。该功能会在0.0.5版本支持。
        
        欢迎使用者提出Issue，完善该工具。