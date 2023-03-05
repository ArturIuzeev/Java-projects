<#-- @ftlvariable name="id" type="java.lang.Integer" -->
<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<@c.page_user>
    <#assign u = 0/>
    <#if user??>
        <#list users as q>
            <#if user?? && user.id == q.id>
                <#assign u = user/>
            </#if>
        </#list>
    <#else>
        <#assign u = c.findBy(users, "handle", handle!)!/>
    </#if>



    <div class="content">
        <#if u?has_content>
            <h1><@c.userlink u/></h1>

            <li class="id_user">id: ${u.id}</li>
            <li>handle: ${u.handle}</li>
            <li class="name_user">name: ${u.name}</li>

            <li class="count_posts">Written posts: <a href="/post?user_id=${u.id}"> ${c.CountItem(posts,u.id)}</a></li>

            <img src="/img/icon.jpeg" alt="icon_user"/>
            <div class="arrow">
                <#list users as user>
                    <#if u.id == user.id && user_index != 0>
                        <a class="arrow_links" href="/user?handle=${users[user_index - 1].handle}">←</a>
                    </#if>
                    <#if u.id == user.id && user_index == 0>
                        <a class="default_arrow">←</a>
                    </#if>
                    <#if u.id == user.id && user_has_next>
                        <a class="arrow_links" href="/user?handle=${users[user_index + 1].handle}">→</a>
                    </#if>
                    <#if u.id == user.id && !user_has_next && user_index != 0>
                        <a class="default_arrow">→</a>
                    </#if>
                </#list>
            </div>
        <#else>
            <h1>No such user</h1>
        </#if>
    </div>



</@c.page_user>
