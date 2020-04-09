<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>查看日志</title>
    <script src="/jquery-3.4.1.min.js"></script>
</head>
<body>
<div>
    <input type="hidden" id="hidWsUrl" value="${wsUrl}">
    <input type="hidden" id="hidWsPort" value="${wsPort}">
</div>
<#--<div id="log-container" style="height: 6000px; overflow-y: scroll; background: #333; color: #aaa; padding: 0px;">-->
<#--    <div>-->
<#--    </div>-->
<#--</div>-->
<pre id="log-container" style="word-wrap: break-word; white-space: pre-wrap;">
<div>

</div>
</pre>
</body>
<script>
    $(document).ready(function () {
        function log(data) {
            // 接收服务端的实时日志并添加到HTML页面中
            $("#log-container div").append(data);
            // 滚动条滚动到最低部
            //$("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
        }


        function isValidIP(ip) {
            var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
            return reg.test(ip);
        }


        // 指定websocket路径
        var url = $("#hidWsUrl").val();
        var p = window.location.protocol === "http:" ? "ws://" : "wss://";

        var host = window.location.hostname;
        if (isValidIP(host)) {
            url = p + host + ":" + $("#hidWsPort").val() + url;
        } else {
            url = p + host + url;
        }
        var websocket = new WebSocket(url);
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