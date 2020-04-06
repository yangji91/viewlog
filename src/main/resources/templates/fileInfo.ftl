<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>查看日志</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap.min.css">
</head>
<body>
<div>
    <h3><a href="/">Home</a>
        <#if path??>
            <#list path as item>
                > <a href="${item.url}">${item.name}</a>
            </#list>
        </#if>
    </h3>
</div>
<div>
    <table class="table" style="width: 1200px">
        <thead>
        <tr>
            <td>文件名称</td>
            <td>大小</td>
            <td>修改时间</td>
            <td>操作1</td>
            <td>操作2</td>
            <td>操作3</td>
            <td>操作4</td>
        </tr>
        <#list fs as f>
            <tr>
                <td>
                    <#if f.directory>
                        <a href="${f.dirUrl}"> <img src="${f.fileIcon!}" width="25px"/>${f.name}</a>
                    </#if>
                    <#if !f.directory>
                        <img src="${f.fileIcon!}" width="25px"/>${f.name}
                    </#if>
                </td>
                <td>${f.size}</td>
                <td>${f.modifyTime}</td>
                <td>
                    <#if f.realTimeLogUrl??>
                        <a target="_blank" href="${f.realTimeLogUrl}">查看实时日志</a>
                    </#if>
                </td>
                <td>
                    <#if f.latestNumLogUrl??>
                        <a target="_blank" href="${f.latestNumLogUrl}">查看最近200条日志</a>
                    </#if>
                </td>
                <td><a target="_blank" href="${f.openUrl}">查看全部</a></td>
                <td><a target="_blank" href="${f.downloadUrl}">下载</a></td>
            </tr>
        </#list>
        </thead>
    </table>
</div>
</body>
</html>