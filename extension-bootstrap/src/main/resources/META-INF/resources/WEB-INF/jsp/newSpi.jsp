<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script type="text/javascript" src="/js/jquery-3.5.1.min.js"></script>
    <link rel="stylesheet" href="/css/bootstrap.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <style>
        .form-group.required .control-label:after {
            content:"*";
            color:red;
        }
    </style>
</head>
<body>
<form id="spiForm" class="form-horizontal" role="form" style="margin-top: 20px" action="/api/configs" method="post">
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
    </div>
    <div class="form-group required">
        <label for="bizCode" class="col-sm-2 control-label">bizCode</label>
        <div class="col-sm-6">
            <input type="text" class="form-control" id="bizCode" name="bizCode" placeholder="请输入bizCode" required>
        </div>
    </div>
    <div class="form-group">
        <label for="invokeMethod" class="col-sm-2 control-label">调用方式</label>
        <div class="col-sm-6">
            <select id="invokeMethod" name="invokeMethod" class="form-control" required>
                <option>local</option>
                <option>dubbo</option>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label for="expireTime" class="col-sm-2 control-label">超时时间</label>
        <div class="col-sm-6">
            <input type="text" class="form-control" id="expireTime" name="expireTime" placeholder="默认为10s">
        </div>
    </div>
    <div class="form-group">
        <label for="remark" class="col-sm-2 control-label">备注</label>
        <div class="col-sm-6">
            <input type="text" class="form-control" id="remark" name="remark">
        </div>
    </div>
    <div class="form-group">
        <label for="isDefault" class="col-sm-2 control-label">是否默认</label>
        <div class="col-sm-6">
            <select id="isDefault" name="isDefault" class="form-control">
                <option value="0">否</option>
                <option value="1">是</option>
            </select>
        </div>
    </div>
        <div class="col-sm-offset-2 col-sm-4">
            <button type="submit" class="btn btn-primary">提交</button>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button type="submit" class="btn btn-primary" onclick="reset()">重置</button>
        </div>
</form>
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

    function reset(){
       $('spiForm').getAttribute(0).reset()
    }
</script>
</body>
</html>