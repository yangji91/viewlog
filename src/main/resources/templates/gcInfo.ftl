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
    停顿总时间：${gc.totalStopWorldTime}
    <table class="table" style="width:1000px">
        <tbody>
        <tr>
            <td>统计时间范围：</td>
            <td>${gc.beginRunTime}--${gc.endRunTime}</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>年轻代回收次数：</td>
            <td>${gc.youngGcCount}</td>
            <td>年轻代多久回收一次：</td>
            <td>${gc.youngGcFrequency}</td>
            <td>年轻代停顿总时间：</td>
            <td>${gc.youngGcStopWorldTime}</td>
        </tr>
        <tr>
            <td>老年代回收次数：</td>
            <td>${gc.oldGcCount}</td>
            <td>老年代多久回收一次：</td>
            <td>${gc.oldGcFrequency}</td>
            <td>老年代停顿总时间：</td>
            <td>${gc.oldGcStopWorldTime}</td>
        </tr>
        </tbody>
    </table>
</div>
<div>
    <table class="table">
        <thead>
        <tr>
            <td>序号</td>
            <td>回收时间</td>
            <td>已运行时间(秒)</td>
            <td>垃圾回收器</td>
            <td>回收原因</td>
            <td>距上次回收时间</td>
            <td>年轻代已用内存</td>
            <td>年轻代回收后占用内存</td>
            <td>年轻代总内存</td>
            <td>停顿时间1</td>
            <td>堆已用内存</td>
            <td>堆回收后占用内存</td>
            <td>堆总内存</td>
            <td>停顿时间2</td>
        </tr>
        <#list gc.gcRecordList! as g>
            <tr>
                <td>${g.id}</td>
                <td>${g.dateTime}</td>
                <td>${g.runTime}</td>
                <td>${g.gcType!}</td>
                <td>${g.gcReason!}</td>
                <td>${g.intervalTime!}</td>
                <td>${g.youngUsedSize!} (${g.youngUsedSizeRate!})</td>
                <td>${g.youngAfterGcUsed!} (${g.youngAfterGcUsedRate!})</td>
                <td>${g.youngTotalSize!}</td>
                <td>${g.usedTime1!}</td>
                <td>${g.heapUsedSize!}</td>
                <td>${g.heapAfterGcUsed!}</td>
                <td>${g.heapTotalSize!}</td>
                <td>${g.usedTime2!}</td>
            </tr>
        </#list>
        </thead>
    </table>
</div>
</body>
</html>

