package com.example.dx_4g;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.DXDeviceReportAdapter;
import com.example.dx_4g.funclass.DX_4G_Report;
import com.example.dx_4g.funclass.DX_Device_Report;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpCallbackListenerReponsCode;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.HttpUtilReponseCode;
import com.example.dx_4g.funclass.httpopenException;
import com.example.dx_4g.funclass.myApplication;
import com.example.dx_4g.funclass.watchdogCallbackListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

public class ReportActivity extends BaseActivity {


    private static final int SEND_REQUEST=1;
    private static final int SEND_REQUEST_ERR=2;
    private static final int WATCHDOG_FINISH=3;
    private static final int READ_FINISH=4;
    private List<List<String>> dataBeansReport;
    private List<List<String>> dataBeansReport2;
    private LinkedList<DX_Device_Report> mDataReport;
    private LinkedList<DX_Device_Report> mDataReport1;
    private EditText reportPage;
    private TextView reportToatlPage;
    private ListView listView;
    private Context mContext;
    private List<DX_4G_Report.PagingBean> dataPagiReport;
    private DXDeviceReportAdapter DXAdapterReport;
    private SwipeRefreshLayout mSwipe;
    private ProgressBar progressBar;
    private  TextView report_querytext;
    private  String reportQueryTimeShow;
    private  Runnable runnable;

    private int page,page1;

    private int pageCount;

    private int sign;

    private  CountDownLatch  countDownLatch;
    private CountDownLatch countDownLatch1;
    private CountDownLatch countDownLatch2;
    private CountDownLatch countDownLatch3;
    private CountDownLatch countDownLatch4;
    private CountDownLatch countDownLatch5;

    private int readError;


    private int[] pa=new int[3];

    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.tab2;
    }

    @Override
    protected void initView() {
        mContext =this;
        progressBar=(ProgressBar)findViewById(R.id.progressBar4);
        reportPage=(EditText)findViewById(R.id.report_edit);
        reportToatlPage=(TextView)findViewById(R.id.reporttotalpageshow);
        listView=(ListView)findViewById(R.id.report_list);
        report_querytext=(TextView)findViewById(R.id.report_querytext);
        ImageView alarm_next_page=(ImageView)findViewById(R.id.report_next_page);
        ImageView alarm_previous_page=(ImageView)findViewById(R.id.report_previous_page);
        myApplication.getInstance().setQuerytime(null);
        report_querytext.setText(getResources().getText(R.string.alarmquery_text));



        androidx.appcompat.widget.Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar3);
        toolbar.setTitle("报表:"+myApplication.getInstance().getDeviceName());

        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
        // 以下动作让标题居中显地
        TextView textView = (TextView) toolbar.getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);
        toolbar.setNavigationIcon(R.drawable.rego);//设置导航图标


        /***************************************/
        //导航按钮点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
        /****************************************/



        /***************************************/
        //溢出菜单点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_exit:
                        ActivityCollector.killAllActivity();
                        break;
                    case R.id.action_test:
                        //Intent intent=new Intent(Main4Activity.this,Main3Activity.class);
                        //startActivity(intent);
                        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
                return true;
            }
        });
        /*************************************/


        /*************************************/
        //页面加载数据
        //int deviceID= myApplication.getInstance().getRegID();
        //readRegValue(deviceID,1,null);
        //progressBar=getActivity().findViewById(R.id.progressBar3);
        //progressBar.setVisibility(View.VISIBLE);

        /***************************************/


        /***************************************/
        //下拉刷新
        mSwipe=(SwipeRefreshLayout)findViewById(R.id.swipelayout2);
        /*
         * 设置进度条的颜色
         * 参数是一个可变参数、可以填多个颜色
         */
        mSwipe.setColorSchemeColors(Color.parseColor("#d7a101"),Color.parseColor("#54c745"),Color.parseColor("#f16161"),Color.BLUE,Color.YELLOW);
        /*
         * 设置下拉刷新的监听
         */
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listView.setAdapter(null);
                report_querytext.setText(getResources().getText(R.string.alarmquery_text));
                int devicdID= myApplication.getInstance().getDeviceID();
                int regID=myApplication.getInstance().getRegID();
                int valueType=myApplication.getInstance().getValuetype();
                myApplication.getInstance().setQuerytime(null);
                readRegValue(devicdID,regID,1,valueType,null);
                mSwipe.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*
                         * 加载完毕之后就关闭进度条
                         */
                        mSwipe.setRefreshing(false);
                    }
                }, 5000);
            }
        });


        /******************************************/
        //Edit页注册改变响应事件
        @SuppressLint("CutPasteId") final EditText report_page=(EditText)findViewById(R.id.report_edit);
        report_page.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isInteger((String)s.toString())){
                    int reportpage=Integer.parseInt((String)s.toString());
                    int deviceID= myApplication.getInstance().getRegID();
                    int regID=myApplication.getInstance().getRegID();
                    int valueType=myApplication.getInstance().getValuetype();
                    progressBar.setVisibility(View.VISIBLE);
                    readRegValue(deviceID,regID,reportpage,valueType,myApplication.getInstance().getQuerytime());
                    report_page.clearFocus();
                    //输入法处理
                    InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    boolean isOpen = imm.isActive();
                    if (isOpen) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }

            }
        });
        /********************************************/


        /*******************************************/
        /**********alarm_next_page点击事件注册*******/

        alarm_next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInteger((String)report_page.getHint())){
                    int reportpage=Integer.parseInt((String)report_page.getHint())+1;
                    report_page.setText(String.valueOf(reportpage));
                }

            }

        });

        /******************************************/

        /*******************************************/
        /**********alarm_previous_page点击事件注册*******/

        alarm_previous_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInteger((String)report_page.getHint())){
                    int alarepage=Integer.parseInt((String)report_page.getHint())-1;
                    if (alarepage>=1) {
                        report_page.setText(String.valueOf(alarepage));
                    }
                    else{
                        Toast mytoast=Toast.makeText(ReportActivity.this,"已到第一页",Toast.LENGTH_LONG);
                        mytoast.setGravity(Gravity.CENTER,0,190);
                        mytoast.show();
                        report_page.setHint("1");
                    }
                }

            }

        });

        /******************************************/



        /*****************************************/
        /**************TextView事件注册***************/

        report_querytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getContext(),Main4Activity.class);
