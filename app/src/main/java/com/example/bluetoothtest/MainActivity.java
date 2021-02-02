package com.example.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.glinks.lib.bt.BluetoothOperationCallback;
import cn.glinks.lib.bt.BluetoothOperator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "蓝牙" + MainActivity.class.getSimpleName();
    private static final String MAC = "98:D3:41:FD:35:FF";//更改这里的MAC

    // Bluetooth
    private boolean hasEnabled = false;
    private BluetoothOperator mBluetoothOp;
    private BluetoothAdapter mBluetoothAdapter;
    private TextView textView;

    private Thread b;
    private boolean bb=false,bbf=true,iskong=false;//第一次

    /**
     * 蓝牙连接成功回调和接受数据回调
     */
    private BluetoothOperationCallback callback = new BluetoothOperationCallback() {

        @Override
        public void onConnect(int err, String desc) {
            if (err == 0) {
                Log.d(TAG, "蓝牙连接成功");
                Toast.makeText(MainActivity.this, "连接成功,可以开始操作了", Toast.LENGTH_SHORT).show();
            }else{
                Log.d(TAG, "onConnect: "+err);
            }
        }

        @Override
        public void onDataRecv(int err, String desc, byte[] data, int len) {
            if (err == 0) {
                textView.setText(new String(data,0,len));
                Log.d(TAG, "\n 蓝牙接收数据:" + new String(data, 0, len));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //横屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mBluetoothOp = new BluetoothOperator(this);
        mBluetoothOp.registerCallback(callback);
        if (mBluetoothOp.isEnabled())
            hasEnabled = true;

        initbtclick();//初始化按钮wasd按钮单击监听，避障按钮线程

        textView=(TextView)findViewById(R.id.textView);


//        //初始化蓝牙适配器
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        //获取已经保存过的设备信息
//        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
//        if (devices.size() > 0) {
//            for (Iterator<BluetoothDevice> iterator = devices.iterator(); iterator.hasNext(); ) {
//                BluetoothDevice bluetoothDevice = (BluetoothDevice) iterator.next();
//                Log.d(TAG, "onCreate: " + bluetoothDevice.getName() + " " + bluetoothDevice.getAddress());
//            }
//        }


    }

    /**
     * 打开蓝牙
     */
    @Override
    protected void onResume() {
        super.onResume();
//打开蓝牙
        mBluetoothOp.enable();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//关闭蓝牙
        mBluetoothOp.unregisterCallback(callback);
        if (!hasEnabled)
            mBluetoothOp.disable();
        mBluetoothOp.destroy();
    }

    /**
     * 按钮单击事件监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan:{
                mBluetoothOp.connect(MAC);//连接蓝牙
                iskong=false;
                Log.d(TAG, "onClick: 连接蓝牙");
            }
                break;
            case R.id.xunji: {
                bb = false;
                mBluetoothOp.write("x".getBytes());
                iskong=false;
            }
            break;
            case R.id.bizhang: {
                bb=true;
                if (bbf) {
                    b.start();
                    bbf=false;
                }
                iskong=false;
            }
            break;
            case R.id.stop: {
                bb = false;
                mBluetoothOp.write("t".getBytes());
                iskong=false;
            }
            break;
        }
    }



    /**
     * 初始化按钮wasd按钮单击监听，避障按钮线程
     */
    void initbtclick() {
        b=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if (bb) {
                        try {
                            Thread.sleep(400);
                            mBluetoothOp.write("b".getBytes());
                        } catch (InterruptedException e) {
                            Log.d(TAG, "run: " + e.getLocalizedMessage());
                        }
                    } else {//非运行状态
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            Log.d(TAG, "run: " + e.getLocalizedMessage());
                        }
                    }
                }
            }
        });

        Button www = (Button) findViewById(R.id.www);
        www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: W短按执行了一次");
                if (iskong){
                    mBluetoothOp.write("w".getBytes());
                }else{
                    mBluetoothOp.write("k".getBytes());
                    iskong=true;
                    mBluetoothOp.write("w".getBytes());
                }
            }
        });
        Button aaa = (Button) findViewById(R.id.aaa);
        aaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: A短按执行了一次");
                if (iskong){
                    mBluetoothOp.write("a".getBytes());
                }else{
                    mBluetoothOp.write("k".getBytes());
                    iskong=true;
                    mBluetoothOp.write("a".getBytes());
                }
            }
        });

        Button sss = (Button) findViewById(R.id.sss);
        sss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: S短按执行了一次");
                if (iskong){
                    mBluetoothOp.write("s".getBytes());
                }else{
                    mBluetoothOp.write("k".getBytes());
                    iskong=true;
                    mBluetoothOp.write("s".getBytes());
                }
            }
        });

        Button ddd = (Button) findViewById(R.id.ddd);
        ddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: D短按执行了一次");
                if (iskong){
                    mBluetoothOp.write("d".getBytes());
                }else{
                    mBluetoothOp.write("k".getBytes());
                    iskong=true;
                    mBluetoothOp.write("d".getBytes());
                }
            }
        });
    }

}

