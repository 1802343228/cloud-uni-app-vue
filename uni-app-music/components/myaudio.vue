<template>
	<view class="fixed-bottom rounded mx-2 mb-1" style="height: 160rpx;background-color: #d1ccc0;opacity: .9;">
		<!-- 进度部分 -->
		<view class="flex align-center justify-center font" style="color: #7a8388;height: 65rpx;">
			<!-- 总时长 -->
			<view>{{ durationTime | formatTime }}</view>
			<!-- 进度条部分 -->
			<view style="width: 500rpx ;display: inline;">
				<slider @change="sliderToPlay" @changing="sliderToPlay" block-size="16" activeColor="#e48267" backgroundColor="#eef2f3"
				 :max="durationTime" :value="currentTime" style="display: inline-block;width: 350rpx;" />
				<!-- 播放 -->
				<view style="display: inline-block;">{{ currentTime | formatTime }}</view>
			</view>
		</view>
		<view class="flex align-center justify-between mx-2" style="height: 95rpx;">
			<!-- 音频相关信息 -->
			<view class="font" style="color: #424651;">
				<view>歌手-{{ singerName }}</view>
				<view>歌曲-{{ audioName }}</view>
			</view>
			<!-- 音频按钮部分 -->
			<view class="flex align-center">
				<view class="animated" hover-class="pulse">
					<my-icon iconId="icon-shangyishou" iconSize="75" @my-click="PreOrNext('pre')"></my-icon>
				</view>
				<my-icon :iconId="playStatus ? 'icon-bofang' : 'icon-ziyuan'" iconSize="75" class="mx-2" @my-click="PlayOrPause"></my-icon>
				<view class="animated" hover-class="pulse">
					<my-icon iconId="icon-xiayishou" iconSize="75" @my-click="PreOrNext('next')"></my-icon>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapGetters,
		mapMutations,
		mapActions
	} from 'vuex'
	import unit from '../common/unit.js'
	export default {
		filters: {
			...unit
		},
		computed: {
			...mapState({
				playStatus: ({
					audio,
				}) => audio.playStatus,
				durationTime: ({
					audio
				}) => audio.durationTime,
				currentTime: ({
					audio
				}) => audio.currentTime
			}),
			...mapGetters([
				'audioName',
				'singerName'
			])
		},
		methods: {
			...mapMutations([
				'destruction'
			]),
			...mapActions([
				'init',
				'PlayOrPause',
				'PreOrNext',
				'sliderToPlay'
			])
		},
		data() {
			return {
			}
		},
		mounted() {
			this.init()
		},
		destroyed() {
			ths.description()
		}
	}
</script>

<style>
</style>
