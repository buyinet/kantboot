# kantboot基础架构
### 安装
> 
> git clone https://github.com/buyinet/kantboot.git
## 1、kantboot-application（配置文件模块）

> 此模块为配置文件框架，所有子模块都会使用此处的配置文件。 
>
> 是否为启动模块：否 
>
> 是否为pom模块：否
 
## 2、kantboot-util（工具类模块）
> 此模块为工具类模块
> 
> 是否为启动模块：否
> 
> 是否为pom模块：是
> 
> #### 子模块：
> 
> ##### (1) kantboot-util-common
> 
> ##### (2) kantboot-util-core
> 

### 2.1、kantboot-util-common（通用工具类）
> 此模块为通用工具类，引入此工具类的项目，无需配置数据库。
> 
> 是否为启动模块：否
>
> 是否为pom模块：是
> #### 依赖
>  ```xml
>  <dependency>
>      <groupId>com.kantboot</groupId>
>      <artifactId>kantboot-util-common</artifactId>
>      <version>${kantboot.version}</version>
>  </dependency>
>  ```
> 
> #### 例如
> 
> RestResult 此类是用来规范RestfulApi，让控制器返回
> > #### 引入:
> > ```java
> > import RestResult;
> > ```   
> > 
> > #### 操作案例:
> >
> > ``` java
> > public RestResult<?> opt(){
> >     Map<String,Object> map=new HashMap<>();
> >     map.put("intoData",new Date());
> >     map.put("opt":"笑笑");
> >     return RestResult.success(map,"操作成功");
> > }
> 详细阅读此模块，查看
> <a src="kantboot-util/kantboot-util-common">
>   kantboot-util/kantboot-util-common
> </a>
> 下的README.md


## 3、kantboot-system （系统模块）

> 此模块为系统模块。
>
> 是否为启动模块：否
>
> 是否为pom模块: 是
>
> > 子模块：
> >
> > kantboot-system-module (包含实体和业务逻辑，但不运行)
> >
> > kantboot-system-web-starter（调用kantboot-system-module来运行）
