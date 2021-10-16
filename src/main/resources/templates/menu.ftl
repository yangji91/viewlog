<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>查看日志</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap.min.css">
</head>
<body>
<h3><a href="/">查看日志</a></h3>
<div>
    <table class="table" style="">
        <thead>
        <tr>
            <td>分组</td>
            <td>项目名称</td>
            <td>测试环境</td>
            <td>生产环境</td>
        </tr>
        <#if logs??>
            <#list logs as l>
                <tr>
                    <td>${l.groupName!}</td>
                    <td>
                        ${l.name}
                    </td>
                    <td>
                        <#list l.nodeList as node>
                            <#if node.env!='prod'>
                                <a target="" href="${node.viewFileInfoUrl}">${node.ip}</a> |
                            </#if>
                        </#list>
                    </td>
                    <td>
                        <#list l.nodeList as node>
                            <#if node.env='prod'>
                                <a target="" href="${node.viewFileInfoUrl}">${node.ip}</a> |
                            </#if>
                        </#list>
                    </td>
                </tr>
            </#list>
        </#if>
        </thead>
    </table>
</div>
</body>
</html>