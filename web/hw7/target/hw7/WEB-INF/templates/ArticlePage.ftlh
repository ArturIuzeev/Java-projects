<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>

    <div class="text-box form-box">

        <div class="header">Create article</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="save"/>
                <div class="field">
                    <div class="name">
                        <label for="title">Title</label>
                    </div>
                    <div class="value">
                        <textarea class="title" id="title" name="title" type="text"></textarea>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="text">Text</label>
                    </div>
                    <textarea class="text" id="text" name="text" type="text"></textarea>
                </div>

                <div class="error">

                </div>

                <div class="button-field">
                    <input type="submit" value="Create">
                </div>
            </form>
        </div>
    </div>

    <script>
        $(function () {
            $(".text-box form").submit(function () {
                const title = $(this).find("textarea[name='title']").val();
                const text = $(this).find("textarea[name='text']").val();
                const $error = $(this).find(".error");

                $.ajax({
                    type: "POST",
                    url: "",
                    dataType: "json",
                    data: {
                        action: "save",
                        title: title,
                        text: text
                    },
                    success: function (response) {
                        if (response["error"]) {
                            $error.text(response["error"]);
                        } else {
                            location.href = response["redirect"];
                        }
                    }
                });

                return false;
            });
        })
    </script>
</@c.page>
