<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	/*每个页面公共scss */
	@import "/static/style/app.scss";
	/*每个页面图标css */
	@import "/static/iconfont/iconfont.css";
</style>
