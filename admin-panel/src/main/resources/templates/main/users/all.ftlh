<#import "../../common/home.ftlh" as h>
<#import "../../common/table.ftlh" as t>

<@h.home "Список клиентов">

  <@t.table "users" />
  <table id="users" class="table table-striped table-bordered" cellspacing="0" width="100%">
    <thead>
      <tr>
        <th>ChatId</th>
        <th>Имя</th>
        <th>Активный</th>
        <th>Действие</th>
      </tr>
    </thead>
    <tbody>
      <#list users as user>
        <tr>
          <td>${user.chatId?c}</td>
          <td>${user.username!}</td>
          <td><#if user.active>Да<#else>Нет</#if></td>
          <td>
            <a class="btn btn-xs btn-info" href="users/edit/${user.chatId?c}">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </a>
          </td>
        </tr>
      </#list>
    </tbody>
  </table>

</@h.home>
