<#import "../../common/home.ftlh" as h>

<@h.home "Добавление админа">
    <form method="post" action="admins/create">
        <div class="form-group row">
            <label class="col-sm-2">Ник в Telegram:</label>
            <div class="col-sm-10">
                <input class="form-control" name="username" value="<#if admin??>${admin.username!}</#if>">
                <#if usernameError??><div class="text-warning">${usernameError}</div></#if>
            </div>
        </div>

        <#--<input type="hidden" name="_csrf" value="${_csrf.token}">-->
        <button class="btn btn-success" type="submit">Добавить</button>
    </form>

    <form method="get" action="admins">
        <button class="btn btn-info" type="submit">Назад</button>
    </form>

</@h.home>
