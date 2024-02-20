<#if SPRING_SECURITY_CONTEXT??>
  <#assign
    <#--isLoggedIn = true-->
    currentUser = SPRING_SECURITY_CONTEXT.authentication.principal
>
<#--
<#else>
<#assign
isLoggedIn = false
>-->
</#if>
