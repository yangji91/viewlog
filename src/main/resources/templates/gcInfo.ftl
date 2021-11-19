<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${name!}>${ip!}>gc</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap.min.css">
    <script src="/jquery-3.4.1.min.js"></script>
</head>
<style>
    .young {
        background-color: #F0F8FF;
    }

    .old {
        background-color: #FAEBD7;
    }

    .heap {
        background-color: #F0FFFF;
    }
</style>
<body>
<div>
    <a href="/" style="font-size: xx-large">查看日志</a> >${name!}>${ip!}
</div>
<div>
    <h4>统计最新1000次gc日志</h4>
    <table class="table" style="width:1200px">
        <tbody>
        <tr>
            <td>统计时间范围：</td>
            <td>${gc.beginRunTime}--${gc.endRunTime}</td>
            <td>停顿总时间：</td>
            <td>${gc.totalStopWorldTime}</td>
            <td>吞吐率：</td>
            <td>${gc.throughput!}</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>年轻代回收次数：</td>
            <td>${gc.youngGcCount}</td>
            <td>年轻代多久回收一次：</td>
            <td>${gc.youngGcFrequency}</td>
            <td>年轻代停顿总时间：</td>
            <td>${gc.youngGcTotalStopWorldTime}</td>
            <td>年轻代最长一次停顿时间：</td>
            <td>${gc.youngGcMaxStopWorldTime}</td>
        </tr>
        <tr>
            <td>老年代回收次数：</td>
            <td>${gc.oldGcCount}</td>
            <td>老年代多久回收一次：</td>
            <td>${gc.oldGcFrequency!}</td>
            <td>老年代停顿总时间：</td>
            <td>${gc.oldGcTotalStopWorldTime}</td>
            <td>老年代最长一次停顿时间：</td>
            <td>${gc.oldGcMaxStopWorldTime}</td>
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
            <td class="young">年轻代已用内存</td>
            <td class="young">年轻代gc后已用内存</td>
            <td class="young">年轻代总内存</td>
            <td class="old">老年代已用内存</td>
            <td class="old">老年代gc后已用内存</td>
            <td class="old">老年代总内存</td>
            <td>STW停顿时间</td>
            <td class="heap">堆已用内存</td>
            <td class="heap">堆回收后占用内存</td>
            <td class="heap">堆总内存</td>
            <td>执行耗时</td>
        </tr>
        <#list gc.gcRecordList! as g>
            <tr>
                <td>${g.id}</td>
                <td>${g.dateTime}</td>
                <td>${g.gcRunTime}</td>
                <td>${g.gcType!}</td>
                <td>${g.gcReason!}</td>
                <td>${g.intervalTime!}</td>
                <td class="young">${g.youngUsedSize!} (${g.youngUsedSizeRate!})</td>
                <td class="young">${g.youngAfterGcUsed!} (${g.youngAfterGcUsedRate!})</td>
                <td class="young">${g.youngTotalSize!}</td>
                <td class="old">${g.oldUsedSize!} (${g.oldUsedSizeRate!})</td>
                <td class="old">${g.oldAfterGcUsed!} (${g.oldAfterGcUsedRate!})</td>
                <td class="old">${g.oldTotalSize!}</td>
                <td>${g.stopWorldTime!}</td>
                <td class="heap">${g.heapUsedSize!}</td>
                <td class="heap">${g.heapAfterGcUsed!}</td>
                <td class="heap">${g.heapTotalSize!}</td>
                <td>${g.gcRunTime!}</td>
            </tr>
        </#list>
        </thead>
    </table>
</div>
</body>
</html>

