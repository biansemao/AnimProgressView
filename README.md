# AnimProgressView
Android 自定义控件，圆形进度条，progressbar

效果见下图:

![](https://github.com/biansemao/AnimProgressView/blob/master/anim.gif)

# How To Use
## 1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
## 2. Add the dependency:
```
dependencies {
    implementation 'com.github.biansemao:AnimProgressView:1.0.0'
}
```
# Attributes
| attr 属性 | description 描述 |
|-----------|-----------------|
| circleColor | 各圆的颜色 |
| circleNum | 圆个数 |
| circleRadius | 各圆半径 |
| lightIndex | 起始最亮的圆（0开始） |
| animTime | 动画时间（至少大于50毫秒） |
| isAutoAnim | 是否自动开始动画 |
| isClockwise | 是否顺时针旋转（默认顺时针，true为顺时针，false为逆时针） |
| animMode | 动画模式（共六种，默认RotateMode模式） |
## AnimMode动画模式
共六种动画模式可选择，参照效果图，每行依次为：RotateMode模式、DotMode模式、TailMode模式、GarbMode模式、ZoomMode模式、ChangeMode模式）
# Method
## 1.startAnim()
开启动画
## 2.stopAnim()
停止动画
## 3.resetAnim()
停止并重置动画
