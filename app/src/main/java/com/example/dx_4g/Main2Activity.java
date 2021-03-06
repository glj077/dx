package com.example.dx_4g;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.DXDeviceAdapter;
import com.example.dx_4g.funclass.DX_4G;
import com.example.dx_4g.funclass.DX_Device;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.myApplication;
import com.example.dx_4g.funclass.watchdogCallbackListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("RedundantCast")
public class Main2Activity extends BaseActivity implements View.OnClickListener {

    private static final int SEND_REQUEST = 1;
    private static final int SEND_REQUEST_ERR=2;
    private static final int WATCHDOG_FINISH=3;
    private myApplication application;
    private SearchView searchView;
    private DXDeviceAdapter DXAdapter;
    private DXDeviceAdapter DXAdapter_online;
    private DXDeviceAdapter DXAdapter_query;
    private LinkedList<DX_Device> mData;
    private LinkedList<DX_Device> mData_online;
    private List<DX_4G.DataBean> dataBeans;
    private LinkedList<DX_Device> queryData;
    private  ListView list1;
    private  Context mContext;
    private int online;
    private int offline;
    private TextView dx_count;
    private TextView dx_show;
    private int search_view_dx_show;
    private  SwipeRefreshLayout mSwipe;
    private Runnable runnable;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SEND_REQUEST) {
                if (msg.arg1 == 200) {
                    String response = (String) msg.obj;

                    try {
                        RemoveWatchDog(handler,runnable);
                        parseJSONWITHGSON(response);
                        mSwipe.setRefreshing(false);


                    } catch (JSONException e) {
                        RemoveWatchDog(handler,runnable);
                        e.printStackTrace();
                        mSwipe.setRefreshing(false);

                    }
                }
                else{
                    RemoveWatchDog(handler,runnable);
                    mSwipe.setRefreshing(false);
                    Toast mytoast=Toast.makeText(Main2Activity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                    mytoast.setGravity(Gravity.CENTER,0,190);
                    mytoast.show();

                }

            }

            if (msg.what==SEND_REQUEST_ERR){
                RemoveWatchDog(handler,runnable);
                mSwipe.setRefreshing(false);
                Toast.makeText(Main2Activity.this,"code:"+msg.arg1+" "+(String)msg.obj,Toast.LENGTH_SHORT).show();


            }

            if(msg.what==WATCHDOG_FINISH){
                RemoveWatchDog(handler,runnable);
                Toast mytoast=Toast.makeText(Main2Activity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                mytoast.setGravity(Gravity.CENTER,0,190);
                mytoast.show();
            }

        }
    };



    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.activity_main2;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {

         dx_show=(TextView)findViewById(R.id.dx_show);
         dx_count=(TextView)findViewById(R.id.dx_count);
         dx_show.setOnClickListener(this);
         dx_count.setOnClickListener(this);

         list1 = (ListView) findViewById(R.id.dxlist);
         mContext = this;

         mSwipe=(SwipeRefreshLayout) findViewById(R.id.swipelayout);
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
                list1.setAdapter(null);
                initData();

                watchdog(handler,runnable,10000, new watchdogCallbackListener() {
                    @Override
                    public void onWatchDogFinish(long code, String message) {
                        Message msg = Message.obtain();
                        msg.what = WATCHDOG_FINISH;
                        msg.arg1 =(int)(code);
                        msg.obj=message;
                        handler.sendMessage(msg);
                    }

                });





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


        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1)).setTitle("设备");
        // 以下动作让标题居中显地
        TextView textView = (TextView) ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1)).getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);
        //((androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar1)).setSubtitle("副标题");
//        //((Toolbar)findViewById(R.id.toolbar)).setLogo(R.mipmap.ic_launcher);//设置标题LOGO
        // ((androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar1)).setSubtitleTextColor(Color.WHITE);//设置副标题文本颜色
        //      ((androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar1)).setNavigationIcon(R.mipmap.ic_launcher);//设置导航图标

        //搜索栏设置
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);//增加提交按钮
       // TextView textView1=searchView.findViewById(android.support.
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);

//获取到searchview TextView的控件
        TextView textView1 = (TextView) searchView.findViewById(id);
//设置字体大小为14sp
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);//14sp
        textView1.setTextColor(getResources().getColor(R.color.colorwhite));
        textView1.setHintTextColor(getResources().getColor(R.color.colorwhite));
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);//让键盘回车设置成搜索功能


        /******
         * 以下功能可以在布局文件中实现
         */
