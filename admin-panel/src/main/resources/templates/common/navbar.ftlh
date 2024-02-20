<#include "security.ftlh">

<nav class="navbar navbar-default">
  <div class="container-fluid">

    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed"
              data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
        <span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">Админ-панель</a>
    </div>

    <div id="navbar" class="navbar-collapse collapse">
      <ul class="nav navbar-nav">
        <li><a href="users"><i class="fa fa-users mr-1"></i>Пользователи</a></li>
        <li><a href="broadcasts/add"><i class="fa fa-comment mr-1"></i>Громкоговоритель</a></li>
        <li><a href="messages"><i class="fa fa-envelope mr-1"></i>Текста</a></li>
        <li><a href="admins"><i class="fa fa-users mr-1"></i>Админы</a></li>
      </ul>

      <div class="navbar-text pull-right">
        <i>@<#if currentUser??>${currentUser.getUsername()}</#if></i>
      </div>
    </div>

  </div>
</nav>
