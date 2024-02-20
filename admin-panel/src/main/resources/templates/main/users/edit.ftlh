<#import "../../common/home.ftlh" as h>

<@h.home "Редактирование клиента">

  <#if user??>
    <form method="post" action="users/update">
      <div class="form-group row">
        <label class="col-sm-2">Id:</label>
        <div class="col-sm-10">
          <input class="form-control" type="number" name="id" value="${user.id?c}" readonly>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2">Chat id:</label>
        <div class="col-sm-10">
          <input class="form-control" type="number" name="chatId" value="${user.chatId?c}" readonly>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2">Имя:</label>
        <div class="col-sm-10">
          <input class="form-control" name="name" value="${user.name!}">
          <#if nameError??><div class="text-warning">${nameError}</div></#if>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2">Номер телефона:</label>
        <div class="col-sm-10">
          <input class="form-control" name="phoneNumber" value="${user.phoneNumber!}">
          <#if phoneNumberError??><div class="text-warning">${phoneNumberError}</div></#if>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2">Город:</label>
        <div class="col-sm-10">
          <input class="form-control" name="city" value="${user.city!}">
          <#if cityError??><div class="text-warning">${cityError}</div></#if>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2">Адрес:</label>
        <div class="col-sm-10">
          <input class="form-control" name="address" value="${user.address!}">
          <#if addressError??><div class="text-warning">${addressError}</div></#if>
        </div>
      </div>

      <div class="form-group row">
        <label class="col-sm-2">Активный:</label>
        <div class="col-sm-10">
          <select class="form-control" name="active">
            <option value="true" <#if user.active>selected</#if>>Да</option>
            <option value="false" <#if !user.active>selected</#if>>Нет</option>
          </select>
        </div>
      </div>

      <#--<input type="hidden" name="_csrf" value="${_csrf.token}">-->
      <button class="btn btn-success" type="submit">Изменить</button>
    </form>

    <form method="get" action="orders">
      <button class="btn btn-info" type="submit">Назад</button>
    </form>

  <#else>
    <div class="alert alert-danger" role="alert">
      <h4 class="alert-heading">Error</h4>
      <hr>Такого клиента не существует!
    </div>
  </#if>

</@h.home>
