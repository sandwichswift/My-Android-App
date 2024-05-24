package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.aitool.AitoolViewModel
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.ui.home.Weather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


fun log(info: Any){
    Log.d("Todo", info.toString())
}
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var  homeViewModel: HomeViewModel
    lateinit var aitoolViewModel: AitoolViewModel

    private var city: String = "Changsha"//默认长沙

    private lateinit var locationManager: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)//获取导航控制器
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_aitool, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)//设置底部导航栏
        //获取ViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        aitoolViewModel = ViewModelProvider(this).get(AitoolViewModel::class.java)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
            !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            askLocationSettings();
        }
        log("LocationManager: $locationManager")
        val location = getLocation()
        getCityName(location)

    }

    private fun askLocationSettings() {
        val builder:AlertDialog.Builder = AlertDialog.Builder(this);
        builder.setTitle("开启位置服务");
        builder.setMessage("本应用需要开启位置服务，是否去设置界面开启位置服务？");
        builder.setPositiveButton("是"){dialog, which ->
            val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        builder.setNegativeButton("否"){dialog, which ->
            Toast.makeText(this,"定位失败,请开启定位权限", Toast.LENGTH_SHORT).show()
        }
        builder.show();
    }

    private fun getProvider(locationManager: LocationManager):String?{

        //获取位置信息提供者列表
        val providerList = locationManager.getProviders(true);

        if (providerList.contains(LocationManager.GPS_PROVIDER)){
            //获取GPS定位
            log("GPS_PROVIDER:${LocationManager.GPS_PROVIDER}")
            return LocationManager.GPS_PROVIDER;
        }
        //网络定位不允许
        return null;
    }

    private fun getLocation(): Location? {
        /*获取LocationManager对象*/

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val provider = getProvider(locationManager)
        if (provider == null) {
            log("No location provider to use")
            Toast.makeText(this,"定位失败", Toast.LENGTH_SHORT).show()
        }
        //系统权限检查警告，需要做权限判断
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            requestPermission()
            return null
        }
        val location = locationManager.getLastKnownLocation(provider!!)
        log("Location: $location")
        return location
    }
    private fun getCityName(location: Location?){
        if (location != null) {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = location?.let { geocoder.getFromLocation(it.latitude, it.longitude, 1) }
            log("Addresses: $addresses")
            val cityName = addresses?.get(0)?.locality
            log("City Name: $cityName")
            if (cityName != null) {
                city = cityName
            }
        }
        lifecycleScope.launch {
            val weather = withContext(Dispatchers.IO){
                Weather.getWeather(city)
            }
            homeViewModel.weatherInfo.value = weather
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            1
        )
    }

}