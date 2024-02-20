<html lang="en">
<head>
    <script src="https://telegram.org/js/telegram-web-app.js"></script>
    <base target="_self">
    <title>Login</title>
    <#--<meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>-->
</head>
<body>
<div><p id="error"></p></div>
<script>
    window.Telegram.WebApp.ready();
    window.Telegram.WebApp.expand();

    let initData = window.Telegram.WebApp.initData;
    let initDataUnsafe = window.Telegram.WebApp.initDataUnsafe;
    let data = JSON.stringify(window.Telegram.WebApp)

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/admin/home", true);
    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    xhr.send(data);

    xhr.onloadend = function () {
        if (xhr.status === 200) {
            window.location.href = "/admin/home";
        } else {
            document.getElementById("error").innerHTML = "HTTP " + xhr.status + ": " + xhr.response
        }
    };
</script>
</body>
</html>
