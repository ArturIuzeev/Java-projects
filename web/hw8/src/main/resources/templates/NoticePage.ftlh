<#import "/spring.ftl" as spring/>
<#import "commons.ftlh" as c>

<@c.page>
    <div class="write form-box">
        <div class="header">Notice</div>
        <div class="body">
            <form method="post" id="noticeCreating">
                <div class="field">
                    <div class="name">
                        <label for="text">Text</label>
                    </div>
                    <div class="value">
                        <textarea id="text" name="content"></textarea>
                    </div>
                    <@c.error "noticeForm.content"/>
                </div>
                <div class="button-field">
                    <input type="submit" value="Create">
                </div>
            </form>
        </div>
    </div>
</@c.page>
