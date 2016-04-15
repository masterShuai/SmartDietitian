/**
 * Created by Administrator on 2015/7/5.
 */


//初始化地图对象，加载地图
var map = new AMap.Map("mapContainer", {
    resizeEnable: true,
    //二维地图显示视口
    view: new AMap.View2D({
        center: new AMap.LngLat(123.45688, 31.123456),//地图中心点
        zoom: 13 //地图显示的缩放级别
    })
});

