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
import android.util.TypedValue;
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
import android.widget.SearchView;
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
import com.example.dx_4g.funclass.DXDeviceRegAdapter;
import com.example.dx_4g.funclass.DX_4G_Alarm;
import com.example.dx_4g.funclass.DX_4G_Reg;
import com.example.dx_4g.funclass.DX_Device_Alarm;
import com.example.dx_4g.funclass.DX_Device_Reg;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.myApplication;
import com.example.dx_4g.funclass.myToast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class tab3 extends Fragment {

    private static final int SEND_REQUEST=5;
    private static final int SEND_REQUEST_ERR=6;
    private List<DX_4G_Alarm.DataBean> dataBeansAlarm;
    private LinkedList<DX_Device_Alarm> mDataAlarm;
    private EditText alarmPage;
    private TextView alarmToatlPage;
    private ListView listView;
    private Context mContext;
    private List<DX_4G_Alarm.PagingBean> dataPaginAlarm;
    private DXDeviceAlarmAdapter DXAdapterAlarm;
    private  SwipeRefreshLayout mSwipe;
    private ProgressBar progressBar;
    /****************************************/
    //消息处理
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == SEND_REQUEST) {

                String response = (String) msg.obj;

                if (msg.arg1==200) {
                    try {

                        parseJSONWITHGSON(response);
                        progressBar.setVisibility(View.GONE);
                        mSwipe.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }

                }else{
                    mSwipe.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    Toast mytoast=Toast.makeText(getContext(),"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                    mytoast.setGravity(Gravity.CENTER,0,190);
                    mytoast.show();
                }

            }
            if (msg.what==SEND_REQUEST_ERR){
                progressBar.setVisibility(View.GONE);
                Toast mytoast=Toast.makeText(getContext(),"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                mytoast.setGravity(Gravity.CENTER,0,190);
                mytoast.show();
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.tab3,container,false);
        alarmPage=(EditText)view.findViewById(R.id.alarm_edit);
        alarmToatlPage=(TextView)view.findViewById(R.id.alarmtotalpageshow);
        listView=(ListView)view.findViewById(R.id.alarm_list);
        ImageView alarm_next_page=(ImageView)view.findViewById(R.id.alarm_next_page);


        androidx.appcompat.widget.Toolbar toolbar =(Toolbar)view.findViewById(R.id.toolbar2);
        toolbar.setTitle("报警");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
        // 以下动作让标题居中显地
        TextView textView = (TextView) toolbar.getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);
        toolbar.setNavigationIcon(R.drawable.reexit);//设置导航图标

        SearchView searchView = (SearchView) view.findViewById(R.id.search_alarm);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);//增加提交按钮
        // TextView textView1=searchView.findViewById(android.support.
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
       //获取到searchview TextView的控件
        TextView textView1 = (TextView) searchView.findViewById(id);
       //设置字体大小为14sp
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);//18sp

        /***************************************/
        //导航按钮点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Main2Activity.class);
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
        int deviceID= myApplication.getInstance().getRegID();
        readRegValue(deviceID,1,null);
        progressBar=getActivity().findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);

        /***************************************/


        /***************************************/
        //下拉刷新
         mSwipe=(SwipeRefreshLayout)view.findViewById(R.id.swipelayout1);
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
                int devicdID= myApplication.getInstance().getRegID();
                readRegValue(devicdID,1,null);
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
     @SuppressLint("CutPasteId") final EditText alarm_page=(EditText)view.findViewById(R.id.alarm_edit);
     alarm_page.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {

         }

         @Override
         public void afterTextChanged(Editable s) {
             if (isInteger((String)s.toString())){
             int alarepage=Integer.parseInt((String)s.toString());
             int deviceID= myApplication.getInstance().getRegID();

             progressBar=getActivity().findViewById(R.id.progressBar3);
             progressBar.setVisibility(View.VISIBLE);
             readRegValue(deviceID,alarepage,null);
             alarm_page.clearFocus();
                 //输入法处理
                 InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                 boolean isOpen = imm.isActive();
                 if (isOpen) {
                     imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
             if (isInteger((String)alarm_page.getHint())){
                 int alarepage=Integer.parseInt((String)alarm_page.getHint())+1;
                alarm_page.setText(String.valueOf(alarepage));
             }

         }

     });

     /******************************************/


     /*****************************************/
     /**************搜索事件注册***************/
     SearchView alarm_search=(SearchView)view.findViewById(R.id.search_alarm);
     alarm_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {
             
             return true;
         }

         @Override
         public boolean onQueryTextChange(String newText) {
             return false;
         }
     });


     /****************************************/

        return view;
    }


    /******************************************/

    private void readRegValue(int deviceID,int alarmPage,String alarmQueryTime) {
           String webAddr = null;
        if ((alarmPage==1)&&(alarmQueryTime==null)) {
             webAddr = "https://api.diacloudsolutions.com/devices/" + deviceID + "/alarms";
        }
        if ((alarmPage!=1)&&(alarmQueryTime==null)){
             webAddr = "https://api.diacloudsolutions.com/devices/" + deviceID + "/alarms?page="+alarmPage;
        }

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

    private void parseJSONWITHGSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组
        JSONObject jsonArray1=jsonObject.getJSONObject("paging");
        Gson gson = new Gson();
        dataBeansAlarm = gson.fromJson(jsonArray.toString(), new TypeToken<List<DX_4G_Alarm.DataBean>>() {
        }.getType());
        RegValueHandle(dataBeansAlarm);
        mContext = this.getContext();
        alarmPage.setText(null);
        alarmPage.setHint(jsonArray1.getString("page"));
        alarmToatlPage.setText(jsonArray1.getString("pageCount"));
        DXAdapterAlarm = new DXDeviceAlarmAdapter((LinkedList<DX_Device_Alarm>) mDataAlarm, mContext);
        listView.setAdapter(DXAdapterAlarm);

    }

    private  void RegValueHandle(List<DX_4G_Alarm.DataBean> RegValue){
        mDataAlarm = new LinkedList<DX_Device_Alarm>();

        for(int i=0;i<RegValue.size();i++){
                mDataAlarm.add(new DX_Device_Alarm(dataBeansAlarm.get(i).getTitle(),dataBeansAlarm.get(i).getContent()));
        }
    }

    @Override
    public void onResume() {
        listView.setAdapter(null);
        int devicedID= myApplication.getInstance().getRegID();
        progressBar.setVisibility(View.VISIBLE);
        readRegValue(devicedID,1,null);
        super.onResume();
    }

    //判断是否为整型数
    private boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        //Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        Pattern pattern = Pattern.compile("^-?[1-9]\\d*$");
        return pattern.matcher(str).matches();
    }
    /******************************************/

}
