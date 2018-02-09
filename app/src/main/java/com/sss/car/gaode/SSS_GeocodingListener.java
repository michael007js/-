package com.sss.car.gaode;

import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * Created by leilei on 2017/6/2.
 */

public interface SSS_GeocodingListener {
    void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i);

    void onGeocodeSearched(GeocodeResult geocodeResult, int i);
}
