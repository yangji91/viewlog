<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>查看日志</title>
    <link rel="stylesheet" type="text/css" href="/static/bootstrap.min.css">
</head>
<body>
<h3>查看日志</h3>
<div>
    <table class="table" style="width: 800px">
        <thead>
        <tr>
            <td>项目名称</td>
            <td>操作1</td>
            <td>操作2</td>
            <td>操作3</td>
            <td>日志文件最后修改时间</td>
        </tr>
        <#list logs as l>
            <tr>
                <td>${l.name}</td>
                <td>
                    <#if l.viewFileInfoUrl??>
                        <a target="_blank" href="${l.viewFileInfoUrl}">查看全部日志</a>
                    </#if>
                </td>
                <td>
                    <#if l.realTimeLogUrl??>
                        <a target="_blank" href="${l.realTimeLogUrl}">查看实时日志</a>
                    </#if>
                </td>
                <td>
                    <#if l.latestNumLogUrl??>
                        <a target="_blank" href="${l.latestNumLogUrl}">查看最近200条日志</a>
                    </#if>
                </td>
                <td>${l.latestModifyTime!}</td>
            </tr>
        </#list>
        </thead>
    </table>
</div>
</body>
</html>