//                startActivity(intent);
            }
        });

        /****************************************/




    }

    @Override
    protected void initData() throws httpopenException, InterruptedException {
        mDataReport = new LinkedList<>();
        mDataReport1=new LinkedList<>();
        final int deviceID=myApplication.getInstance().getDeviceID();
        final int regID=myApplication.getInstance().getRegID();
        int valueType=myApplication.getInstance().getValuetype();
        progressBar.setVisibility(View.VISIBLE);
        //定义线程定时器

        if (valueType==0) {
            countDownLatch=new CountDownLatch(1);
            readRegValue(deviceID, regID, 1, valueType, null);
        }else {
            countDownLatch=new CountDownLatch(2);
            countDownLatch3=new CountDownLatch(2);

            /**
             * 线程1读取第一个地址的页数
             */
            new Thread(new Runnable() {
                  @Override
                  public void run() {
                      readRegValue1(deviceID, regID, 1, 0, null,1);

                  }
              }).start();

            /**
             * 线程2读取第二个地址的页数
             */

            new Thread(new Runnable() {
                  @Override
                  public void run() {
                      try {
                          Thread.sleep(300);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                      readRegValue1(deviceID, regID+1, 1, 0, null,2);
                  }
              }).start();


            /**
             * 线程3读取第一个地址的数据
             */
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         mDataReport.clear();
                         countDownLatch.await();
                         for (int i=1;i<=pa[0];i++) {
                             countDownLatch1=new CountDownLatch(1);
                             readRegValue1(deviceID, regID, i, 0, null, 3);
                             countDownLatch1.await();
                         }
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                    countDownLatch3.countDown();

                 }
             }).start();

            /**
             * 线程4读取第二个地址的数据
             */
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         mDataReport1.clear();
                         countDownLatch.await();
                         for (int i=1;i<=pa[1];i++) {
                             countDownLatch2=new CountDownLatch(1);
                             readRegValue1(deviceID, regID+1, i, 0, null, 4);
                             countDownLatch2.await();
                         }
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                     countDownLatch3.countDown();

                 }
             }).start();



            /**
             * 测试
             */
            new Thread(new Runnable() {
                  @Override
                  public void run() {
                      try {
                          countDownLatch3.await();
                          Message msg=Message.obtain();
                          msg.what=READ_FINISH;
                          msg.arg1=mDataReport.size();
                          msg.arg2=mDataReport1.size();
                          handler.sendMessage(msg);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }


                  }
              }).start();


        }
        }







    /****************************************/
    //消息处理
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){

                case SEND_REQUEST:
                    if (msg.arg1==200) {
                        String response = (String) msg.obj;
                        try {

                            if(myApplication.getInstance().getValuetype()==0) {
                                parseJSONWITHGSON(response);
                            }

                        } catch (JSONException e) {
                            readError=readError+1;
                            e.printStackTrace();
                            Toast mytoast=Toast.makeText(ReportActivity.this,"Code:0"+e.toString(),Toast.LENGTH_LONG);
                            mytoast.setGravity(Gravity.CENTER,0,190);
                            mytoast.show();
                        }

                    }else{
                        readError=readError+1;
                        Toast mytoast=Toast.makeText(ReportActivity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                        mytoast.setGravity(Gravity.CENTER,0,190);
                        mytoast.show();
                    }
                    RemoveWatchDog(handler,runnable);
                    mSwipe.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    countDownLatch.countDown();
                    break;

                case SEND_REQUEST_ERR:
                    readError=readError+1;
                    RemoveWatchDog(handler,runnable);
                    progressBar.setVisibility(View.GONE);
                    Toast mytoast=Toast.makeText(ReportActivity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                    mytoast.setGravity(Gravity.CENTER,0,190);
                    mytoast.show();
                    break;

                case WATCHDOG_FINISH:
                    mSwipe.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    Toast mytoast1=Toast.makeText(ReportActivity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                    mytoast1.setGravity(Gravity.CENTER,0,190);
                    mytoast1.show();
                    RemoveWatchDog(handler,runnable);
                    break;
                case READ_FINISH:
                    Toast.makeText(ReportActivity.this,msg.arg1+" "+msg.arg2,Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    break;

                case 11:
                    if (msg.arg1==200) {
                        String response = (String) msg.obj;
                        try {
                            pa[0] = isPageNumber(response);

                        } catch (JSONException e) {
                            readError=readError+1;
                            e.printStackTrace();
                            Toast mytoast4=Toast.makeText(ReportActivity.this,"Code:0"+e.toString(),Toast.LENGTH_LONG);
                            mytoast4.setGravity(Gravity.CENTER,0,190);
                            mytoast4.show();
                        }
                    }
                    else{
                        readError=readError+1;
                        Toast mytoast2=Toast.makeText(ReportActivity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                        mytoast2.setGravity(Gravity.CENTER,0,190);
                        mytoast2.show();
                    }
                    countDownLatch.countDown();
                    break;
                case 12:
                    if (msg.arg1==200) {
                        String response = (String) msg.obj;
                        try {
                            pa[1] = isPageNumber(response);
                        } catch (JSONException e) {
                            readError=readError+1;
                            e.printStackTrace();
                            Toast mytoast5=Toast.makeText(ReportActivity.this,"Code:0"+e.toString(),Toast.LENGTH_LONG);
                            mytoast5.setGravity(Gravity.CENTER,0,190);
                            mytoast5.show();
                        }
                    }
                    else{
                        readError=readError+1;
                        Toast mytoast3=Toast.makeText(ReportActivity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                        mytoast3.setGravity(Gravity.CENTER,0,190);
                        mytoast3.show();
                    }
                    countDownLatch.countDown();
                    break;
                case 13:
                    if (msg.arg1==200) {
                        String response = (String) msg.obj;
                        try {
                            parseJSONWITHGSON1(response);
                        } catch (JSONException e) {
                            readError=readError+1;
                            e.printStackTrace();
                            Toast mytoast6=Toast.makeText(ReportActivity.this,"Code:0"+e.toString(),Toast.LENGTH_LONG);
                            mytoast6.setGravity(Gravity.CENTER,0,190);
                            mytoast6.show();
                        }
                    }
                    else{
                        readError=readError+1;
                        Toast mytoast7=Toast.makeText(ReportActivity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                        mytoast7.setGravity(Gravity.CENTER,0,190);
                        mytoast7.show();
                    }
                    countDownLatch1.countDown();
                    break;
                case 14:
                    if (msg.arg1==200) {
                        String response = (String) msg.obj;
                        try {
                            parseJSONWITHGSON2(response);
                        } catch (JSONException e) {
                            readError=readError+1;
                            e.printStackTrace();
                            Toast mytoast8=Toast.makeText(ReportActivity.this,"Code:0"+e.toString(),Toast.LENGTH_LONG);
                            mytoast8.setGravity(Gravity.CENTER,0,190);
                            mytoast8.show();
                        }
                    }
                    else{
                        readError=readError+1;
                        Toast mytoast9=Toast.makeText(ReportActivity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                        mytoast9.setGravity(Gravity.CENTER,0,190);
                        mytoast9.show();
                    }
                    countDownLatch2.countDown();
                    break;





          //以下是SWITCH末尾
            }
        }
    };





    /******************************************/

    private void readRegValue(int deviceID,int regID,int reportPage,int valuetype,String reportQueryTime) {
        String webAddr = null;
        if (valuetype==0){
            if (reportQueryTime==null) {
                webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/regs/"+regID+"/history?page="+reportPage;
            }else{
                webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/regs/"+regID+"/history?"+reportQueryTime+"& page="+reportPage;

            }
        }
        if (valuetype==1){
            if (reportQueryTime==null){
                webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/regs/"+regID+"/history?page="+reportPage;

            }else{
                webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/regs/"+regID+"/history?"+reportQueryTime+"& page="+reportPage;
            }

        }


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




        HttpUtil.sendHttpRequest(webAddr, myApplication.getInstance().getPasbas64(), new HttpCallbackListener() {
            @Override
            public void onFinish(String response,int httpcode) {
                Message msg = Message.obtain();
                msg.what = SEND_REQUEST;
                msg.obj = response;
                msg.arg1=httpcode;

                handler.sendMessage(msg);

            }

            @Override
            public void onError(int httpcode,String httpmessage) {
                Message msg = Message.obtain();
                msg.what = SEND_REQUEST_ERR;
                msg.obj = httpmessage;
                msg.arg1=httpcode;

                handler.sendMessage(msg);
            }
        });

    }


    /**
     *
     * test
     * test
     */
    private void readRegValue1(int deviceID, int regID, int reportPage, int valuetype, String reportQueryTime, final int rcode) {
        String webAddr = null;
        if (valuetype==0){
            if (reportQueryTime==null) {
                webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/regs/"+regID+"/history?page="+reportPage;
            }else{
                webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/regs/"+regID+"/history?"+reportQueryTime+"& page="+reportPage;

            }
        }
        if (valuetype==1){
            if (reportQueryTime==null){
                webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/regs/"+regID+"/history?page="+reportPage;

            }else{
                webAddr = "https://api.diacloudsolutions.com.cn/devices/" + deviceID + "/regs/"+regID+"/history?"+reportQueryTime+"& page="+reportPage;
            }

        }


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




        HttpUtilReponseCode.sendHttpRequest(webAddr, myApplication.getInstance().getPasbas64(), new HttpCallbackListenerReponsCode() {

            @Override
            public void onFinish(String response, int httpcode, int reponsecode) {
                Message msg = Message.obtain();
                msg.what = reponsecode+rcode;
                msg.obj = response;
                msg.arg1=httpcode;
                handler.sendMessage(msg);


            }

            @Override
            public void onError(int httpcode, String httpmessage, int reponsecode) {
                Message msg = Message.obtain();
                msg.what = reponsecode+rcode;
                msg.obj = httpmessage;
                msg.arg1=httpcode;
                handler.sendMessage(msg);
            }
        });

    }









    /************************************************/

    private void parseJSONWITHGSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组
        JSONObject jsonArray1=jsonObject.getJSONObject("paging");
        Gson gson = new Gson();
        dataBeansReport = gson.fromJson(jsonArray.toString(), new TypeToken<List<List<String>>>() {
        }.getType());
        RegValueHandle(dataBeansReport);
        reportPage.setText(null);
        reportPage.setHint(jsonArray1.getString("page"));
        reportToatlPage.setText(jsonArray1.getString("pageCount"));
        if (Integer.parseInt((String) reportToatlPage.getText())==0){
            reportPage.setHint("0");
            Toast mytoast=Toast.makeText(ReportActivity.this,"在查询日期范围无数据！",Toast.LENGTH_LONG);
            mytoast.setGravity(Gravity.CENTER,0,190);
            mytoast.show();
        }
        else{
            reportPage.setHint(jsonArray1.getString("page"));
        }
       listView.setAdapter(null);
       DXAdapterReport = new DXDeviceReportAdapter((LinkedList<DX_Device_Report>) mDataReport, mContext);
        listView.setAdapter(DXAdapterReport);


    }



    private void parseJSONWITHGSON1(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组
        JSONObject jsonArray1=jsonObject.getJSONObject("paging");
        Gson gson = new Gson();
        dataBeansReport = gson.fromJson(jsonArray.toString(), new TypeToken<List<List<String>>>() {
        }.getType());
        RegValueHandle(dataBeansReport);
    }

    private void parseJSONWITHGSON2(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组
        Gson gson = new Gson();
        dataBeansReport2 = gson.fromJson(jsonArray.toString(), new TypeToken<List<List<String>>>() {
        }.getType());
        RegValueHandle1(dataBeansReport2);
    }

    /**
     *
     * @param jsonData
     * @return int 查询数据的页数
     * @throws JSONException
     */
    private int isPageNumber(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONObject jsonArray1=jsonObject.getJSONObject("paging");
        return  Integer.parseInt(jsonArray1.getString("pageCount"));
    }




    private  void RegValueHandle(List<List<String>> RegValue){

        for(int i=0;i<RegValue.size();i++){
            mDataReport.add(new DX_Device_Report(RegValue.get(i).get(0),Integer.parseInt(RegValue.get(i).get(3))));
        }
    }

    private  void RegValueHandle1(List<List<String>> RegValue){

        for(int i=0;i<RegValue.size();i++){
            mDataReport1.add(new DX_Device_Report(RegValue.get(i).get(0),Integer.parseInt(RegValue.get(i).get(3))));
        }
    }

    @Override
    public void onResume() {
        if(myApplication.getInstance().getQuerytime()!=null) {
            reportQueryTimeShow = "日期范围:"+myApplication.getInstance().getQuerytime().substring(5, 15) + "至" +
                    myApplication.getInstance().getQuerytime().substring(30, 40);
            report_querytext.setText(reportQueryTimeShow);
        }
        //listView.setAdapter(null);
        super.onResume();
    }

    //判断是否为整型数
    private boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        //Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        Pattern pattern = Pattern.compile("^-?[0-9]\\d*$");
        return pattern.matcher(str).matches();
    }
    /******************************************/


    private void watchdog(Handler handler,Runnable runnable,final long watchtime, final watchdogCallbackListener listener){
        handler.postDelayed(runnable=new Runnable(){
            public void run() {


                if (listener != null) {
                    listener.onWatchDogFinish(watchtime, "无网络或服务器无响应");
                }


            }
        }, watchtime);
    }


    private void RemoveWatchDog(Handler handler,Runnable runnable){
        handler.removeCallbacksAndMessages(runnable);
    }


}