<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script type="text/javascript" src="/js/jquery-3.5.1.min.js"></script>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/js/bootstrap-treeview.min.js"></script>

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
<div id="body">
    <div>
        <div style="padding: 50px 200px 10px 200px;">
            <form class="bs-example bs-example-form" role="form">
                <div class="row">
                    <div class="col-lg-6">
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="请输入应用名称" id="searchApp">
                            <span class="input-group-btn">
						<button class="btn btn-default" type="button" onclick="getApp()">
							Go!
						</button>
					</span>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div id="tree"></div>
    </div>
    <!--app Modal -->
    <div id="appModal" class="modal fade" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">新增应用</h4>
                </div>
                <div class="modal-body">
                    应用名称: <input id="appName" type="text" name="appName" class="form-control">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="submitNewApp()">提交</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>

        </div>
    </div>
    <!--spi modal-->
    <div id="spiModal" class="modal fade" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">新增spi接口</h4>
                </div>
                <div class="modal-body">
                    spi接口: <input id="spiInterface" type="text" name="spiInterface" class="form-control"
                                  placeholder="请输入接口全限定名">
                    <input name="spi-appName" id="spi-appName" hidden>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="submitNewSpi()">提交</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>

        </div>
    </div>
    <!--config modal-->
    <div id="configModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">新增extension</h4>
                </div>
                <div class="modal-body">
                    <div>
                        <form id="spiForm" class="form-horizontal" role="form" style="margin-top: 20px"
                              action="/api/configs" method="post">
                            <div class="form-group required">
                                <label for="appName" class="col-sm-2 control-label">应用名称</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" id="c-appName" name="appName" required
                                           readonly value='<%=(String)request.getAttribute("appName") %>'>
                                </div>
                            </div>
                            <div class="form-group required">
                                <label for="spiInterface" class="col-sm-2 control-label">spiInterface</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" id="c-spiInterface" name="spiInterface"
                                           readonly required>
                                </div>
                            </div>
                            <div class="form-group required">
                                <label for="bizCode" class="col-sm-2 control-label">bizCode</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" id="bizCode" name="bizCode"
                                           placeholder="请输入bizCode" required>
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
                                    <input type="text" class="form-control" id="expireTime" name="expireTime"
                                           placeholder="默认为10s">
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
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">提交</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <div id="configEditModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">编辑extension</h4>
                </div>
                <div class="modal-body">
                    <div>
                        <form id="spiEditForm" class="form-horizontal" role="form" style="margin-top: 20px"
                              action="/api/updateConfigs" method="post">
                            <div class="form-group required">
                                <label for="appName" class="col-sm-2 control-label">应用名称</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" id="s-appName" name="appName" required
                                           readonly>
                                </div>
                            </div>
                            <div class="form-group required">
                                <label for="spiInterface" class="col-sm-2 control-label">spiInterface</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" id="s-spiInterface" name="spiInterface"
                                           readonly required>
                                </div>
                            </div>
                            <div class="form-group required">
                                <label for="bizCode" class="col-sm-2 control-label">bizCode</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" id="s-bizCode" name="bizCode"
                                           placeholder="请输入bizCode" required readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="invokeMethod" class="col-sm-2 control-label">调用方式</label>
                                <div class="col-sm-6">
                                    <select id="s-invokeMethod" name="invokeMethod" class="form-control" required>
                                        <option>local</option>
                                        <option>dubbo</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="expireTime" class="col-sm-2 control-label">超时时间</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" id="s-expireTime" name="expireTime"
                                           placeholder="默认为10s">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="remark" class="col-sm-2 control-label">备注</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" id="s-remark" name="remark">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="isDefault" class="col-sm-2 control-label">是否默认</label>
                                <div class="col-sm-6">
                                    <select id="s-isDefault" name="isDefault" class="form-control">
                                        <option value="0">否</option>
                                        <option value="1">是</option>
                                    </select>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">提交</button>
                                <button type="button" class="btn btn-danger" data-dismiss="modal"
                                        onclick="show_confirm()">删除
                                </button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <!--spi Delete Modal -->
    <div id="spiDeleteModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">删除spi</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="d-appName" class="col-sm-2 control-label">应用名</label>
                        <div class="col-sm-6">
                            <input id="d-appName" type="text" name="appName" class="form-control" readonly>
                        </div>
                    </div>
                    <br/>
                    <div class="form-group">
                        <label for="d-spiInterface" class="col-sm-2 control-label">接口名</label>
                        <div class="col-sm-6">
                            <input id="d-spiInterface" type="text" name="spiInterface" class="form-control" readonly>
                        </div>
                    </div>
                    <br/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="deleteSpiConfirm()">删除
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>

        </div>
    </div>
