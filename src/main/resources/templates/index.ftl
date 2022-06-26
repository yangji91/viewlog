<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>日志搜索</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap.min.css">
</head>
<style>
    .divProject a {
        float: left;
        width: 125px;
        height: 30px;
        border: 1px solid #000
    }

    .product {
        background-color: #FFEBCD;
    }
</style>
<body>
<a href="/" style="font-size: xx-large">日志搜索</a>
<div class="divProject">
    <table class="table" style="">
        <thead>
        <tr>
            <td>分组</td>
            <td>项目名称</td>
        </tr>
        <#if groupList??>
            <#list groupList as g>
                <tr>
                    <td>${g.groupName!}</td>
                    <td>
                        <#list g.projectList as p>
                            ${p.name}
                        </#list>
                    </td>
                </tr>
            </#list>
        </#if>
        </thead>
    </table>
</div>
</body>
</html>