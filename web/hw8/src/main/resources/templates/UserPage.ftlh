<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <#if userInfo??>
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Created</th>
            </tr>
            </thead>
            <tbody>
                <tr>
                    <td class="id">${userInfo.id}</td>
                    <td class="login">${userInfo.login}</td>
                    <td class="creationTime">${userInfo.creationTime}</td>
                </tr>
            </tbody>
        </table>
        <#else>
        <tr class="noData">
            <td colspan="3">
                No data
            </td>
        </tr>
        </#if>
    </div>
</@c.page>