# mybatis-helper

#### 介绍
    该程序是一款加入lombok支持的mybatis-plus代码生成工具，
    可生成controller成的基本增删改查方法，实体类自动生成toString equals和hashCode方法
    doMain_X64.exe4j为exe4j生成exe文件的代码
    项目依赖于
        javafx-springboot项目（由我改造支持webview）请移步https://gitee.com/yangliu817/springboot-javafx.git
        comm项目 请移步https://git.oschina.net/yangliu817/comm.git
#### 软件架构
    软件架构说明
    1 采用springboot管理容器中的bean
    2 使用javafx进行视图渲染
    3 使用html编辑视图页面
    4 使用mybatis-plus作为orm
    5 使用sqlite保存用户自定义配置
    6 使用exe4j生成启动文件

#### 安装教程

    1. 将代码download下来后添加到ide中，配置为springboot项目
    2. 运行MybatisPlusHelperApplication的main方法

#### release使用说明
    请将对应的jre目录添加到软件根目录
    第一次启动程序可能很慢,会进行一些初始化工作
    目录结构如下
    -jre               ----- java程序运行环境
    -libs		       ------java程序包
    -view		       ------html视图文件
    -templates	       ------代码生成模板
    generator 86.exe   ------32位环境启动文件exe
    generator 64.exe   ------64位环境启动文件exe
    -log               ------日志目录,运行后生成
    -config            ------配置文件目录,运行后生成
    -db 		       ------本地数据库目录

    必要的文件或文件夹
    -jre
    -libs
    -view
    -templates
    generator 86.exe或者generator 64.exe
    其他文件或文件夹在运行后自动创建

#### 参与贡献

    问道于盲 yangliu817@126.com
