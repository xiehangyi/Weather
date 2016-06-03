package com.xhy.weather;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.xhy.Adapter.WeaAdapter;
import com.xhy.Thread.ThreadCitys;
import com.xhy.Thread.ThreadProvinces;
import com.xhy.entity.Weather;
import com.xhy.tool.IconChange;
import com.xhy.tool.WeatherAnalysis;
import com.xhy.tool.WeatherService;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;

public class MainActivity extends AppCompatActivity {

    WeatherService weatherService;
    WeatherAnalysis weatherAnalysis;

    String[] StrProvinces;
    String[] StrCitys;
    String city = "长沙";

    Spinner spinnerProvince;
    Spinner spinnerCity;

    public static Handler handler;

    //适配器
    WeaAdapter weaAdapter;
    ArrayAdapter<String> provinceAdapter;
    ArrayAdapter<String> cityAdapter;
    AlertDialog dialog;

    // 错误节点
    public Boolean isHaveNet;

    //数据
    ArrayList<Weather> data;

    // 控件
    private TextView tv_1;
    private ListView listView_weather;
    private ImageView listView_add;

    // 存储天气信息
    private String weatherToday;
    private String weatherTomorrow;
    private String weatherAfterday;
    private String weatherAfterday2;
    private String weatherAfterday3;

    //Preferece机制操作的文件名
    public static final String PREFERENCE_NAME = "SaveCity";
    //Preferece机制的操作模式
    public static int MODE = Context.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_1 = (TextView) findViewById(R.id.tv_1);
        listView_weather = (ListView) findViewById(R.id.listView_weather);
        listView_add = (ImageView) findViewById(R.id.imageView_loc);

