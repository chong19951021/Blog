# Blog
# 技术说明

  ## 一、技术栈

### 前端

- Js框架 ：JQuery
- CSS框架：SemanticUi
- 目录插件：Tocbot.js
- 首页看板娘 ：Live2d.js
- 首页走马灯：Swiper.js
- 分类页雷达图：ECharts.js
- 标签页标签云：Jqcloud.js
- 时间轴：TimeLine.js

### 后端

- SpringBoot ： 2.3.4
- Jdk环境：1.8
- 构建工具：Maven 4
- 持久层框架：mybatis
- 前端解析模板：Thymeleaf
- 项目部署环境：阿里云Centos7
- 消息队列：Rabbitmq
- 缓存：Redis

## 二、功能需求

本次开发的个人博客系统是基于一对多的模式，即一个管理员用户(博主)对应多个用户(访客)，所以我们需要展现给访客的是我们的前台模块，即首页，分类页，时间轴，留言板，标签，关于我，订阅页面，在本次系统设计中没有设计用户注册功能，只保留了一个管理员来拥有绝对权限。下面将从用户和博主两个角度来阐述功能需求。

- 访客

  - 首页能查看首页推荐文章，通过点击首页分类轮播图，进入分类页面，查看与之对应的文章信息，通过首页导航栏进入各类页面
  - 博客详情页查看博客的具体信息，包括作者姓名，发表日期，博客对应的分类，浏览次数，浏览对应的评论，发表评论并回复对应的评论。评论要求访客留下姓名，邮箱，并能在评论界面，鼠标悬浮在用户姓名上显示用户邮箱
  - 分类页能展示具体分类，并能通过ajax技术请求后端实现不同分类对应文章列表。
  - 标签页能展示具体标签，并能通过ajax技术请求后端实现不同标签对应文章列表
  - 时间轴能按照发表日期展示文章列表
  - 留言板能展示访客的留言，功能与文章评论功能类似。能用消息队列技术实现留言实时通知博主，在这里用Rabbitmq技术实现
  - 订阅页能提供给访客订阅博主的功能，运用Rabbitmq来实现博主发布博客通过邮件实时通知订阅者功能
- 博主

  - 拥有和所有访客同样的功能实现
  - 登录：通过导航栏的登录图标账号密码登录后台
  - 文章管理：实现博文的增删改查，对已发布博客的编辑、查询，对新博客的发布，对草稿的保存，以及对已发布和草稿状态的博文的删除
  - 分类管理：实现分类的增删改查，以及分类对应的图片，以便于在首页展示
  - 标签管理：实现标签的增删改查
  - 订阅管理：实现对订阅者的管理，包括其订阅时间，留下的邮箱，电话，姓名等个人信息
- 注

  - 为性能优化考虑，对首页列表文章进行redis缓存，并设置过期时间，实现每五小时动态刷新
  - 为具体文章实现redis缓存，用redis的Hash将Blog的实体以键值对的形式存储在redis缓存中，并在修改博客内容的同时，实现部分推送到redis进行修改，减少性能损耗

## 三、数据库设计

### 1、数据表展示

- 博客表 ： t_blog
- 分类表 ： t_type
- 评论表 ： t_comment
- 留言表 ：t_message
- 订阅表 ： t_subscribe
- 标签表 ： t_tag
- 用户表 ： t_user
- 标签，博客中间表 ： t_bt

### 2、表间关系


<img src="https://s1.ax1x.com/2020/10/31/BNxpwt.png" style="width:600;height:400"/>

- 博客与用户是多对一的关系，一个用户可以有多个博客，一个博客只能对应一个用户
- 博客和分类是多对一的关系：一个博客对应一个分类，一个分类可以对应多个博客
- 博客和评论是一对多的关系：一个博客可以对应多个评论，一个评论对应一个博客
- 评论和子评论是一对多的关系：一个评论可以对应多个子评论，一个子评论对应一个父评论
- 留言和子留言是一对多的关系：一个留言可以对应多个子留言，一个子留言对应一个父留言

###  3、表字段

博客字段：

<img src="https://s1.ax1x.com/2020/10/31/BNvBss.png" style="width:600px;height:400px">

评论字段：

<img src="https://s1.ax1x.com/2020/10/31/BNv0Mj.png" style="width:600px;height:400px">

留言字段：

<img src="https://s1.ax1x.com/2020/10/31/BNvdzQ.png" style="width:600px;height:400px">

订阅字段：

<img src="https://s1.ax1x.com/2020/10/31/BNvUJS.png" style="width:600px;height:400px">

标签字段：

<img src="https://s1.ax1x.com/2020/10/31/BNvDLn.png" style="width:600px;height:400px">

分类字段：

<img src="https://s1.ax1x.com/2020/10/31/BNvsZq.png" style="width:600px;height:400px">

用户字段：

<img src="https://s1.ax1x.com/2020/10/31/BNv6oV.png" style="width:600px;height:400px">

博客-标签中间表字段：

<img src="https://s1.ax1x.com/2020/10/31/BNztgg.png" style="width:600px;height:400px">

### 4、表结构

博客表：

<img src="https://s1.ax1x.com/2020/10/31/BNvRWF.png" style="width:600px;height:400px">

<img src="https://s1.ax1x.com/2020/10/31/BNv2JU.png" style="width:600px;height:400px">


评论表：

<img src="https://s1.ax1x.com/2020/10/31/BNvWz4.png" style="width:600px;height:400px">

<img src="https://s1.ax1x.com/2020/10/31/BNvhQJ.png" style="width:600px;height:400px">

留言表：

<img src="https://s1.ax1x.com/2020/10/31/BNv4y9.png" style="width:600px;height:400px">

订阅表：

<img src="https://s1.ax1x.com/2020/10/31/BNv5LR.png" style="width:600px;height:400px">

标签表：

<img src="https://s1.ax1x.com/2020/10/31/BNvoe1.png" style="width:600px;height:400px">

分类表：

<img src="https://s1.ax1x.com/2020/10/31/BNvTdx.png" style="width:600px;height:400px">

用户表：

<img src="https://s1.ax1x.com/2020/10/31/BNv7o6.png" style="width:600px;height:400px">

博客-标签中间表：

<img src="https://s1.ax1x.com/2020/10/31/BNvbFK.png" style="width:600px;height:400px">

<img src="https://s1.ax1x.com/2020/10/31/BNvqJO.png" style="width:600px;height:400px">

## 四、基本配置

### 1、yml配置



<img src="https://s1.ax1x.com/2020/10/31/BavYi6.png" style="width:600px;height:400px">


<img src="https://s1.ax1x.com/2020/10/31/BavQsJ.png" style="width:600px;height:400px">


### 2、pom依赖

<img src="https://s1.ax1x.com/2020/10/31/BavGIx.png" style="width:600px;height:400px">

<img src="https://s1.ax1x.com/2020/10/31/Bav8d1.png" style="width:600px;height:400px">

<img src="https://s1.ax1x.com/2020/10/31/Bav3ZR.png" style="width:600px;height:400px">

<img src="https://s1.ax1x.com/2020/10/31/BavlL9.png" style="width:600px;height:400px">






da



