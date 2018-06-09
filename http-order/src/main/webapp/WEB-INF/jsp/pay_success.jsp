<%@ page contentType="text/html; charset=UTF8"%>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>支付结果</title>
</head>
<body>
<div>
    <p>支付状态:<b>${payResult} </b> </p>

    <p>剩余可用账户余额: ${capitalAmount}元</p>
    <p>剩余可用红包余额: ${redPacketAmount}元</p>
</div>
</body>
</html>