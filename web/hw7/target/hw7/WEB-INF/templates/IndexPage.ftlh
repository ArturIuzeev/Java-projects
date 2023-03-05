<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->

<#import "commons.ftlh" as c>

<@c.page2>

    <div class="articles  datatable">
        <div class="noData">
            <div>
                No data
            </div>
        </div>

        <template>
            <article>
                <div class="title"></div>
                <div class="information"></div>
                <div class="body"></div>
                <div class="time"></div>
            </article>
        </template>
    </div>

    <script>
        $(function () {
            const $noData = $(".articles .noData");
            $noData.find("div").text("Loading...");

            $.ajax({
                dataType: "json",
                data: {
                    action: "findAll"
                },
                success: function (response) {
                    response["articles"].forEach(function (article) {
                        $noData.hide();
                        const $tr = $($(".articles template").prop("content")).clone();
                        $tr.find(".title").text(article["title"]);
                        $tr.find(".time").text("Date: " + article["creationTime"]);
                        $tr.find(".information").text("UserId: " + article["userId"]);
                        $tr.find(".body").text(article["text"]);
                        $(".articles").append($tr);
                    });
                },
                type: "POST"
            });
        })
    </script>
</@c.page2>
