<#--
<#import "../../common/home.ftlh" as h>

<@h.home "Редактирование админа">

  <#if Error??>
    <div class="alert alert-warning" role="alert">
      <h4 class="alert-heading">Ошибка</h4>
      <hr>${Error}
    </div>
  </#if>

  <#if admin??>
    <form method="post" action="admins/update">
      <div class="form-group row">
        <label class="col-sm-2">Id:</label>
        <div class="col-sm-10">
          <input class="form-control" type="number" name="id" value="${admin.id?c}" readonly>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2">Никнейм:</label>
        <div class="col-sm-10">
          <input class="form-control" name="username" value="${admin.username}">
          <#if usernameError??><div class="text-warning">${usernameError}</div></#if>
        </div>
      </div>

      <button class="btn btn-success" type="submit">Изменить</button>
    </form>

    <form method="get" action="admins">
      <button class="btn btn-info" type="submit">Назад</button>
    </form>

    <form method="post" action="admins/delete">
      <input type="hidden" name="id" value="${admin.id?c}">
      <button class="btn btn-danger" type="submit">Удалить</button>
    </form>

  <#else>
    <div class="alert alert-danger" role="alert">
      <h4 class="alert-heading">Ошибка</h4>
      <hr>Такого админа не существует!
    </div>
  </#if>

</@h.home>-->
