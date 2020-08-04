<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script type="text/javascript" src="/js/jquery-3.5.1.min.js"></script>
    <link rel="stylesheet" href="/css/bootstrap.css">
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style>
        .form-group.required .control-label:after {
            content:"*";
            color:red;
        }
    </style>
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
<table class="table table-striped">
    <caption><b> 应用名称:<%=request.getAttribute("appName") %></b></caption>
    <thead>
    <td class="col-sm-6"> spi接口全限定名</td><td class="col-sm-6">操作</td>>
    </thead>
    <tbody id="tb_data"></tbody>

</table>
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">
    新增Spi接口
</button>
<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">新增spi接口</h4>
            </div>
            <div class="modal-body">
                <form id="spiForm" class="form-horizontal" role="form" style="margin-top: 20px" action="/api/spis" method="post">
                    <div class="form-group required">
                        <label for="appName" class="col-sm-2 control-label">appName</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="appName" name="appName" required readonly value='<%=(String)request.getAttribute("appName") %>'>
                        </div>
                    </div>
                    <div class="form-group required">
                        <label for="spiInterface" class="col-sm-2 control-label">spiInterface</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="spiInterface" name="spiInterface" placeholder="请输入spi接口全限定名" required>
                        </div>
                        <div class="col-sm-offset-2 col-sm-4" style="margin-top: 5px">
                            <button type="submit" class="btn btn-primary">提交</button>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <button type="submit" class="btn btn-primary" data-dismiss="modal">关闭</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

    </div>
</div>
<script type="application/javascript">
    (function() {
        'use strict';
        window.addEventListener('load', function() {
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
            var forms = document.getElementsByClassName('form-horizontal');
            // Loop over them and prevent submission
            var validation = Array.prototype.filter.call(forms, function(form) {
                form.addEventListener('submit', function(event) {
                    if (form.checkValidity() === false) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        }, false);
    })();

    $(document).ready(function () {
        getSpis();
    });

    function getSpis() {
        $.get('/api/spis', {appName:'<%=(String)request.getAttribute("appName") %>'}, function (res) {
            if (res.success) {
                renderData(res.data)
            }
        })
    }

    function renderData(spis) {
        if (spis.length <= 0) {
            return
        }
        var appName='<%=(String)request.getAttribute("appName") %>';
        var html = "";
        for (var i = 0; i < spis.length; i++) {
            html+="<tr><td><a href='app?appName=" +spis[i]+"'> " + spis[i].spiInterface+ "</a></td><td><a>详情</a>&nbsp;<a>编辑</a></td></tr>"
        }
        $("#tb_data").html(html)
    }
</script>
</body>
</html>