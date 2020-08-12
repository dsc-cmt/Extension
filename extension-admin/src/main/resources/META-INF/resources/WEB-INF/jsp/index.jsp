<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">   <%-- 在IE运行最新的渲染模式 --%>
    <meta name="viewport" content="width=device-width, initial-scale=1">   <%-- 初始化移动浏览显示 --%>
    <!-- 引入各种CSS样式表 -->
    <link rel="stylesheet" href="/css/bootstrap.css">

    <script type="text/javascript" src="/js/jquery-3.5.1.min.js"></script>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>

    <title>spi-admin</title>
</head>

<body>
<!-- 顶部菜单-->
<div style="height: 50px">
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/">SPI-ADMIN</a>
            </div>
            <div>
                <ul class="nav navbar-nav">
                    <li class="active"><a href="appList">应用管理</a></li>
                </ul>
            </div>
        </div>
    </nav>
</div>

    <!-- 右侧内容展示==================================================   -->
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h1 class="page-header">控制台</h1>
        <div id="content">
            <p>extension1.0 现已发布至maven公库</p>
            <a target="_blank" rel="noopener noreferrer" href="https://github.com/dsc-cmt/Extension">
                欢迎来提PR和issue，更多功能了解请前往GitHub
            </a>
        </div>
    </div>
</div>
</body>
</html>