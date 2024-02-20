<#import "../../common/home.ftlh" as h>
<#import "../../common/table.ftlh" as t>

<@h.home "Список админов">

    <!--
  <center class="mb-4">
    <a class="btn btn-sm btn-info" href="admins/add">Добавить админа</a>
  </center>-->

  <@t.table "admins" />

    <table id="admins" class="table table-striped table-bordered" cellspacing="0" width="100%">
    <thead>
      <tr>
        <th>ChatId</th>
        <th>Имя</th>
        <th>Действие</th>
      </tr>
    </thead>
    <tbody>
      <#list admins as admin>
        <tr>
          <td>${admin.chatId?c}</td>
          <td>${admin.username}</td>
          <td>
            <form action="admins/delete?chatId=${admin.chatId?c}" method="POST">
                <button type="submit" class="btn btn-xs btn-danger">
                    <i class="fa fa-close" aria-hidden="true"></i>
                </button>
            </form>
          </td>
        </tr>
      </#list>
    </tbody>
  </table>

  <script>
    window.Telegram.WebApp.MainButton.setText('Добавить админа').show().enable()
    window.Telegram.WebApp.MainButton.onClick(() => {window.location.href = "/admin/admins/add";});
  </script>

</@h.home>
