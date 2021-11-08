<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${name!}>${ip!}</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap.min.css">
    <script src="/jquery-3.4.1.min.js"></script>
</head>
<body>
<div>
    <a href="/" style="font-size: xx-large">查看日志</a> >${name!}>${ip!}
</div>
<div>
    <table class="table" style="width: 1500px">
        <thead>
        <tr>
            <td>时间</td>
            <td>运行时间</td>
            <td>名称</td>
            <td>垃圾回收器</td>
            <td>停顿时间</td>
        </tr>
        <#list gc.gcRecordList! as g>
            <tr>
                <td>${g.dateTime}</td>
                <td>${g.runTime}</td>
                <td>${g.name}</td>
                <td>${g.gcType}</td>
                <td>${g.realTime!}</td>
            </tr>
        </#list>
        </thead>
    </table>
</div>
</body>
</html>

