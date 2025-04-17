package vn.iotstar.socketconnection;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BlueControl extends AppCompatActivity {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    ImageButton btnTb1, btnTb2;
    Button btnDisconnect;
    TextView txtStatus, txtMAC;
    BluetoothAdapter myBluetooth;
    BluetoothSocket btSocket;
    String deviceAddress;
    boolean isBtConnected = false;
    int flagLamp1 = 0, flagLamp2 = 0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        // Ánh xạ view
        btnTb1 = findViewById(R.id.btnTb1);
        btnTb2 = findViewById(R.id.btnTb2);
        btnDisconnect = findViewById(R.id.btnDisconnect);
        txtStatus = findViewById(R.id.textV1);
        txtMAC = findViewById(R.id.textViewMAC);

        // Nhận địa chỉ thiết bị từ Intent
        Intent intent = getIntent();
        deviceAddress = intent.getStringExtra(MainActivity.EXTRA_ADDRESS);
        txtMAC.setText("Địa chỉ MAC: " + deviceAddress);

        // Khởi tạo Bluetooth Adapter
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        // Kết nối Bluetooth
        new ConnectBT().execute();

        // Sự kiện click
        btnTb1.setOnClickListener(v -> controlDevice(1));
        btnTb2.setOnClickListener(v -> controlDevice(2));
        btnDisconnect.setOnClickListener(v -> disconnect());
    }

    private void controlDevice(int device) {
        if (!isBtConnected) {
            Toast.makeText(this, "Chưa kết nối đến thiết bị", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String command;
            String status;

            if (device == 1) {
                flagLamp1 = flagLamp1 == 0 ? 1 : 0;
                if (flagLamp1 == 1) {
                    btnTb1.setBackgroundResource(R.drawable.tb1on);
                    command = "1";
                    status = "Thiết bị 1 đang BẬT";
                } else {
                    btnTb1.setBackgroundResource(R.drawable.tb1off);
                    command = "A";
                    status = "Thiết bị 1 đang TẮT";
                }
            } else {
                flagLamp2 = flagLamp2 == 0 ? 1 : 0;
                if (flagLamp2 == 1) {
                    btnTb2.setBackgroundResource(R.drawable.tb2on);
                    command = "2";
                    status = "Thiết bị 2 đang BẬT";
                } else {
                    btnTb2.setBackgroundResource(R.drawable.tb2off);
                    command = "B";
                    status = "Thiết bị 2 đang TẮT";
                }
            }

            btSocket.getOutputStream().write(command.getBytes());
            txtStatus.setText(status);
        } catch (IOException e) {
            Toast.makeText(this, "Lỗi khi gửi lệnh", Toast.LENGTH_SHORT).show();
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean connectSuccess = true;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BlueControl.this,
                    "Kết nối Bluetooth", "Đang kết nối...", true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (btSocket == null || !isBtConnected) {
                    if (ContextCompat.checkSelfPermission(BlueControl.this,
                            Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }

                    BluetoothDevice device = myBluetooth.getRemoteDevice(deviceAddress);
                    btSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    myBluetooth.cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                connectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            if (!connectSuccess) {
                Toast.makeText(BlueControl.this,
                        "Kết nối thất bại", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                isBtConnected = true;
                Toast.makeText(BlueControl.this,
                        "Kết nối thành công", Toast.LENGTH_SHORT).show();
                txtStatus.setText("Đã kết nối");
            }
        }
    }

    private void disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();
                isBtConnected = false;
                Toast.makeText(this, "Đã ngắt kết nối", Toast.LENGTH_SHORT).show();
                finish();
            } catch (IOException e) {
                Toast.makeText(this, "Lỗi khi ngắt kết nối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }
}