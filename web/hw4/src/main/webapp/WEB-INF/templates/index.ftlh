<#import "./common.ftlh" as c>

<@c.page_index>

    <div class="datatable">
        <div class="caption">Users</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>User</th>
            </tr>
            </thead>
            <tbody>
            <#list users as u>
                <tr>
                    <#if user?? && user.id == u.id>*</#if>
                    <td><@c.userId u/></td>
                    <td><@c.userlink u/></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page_index>
