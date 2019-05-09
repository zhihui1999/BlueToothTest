package com.example.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import cn.glinks.lib.bt.BluetoothOperationCallback;
import cn.glinks.lib.bt.BluetoothOperator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "蓝牙test" + MainActivity.class.getSimpleName();

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Bluetooth
    private boolean hasEnabled = false;
    private BluetoothOperator mBluetoothOp;
    private BluetoothAdapter mBluetoothAdapter;

    private TextView textView,textView2;
    private String zhong;
    private EditText editText;

    private BluetoothOperationCallback callback = new BluetoothOperationCallback() {

        @Override
        public void onConnect(int err, String desc) {
            if (err == 0) {
                Log.d(TAG, "蓝牙连接成功");
                textView.setText("已连接");
                //mBluetoothOp.write("hello".getBytes());
            }
        }

        @Override
        public void onDataRecv(int err, String desc, byte[] data, int len) {
            if (err == 0) {
                Log.d(TAG, "\n 蓝牙接收数据:" + new String(data, 0, len));
                zhong=textView2.getText().toString();
                textView2.setText(zhong+new String(data,0,len));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothOp = new BluetoothOperator(this);
        mBluetoothOp.registerCallback(callback);
        if (mBluetoothOp.isEnabled())
           hasEnabled = true;

        textView=(TextView)findViewById(R.id.textView);
        textView2=(TextView)findViewById(R.id.textView2);

        editText =(EditText)findViewById(R.id.editText);

        //初始化蓝牙适配器
        mBluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
        //获取已经保存过的设备信息
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        if (devices.size()>0) {
            for(Iterator<BluetoothDevice> iterator = devices.iterator(); iterator.hasNext();){
                BluetoothDevice bluetoothDevice=(BluetoothDevice)iterator.next();
                Log.d(TAG, "onCreate: "+bluetoothDevice.getName() + " " + bluetoothDevice.getAddress());
            }
        }
    }


    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();

        mBluetoothOp.enable();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBluetoothOp.unregisterCallback(callback);
        if (!hasEnabled)
            mBluetoothOp.disable();
        mBluetoothOp.destroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan:
                mBluetoothOp.connect("98:D3:41:FD:35:FF");
                break;
            case R.id.on:
                    mBluetoothOp.write("a".getBytes());
//                    zhong=textView2.getText().toString();
//                    textView2.setText(zhong+"a\n");
                break;
            case R.id.off:
                mBluetoothOp.write("d".getBytes());
//                zhong=textView2.getText().toString();
//                textView2.setText(zhong+"d\n");
                break;
            case R.id.button:

                textView2.setText("");

                break;
            case R.id.send:
                if (!editText.getText().toString().equals("")){
                    mBluetoothOp.write(editText.getText().toString().getBytes());
                    // editText.getText()
                }

        }
    }
}
