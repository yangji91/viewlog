<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>查看日志</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap.min.css">
    <script src="/jquery-3.4.1.min.js"></script>
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
    <table class="table" style="width: 1500px">
        <thead>
        <tr>
            <td>文件名称</td>
            <td>大小</td>
            <td>修改时间</td>
            <td>查看1</td>
            <td>查看2</td>
            <td>查看3</td>
            <td>查看4</td>
            <td>查看5</td>
        </tr>
        <#list fs as f>
            <tr>
                <td>
                    <#if f.directory>
                        <a href="${f.dirUrl}"> <img src="${f.fileIcon!}" width="25px"/>${f.name}</a>
                    <#else>
                        <img src="${f.fileIcon!}" width="25px"/>${f.name}
                    </#if>
                </td>
                <td>${f.size}</td>
                <td>${f.modifyTime}</td>
                <td>
                    <#if f.realTimeLogUrl?? && !f.directory && f.logFile>
                        <a target="_blank" href="${f.realTimeLogUrl}">查看实时日志</a>
                    </#if>
                </td>
                <td>
                    <#if !f.directory && f.logFile>
                        <input type="text" value="" size="10">
                        <a atype="searchLog1" href="javascript:" url="${f.searchLogUrl}">搜索|前后行数</a>
                        <input type="text" value="3" size="1px">
                    </#if>
                </td>
                <td>
                    <#if !f.directory && f.logFile>
                        <a atype="latestNum1" href="javascript:" url="${f.latestNumLogUrl}">查看最后n行日志</a>
                        <input type="text" value="200" size="1">
                    </#if>
                </td>
                <td>
                    <#if !f.directory && f.logFile>
                        <a target="_blank" href="${f.openUrl}">查看全部</a>
                    </#if>
                </td>
                <td>
                    <#if !f.directory>
                        <a target="_blank" href="${f.downloadUrl}">下载</a>
                    </#if>
                </td>
            </tr>
        </#list>
        </thead>
    </table>
</div>
</body>
</html>

<script>
    $(document).ready(function () {
        function openLog(url, key, length) {
            window.open(url + "&key=" + key + "&length=" + length, "_blank");
        }

        $("[atype='latestNum1']").click(function () {
            var obj = $(this);
            openLog(obj.attr("url"), "", obj.next().val());
        });

        $("[atype='searchLog1']").click(function () {
            var obj = $(this);
            openLog(obj.attr("url"), obj.prev().val(), obj.next().val());
        });
    });
</script>
