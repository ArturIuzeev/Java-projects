<template>
    <div>
        <article>
            <div class="title"><a href="#">{{ post.title }} {{ post.id }}</a></div>
            <div class="information">By {{ post.user.name }}</div>
            <div class="body">{{ post.text }}</div>
            <div class="footer">
                <div class="left">
                    <img src="../../assets/img/voteup.png" title="Vote Up" alt="Vote Up"/>
                    <span class="positive-score">+173</span>
                    <img src="../../assets/img/votedown.png" title="Vote Down" alt="Vote Down"/>
                </div>
                <div class="right">
                    <img src="../../assets/img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                    <img src="../../assets/img/comments_16x16.png" title="Comments" alt="Comments"/>
                </div>
            </div>
        </article>
    </div>
</template>

<script>
export default {
    name: "Post",
    props: ["post"]
}
</script>

<style scoped>

</style>