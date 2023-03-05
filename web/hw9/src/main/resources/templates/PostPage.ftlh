<#-- @ftlvariable name="tags" type="java.lang.String[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <@c.post post=post/>
    <#if user??>
        <div class="form-comment">
            <div class="header">Write Comment</div>
            <div class="body">
                <form method="post" action="">
                    <input type="hidden" name="action" value="writeComment"/>
                    <div class="field">
                        <div class="value">
                            <textarea id="text" name="text">${comment.text!}</textarea>
                        </div>
                        <@c.error "comment.text"/>
                    </div>
                    <div class="button-field">
                        <input type="submit" value="Write">
                    </div>
                </form>
            </div>
        </div>
    </#if>
    <#list post.comments as comment>
        <div class="comment">
            <div class="header">
                ${comment.user.login}
            </div>
            <div class="text">
                ${comment.text}
            </div>
            <div class="footer">
                At ${comment.creationTime}
            </div>
        </div>
    </#list>

</@c.page>