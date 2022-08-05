<template>
	<router-view />
</template>

<script>
	export default {
		mounted() {
			this.$request.getMenus();
		}
	}
</script>
<style lang="scss">
	html,
	body {
		margin: 0;
		background-color: #F4F4F4;
	}

</style>