        judgeHaveNet();
        initData();


    }

    /**
     * 判断是否有网
     */
    public void judgeHaveNet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // 检查网络连接，如果无网络可用，就不需要进行连网操作等
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !manager.getBackgroundDataSetting()) {
            isHaveNet = false;
        }
        isHaveNet = true;


    }

    /**
     * 初始化天气信息
     */
    private void initListView() {

        weatherAnalysis = new WeatherAnalysis(city);
        weatherAnalysis.start();

    }

    /**
     * 初始化数据并建立Handler接收数据
     */
    private void initData() {
        if(isHaveNet) {

            Intent intent = getIntent();
            String cityName = intent.getStringExtra("cityName");

            if(cityName != null){
                city = cityName;
            }

            //运行子线程获取数据
            ThreadProvinces threadProvinces = new ThreadProvinces();
            threadProvinces.start();

            //拿到子线程的数据
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 0x0001:
                            Bundle data = msg.getData();
                            StrProvinces = data.getStringArray("Provinces");
                            for (int i = 0; i < StrProvinces.length; i++) {
                                String[] s = StrProvinces[i].split(",");
                                StrProvinces[i] = s[0];
                                Log.v(i + "", "cc");
                                Log.v(StrProvinces[i], "aa");
                            }
                            break;
                        case 0x00010:
                            Bundle dataWr = msg.getData();
                            String wrong = dataWr.getString("wrong");
                            Toast.makeText(MainActivity.this, wrong, Toast.LENGTH_SHORT).show();
                            break;
                        case 0x0002:
                            Bundle dataC = msg.getData();
                            StrCitys = dataC.getStringArray("Citys");
                            cityAdapter = new ArrayAdapter<String>(MainActivity.this
                                    , android.R.layout.simple_list_item_1, StrCitys);
                            spinnerCity.setAdapter(cityAdapter);
                            break;
                        case 0x0003:
                            Bundle dataW = msg.getData();
                            weatherToday = dataW.getString("WeatherToday");
                            weatherTomorrow = dataW.getString("weatherTomorrow");
                            weatherAfterday = dataW.getString("weatherAfterday");
                            weatherAfterday2 = dataW.getString("weatherAfterday2");
                            weatherAfterday3 = dataW.getString("weatherAfterday3");
                            UpdateData();
                            break;

                    }

                }
            };
            listView_add.setEnabled(true);
            initListView();
        } else {
            
            Toast.makeText(MainActivity.this, "网络不可连接", Toast.LENGTH_SHORT).show();
        }

}


    /**
     * imageButtonLoc的点击执行方法
     * @param view
     */
    public void imageViewLoc(View view) {

        if(isHaveNet == true) {
            initData();
            weatherService = new WeatherService();

            View v = getLayoutInflater().inflate(R.layout.loc_demo, null);
            spinnerProvince = (Spinner) v.findViewById(R.id.spinner_province);
            spinnerCity = (Spinner) v.findViewById(R.id.spinner_city);
            ImageButton imageBtn_sear = (ImageButton) v.findViewById(R.id.imageBtn_sear);

            //为Spinner装数据
            provinceAdapter = new ArrayAdapter<String>
                    (MainActivity.this, android.R.layout.simple_list_item_1, StrProvinces);
            spinnerProvince.setAdapter(provinceAdapter);

            dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("城市搜索")
                    .setView(v)
                    .create();

            dialog.show();

            GetSpinnerCity();


            imageBtn_sear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    saveToPreference(city);
                    weatherAnalysis = new WeatherAnalysis(city);
                    weatherAnalysis.start();


                }
            });
        } else {
            Toast.makeText(MainActivity.this, "网络不可连接", Toast.LENGTH_SHORT).show();
        }
    }


    private void GetSpinnerCity() {

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String province = spinnerProvince.getSelectedItem().toString();
                ThreadCitys threadCitys = new ThreadCitys(province);
                threadCitys.start();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                city = spinnerCity.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 更新天气
     */
    private void UpdateData(){
        data = new ArrayList<>();
        String[] weatherTodays = weatherToday.split(";");
        String[] weatherTomorrows = weatherTomorrow.split(";");
        String[] weatherAfterdays = weatherAfterday.split(";");
        String[] weatherAfterday2s = weatherAfterday2.split(";");
        String[] weatherAfterday3s = weatherAfterday3.split(";");

        IconChange ic = new IconChange();

        data.add(new Weather(weatherTodays[0], weatherTodays[1], weatherTodays[2],
                ic.parseIcon(weatherTodays[3]), ic.parseIcon(weatherTodays[4])));
        data.add(new Weather(weatherTomorrows[0], weatherTomorrows[1], weatherTomorrows[2],
                ic.parseIcon(weatherTomorrows[3]), ic.parseIcon(weatherTomorrows[4])));
        data.add(new Weather(weatherAfterdays[0], weatherAfterdays[1], weatherAfterdays[2],
                ic.parseIcon(weatherAfterdays[3]), ic.parseIcon(weatherAfterdays[4])));


        if(data != null){
            Log.v("aa","data中有数据");
        }
        weaAdapter = new WeaAdapter(data,MainActivity.this);
        listView_weather.setAdapter(weaAdapter);
        tv_1.setText(city);
        String s;

        s = weatherTodays[2]+"/"+weatherTomorrows[2]+"/"
                +weatherAfterdays[2]+"/"+weatherAfterday2s[2]+"/"+weatherAfterday3s[2];

        UpdateWeatherView(StringGetNum(s));
    }


    /**
     *  创建菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_more, menu);
        MenuItem item = menu.findItem(R.id.menu_add_remove);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(MainActivity.this,AddRemoveActivity.class);
                startActivity(intent);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    /**
     * 剪切字符串
     * @param s
     * @return
     */
    private String[] StringGetNum(String s){

        s = s.replace("℃","");
        Log.i("aa",s);
        String[] s1 = s.split("/");


        return s1;
    }

    /**
     * 以下都是关于画图表的方法
     * @param s
     */
    private void UpdateWeatherView(String[] s) {

        int m = 0,n=0;
        String[] highTmp = new String[5];
        String[] lowTmp = new String[5];
        for(int i = 0;i < 10;i+=2){

            lowTmp[m] = s[i];
            m++;
        }
        for(int i = 1;i < 10;i+=2){

            highTmp[n] = s[i];
            n++;
        }
        UpdateView(highTmp, lowTmp);
    }

    private void UpdateView(String[] highTmp,String[] lowTmp) {
        LineChart mChart = (LineChart) findViewById(R.id.chart);
        setChart(mChart);

        // 制作5个数据点。
        setData(mChart, 5,highTmp,lowTmp);

        Legend l = mChart.getLegend();
        l.setForm(LegendForm.LINE);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);

        XAxis xAxis = mChart.getXAxis();

        // 将X坐标轴的标尺刻度移动底部。
        xAxis.setPosition(XAxisPosition.BOTTOM);

        // X轴之间数值的间隔
        xAxis.setSpaceBetweenLabels(1);

        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);

        YAxis leftAxis = mChart.getAxisLeft();
        setYAxisLeft(leftAxis);

        YAxis rightAxis = mChart.getAxisRight();
        setYAxisRight(rightAxis);

    }


    private void setChart(LineChart mChart) {
        //mChart.setDescription("@ http://blog.csdn.net/zhangphil");
        //mChart.setNoDataTextDescription("如果传递的数值是空，那么你将看到这段文字。");
        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);
        mChart.setHighlightPerDragEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.LTGRAY);
        mChart.animateX(3000);
    }

    private void setYAxisLeft(YAxis leftAxis) {
        // 在左侧的Y轴上标出4个刻度值
        leftAxis.setLabelCount(4, true);

        // Y坐标轴轴线的颜色
        leftAxis.setGridColor(Color.RED);

        // Y轴坐标轴上坐标刻度值的颜色
        leftAxis.setTextColor(Color.RED);

        // Y坐标轴最大值
        leftAxis.setAxisMaxValue(50);

        // Y坐标轴最小值
        leftAxis.setAxisMinValue(10);

        leftAxis.setStartAtZero(false);

        leftAxis.setDrawLabels(true);
    }

    private void setYAxisRight(YAxis rightAxis) {
        // Y坐标轴上标出8个刻度值
        rightAxis.setLabelCount(8, true);

        // Y坐标轴上刻度值的颜色
        rightAxis.setTextColor(Color.BLUE);

        // Y坐标轴上轴线的颜色
        rightAxis.setGridColor(Color.BLUE);

        // Y坐标轴最大值
        rightAxis.setAxisMaxValue(30);

        // Y坐标轴最小值
        rightAxis.setAxisMinValue(-5);

        rightAxis.setStartAtZero(false);
        rightAxis.setDrawLabels(true);
    }

    private void setData(LineChart mChart, int count,String[] highTmp,String[] lowTmp) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add("某月" + (i + 1) + "日");
        }

        ArrayList<Entry> yHigh = new ArrayList<Entry>();
        LineDataSet high = new LineDataSet(yHigh, "高温");
        setHighTemperature(high, yHigh, count, highTmp);

        ArrayList<Entry> yLow = new ArrayList<Entry>();
        LineDataSet low = new LineDataSet(yLow, "低温");
        setLowTemperature(low, yLow, count, lowTmp);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(high);
        dataSets.add(low);

        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueTextSize(10f);
        mChart.setData(data);
    }

    private void setHighTemperature(LineDataSet high, ArrayList<Entry> yVals,
                                    int count,String[] highTmp) {

        for (int i = 0; i < count; i++) {

            float val = (float) Integer.parseInt(highTmp[i]);
            yVals.add(new Entry(val, i));
        }

        // 以左边的Y坐标轴为准
        high.setAxisDependency(AxisDependency.LEFT);

        high.setLineWidth(5f);
        high.setColor(Color.RED);
        high.setCircleSize(8f);
        high.setCircleColor(Color.YELLOW);
        high.setCircleColorHole(Color.DKGRAY);
        high.setDrawCircleHole(true);

        // 设置折线上显示数据的格式。如果不设置，将默认显示float数据格式。
        high.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                DecimalFormat decimalFormat = new DecimalFormat(".0");
                String s = "高温" + decimalFormat.format(value);
                return s;
            }
        });

    }

    private void setLowTemperature(LineDataSet low, ArrayList<Entry> yVals,
                                   int count,String[] lowTmp) {

        for (int i = 0; i < count; i++) {
            float val = (float) Integer.parseInt(lowTmp[i]);
            yVals.add(new Entry(val, i));
        }

        // 以右边Y坐标轴为准
        low.setAxisDependency(AxisDependency.RIGHT);

        // 折现的颜色
        low.setColor(Color.GREEN);

        // 线宽度
        low.setLineWidth(3f);

        // 折现上点的圆球颜色
        low.setCircleColor(Color.BLUE);

        // 填充圆球中心部位洞的颜色
        low.setCircleColorHole(Color.LTGRAY);

        // 圆球的尺寸
        low.setCircleSize(5f);

        low.setDrawCircleHole(true);

        low.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                DecimalFormat decimalFormat = new DecimalFormat(".0");
                String s = "低温" + decimalFormat.format(value);
                return s;
            }
        });
    }


    // 保存城市数据
    private void saveToPreference(String city_name) {
        SharedPreferences sharedPreferences =
                getSharedPreferences(PREFERENCE_NAME, MODE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(city_name, city_name);
        editor.commit();
    }



}
