package com.example.dx_4g;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.DXDeviceRegAdapter;
import com.example.dx_4g.funclass.DX_4G_Reg;
import com.example.dx_4g.funclass.DX_Device_Reg;
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

public class tab1 extends Fragment {
   private SearchView searchView;
   private ListView listView;
   private  int regPosition;
   private  int regsign;
    private static final int SEND_REQUEST = 3;
    private static final int SEND_REQUEST_ERR=4;
    private static final int WATCHDOG_FINISH=5;
    private LinkedList<DX_Device_Reg>  queryData;
    private LinkedList<DX_Device_Reg> mDataReg;
    private List<DX_4G_Reg.DataBean> dataBeansReg;
    private DXDeviceRegAdapter DXAdapterReg;
    private ListView list2;
    private Context mContext;
    private TextView regCount;
    private SwipeRefreshLayout mSwipe;
    private int search_sign;
    private ProgressBar progressBar;
    private int varNumber;
    private Runnable runnable;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.tab1,container,false);
        listView=(ListView)view.findViewById(R.id.reg_list);
        regCount=(TextView)view.findViewById(R.id.reg_count);
        search_sign=0;

        //listview控件点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(view.getContext(),Regedit.class);
                if (search_sign==0) {
                    getRegValue_type(position, mDataReg);
                    intent.putExtra("regaddr",regPosition);
                    intent.putExtra("regaddrtype",regsign);
                    intent.putExtra("regname",mDataReg.get(position).getRegName());
                    intent.putExtra("regvalue",mDataReg.get(position).getRegValue());
                    myApplication.getInstance().setRegName(mDataReg.get(position).getRegName());
                }else{
                    getRegValue_type(position, queryData);
                    intent.putExtra("regaddr",regPosition);
                    intent.putExtra("regaddrtype",regsign);
                    intent.putExtra("regname",queryData.get(position).getRegName());
                    intent.putExtra("regvalue",queryData.get(position).getRegValue());
                    myApplication.getInstance().setRegName(queryData.get(position).getRegName());
                }
                startActivity(intent);
                //getActivity().onBackPressed();//关闭fragment tab1

            }
        });

        androidx.appcompat.widget.Toolbar toolbar =(Toolbar)view.findViewById(R.id.toolbar2);
        toolbar.setTitle("设备:"+myApplication.getInstance().getDeviceName());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title

        // 以下动作让标题居中显地
        TextView textView = (TextView) toolbar.getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);
        toolbar.setNavigationIcon(R.drawable.rego);//设置导航图标

        searchView = (SearchView) view.findViewById(R.id.search_view1);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);//增加提交按钮
        // TextView textView1=searchView.findViewById(android.support.
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
//获取到searchview TextView的控件
        TextView textView1 = (TextView) searchView.findViewById(id);
        textView1.setTextColor(getResources().getColor(R.color.colorwhite));
        textView1.setHintTextColor(getResources().getColor(R.color.colorwhite));
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);//让键盘回车设置成搜索功能
//设置字体大小为14sp
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);//14sp




        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Main2Activity.class);
                startActivity(intent);
            }
        });


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
       /*********************************************/
       //页面初始数据加载
        int devicdID= myApplication.getInstance().getDeviceID();

            readRegValue(devicdID);
            progressBar=getActivity().findViewById(R.id.progressBar3);
            progressBar.setVisibility(View.VISIBLE);
       /********************************************/

        mSwipe=(SwipeRefreshLayout)view.findViewById(R.id.swipelayout);
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
                int devicdID= myApplication.getInstance().getDeviceID();


                readRegValue(devicdID);
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


        //注册搜索事件
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        queryData = new LinkedList<DX_Device_Reg>();
                        if (query.equals(null)) {
                            Toast.makeText(getContext(), "请输入查询条件！", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i=0;i<DXAdapterReg.getCount();i++){
                                if (query.equals(mDataReg.get(i).getRegName())){
                                    queryData.add(new DX_Device_Reg(mDataReg.get(i).getRegName(),mDataReg.get(i).getRegValue(),mDataReg.get(i).getRegAddr(),0,0));
                                }
                            }
                            if (!queryData.isEmpty()){
                            DXDeviceRegAdapter   DXARegdapter_query = new DXDeviceRegAdapter((LinkedList<DX_Device_Reg>) queryData, mContext);
                            listView.setAdapter(null);
                            listView.setAdapter(DXARegdapter_query);
                            search_sign=1;
                            }else{
                                Toast mytoast=Toast.makeText(getContext(),"未查到相关数据！",Toast.LENGTH_LONG);
                                mytoast.setGravity(Gravity.CENTER,0,190);
                                mytoast.show();
                            }

                            //输入法处理
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            boolean isOpen=imm.isActive();
                            if (isOpen){
                                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                            //让查询控件失去焦点
                            searchView.clearFocus();
                        }

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (newText.isEmpty()){
                            listView.setAdapter(null);
                            listView.setAdapter(DXAdapterReg);
                            search_sign=0;
                            searchView.clearFocus();
                        }
                        return true;
                    }
                });


        return view;
    }

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
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        RemoveWatchDog(handler,runnable);
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                        Toast mytoast = Toast.makeText(getContext(), "Code0:" + " Message:" + e.toString(), Toast.LENGTH_LONG);
                        mytoast.setGravity(Gravity.CENTER, 0, 190);
                        mytoast.show();
                    }
                }
                else{
                    RemoveWatchDog(handler,runnable);
                    mSwipe.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);

                    Toast mytoast=Toast.makeText(getContext(),"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                    mytoast.setGravity(Gravity.CENTER,0,190);
                    mytoast.show();

                }

            }
            if (msg.what==SEND_REQUEST_ERR){
                RemoveWatchDog(handler,runnable);
                mSwipe.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                Toast mytoast=Toast.makeText(getContext(),"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                mytoast.setGravity(Gravity.CENTER,0,190);
                mytoast.show();
            }

            if(msg.what==WATCHDOG_FINISH){
                progressBar.setVisibility(View.GONE);
                mSwipe.setRefreshing(false);
                if (getContext()!=null){
                Toast mytoast=Toast.makeText(getContext(),"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                mytoast.setGravity(Gravity.CENTER,0,190);
                mytoast.show();}
                RemoveWatchDog(handler,runnable);
            }
        }
    };

    private void readRegValue(int deviceID) {

        watchdog(handler,runnable,20000, new watchdogCallbackListener() {
            @Override
            public void onWatchDogFinish(long code, String message) {
                Message msg = Message.obtain();
                msg.what = WATCHDOG_FINISH;
                msg.arg1 =(int)(code);
                msg.obj=message;
                handler.sendMessage(msg);
            }

        });



        String webAddr="https://api.diacloudsolutions.com.cn/devices/"+deviceID+"/regs";
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

        Gson gson = new Gson();
        dataBeansReg = gson.fromJson(jsonArray.toString(), new TypeToken<List<DX_4G_Reg.DataBean>>() {
        }.getType());
        RegValueHandle(dataBeansReg);
        regCount.setText(String.valueOf(varNumber));
        mContext = this.getContext();
        DXAdapterReg = new DXDeviceRegAdapter((LinkedList<DX_Device_Reg>) mDataReg, mContext);

        listView.setAdapter(DXAdapterReg);

    }

    private  void RegValueHandle(List<DX_4G_Reg.DataBean> RegValue){
        mDataReg = new LinkedList<DX_Device_Reg>();
        varNumber=0;
        for(int i=0;i<RegValue.size();i++){
            varNumber=varNumber+1;
            if(i==RegValue.size()-1){
                mDataReg.add(new DX_Device_Reg(RegValue.get(i).getName(),String.valueOf(RegValue.get(i).getValue()),RegValue.get(i).getAddr(),0,0));
                break;
            }
            if ((RegValue.get(i).getTemplate()!=null)&&(RegValue.get(i+1).getTemplate()==null)){
                float dx_regvalue=intToFloat(RegValue.get(i+1).getValue(),RegValue.get(i).getValue());
                mDataReg.add(new DX_Device_Reg(RegValue.get(i).getName(),String.valueOf(dx_regvalue),RegValue.get(i).getAddr(),0,1));
                i=i+1;
                continue;
            }

            if ((RegValue.get(i).getTemplate()!=null)&&(RegValue.get(i+1).getTemplate()!=null)) {
                mDataReg.add(new DX_Device_Reg(RegValue.get(i).getName(), String.valueOf(RegValue.get(i).getValue()),RegValue.get(i).getAddr(), 0,0));

            }


        }

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


   private void getRegValue_type(int position,LinkedList<DX_Device_Reg> mDataReg){
         regPosition=mDataReg.get(position).getRegAddr();
         regsign=mDataReg.get(position).getRegsign();

   }

    @Override
    public void onResume() {
        searchView.clearFocus();
        search_sign=0;
        varNumber=0;
        listView.setAdapter(null);
        progressBar.setVisibility(View.VISIBLE);
        int devicdID= myApplication.getInstance().getDeviceID();
        readRegValue(devicdID);
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        varNumber=0;
        super.onCreate(savedInstanceState);
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


}
