package com.example.comgentask;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.Manifest;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.comgentask.databinding.ActivityMain2Binding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.LocationServices;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity2 extends AppCompatActivity {
    private ActivityMain2Binding binding;
    private Handler handler = new Handler();
    private int seconds;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<String> pairedDeviceList = new ArrayList<>();
    private ArrayList<String> unpairedDeviceList = new ArrayList<>();
    private ArrayAdapter<String> pairedAdapter;
    private ArrayAdapter<String> unpairedAdapter;

//    GraphView graphView;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        binding.timerTV.
        startTimer();

        pairedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pairedDeviceList);
        binding.pairedList.setAdapter(pairedAdapter);


        unpairedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, unpairedDeviceList);
        binding.unPairedList.setAdapter(unpairedAdapter);


        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        initBluetoth();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, intentFilter);

        // Initial check for network status
        checkNetworkStatus();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // Check and request location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH_CONNECT}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                // on below line we are adding
                // each point on our x and y axis.
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 9),
                new DataPoint(4, 6),
                new DataPoint(5, 7),

        });


        binding.graphView.setTitle("My Graph View");

        binding.graphView.setTitleColor(R.color.black);

        binding.graphView.setTitleTextSize(18);

        binding.graphView.addSeries(series);

        StaticLabelsFormatter labelsFormatter = new StaticLabelsFormatter(binding.graphView);
        labelsFormatter.setHorizontalLabels(new String[]{"24/01", "20/01", "21/01", "22/01", "23/01", "24/01"});
        binding.graphView.getGridLabelRenderer().setLabelFormatter(labelsFormatter);


    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    getAddressFromLocation(latitude, longitude);
                } else {
                    binding.addressTV.setText("Unable to fetch location.");
                }
            }
        });
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressDetails = address.getAddressLine(0);
                binding.addressTV.setText("Current Location: " + addressDetails);
            } else {
                binding.addressTV.setText("Unable to get address.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            binding.addressTV.setText("Error fetching address.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
                initBluetoth();
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    void initBluetoth(){
        // Initialize Bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            // Request to enable Bluetooth
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(enableBtIntent, 1);
        } else {
            fetchPairedDevices();
            discoverNearbyDevices();
        }
    }

    private void fetchPairedDevices() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceList.add(device.getName());
            }
            pairedAdapter.notifyDataSetChanged();
        }
    }

    private void discoverNearbyDevices() {
        // Register for discovery results
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        // Start discovery
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        bluetoothAdapter.startDiscovery();
    }
    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                if (device != null && !pairedDeviceList.contains(device.getName())) {
                    unpairedDeviceList.add(device.getName() );
                    unpairedAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void checkNetworkStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isOnline = false;

        Network network = connectivityManager.getActiveNetwork();
        if (network != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities != null) {
                isOnline = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }

        updateNetworkStatus(isOnline);
    }

    private void updateNetworkStatus(boolean isOnline) {
        String status = isOnline ? "Online" : "Offline";
        binding.networkStateTV.setText("Network Status: " + status);
    }

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkNetworkStatus();
        }
    };
    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = (level / (float) scale) * 100;

            binding.batteryLevelTV.setText("Battery Level: " + Math.round(batteryPct) + "%");

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            String chargingStatus;
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                chargingStatus = "Charging";
            } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
                chargingStatus = "Fully Charged";
            } else {
                chargingStatus = "Not Charging";
            }

            binding.batteryStatusTV.setText("Charging Status: " + chargingStatus);
        }
    };
    void startTimer(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Increment the seconds counter
                seconds++;

                // Update the TextView
                binding.timerTV.setText(seconds + "");

                // Repeat this runnable code block every 1 second
                handler.postDelayed(this, 1000);
            }
        }, 1000); // Initial delay of 1 second

    }
}