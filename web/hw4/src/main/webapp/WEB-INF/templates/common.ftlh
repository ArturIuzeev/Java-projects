<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-logo-with-telegram.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/HomePage", "name": "Home"},
            {"href": "/index", "name": "Users"},
            {"href": "/misc/help", "name": "Help"},
            {"href": "/contests", "name": "Contests"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <#if item.href == cur_menu>
                            <a class="curmenu" href="${item.href}">${item.name}</a>
                        <#else>
                            <a href="${item.href}">${item.name}</a>
                        </#if>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <section>
            <#list posts as p>
                <div class="header">Post #${p.id}</div>
                <div class="body">
                    ${substring(p.text)}...
                </div>
                <div class="footer">
                    <a class="links_view" href="/post?post_id=${p.id}">View all</a>
                </div>
            </#list>
        </section>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2022 &copy; by Artur Yuzeev
    </footer>
</#macro>

<#macro userlink user>
<#-- @ftlvariable name="user" type="ru.itmo.web.hw4.model.User" -->
    <a href="/user?handle=${user.handle}">${user.name}</a>
</#macro>

<#macro userId user>
<#-- @ftlvariable name="user" type="ru.itmo.web.hw4.model.User" -->
    ${user.id}
</#macro>

<#macro postTitle post>
<#-- @ftlvariable name="post" type="ru.itmo.web.hw4.model.Post" -->
    ${post.title}
</#macro>


<#macro page_user>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/userStyle.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header_user/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro page_index>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/indexstyle.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header_user>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-logo-with-telegram.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">

            <a href="/enter">Enter</a>
            |
            <a href="/register">Register</a>

        </div>
        <nav>
            <#assign items=[
            {"href": "/HomePage", "name": "Home"},
            {"href": "/index", "name": "Users"},
            {"href": "/misc/help", "name": "Help"},
            {"href": "/contests", "name": "Contests"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <#if item.href == cur_menu>
                            <a class="curmenu" href="${item.href}">${item.name}</a>
                        <#else>
                            <a href="${item.href}">${item.name}</a>
                        </#if>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro page_post>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/poststyle.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function substring text>
<#-- @ftlvariable name="text" type="String" -->
    <#if text?length gt 350>
        <#return text?substring(0, 350)/>
    </#if>
    <#return text/>
</#function>

<#function CountItem items id_u>
    <#assign count = 0/>
    <#list items as item>
        <#if item.user_id == id_u>
            <#assign count += 1/>
        </#if>
    </#list>
    <#return count/>
</#function>

<#function contains id_u p>
    <#if p.user_id == id_u>
        <#return true/>
    </#if>
    <#return false/>
</#function>

<#macro post_creater flag p>
    <h1 class="title">
        <@postTitle p/>
    </h1>
    <p class="body">
        <#if flag>
            ${p.text}
        <#else>
            ${substring(p.text)}...
        </#if>
    </p>
    <div class="footer">
        <div class="left">
            <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
            <span class="positive-score">${p.id}</span>
            <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
        </div>
        <div class="right">
            <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
            ${p.id} days ago
            <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
            <a href="#"> ${p.user_id * 2}</a>
        </div>
    </div>
</#macro>

