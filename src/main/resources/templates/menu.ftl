<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>查看日志</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap.min.css">
</head>
<body>
<h3><a href="/">查看日志</a></h3>
<h5>${logLink}</h5>
<div>
    <table class="table" style="width: 800px">
        <thead>
        <tr>
            <td>项目名称</td>
            <td>修改时间</td>
            <td>总大小</td>
            <td>文件个数</td>
        </tr>
        <#if logs??>
            <#list logs as l>
                <tr>
                    <td><a target="" href="${l.viewFileInfoUrl}">
                            <img src="${l.fileIcon!}" width="25px"/>
                            ${l.name}
                        </a></td>
                    <td>${l.latestModifyTime!}</td>
                    <td>${l.fileTotalLength}</td>
                    <td>${l.fileNum}</td>
                </tr>
            </#list>
        </#if>
        </thead>
    </table>
</div>
</body>
</html>