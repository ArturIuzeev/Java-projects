<#import "common.ftlh" as c>

<@c.page_post>
    <article>
        <#list posts?reverse as p>
            <@c.post_creater true p/>
        </#list>
    </article>
</@c.page_post>