</div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        getApps();
    });

    (function () {
        'use strict';
        window.addEventListener('load', function () {
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
            var forms = document.getElementsByClassName('form-horizontal');
            // Loop over them and prevent submission
            var validation = Array.prototype.filter.call(forms, function (form) {
                form.addEventListener('submit', function (event) {
                    if (form.checkValidity() === false) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        }, false);
    })();

    function refresh(e) {
        var form = new FormData(e.target);
        var appName = form.get("appName")
        $.ajax({
            type: 'post',
            url: '/api/configs',
            data: {
                appName: appName
            },
            success: function (res) {
                if (res.success) {
                    debugger
                } else {
                    alert(res.msg)
                    debugger
                }
            }
        })
    }

    function getTree(apps) {
        var tree = []
        for (var i = 0; i < apps.length; i++) {
            var app = apps[i];
            var appNode = {
                text: app.appName,
                nodes: [], dataType: "app", backColor: "#DCDCDC"
            };
            if (app.spis.length > 0) {
                for (var j = 0; j < app.spis.length; j++) {
                    var spi = app.spis[j];
                    var spiNode = {text: spi.spiInterface, nodes: [], dataType: "spi", backColor: "#E1FFFF"}
                    if (spi.extensions.length > 0) {
                        for (var k = 0; k < spi.extensions.length; k++) {
                            spiNode.nodes.push({
                                text: spi.extensions[k].bizCode,
                                dataType: "extension",
                                backColor: "#ADD8E6"
                            })
                        }
                    }
                    spiNode.nodes.push({text: "新增extension", dataType: "newExtension", backColor: "#ADD8E6"})
                    appNode.nodes.push(spiNode)
                }
            }
            appNode.nodes.push({text: "新增spi接口", dataType: "newSpi", backColor: "#E1FFFF"})
            tree.push(appNode)
        }
        tree.push({text: '新增应用', dataType: "newApp", backColor: "#DCDCDC"})
        return tree;
    }

    function getApps() {
        $.get('/api/applications', {}, function (res) {
            if (res.success) {
                renderData(res.data)
            } else if (!res.msg) {
                var newWin = window.open('', '_blank');
                newWin.document.write(res);
            }
        })
    }

    var showEditModal = function (node) {
        var spiNode = $('#tree').treeview('getParent', node);
        var appNode = $('#tree').treeview('getParent', spiNode);
        $.ajax({
            type: 'get',
            url: '/api/config',
            data: {
                appName: appNode.text,
                spiInterface: spiNode.text,
                bizCode: node.text
            },
            success: function (res) {
                if (res.success) {
                    $(".modal-body #s-appName").val(appNode.text);
                    $(".modal-body #s-spiInterface").val(spiNode.text);
                    $(".modal-body #s-bizCode").val(node.text);
                    $(".modal-body #s-invokeMethod").val(res.data.invokeMethod);
                    $(".modal-body #s-expireTime").val(res.data.expireTime);
                    $(".modal-body #s-remark").val(res.data.remark);
                    $(".modal-body #s-isDefault").val(res.data.isDefault);

                    $('#configEditModal').modal('show');
                } else {
                    alert(res.msg)
                    window.location.href = 'appList'
                }
            }
        })
    };

    function spiDeleteModal(node) {
        var appNode = $('#tree').treeview('getParent', node);

        $(".modal-body #d-spiInterface").val(node.text);
        $(".modal-body #d-appName").val(appNode.text);
        $('#spiDeleteModal').modal('show');
    }

    function renderData(apps) {
        $('#tree').treeview({data: getTree(apps)});
        $('#tree').on('nodeSelected', function (e, node) {
            if (node.dataType === "newApp") {
                $('#appModal').modal('show');
            }
            if (node.dataType === "newSpi") {
                var appNode = $('#tree').treeview('getParent', node);
                var appName = appNode.text;
                $(".modal-body #spi-appName").val(appName);
                $('#spiModal').modal('show');
            }
            if (node.dataType === "newExtension") {
                var spiNode = $('#tree').treeview('getParent', node);
                var appNode = $('#tree').treeview('getParent', spiNode);
                $(".modal-body #c-appName").val(appNode.text);
                $(".modal-body #c-spiInterface").val(spiNode.text);
                $('#configModal').modal('show');
            }
            if (node.dataType === "extension") {
                showEditModal(node);
            }
            if (node.dataType === "spi") {
                spiDeleteModal(node)
            }
        })
    }

    function submitNewApp() {
        var appName = document.getElementById('appName').value
        $.ajax({
            type: 'post',
            url: '/api/applications',
            data: 'appName=' + appName,
            success: function (res) {
                if (res.success) {
                    window.location.href = 'appList'
                } else {
                    alert(res.msg)
                    window.location.href = 'appList'
                }
            }
        })
    }

    function submitNewSpi() {
        var appName = document.getElementById('spi-appName').value
        var spiInterface = document.getElementById('spiInterface').value
        $.ajax({
            type: 'post',
            url: '/api/spis',
            data: {appName: appName, spiInterface: spiInterface},
            success: function (res) {
                if (res.success) {
                    window.location.href = 'appList'
                } else {
                    alert(res.msg)
                    window.location.href = 'appList'
                }
            }
        })
    }

    function getApp() {
        var appName = document.getElementById('searchApp').value

        $.get('/api/app', {appName: appName}, function (res) {
            if (res.success) {
                renderData([res.data])
            } else {
                alert(res.msg)
                window.location.href = 'appList'
            }
        })
    }

    function deleteSpiConfirm() {
        var spiInterface = $('#d-spiInterface').val()
        var appName = $('#d-appName').val()
        var r = confirm("删除spi将同时删除其下extension配置,确认删除?");
        if (r === true) {
            $.get('/api/deleteSpi', {
                appName: appName,
                spiInterface: spiInterface,
            }, function (res) {
                if (res.success) {
                    window.location.href = 'appList'
                } else {
                    alert(res.msg)
                    window.location.href = 'appList'
                }
            })
        }
    }

    function show_confirm() {
        var appName = $('#s-appName').val()
        var spiInterface = $('#s-spiInterface').val()
        var bizCode = $('#s-bizCode').val()
        var r = confirm("确认删除?");
        if (r === true) {
            $.get('/api/deleteConfig', {
                appName: appName,
                spiInterface: spiInterface,
                bizCode: bizCode
            }, function (res) {
                if (res.success) {
                    window.location.href = 'appList'
                } else {
                    alert(res.msg)
                    window.location.href = 'appList'
                }
            })
        }
    }
</script>
</body>
</html>