//        int search_mag_icon_id = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
//        ImageView mSearchViewIcon = (ImageView) searchView.findViewById(search_mag_icon_id);
//        mSearchViewIcon.setImageResource(R.drawable.search);
//
//        int search_close_icon_id = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
//        ImageView mSearchCloseIcon = (ImageView) searchView.findViewById(search_close_icon_id);
//        mSearchCloseIcon.setImageResource(R.drawable.close);






        //注册SearchView监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String query_name="设备名称:"+query;
                queryData = new LinkedList<DX_Device>();

                if (query.equals(null)) {
                    Toast.makeText(getApplication(), "请输入查询条件！", Toast.LENGTH_SHORT).show();
                } else {
                   for (int i=0;i<DXAdapter.getCount();i++){
                       if (query_name.equals(mData.get(i).getDxName())){
                           queryData.add(new DX_Device(mData.get(i).getDxName(),mData.get(i).getDxIp(),R.mipmap.earth_foreground,mData.get(i).getDxStatus(),mData.get(i).getDxID()));

                       }
                   }

                    DXAdapter_query = new DXDeviceAdapter((LinkedList<DX_Device>) queryData, mContext);
                    list1.setAdapter(null);
                    list1.setAdapter(DXAdapter_query);
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty()){
                    list1.setAdapter(null);
                    list1.setAdapter(DXAdapter);
                    searchView.clearFocus();
                }
                return true;
            }
        });


        //标题栏中的menu中的按钮监听
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1)).setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_exit:
                        ActivityCollector.killAllActivity();
                        break;
                    case R.id.action_test:
                        Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
                return true;
            }
        });

     //***********************************
     //为listview没册事件监听
     //************************************
     list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             String deviceName=null;
             int deviceID=0;
             if (search_view_dx_show==0){
                 deviceName=dataBeans.get(position).getName();
                  deviceID=dataBeans.get(position).getId();
             }
             else{
                     deviceName=mData_online.get(position).getDxName();
                     deviceID=mData_online.get(position).getDxID();
             }
             myApplication.getInstance().setDeviceID(deviceID);
             myApplication.getInstance().setDeviceName(deviceName);
             Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
            intent.putExtra("deviceID",deviceID);
             intent.putExtra("devieName",deviceName);

             startActivity(intent);
             finish();
         }
     });







    }
    //***********************************//

    @Override
    protected void initData()  {
        myApplication.getInstance().setQuerytime(null);
        myApplication.getInstance().setDeviceID(0);

        mData_online = new LinkedList<DX_Device>();
        online=0;
        offline=0;
        search_view_dx_show=0;

        watchdog(handler,runnable,15000, new watchdogCallbackListener() {
            @Override
            public void onWatchDogFinish(long code, String message) {
                Message msg = Message.obtain();
                msg.what = WATCHDOG_FINISH;
                msg.arg1 =(int)(code);
                msg.obj=message;
                handler.sendMessage(msg);
            }

        });




        HttpUtil.sendHttpRequest("https://api.diacloudsolutions.com.cn/devices", myApplication.getInstance().getPasbas64(), new HttpCallbackListener() {

            @Override
            public void onFinish(String response,int httpcode) {
                    Message msg = Message.obtain();
                    msg.what = SEND_REQUEST;
                    msg.obj = response;
                    msg.arg1 = httpcode;
                    handler.sendMessage(msg);

            }

            @Override
            public void onError(int httpcode,String httpmessage) {
                Message msg = Message.obtain();
                msg.what = SEND_REQUEST_ERR;
                msg.obj = (Object)httpmessage;
                msg.arg1=httpcode;
                handler.sendMessage(msg);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
       if(search_view_dx_show==0){
           search_view_dx_show=1;
       }
       else{search_view_dx_show=0;}
       switch (v.getId()){
           case R.id.dx_show:
           case R.id.dx_count:
               if (search_view_dx_show==1){
               dx_show.setText(this.getString(R.string.dx_show_online));
               dx_count.setText(String.valueOf(online));
               if (!mData_online.isEmpty()){
                mData_online.clear();}

                   for (int i = 0; i < dataBeans.size(); i++) {

                       if (dataBeans.get(i).getOnline()==1){
                           mData_online.add(new DX_Device( dataBeans.get(i).getName(), "IP地址:" + dataBeans.get(i).getIp(), R.mipmap.earth_foreground,dataBeans.get(i).getOnline(),dataBeans.get(i).getId()));
                       }

                   }

               DXAdapter_online = new DXDeviceAdapter((LinkedList<DX_Device>) mData_online, mContext);
               list1.setAdapter(null);
               list1.setAdapter(DXAdapter_online);
               }
               else{
                   dx_show.setText(this.getString(R.string.dx_show_all));
                   dx_count.setText(String.valueOf(offline));
                   list1.setAdapter(null);
                   list1.setAdapter(DXAdapter);
               }
               break;
           //case
           default:
               break;
       }
    }

    private void parseJSONWITHGSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组

        Gson gson = new Gson();
         dataBeans = gson.fromJson(jsonArray.toString(), new TypeToken<List<DX_4G.DataBean>>() {
        }.getType());

         mData = new LinkedList<DX_Device>();
        for (int i = 0; i < dataBeans.size(); i++) {
            mData.add(new DX_Device("设备名称:" + dataBeans.get(i).getName(), "IP地址:" + dataBeans.get(i).getIp(), R.mipmap.earth_foreground,dataBeans.get(i).getOnline(),dataBeans.get(i).getId()));
            if (dataBeans.get(i).getOnline()==1){
                online=online+1;

            }

        }
         DXAdapter = new DXDeviceAdapter((LinkedList<DX_Device>) mData, mContext);
        offline=dataBeans.size();
        list1.setAdapter(DXAdapter);
        dx_count.setText(String.valueOf(offline));

    }

    @Override
    protected void onResume() {
        searchView.clearFocus();
        super.onResume();
    }


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


    private static float intToFloat(int HValue,int LValue){
        String Hhex= Integer.toHexString(HValue);
        String Lhex=Integer.toHexString(LValue);
        for(int i=Lhex.length();i<4;i++){
            Lhex="0"+Lhex;
        }
        String hex=Hhex+Lhex;
        return Float.intBitsToFloat(new BigInteger(hex, 16).intValue());
    }

}


