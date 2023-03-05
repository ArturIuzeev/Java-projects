<template>
  <footer>
    <a href="/">Codeforces</a> &copy; 2021 by Mike Mirzayanov
    <div>
      Post Count: {{ postCount }} | User Count: {{ userCount }}
    </div>
  </footer>
</template>

<script>
export default {
  name: "Footer",
  props: ["postCount", "userCount"]
}
</script>

<style scoped>
</style>