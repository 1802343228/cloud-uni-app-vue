(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["components/uni-transition/uni-transition"],{"0099":function(t,n,e){"use strict";function r(t,n){var e=Object.keys(t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(t);n&&(r=r.filter((function(n){return Object.getOwnPropertyDescriptor(t,n).enumerable}))),e.push.apply(e,r)}return e}function i(t){for(var n=1;n<arguments.length;n++){var e=null!=arguments[n]?arguments[n]:{};n%2?r(Object(e),!0).forEach((function(n){o(t,n,e[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(e)):r(Object(e)).forEach((function(n){Object.defineProperty(t,n,Object.getOwnPropertyDescriptor(e,n))}))}return t}function o(t,n,e){return n in t?Object.defineProperty(t,n,{value:e,enumerable:!0,configurable:!0,writable:!0}):t[n]=e,t}Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var a={name:"uniTransition",props:{show:{type:Boolean,default:!1},modeClass:{type:Array,default:function(){return[]}},duration:{type:Number,default:300},styles:{type:Object,default:function(){return{}}}},data:function(){return{isShow:!1,transform:"",ani:{in:"",active:""}}},watch:{show:{handler:function(t){t?this.open():this.close()},immediate:!0}},computed:{stylesObject:function(){var t=i({},this.styles,{"transition-duration":this.duration/1e3+"s"}),n="";for(var e in t){var r=this.toLine(e);n+=r+":"+t[e]+";"}return n}},created:function(){},methods:{change:function(){this.$emit("click",{detail:this.isShow})},open:function(){var t=this;for(var n in clearTimeout(this.timer),this.isShow=!0,this.transform="",this.ani.in="",this.getTranfrom(!1))"opacity"===n?this.ani.in="fade-in":this.transform+="".concat(this.getTranfrom(!1)[n]," ");this.$nextTick((function(){setTimeout((function(){t._animation(!0)}),50)}))},close:function(t){clearTimeout(this.timer),this._animation(!1)},_animation:function(t){var n=this,e=this.getTranfrom(t);for(var r in this.transform="",e)"opacity"===r?this.ani.in="fade-".concat(t?"out":"in"):this.transform+="".concat(e[r]," ");this.timer=setTimeout((function(){t||(n.isShow=!1),n.$emit("change",{detail:n.isShow})}),this.duration)},getTranfrom:function(t){var n={transform:""};return this.modeClass.forEach((function(e){switch(e){case"fade":n.opacity=t?1:0;break;case"slide-top":n.transform+="translateY(".concat(t?"0":"-100%",") ");break;case"slide-right":n.transform+="translateX(".concat(t?"0":"100%",") ");break;case"slide-bottom":n.transform+="translateY(".concat(t?"0":"100%",") ");break;case"slide-left":n.transform+="translateX(".concat(t?"0":"-100%",") ");break;case"zoom-in":n.transform+="scale(".concat(t?1:.8,") ");break;case"zoom-out":n.transform+="scale(".concat(t?1:1.2,") ");break}})),n},_modeClassArr:function(t){var n=this.modeClass;if("string"!==typeof n){var e="";return n.forEach((function(n){e+=n+"-"+t+","})),e.substr(0,e.length-1)}return n+"-"+t},toLine:function(t){return t.replace(/([A-Z])/g,"-$1").toLowerCase()}}};n.default=a},"249b":function(t,n,e){"use strict";e.r(n);var r=e("29cb"),i=e("c1b9");for(var o in i)"default"!==o&&function(t){e.d(n,t,(function(){return i[t]}))}(o);e("3916");var a,c=e("f0c5"),s=Object(c["a"])(i["default"],r["b"],r["c"],!1,null,"08ec7412",null,!1,r["a"],a);n["default"]=s.exports},"29cb":function(t,n,e){"use strict";var r,i=function(){var t=this,n=t.$createElement;t._self._c},o=[];e.d(n,"b",(function(){return i})),e.d(n,"c",(function(){return o})),e.d(n,"a",(function(){return r}))},3916:function(t,n,e){"use strict";var r=e("54d1"),i=e.n(r);i.a},"54d1":function(t,n,e){},c1b9:function(t,n,e){"use strict";e.r(n);var r=e("0099"),i=e.n(r);for(var o in r)"default"!==o&&function(t){e.d(n,t,(function(){return r[t]}))}(o);n["default"]=i.a}}]);
;(global["webpackJsonp"] = global["webpackJsonp"] || []).push([
    'components/uni-transition/uni-transition-create-component',
    {
        'components/uni-transition/uni-transition-create-component':(function(module, exports, __webpack_require__){
            __webpack_require__('543d')['createComponent'](__webpack_require__("249b"))
        })
    },
    [['components/uni-transition/uni-transition-create-component']]
]);