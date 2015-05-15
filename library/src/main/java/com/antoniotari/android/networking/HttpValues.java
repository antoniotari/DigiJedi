package com.antoniotari.android.networking;

import com.antoniotari.android.meanutil.MeanUtil;

import android.content.Context;
import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by antonio on 15/07/14.
 */
public class HttpValues
{
    private static Map<HttpStatus,String> _httpValues=null;

    public static Map<HttpStatus,String> getHttpValues(){
        if(_httpValues==null)
            _httpValues=new HashMap<HttpStatus, String>();
        return _httpValues;
    }


//    public static class HttpResponseStatus
//    {
//        public HttpStatus status;
//        public String message;
//
//        public HttpResponseStatus(int valueI,String message)
//        {
//            status=HttpStatus.valueOf(valueI);
//            this.message=message;
//        }
//    }

    public static enum HttpStatus{
        OK(200),
        CREATED(201),
        ACCEPTED(202),
        PARTIAL(203),
        NORESPONSE(204),
        BADREQUEST(400),
        UNAUTHORIZED(401),
        PAYMENTREQUIRED(402),
        FORBIDDEN(403),
        NOTFOUND(404),
        INTERNALERROR(500),
        NOTIMPLEMENTED(501),
        OVERLOADED(502),
        GATEWAYTIMEOUT(503),
        MOVED(301),
        FOUND(302),
        METHOD(303),
        NOTMODIFIED(304),
        NONE(0);

        private int _value;
        private static Map<Integer, HttpStatus> _map = new HashMap<Integer, HttpStatus>();

        static {
            for (HttpStatus legEnum : HttpStatus.values()) {
                _map.put(legEnum._value, legEnum);
            }
        }

        HttpStatus(int value){
            _value=value;
        }

        public static HttpStatus valueOf(int legNo)
        {
            if (_map.get(legNo)==null)
                return NONE;
            return _map.get(legNo);
        }

        @Override
        public String toString() {
            return String.valueOf(_value);
        }



//        public static HttpStatus getStatus(int value)
//        {
//            for (HttpStatus l : HttpStatus.values()) {
//                if (l._value == value) return l;
//            }
//            throw new IllegalArgumentException("Leg not found. Amputated?");
//        }
    }

    public static void generateHttpValues(Context context)
    {
        if(context==null)
            return;

        _httpValues=new HashMap<HttpStatus, String>();
        Resources res=context.getResources();
        for (HttpStatus status : HttpStatus.values()) {
            if(status==HttpStatus.NONE) {
                _httpValues.put(
                        status,
                        "NONE");
            }
            else {
                try {
                    _httpValues.put(
                            status,
                            res.getString(MeanUtil.getResourceIdFromName(context, "string", "HTTP_" + status.toString())));
                }catch (Exception e){
                    _httpValues.put(
                            status,
                            "ERROR");
                }
            }
        }


//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_OK)),res.getString(R.string.HTTP_OK)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_CREATED)),res.getString(R.string.HTTP_CREATED)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_ACCEPTED)),res.getString(R.string.HTTP_ACCEPTED)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_PARTIAL)),res.getString(R.string.HTTP_PARTIAL)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_NORESPONSE)),res.getString(R.string.HTTP_NORESPONSE)) ;
//
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_BADREQUEST)),res.getString(R.string.HTTP_BADREQUEST)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_UNAUTHORIZED)),res.getString(R.string.HTTP_UNAUTHORIZED)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_PAYMENTREQUIRED)),res.getString(R.string.HTTP_PAYMENTREQUIRED)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_FORBIDDEN)),res.getString(R.string.HTTP_FORBIDDEN)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_NOTFOUND)),res.getString(R.string.HTTP_NOTFOUND)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_INTERNALERROR)),res.getString(R.string.HTTP_INTERNALERROR)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_NOTIMPLEMENTED)),res.getString(R.string.HTTP_NOTIMPLEMENTED)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_OVERLOADED)),res.getString(R.string.HTTP_OVERLOADED)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_GATEWAYTIMEOUT)),res.getString(R.string.HTTP_GATEWAYTIMEOUT)) ;
//
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_MOVED)),res.getString(R.string.HTTP_MOVED)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_FOUND)),res.getString(R.string.HTTP_FOUND)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_METHOD)),res.getString(R.string.HTTP_METHOD)) ;
//        HTTPVALUES.put(HttpStatus.valueOf(res.getInteger(R.integer.HTTP_NOTMODIFIED)),res.getString(R.string.HTTP_NOTMODIFIED)) ;
    }
}
