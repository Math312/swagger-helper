# READEME

## 使用方法
1. 安装插件
2. 选中类名或方法名 -> generate code -> generate swagger annotation 

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