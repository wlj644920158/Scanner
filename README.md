# Scanner
仿
 支付宝的AR红包扫描效果
 
ScannerView的实现主要是利用canvas绘制圆弧通过更改角度值实现的动画
使用的 方法很简单,xml文件申明,通过调用setState方法来更改状态值就行

 
# 项目截图
<div align=center><img width="320" height="600" src="https://github.com/wlj644920158/Scanner/blob/master/screenshots/Screenshot_2017-05-15-16-05-41.png"/></div>

# 使用方式

xml布局使用
```
  <cn.com.hadon.scanner.ScanView
        android:id="@+id/scanView"
        android:layout_width="240dp"
        android:layout_height="240dp"
        android:layout_centerInParent="true"
        app:blueCircleBorderWidth="5dp"
        app:insideRedCircleBorderWidth="8dp"
        app:outsideRedCircleBorderWidth="8dp"
        app:whiteCircleBorderWidth="8dp"
        app:blueCircleSwepAngle="30"
        app:outRedCircleSwepAngle="20"
        app:whiteCircleSwepAngle="85"
        />
```
控制语句
```
 public void onReady(View view) {
        scanView.setState(ScanView.STATE_READY);
    }

    public void onScan(View view) {
        scanView.setState(ScanView.STATE_SCANING);
    }

    public void onSuccess(View view) {
        scanView.setState(ScanView.STATE_SUCCESS);
    }
```
