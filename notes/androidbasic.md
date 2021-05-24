# [1.TextView](https://www.bilibili.com/video/BV13y4y1E7pF?p=3)

+ 1 layout_width: 组件的宽度
+ 2 layout_heighy: 组件的高度
+ 3 id: 为TextView设置一个组件id
+ 4 text: 设置显示的文本内容
+ 5 textColor: 设置字体颜色
+ 6 textStyle: 设置字体风格，normal(无效果)，bold(加粗)，italic(斜体)
+ 7 textSize: 字体大小，一般用sp
+ 8 background: 控件的背景颜色，可以是图片
+ 9 gravity: 设置控件的对齐方向

## [1.1 带阴影的TextView](https://www.bilibili.com/video/BV13y4y1E7pF?p=4)

+ 1 shadowColor: 设置阴影颜色，需要与shadowFadius一起使用
+ 2 shadowRadous: 设置阴影的模糊程度，设为0.1就变成字体颜色，建议使用3.0
+ 3 shadowDx: 设置阴影在水平方向的偏移
+ 4 shadowDy: 设置阴影在垂直方向的偏移

## [1.2 实现跑马灯TextView](https://www.bilibili.com/video/BV13y4y1E7pF?p=5)

+ 1 singleLine: 内容单行显示
+ 2 focusable: 是否可以获取焦点
+ 3 focusableInTouchMode: 用于控制视图在触屏模式下是否可以聚焦
+ 4 ellipsize:在哪里省略文本
+ 5 marqueeRepeatLimit:字幕动画重复的次数

*跑马灯 ellipsize=marquee  并且在TextView中添加 \<requestFocus/>*


# [2.Button](https://www.bilibili.com/video/BV13y4y1E7pF?p=6)

+ 1 Button 按键图片的选择
+ 2 Button 的三个事件

# [3.EditText](https://www.bilibili.com/video/BV13y4y1E7pF?p=8)

+ 1 hint: 输入提示
+ 2 textColorHint: 输入提示的文字颜色
+ 3 inputType: 输入类型 （可在此指定password）
+ 4 drawableXxxx: 在输入框的指定方位添加图片
+ 5 drawablePadding: 设置图片与输入内容的间距
+ 6 paddingXxxx: 设置内容与边框的间距
+ 7 backgroud: 背景色

*视频教程可获取EdieText的内容*


# [4.ImageView](https://www.bilibili.com/video/BV13y4y1E7pF?p=9)

+ 1 src: 设置图片资源
+ 2 scaleType: 设置图片的缩放类型
+ 3 maxHeight: 最大高度
+ 4 maxWidth: 最大宽度
+ 5 adjustViewBounds: 调整View的界限


# [5.Intent](https://www.bilibili.com/video/BV1sK411s7Vp?p=6)

+ 1 显式跳转
   - 写法一:  class 跳转
   ```
   Intent intent = new Intent(this,SecondActivity,class);
   this.startActivity(intent);
   ```

   - 写法二： 包名.类名
   ```
   Intent intent = new Intent();
   intent.setClassName(this,"xx.xx.xx.mainactivity.xxxactivity");
   startActivity(intent);
   ```
	
   - 写法三：ComponentName
   ```
   Intent intent = new Intent();
   ComponentName cname = new ComponentName(this,xxxactivity.class);
   intent.setComponent(cname);
   startActivity(intent);
   ```

+ 2 隐式跳转
	
	首先在AndroidManifest.xml 文件中修改如下
	```
	<activity android:name=".SecondActivity" >
		<intent-filter>
			<action android:name="action.nextActivity" />

			<category android:name="android.intent.category.DEFAULT" />
		</intent-filter>
	</activity>
	```
	*android.intent.category.LAUNCHER 表示是否创建桌面图标*

	- 写法一
	```
	Intent intent = new Intent();
	intent.setAction("action.nextActivity");
	startActivity(intent);
	```

	- 写法二
	```
	Intent intent = new Intent("action.nextActivity");
	startActivity(intent);
	```

# [6.RelativeLayout 相对布局](https://www.bilibili.com/video/BV1sK411s7Vp?p=12)

# [7.TableLayout 表格布局](https://www.bilibili.com/video/BV1sK411s7Vp?p=13)

# [8.GridLayout 网格布局](https://www.bilibili.com/video/BV1sK411s7Vp?p=13)

+ 1 rowCount: 设置行数
+ 2 columnCount: 设置列数
+ 3 layout_rowSpan: 合并行 
+ 4 layout_columnSpan: 合并列
+ 5 layout_row: 指定该控件在某行
+ 6 layout_column: 指定该控件在某列

# [9.Fragment](https://www.bilibili.com/video/BV1sK411s7Vp?p=22)




