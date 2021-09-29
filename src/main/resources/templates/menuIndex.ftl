<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>查看日志</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap.min.css">
</head>
<body>
<h3><a href="/viewlog/menu">查看日志</a></h3>
<div>
    <table class="table table-bordered" style="">
        <thead>
        <tr>
            <#if menus??>
                <#list menus?keys as key>
                    <td> ${key}</td>
                </#list>
            </#if>
        </tr>
        </thead>
        <tbody>
        <#if menus??>
            <#list 0..(maxLenght?number) as i>
                <tr>
                    <#list menus?keys as key>
                        <#if (i<menus[key]?size)>
                            <td><a href="${menus[key][i].getUrl()}" target="_blank">${menus[key][i].getName()}</a></td>
                        <#else>
                            <td></td>
                        </#if>
                    </#list>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>
</body>
</html>