package zzw.velo;



import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import zzw.velo.R;

//import com.example.SmartCup.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @author yangyu
 *	功能描述：自定义TabHost
 */
public class MainTabActivity extends FragmentActivity implements OnCheckedChangeListener{
    //定义FragmentTabHost对象

    private final String BLUENAME = "SmartCup"; // 蓝牙称名称
    private final String BLUEPWD = "2333"; // 蓝牙设备的配对密码

    private static String ACTION_PAIRING_REQUEST =
            "android.bluetooth.device.action.PAIRING_REQUEST"; // 蓝牙常量
    private static final String MyUUID =
            "00001101-0000-1000-8000-00805F9B34FB"; // 蓝牙串口的UUID

    private BluetoothAdapter btAda; // 蓝牙设备的适配器对象
    private BluetoothDevice btDev; // 蓝牙设备的驱动
    private BluetoothSocket btSocket; // 蓝牙Socket

    private Set<BluetoothDevice> devices; // 获得已配对的蓝牙设备
    private String blueAddress = ""; // 所需蓝牙设备地址

    private boolean isBind;	//指定设备是否已绑定
    private TextView tvSound;//静态文本显示

    public static boolean connected = false;

    private DBHelper1 helper1;
    private DBHelper2 helper2;
    public final static int num = 3 ;
    private Fragment fragment_1;
    private Fragment fragment_2;
    private Fragment fragment_3;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private RadioGroup radioGroup;
    private Fragment lastshowFragment;
    public static String WEIGHT="0";
    public static String TEMPERATURE="0";
    public static final String TAG = "MainTabActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup1);
        ((RadioButton)radioGroup.findViewById(R.id.radio0)).setChecked(true);

        transaction = fragmentManager.beginTransaction();
        fragment_1=new FragmentPage1();
        fragment_2=new FragmentPage2();
        fragment_3=new FragmentPage3();
        lastshowFragment=fragment_1;
        transaction.add(R.id.content, fragment_1);
        transaction.add(R.id.content, fragment_2);
        transaction.add(R.id.content, fragment_3);
        transaction.show(fragment_1);
        transaction.hide(fragment_2);
        transaction.hide(fragment_3);

