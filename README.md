## 一个项目用于日常App中常用的一些操作,包括常用的自定义布局和一些工具类(具体生产环境,需要结合实际情况修改)
### 包含内容包括
1>常用的自定义控件(注:需要结合实际生产环境)
 1) 图标类:折线图(支持点击自定义回调),扇形图(支持点击自定义回调),遥控器菜单布局(不规则图形事件的处理)
 2) 图片处理:(1)根据手势处理的图片类(已处理滑动冲突)
2>用网络请求的网络库 采用retrofit+rxJava
### 字符侧滑栏
返回触摸的字符返回值,带有回调
```
XML布局
    <com.bj.zhaoyun.view.slideletter.SlideLetterView
        android:layout_alignParentLeft="true"
        android:id="@+id/slv_letter"
        android:layout_width="50dp"
        android:layout_height="match_parent"
        android:layout_marginBottom="10dp"
        android:layout_marginTop="15dp"
        app:letter_default_color="#999999"
        app:letter_selected_color="#ff0000"
        app:letter_size="13sp" />
 Java
     @BindView(R.id.slv_letter)
      SlideLetterView slv_letter;
      @BindView(R.id.tv_show)
      TextView tv_show;

      @Override
      public int getLayoutId() {
          return R.layout.activity_slide_letter;
      }

      @Override
      public void initData() {
          List<String> mDatas = new ArrayList<>();
          //65-90
          int x = 65;
          while (x <= 90) {
              mDatas.add(String.valueOf((char) x));
              x++;
          }
          mDatas.add(String.valueOf((char) 35));
          slv_letter.setDatas(mDatas);
          //触摸监控回调
          slv_letter.setOnLetterChangeListener(new SlideLetterView.LetterChangeListener() {
              @Override
              public void onLetterChange(String letter, int choose) {
                  System.out.println("choose" + choose);
                  if (choose == -1) {
                      tv_show.setVisibility(View.GONE);
                  } else {
                      tv_show.setVisibility(View.VISIBLE);
                  }
                  tv_show.setText(letter);
              }
          });
      }
```
### 扇形图
**可以自定义初始角度,返回值,带有回调**
```
 <com.bj.zhaoyun.view.chart.PieChartTouchView
                android:id="@+id/pct_chart"
                android:layout_width="200dp"
                android:layout_height="200dp" />
 //设置数据
 pct_chart.setData(num, color, -60);
 num代表百分比数组
 color代表颜色数组
 -60 代表初始的角度
 //回调
 pct_chart.setOnPieChartChildClickListener(new PieChartTouchView.OnPieChartChildClickListener() {
             @Override
             public void onClick(final int position) {
                ...
             }
         });

```
### 折线图(带有点击事件的回调)
```
//添加数据
  String[] x = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
        String[] y = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        final List<Point> points = new ArrayList<>();
        for (int z = 0; z < x.length; z++) {
            Point point = new Point();
            point.y = (int) ((Math.random() + 0.1) * 10);
            point.x = z + 1;
            points.add(point);
        }
        lcv_chart.setData(x, y, points);
//接口回调
lcv_chart.setOnPointClickListener(new LineChartView.OnPointClickListener() {
            @Override
            public void onPointClick( int position) {
                ToastUtil.showToast(mContext,position+"", Toast.LENGTH_SHORT);
            }
        });
```



### 遥控器布局菜单栏
```//
<com.bj.zhaoyun.view.chart.Telecontroller
        android:id="@+id/telecontroller"
        android:layout_width="150dp"
        android:layout_height="150dp" />
    //接口回调
 telecontroller.setOnTelecontrollerClickListener(new Telecontroller.OnTelecontrollerClickListener() {
                 @Override
                 public void onClick(String result) {
                     if (!TextUtils.isEmpty(result)) {
                         ...
                     }
                 }
             });
```
### 具有手势识别的ImageView 支持，放大、缩小、移动、双击，滑动冲突
```
    <com.bj.zhaoyun.view.picture.GestureImageView
        android:id="@+id/imageview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:src="@mipmap/img_report" />
```

