package com.example.dx_4g;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.DXDeviceAlarmAdapter;
import com.example.dx_4g.funclass.DXDeviceReportAdapter;
import com.example.dx_4g.funclass.DX_4G_Alarm;
import com.example.dx_4g.funclass.DX_4G_Report;
import com.example.dx_4g.funclass.DX_Device_Alarm;
import com.example.dx_4g.funclass.DX_Device_Report;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.myApplication;
import com.example.dx_4g.funclass.watchdogCallbackListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class tab2 extends Fragment {

//    private static final int SEND_REQUEST=8;
//    private static final int SEND_REQUEST_ERR=9;
//    private static final int WATCHDOG_FINISH=10;
//    private List<List<String>> dataBeansReport;
//    private LinkedList<DX_Device_Report> mDataReport;
//    private EditText reportPage;
//    private TextView reportToatlPage;
//    private ListView listView;
//    private Context mContext;
//    private List<DX_4G_Report.PagingBean> dataPagiReport;
//    private DXDeviceReportAdapter DXAdapterReport;
//    private  SwipeRefreshLayout mSwipe;
//    private ProgressBar progressBar;
//    private  TextView report_querytext;
//    private  String reportQueryTimeShow;
//    private  Runnable runnable;
//
//    /****************************************/
//    //消息处理
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//
//            if (msg.what == SEND_REQUEST) {
//
//                if (msg.arg1==200) {
//                    String response = (String) msg.obj;
//                    try {
//                        RemoveWatchDog(handler,runnable);
//                        parseJSONWITHGSON(response);
//                        progressBar.setVisibility(View.GONE);
//                        mSwipe.setRefreshing(false);
//
//                    } catch (JSONException e) {
//                        RemoveWatchDog(handler,runnable);
//                        mSwipe.setRefreshing(false);
//                        e.printStackTrace();
//                        progressBar.setVisibility(View.GONE);
//                        Toast mytoast=Toast.makeText(getContext(),"Code:0"+e.toString(),Toast.LENGTH_LONG);
//                        mytoast.setGravity(Gravity.CENTER,0,190);
//                        mytoast.show();
//                    }
//
//                }else{
//                    mSwipe.setRefreshing(false);
//                    RemoveWatchDog(handler,runnable);
//                    progressBar.setVisibility(View.GONE);
//                    Toast mytoast=Toast.makeText(getContext(),"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
//                    mytoast.setGravity(Gravity.CENTER,0,190);
//                    mytoast.show();
//                }
//
//            }
//            if (msg.what==SEND_REQUEST_ERR){
//                RemoveWatchDog(handler,runnable);
//                progressBar.setVisibility(View.GONE);
//                Toast mytoast=Toast.makeText(getContext(),"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
//                mytoast.setGravity(Gravity.CENTER,0,190);
//                mytoast.show();
//            }
//
//            if(msg.what==WATCHDOG_FINISH){
//
//                progressBar.setVisibility(View.GONE);
//                Toast mytoast=Toast.makeText(getContext(),"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
//                mytoast.setGravity(Gravity.CENTER,0,190);
//                mytoast.show();
//                RemoveWatchDog(handler,runnable);
//            }
//        }
//    };
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        final View view=inflater.inflate(R.layout.tab2,container,false);
//        reportPage=(EditText)view.findViewById(R.id.report_edit);
//        reportToatlPage=(TextView)view.findViewById(R.id.reporttotalpageshow);
//        listView=(ListView)view.findViewById(R.id.report_list);
//        report_querytext=(TextView) view.findViewById(R.id.report_querytext);
//        ImageView alarm_next_page=(ImageView)view.findViewById(R.id.report_next_page);
//        ImageView alarm_previous_page=(ImageView)view.findViewById(R.id.report_previous_page);
//
//        myApplication.getInstance().setQuerytime(null);
//        report_querytext.setText(getResources().getText(R.string.alarmquery_text));
//
//
//
//        androidx.appcompat.widget.Toolbar toolbar =(Toolbar)view.findViewById(R.id.toolbar3);
//        toolbar.setTitle("报表:"+myApplication.getInstance().getDeviceName());
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);
//        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
//        // 以下动作让标题居中显地
//        TextView textView = (TextView) toolbar.getChildAt(0);//主标题
//        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
//        textView.setGravity(Gravity.CENTER);
//        toolbar.setNavigationIcon(R.drawable.rego);//设置导航图标
//
//
//        /***************************************/
//        //导航按钮点击事件
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getContext(),Main2Activity.class);
//                startActivity(intent);
//            }
//        });
//        /****************************************/
//
//
//
//        /***************************************/
//        //溢出菜单点击事件
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.action_exit:
//                        ActivityCollector.killAllActivity();
//                        break;
//                    case R.id.action_test:
//                        //Intent intent=new Intent(Main4Activity.this,Main3Activity.class);
//                        //startActivity(intent);
//                        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//                }
//                return true;
//            }
//        });
//        /*************************************/
//
//
//        /*************************************/
//        //页面加载数据
//        int deviceID= myApplication.getInstance().getRegID();
//        //readRegValue(deviceID,1,null);
//        //progressBar=getActivity().findViewById(R.id.progressBar3);
//        //progressBar.setVisibility(View.VISIBLE);
//
//        /***************************************/
//
//
//        /***************************************/
//        //下拉刷新
////        mSwipe=(SwipeRefreshLayout)view.findViewById(R.id.swipelayout2);
////        /*
////         * 设置进度条的颜色
////         * 参数是一个可变参数、可以填多个颜色
////         */
////        mSwipe.setColorSchemeColors(Color.parseColor("#d7a101"),Color.parseColor("#54c745"),Color.parseColor("#f16161"),Color.BLUE,Color.YELLOW);
////        /*
////         * 设置下拉刷新的监听
////         */
////        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
////            @Override
////            public void onRefresh() {
////                listView.setAdapter(null);
////                report_querytext.setText(getResources().getText(R.string.alarmquery_text));
////                int devicdID= myApplication.getInstance().getRegID();
////                myApplication.getInstance().setQuerytime(null);
////                readRegValue(devicdID,1,null);
////                mSwipe.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        /*
////                         * 加载完毕之后就关闭进度条
////                         */
////                        mSwipe.setRefreshing(false);
////                    }
////                }, 5000);
////            }
////        });
//
//
//        /******************************************/
//        //Edit页注册改变响应事件
//        @SuppressLint("CutPasteId") final EditText report_page=(EditText)view.findViewById(R.id.report_edit);
//        report_page.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (isInteger((String)s.toString())){
//                    int reportpage=Integer.parseInt((String)s.toString());
//                    int deviceID= myApplication.getInstance().getRegID();
//
//                    progressBar=getActivity().findViewById(R.id.progressBar3);
//                    progressBar.setVisibility(View.VISIBLE);
//                    readRegValue(deviceID,reportpage,myApplication.getInstance().getQuerytime());
//                    report_page.clearFocus();
//                    //输入法处理
//                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                    boolean isOpen = imm.isActive();
//                    if (isOpen) {
//                        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                }
//
//            }
//        });
//        /********************************************/
//
//
//        /*******************************************/
//        /**********alarm_next_page点击事件注册*******/
//
//        alarm_next_page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isInteger((String)report_page.getHint())){
//                    int alarepage=Integer.parseInt((String)report_page.getHint())+1;
//                    report_page.setText(String.valueOf(alarepage));
//                }
//
//            }
//
//        });
//
//        /******************************************/
//
//        /*******************************************/
//        /**********alarm_previous_page点击事件注册*******/
//
//        alarm_previous_page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isInteger((String)report_page.getHint())){
//                    int alarepage=Integer.parseInt((String)report_page.getHint())-1;
//                    if (alarepage>=1) {
//                        report_page.setText(String.valueOf(alarepage));
//                    }
//                    else{
//                        Toast mytoast=Toast.makeText(getContext(),"已到第一页",Toast.LENGTH_LONG);
//                        mytoast.setGravity(Gravity.CENTER,0,190);
//                        mytoast.show();
//                        report_page.setHint("1");
//                    }
//                }
//
//            }
//
//        });
//
//        /******************************************/
//
//
//
//        /*****************************************/
//        /**************TextView事件注册***************/
//
//        report_querytext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getContext(),Main4Activity.class);
//                startActivity(intent);
//            }
//        });
//
//        /****************************************/
//
//        return view;
//    }
//
//
//    /******************************************/
//
//    private void readRegValue(int deviceID,int regID,int reportPage,String reportQueryTime) {
//        String webAddr = null;
//        if ((reportPage==1)&&(reportQueryTime==null)) {
//            webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/reg"+regID+"/history?"+reportPage;
//        }
//        if ((reportPage!=1)&&(reportQueryTime==null)){
//            webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/reg"+regID+"/history?"+reportPage;
//        }
//        if(reportQueryTime!=null){
//            webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/reg"+regID+"/history?"+reportQueryTime+"& page="+reportPage;
//        }
//
//        watchdog(handler,runnable,15000, new watchdogCallbackListener() {
//            @Override
//            public void onWatchDogFinish(long code, String message) {
//                Message msg = Message.obtain();
//                msg.what = WATCHDOG_FINISH;
//                msg.arg1 =(int)(code);
//                msg.obj=message;
//                handler.sendMessage(msg);
//            }
//
//        });
//
//
//
//
//        HttpUtil.sendHttpRequest(webAddr, myApplication.getInstance().getPasbas64(), new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response,int httpcode) {
//                Message msg = Message.obtain();
//                msg.what = SEND_REQUEST;
//                msg.obj = response;
//                msg.arg1=httpcode;
//                handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onError(int httpcode,String httpmessage) {
//                Message msg = Message.obtain();
//                msg.what = SEND_REQUEST_ERR;
//                msg.obj = httpmessage;
//                msg.arg1=httpcode;
//                handler.sendMessage(msg);
//            }
//        });
//    }
//
//    private void parseJSONWITHGSON(String jsonData) throws JSONException {
//        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
//        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组
//        JSONObject jsonArray1=jsonObject.getJSONObject("paging");
//        Gson gson = new Gson();
//        dataBeansReport = gson.fromJson(jsonArray.toString(), new TypeToken<List<List<String>>>() {
//        }.getType());
//        RegValueHandle(dataBeansReport);
//        mContext = this.getContext();
//        reportPage.setText(null);
//        reportPage.setHint(jsonArray1.getString("page"));
//        reportToatlPage.setText(jsonArray1.getString("pageCount"));
//        if (Integer.parseInt((String) reportToatlPage.getText())==0){
//            reportPage.setHint("0");
//            Toast mytoast=Toast.makeText(getContext(),"在查询日期范围无数据！",Toast.LENGTH_LONG);
//            mytoast.setGravity(Gravity.CENTER,0,190);
//            mytoast.show();
//        }
//        else{
//            reportPage.setHint(jsonArray1.getString("page"));
//        }
//
//        DXAdapterReport = new DXDeviceReportAdapter((LinkedList<DX_Device_Report>) mDataReport, mContext);
//        listView.setAdapter(DXAdapterReport);
//
//    }
//
//    private  void RegValueHandle(List<List<String>> RegValue){
//        mDataReport = new LinkedList<DX_Device_Report>();
//
//        for(int i=0;i<RegValue.size();i++){
//            mDataReport.add(new DX_Device_Report(RegValue.get(i).get(0),RegValue.get(i).get(3)));
//        }
//    }
//
//    @Override
//    public void onResume() {
//        if(myApplication.getInstance().getQuerytime()!=null) {
//            reportQueryTimeShow = "日期范围:"+myApplication.getInstance().getQuerytime().substring(5, 15) + "至" +
//                    myApplication.getInstance().getQuerytime().substring(30, 40);
//            report_querytext.setText(reportQueryTimeShow);
//        }
//        listView.setAdapter(null);
//        int devicedID= myApplication.getInstance().getRegID();
//        progressBar.setVisibility(View.VISIBLE);
//        readRegValue(devicedID,1,myApplication.getInstance().getQuerytime());
//        super.onResume();
//    }
//
//    //判断是否为整型数
//    private boolean isInteger(String str) {
//        if (null == str || "".equals(str)) {
//            return false;
//        }
//        //Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//        Pattern pattern = Pattern.compile("^-?[0-9]\\d*$");
//        return pattern.matcher(str).matches();
//    }
//    /******************************************/
//
//
//    private void watchdog(Handler handler,Runnable runnable,final long watchtime, final watchdogCallbackListener listener){
//        handler.postDelayed(runnable=new Runnable(){
//            public void run() {
//
//
//                if (listener != null) {
//                    listener.onWatchDogFinish(watchtime, "无网络或服务器无响应");
//                }
//
//
//            }
//        }, watchtime);
//    }
//
//
//    private void RemoveWatchDog(Handler handler,Runnable runnable){
//        handler.removeCallbacksAndMessages(runnable);
//    }

}