        transaction.commit();
        Button mButton_bluetooth=(Button)findViewById(R.id.button_bluetooth);
        mButton_bluetooth.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                findBondedBluetooth();
                if(isBind){//已绑定
                    if(btDev.getBondState() == BluetoothDevice.BOND_BONDED){
                        Toast.makeText(getApplicationContext(), "已绑定,正在获取数据...", 3000).show();
                        CDialog.showDialog(MainTabActivity.this, R.string.loading);

                        connBlue(btDev);


                        readData data=new readData();
                        Log.e("1","12");
                        data.start();

                    }
                }else{
                    //未绑定,搜索附近蓝牙设备
                    //进行配对
                    findRoundBlueTooth();
                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        transaction = fragmentManager.beginTransaction();
                        if(lastshowFragment!=null){
                            transaction.hide(lastshowFragment);}
                        if(fragment_1==null){
                            fragment_1=new FragmentPage1();
                            transaction.add(R.id.content, fragment_1);
                        }

                        transaction.show(fragment_1);
                        lastshowFragment=fragment_1;
                        transaction.commit();
                        break;
                    case R.id.radio1:
                        transaction = fragmentManager.beginTransaction();
                        if(lastshowFragment!=null){
                            transaction.hide(lastshowFragment);}
                        if(fragment_2==null){
                            fragment_2=new FragmentPage2();
                            transaction.add(R.id.content, fragment_2);}
                        transaction.show(fragment_2);


                        lastshowFragment=fragment_2;
                        transaction.commit();
                        break;
                    case R.id.radio2:
                        transaction = fragmentManager.beginTransaction();
                        if(lastshowFragment!=null){
                            transaction.hide(lastshowFragment);}
                        if(fragment_3==null){
                            fragment_3=new FragmentPage3();
                            transaction.add(R.id.content, fragment_3);
                        }
                        transaction.show(fragment_3);
                        lastshowFragment=fragment_3;
                        transaction.commit();
                        break;
                }

            }
        });
        openBlueTooth();
    }

    private void openBlueTooth() {
        // 获取当前本机的蓝牙适配器
        btAda = BluetoothAdapter.getDefaultAdapter();
        // 如果 bluetoothAdapter 为null,当前设备的蓝牙不可用;反之,可用
        if (btAda != null) {
            setBluetoothReceiver(); // 注册广播
            // 打开蓝牙
            if (!btAda.isEnabled()) {
                btAda.enable();
                Toast.makeText(getApplicationContext(), R.string.opening,
                        Toast.LENGTH_LONG).show();
                //Toast类负责管理少量信息的提示,此处使用Toast。makeText方法构造，也可直接new一个，具体内容去RES-values-string中查看和修改
            } else {
                Toast.makeText(getApplicationContext(), R.string.opened,//提示蓝牙开启成功
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.nobluetooth,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 注册蓝牙事件广播监听
     */
    public void setBluetoothReceiver() {
        IntentFilter intent = new IntentFilter();
        //添加intent的监视动作。用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(ACTION_PAIRING_REQUEST);
        intent.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.addAction(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(searchDevices, intent);
    }




    /**
     * 蓝牙广播的处理
     */
    BroadcastReceiver searchDevices = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获得扫描到的远程蓝牙设备
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){	//搜索设备
                //搜索附近的可见
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String blueName=device.getName();
                if(blueName!=null&&BLUENAME.equals(blueName)){
                    //找到指定蓝牙设备,执行配对操作
                    //取消继续搜索
                    btAda.cancelDiscovery();
                    btDev = btAda.getRemoteDevice(device.getAddress());
                    // 判断蓝牙连接状态如果没匹配就匹配
                    if (btDev.getBondState() != BluetoothDevice.BOND_BONDED) {
                        try {
                            privateUtils.createBond(btDev.getClass(), btDev);
                            privateUtils.setPin(btDev.getClass(), btDev, BLUEPWD);
                            privateUtils.cancelPairingUserInput(btDev.getClass(), btDev);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainTabActivity.this, R.string.parError,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    btDev=device;
                    connBlue(btDev);
                    //开始处理数据请求和获取
                    readData data=new readData();
                    data.start();

                }else{
                    //未找到指定的蓝牙设备
                    System.out.println(R.string.searchError);
                    //CDialog.showDialog(TestBluetoothActivity.this,R.string.searchError);
                    Toast.makeText(getApplicationContext(), R.string.searchError, Toast.LENGTH_LONG).show();
                }
            }
        }
    };


    /**
     * 搜索已经绑定的蓝牙设备
     */
    private void findBondedBluetooth() {
        isBind=false;
        devices = btAda.getBondedDevices();
        if (devices.size() > 0) { // 如果数量不等于0,就是有已绑定的设备
            for (Iterator<BluetoothDevice> it = devices.iterator(); it
                    .hasNext();) {
                BluetoothDevice device = (BluetoothDevice) it.next();
                // 打印出远程蓝牙设备的物理地址
                // System.out.println(device.getAddress());
                // System.out.println(device.getName());
                if (BLUENAME.equals(device.getName())) {
                    isBind=true;
                    btDev = device;
                    System.out.println(">>>>>>>findBondedBluetooth>>>"+btDev.getName());
                    Log.d("MainActivity", "findBondedBluetooth()>>>找到指定的蓝牙设备,已是配对状态");//log.d为debug下的测试信息输出，具体参看保存网页中log用法详解
                } else {
                    Log.d("MainActivity", "findBondedBluetooth()>>>未找到指定的蓝牙设备被绑定");
                }
            }
        } else { // 没有绑定设备,开始搜索附近的蓝牙设备
            // System.out.println("还没有已配对的远程蓝牙设备！");
            Log.d("MainActivity", "findBondedBluetooth()>>>当前设备无蓝牙设备绑定");
        }
    }


    /**
     * 开启搜索附近的蓝牙设备
     */
    private void findRoundBlueTooth() {

        // 收索蓝牙
        if (btAda.isEnabled()) {
            CDialog.showDialog(this, R.string.searching);
            btAda.startDiscovery();
        } else {
            Toast.makeText(getApplicationContext(), R.string.ConfirmBTisOpen,
                    Toast.LENGTH_LONG);
        }
    }

    private void connBlue(BluetoothDevice btDev) {

        UUID uuid = UUID.fromString(MyUUID);//获取已经定义好的蓝牙串口UUID
        try {
            btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
            Log.d("BlueToothTestActivity", "开始连接...");
            btSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this).
                    setMessage("真的要走么T^T")
                    .setPositiveButton("拜拜(⊙v⊙)", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            System.exit(0);
                        }
                    })
                    .setNegativeButton("留下了(๑´ㅂ`๑)", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.cancel();
                        }
                    }).show();
        }
        return true;
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO 自动生成的方法存根

    }
    /**
     * 请求、获取数据
     * @author Archer
     */
    class readData extends Thread{
        public void run()
        {

            try {
                InputStream in=null;

                int flag=0;//标志位判断数据类型
                int flag_wei=0;//标志位判断是否写下水重
                int flag_tem=0;//标志位判断是否写下温度
                final int count=10;//设置每收10个数存储下水重和水温

                while(true)
                {
                    in=btSocket.getInputStream();
                    while(true)
                    {
                        int tmpData=in.read();
                        if(tmpData==-1)
                            break;
                        else if(tmpData==255)
                        {
                            flag=1;//提示下一个输入的是水温
                            //			System.out.println("flag"+flag);
                        }
                        //	System.out.println("tmpData  "+tmpData);

                        else{
                            if(flag==1)//如果输入的是温度
                            {
                                flag_tem++;
                                if(flag_tem==count)
                                {
                                    TEMPERATURE="";
                                    //	System.out.println("tmpdata"+tmpData);
                                    TEMPERATURE=Integer.toString(tmpData);
                                    flag_tem=0;//
                                }
                                flag=2;//下一个输入的是水量
                                //			System.out.println("flag"+flag);
                            }
                            else if(flag==2)
                            {
                                flag_wei++;
                                if(flag_wei==count)
                                {
                                    WEIGHT="";
                                    //		System.out.println("tmpdata"+tmpData);
                                    int tmpweight=decode(tmpData);
                                    WEIGHT=Integer.toString(tmpweight);
                                    flag_wei=0;//计数清零
                                    //		helper1.update(1, WEIGHT, TEMPERATURE);
                                    //		helper2.insert(WEIGHT, TEMPERATURE);//数据库插入
                                    //		System.out.println("TEMPERATURE"+TEMPERATURE);
                                    //		System.out.println("WEIGHT"+WEIGHT);
                                }
                                flag=0;//下一个输入的是全1；
                                //		System.out.println("flag"+flag);
                            }


                        }


                    }
                    Thread.sleep(1000);


                }
            } catch (IOException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }

        }

        private int decode(int tmpweight) {
            // TODO 自动生成的方法存根
            int weight;
            switch(tmpweight){

            }
            return 0;
        }
    }


}

