<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>查看日志</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap.min.css">
</head>
<style>
    .divProject a {
        float: left;
        width: 125px;
        height: 30px;
        border: 1px solid #000
    }
</style>
<body>
<a href="/" style="font-size: xx-large">查看日志</a>
<div class="divProject">
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
                        <#assign testNode=1>
                        <#list l.nodeList as node>
                            <#if node.env!='prod'>
                                <a target="" href="${node.viewFileInfoUrl}">${node.ip}</a>&#160;&#160;
                                <#assign testNode=testNode+1>
                            </#if>
                        </#list>
                    </td>
                    <td>
                        <#assign prodNode=1>
                        <#list l.nodeList as node>
                            <#if node.env='prod'>
                                <a target="" href="${node.viewFileInfoUrl}">${node.ip}</a>&#160;&#160;
                                <#assign prodNode=prodNode+1>
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