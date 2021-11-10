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
    停顿总时间：${gc.totalRealTime}
</div>
<div>
    <table class="table" style="width: 1500px">
        <thead>
        <tr>
            <td>序号</td>
            <td>时间</td>
            <td>运行时间(秒)</td>
            <td>垃圾回收器</td>
            <td>回收原因</td>
            <td>停顿时间</td>
        </tr>
        <#list gc.gcRecordList! as g>
            <tr>
                <td>${g.id}</td>
                <td>${g.dateTime}</td>
                <td>${g.runTime}</td>
                <td></td>
                <td>${g.gcReason}</td>
                <td>${g.usedTime1!}</td>
            </tr>
        </#list>
        </thead>
    </table>
</div>
</body>
</html>

