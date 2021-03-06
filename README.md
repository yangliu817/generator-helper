# generator-helper

#### generator-helper视图项目
     https://gitee.com/yangliu817/generator-helper-vue

#### 1.4.1
    1.细节优化
    2.jpa添加默认service实现
    3.restful默认选择
    4.取消shiro注解默认选择
    5.添加版权功能
    6.添加软件信息
    
#### 介绍
    该程序是一款支持mybatis,mybatis-plus以及jpa的代码生成工具，
    同时也支持lombok swagger shiro注解
    可生成controller成的基本增删改查方法，实体类自动生成toString equals和hashCode方法
    doMain_X64.exe4j为exe4j生成exe文件的代码
    项目依赖于
        ojdbc14-10.2.0.3.0.jar 
        springboot-javafx-support-1.5.0.jar  https://gitee.com/yangliu817/springboot-javafx
        comm-3.0.jar git地址: https://git.oschina.net/yangliu817/comm
#### 软件架构
    软件架构说明
    1 采用springboot管理容器中的bean
    2 使用javafx进行视图渲染
    3 使用html编辑视图页面
    4 使用mybatis-plus作为orm
    5 使用sqlite保存用户自定义配置
    6 使用exe4j生成启动文件

#### 安装教程

    1. 将libs目录下的jar包分别安装到本地maven仓库中
        示例命令 
        mvn install:install-file -Dfile=D:/taobao-sdk-java-auto-20160607.jar -DgroupId=com.ganshane.specs -DartifactId=taobao-sdk-java-auto-20160607 -Dversion=1.0.0 -Dpackaging=jar
        安装后 用libs目录下的pom文件替换本地maven仓库里面对应jar包的pom文件
    2. 将代码download下来后添加到ide中，配置为springboot项目
    3. 运行GeneratorHelperApplication的main方法

#### release使用说明
    请将对应的jre目录添加到软件根目录
    第一次启动程序可能很慢,会进行一些初始化工作
    目录结构如下
    -jre               ----- java程序运行环境
    -libs	       ------java程序包
    -view	       ------html视图文件
    -templates	       ------代码生成模板
    generator 86.exe   ------32位环境启动文件exe
    generator 64.exe   ------64位环境启动文件exe
    -log               ------日志目录,运行后生成
    -config            ------配置文件目录,运行后生成
    -db 	       ------本地数据库目录

    必要的文件或文件夹
    -jre
    -libs
    -view
    -templates
    generator 86.exe或者generator 64.exe
    其他文件或文件夹在运行后自动创建

### 软件截图

![avatar](imgs/1.png)

![avatar](imgs/2.png)

![avatar](imgs/3.png)

![avatar](imgs/4.png)

#### 参与贡献

    问道于盲 yangliu817@126.com
