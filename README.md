## 一个项目用于日常App中常用的一些操作,包括常用的自定义布局和一些工具类
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
可以自定义初始角度,返回值
