<#-- @ftlvariable name="user_id" type="ru.itmo.web.hw4.model.Post" -->
<#import "./common.ftlh" as c>

<@c.page_post>
    <#assign p = 0/>
    <#if post??>
        <#list posts?reverse as p>
            <#if post?? && post.id == p.id>
                <#assign p = post/>
            </#if>
        </#list>

        <@c.post_creater true p/>
    <#else>
        <#assign count = 0/>
        <#list posts?reverse as p>
            <#if user?? && user.id == p.user_id>
                <@c.post_creater false p/>
                <#assign count += 1/>
            </#if>
        </#list>
        <#if count == 0>
            no such posts
        </#if>
    </#if>
</@c.page_post>