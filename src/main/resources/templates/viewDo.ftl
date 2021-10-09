<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>查看日志</title>
    <style>
        .key {
            color: red;
            font-weight: bold;
        }
    </style>
    <script src="/jquery-3.4.1.min.js"></script>
</head>
<body>
<div>
    <input type="hidden" id="hidWsUrl" value="${wsUrl!}">
    <input type="hidden" id="hidWsPort" value="${wsPort!}">
    <input type="hidden" id="hidKey" value="${key!}">
</div>
<pre id="log-container" style="word-wrap: break-word; white-space: pre-wrap;">
<div>

</div>
</pre>
</body>
<script src="/jquery-3.4.1.min.js"></script>
<script>
    $(document).ready(function () {
        function log(data) {
            var key = $("#hidKey").val();
            if (key != null && key !== "") {
                if (data.indexOf(key) !== -1) {
                    data = data.replace(key, "<span class='key'>" + key + "</span>");
                }
            }
            // data = data.replace("<", "&lt;");
            // data = data.replace(">", "&gt;");
            $("#log-container div").append(data);
        }


        function isValidIP(ip) {
            var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
            return reg.test(ip);
        }


        // 指定websocket路径
        var url = $("#hidWsUrl").val();
        var p = window.location.protocol === "http:" ? "ws://" : "wss://";

        var host = window.location.hostname;
        url = p + host + ":" + $("#hidWsPort").val() + url;
        // if (isValidIP(host)) {
        //     url = p + host + ":" + $("#hidWsPort").val() + url;
        // } else {
        //     url = p + host + url;
        // }
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