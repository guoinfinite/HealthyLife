package com.example.healthylife.utils;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/14.
 */
public class GetLocation {
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public static Map<String,String> data = new HashMap<>();
    private static final String TAG = "GetLocation";

    public GetLocation(Context context) {
        mLocationClient = new LocationClient(context);//声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener(new MyLocationListener());
        Log.e("执行了", "?");
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000 * 10;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        private StringBuffer sb = null;

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            data.put("time",location.getTime());

            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            data.put("errorCode",location.getLocType()+"");

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            data.put("latitude",location.getLatitude()+"");

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            data.put("lontitude",location.getLongitude()+"");

            sb.append("\nradius : ");
            sb.append(location.getRadius());
            data.put("radius",location.getRadius()+"");

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                data.put("speed",location.getSpeed()+"");

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                data.put("satellite",location.getSatelliteNumber()+"");

                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                data.put("height",location.getAltitude()+"");

                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                data.put("direction",location.getDirection()+"");

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                data.put("addr",location.getAddrStr()+"");

                data.put("province",location.getProvince()+"");
                data.put("country",location.getCountry()+"");
                data.put("city",location.getCity()+"");
                data.put("district",location.getDistrict()+"");
                data.put("street",location.getStreet()+"");

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                data.put("addr",location.getAddrStr()+"");

                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                data.put("operationers",location.getOperators()+"");

                data.put("province",location.getProvince()+"");
                data.put("country",location.getCountry()+"");
                data.put("city",location.getCity()+"");
                data.put("district",location.getDistrict()+"");
                data.put("street",location.getStreet()+"");

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            data.put("locationdescribe",location.getLocationDescribe()+"");

            List<Poi> list = location.getPoiList();// POI数据
            Log.d(TAG, "onReceiveLocation: map" +data);
            SaveKeyValues.putStringValues("district", data.get("district"));
            Log.d(TAG, "onReceiveLocation: district:" + data.get("district"));
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                    Log.d(TAG, "onReceiveLocation: list"+p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.e("BaiduLocationApiDem", "【" + sb.toString() + "】");

            if (location.getLocType() == 161) {
                SaveKeyValues.putStringValues("city", location.getCity().substring(0, location.getCity().length() - 1));
                mLocationClient.stop();
            }
        }

    }
    public Map<String,String> getData() {
        Log.d(TAG, "getData: " + data);
        return data;
    }
}
