<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>查看日志</title>
    <script src="/static/jquery-3.4.1.min.js"></script>
</head>
<body>
<div>
    <input type="hidden" id="hidWsUrl" value="${wsUrl}">
</div>
<div id="log-container" style="height: 6000px; overflow-y: scroll; background: #333; color: #aaa; padding: 0px;">
    <div>
    </div>
</div>
</body>
<script>
    $(document).ready(function () {
        function log(data) {
            // 接收服务端的实时日志并添加到HTML页面中
            $("#log-container div").append(data);
            // 滚动条滚动到最低部
            $("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
        }

        // 指定websocket路径
        var websocket = new WebSocket($("#hidWsUrl").val());
        websocket.onmessage = function (event) {
            log(event.data);
        };
        websocket.onclose = function (ev) {
            log("连接已经关闭")
        }
    });
</script>
</body>
</html>