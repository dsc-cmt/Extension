<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script type="text/javascript" src="/js/jquery-3.5.1.min.js"></script>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="/css/bootstrap.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<div>
    <div style="margin-left: 20px;margin-top: 20px">
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">
            新增应用
        </button>
    </div>
    <!-- Modal -->
    <div id="myModal" class="modal fade" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">新增应用</h4>
                </div>
                <div class="modal-body">
                    应用名称: <input id="appName" type="text" name="appName">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="submit()">提交</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>

        </div>
    </div>
    <table style="margin-left: 20px;margin-top: 20px;border-spacing:0px 10px;border-collapse:separate;">
        <tbody id="tb_data"></tbody>
    </table>
</div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        getApps();
    });

    function getApps() {
        $.get('/api/applications', {}, function (res) {
            if (res.success) {
                renderData(res.data)
            }
        })
    }

    function renderData(apps) {
        if (apps.length <= 0) {
            return
        }

        var html = "<tr>";
        for (var i = 0; i < apps.length; i++) {
            html += "<td width='200px'><div style='border: double;height: 150px;width: 150px;text-align: center;vertical-align:middle; display: table-cell;'>" +
                "<a href='app?appName=" +apps[i]+"'> " + apps[i]+ "</a></div></td>"
            if ((i+1) % 5 === 0) {
                html += "</tr><tr>"
            }
        }
        html += "</tr>"
        $("#tb_data").html(html)
    }

    function submit() {
        var appName =document.getElementById('appName').value
        $.ajax({
            type:'post',
            url:'/api/applications',
            data:'appName='+appName,
            success:function (res) {
                if(res.success){
                    window.location.href='appList'
                }
                else{
                    alert(res.msg)
                    window.location.href='appList'
                }
            }
        })
    }
</script>
</body>
</html>