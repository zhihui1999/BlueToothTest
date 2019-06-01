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

    private Thread w, a, s, d,b;
    private boolean bw = false, ba = false, bs = false, bd = false,bb=false;//循环
    private boolean bwf = true, baf = true, bsf = true, bdf = true,bbf=true;//第一次

    /**
     * 蓝牙连接成功回调和接受数据回调
     */
    private BluetoothOperationCallback callback = new BluetoothOperationCallback() {

        @Override
        public void onConnect(int err, String desc) {
            if (err == 0) {
                Log.d(TAG, "蓝牙连接成功");
                Toast.makeText(MainActivity.this, "连接成功,可以开始操作了", Toast.LENGTH_SHORT).show();
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
        initthread();//初始化线程，开启线程
        initbtclick();//初始化按钮监听，长按监听

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
            case R.id.scan:
                mBluetoothOp.connect(MAC);//连接蓝牙
                break;
            case R.id.button: {
                bb = false;
                mBluetoothOp.write("k".getBytes());
            }
            break;
            case R.id.button2: {
                bb = false;
                mBluetoothOp.write("x".getBytes());
            }
            break;
            case R.id.button3: {
                bb=true;
                if (bbf) {
                    b.start();
                    bbf=false;
                }
            }
            break;
            case R.id.button4: {
                bb = false;
                mBluetoothOp.write("t".getBytes());
            }
            break;
        }
    }

    /**
     * 初始化线程
     */
    void initthread() {
        Log.d(TAG, "initthread: 线程初始化开始");
        b=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if (bb) {
                        try {
                            Thread.sleep(200);
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
        d = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (bd) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    } else {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                        mBluetoothOp.write("d".getBytes());
                    }
                }
            }
        });

        w = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (bw) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            break;
                        }
                    } else {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            break;
                        }
                        mBluetoothOp.write("w".getBytes());
                    }
                }
            }
        });

        a = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (ba) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            break;
                        }
                    } else {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            break;
                        }
                        mBluetoothOp.write("a".getBytes());
                    }
                }
            }
        });

        s = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (bs) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            break;
                        }
                    } else {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            break;
                        }
                        mBluetoothOp.write("s".getBytes());
                    }
                }
            }
        });
        Log.d(TAG, "initthread: 线程初始化完毕");
    }

    /**
     * 初始化按钮长按监听及wasd按钮单击监听
     */
    void initbtclick() {
        Button www = (Button) findViewById(R.id.www);
        www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: W短按执行了一次");
                bw = false;
                mBluetoothOp.write("w".getBytes());
            }
        });
        www.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: W长按了一次");
                bw = true;
                if (bwf) {
                    w.start();
                    bwf = false;
                }
                return false;//true不加onclick   false结尾加个onclick事件
            }
        });
        Button aaa = (Button) findViewById(R.id.aaa);
        aaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: A短按执行了一次");
                ba = false;
                mBluetoothOp.write("a".getBytes());
            }
        });
        aaa.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: A长按了一次");
                ba = true;
                if (baf) {
                    a.start();
                    baf = false;
                }
                return false;//true不加onclick   false结尾加个onclick事件
            }
        });
        Button sss = (Button) findViewById(R.id.sss);
        sss.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: S长按了一次");
                bs = true;
                if (bsf) {
                    s.start();
                    bsf = false;
                }
                return false;//true不加onclick   false结尾加个onclick事件
            }
        });
        sss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: S短按执行了一次");
                bs = false;
                mBluetoothOp.write("s".getBytes());
            }
        });

        Button ddd = (Button) findViewById(R.id.ddd);
        ddd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: D长按了一次");
                bd = true;
                if (bdf) {
                    d.start();
                    bdf = false;
                }
                return false;//true不加onclick   false结尾加个onclick事件
            }
        });
        ddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: D短按执行了一次");
                bd = false;
                mBluetoothOp.write("d".getBytes());
            }
        });
    }

}

