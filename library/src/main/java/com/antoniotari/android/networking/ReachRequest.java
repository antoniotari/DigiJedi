package com.antoniotari.android.networking;

import com.google.gson.Gson;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.antoniotari.android.meanutil.FileUtil;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Antonio Tari on 20/01/15.
 */
public class ReachRequest<T> extends Request<T> {
    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final Response.Listener<T> mListener;
    private final String mRequestBody;
    private final Class<T> classOfT;

    /** Header used for attaching auth_token for registered only API requests */
    private static final String HEADER_AUTHTOKEN = "Authorization";
    protected static final String PARAM_PLATFORM = "platform";

    Map<String, String> _headers;
    Map<String,String> _params;

    //for POST requests
    HttpEntity _httpEntity=null;
    //MultipartEntityBuilder _entityBuilder = MultipartEntityBuilder.create();

    /**
     *
     * @param method Method.POST ot Method.GET
     * @param url
     * @param listener
     * @param mListener
     * @param mRequestBody
     * @param classOfT
     */
    public ReachRequest(int method, String url, Response.Listener<T> mListener, Response.ErrorListener listener, String mRequestBody,
            Class<T> classOfT) {
        super(method, url, listener);
        this.mListener = mListener;
        this.mRequestBody = mRequestBody;
        this.classOfT=classOfT;

        if(method== Method.GET) {
            try {
                checkCache();
            } catch (Exception e) {
                //this can cause any sort of exception
                //TODO research exceptions that can be thrown
            }
        }
    }

//    public void addFile(String key,String filePath){
//        File file=new File(filePath);
//        if (file.exists()) {
//            addFile(key,file) ;
//        }
//    }

//    public void addFile(String key,File file){
//        if(getMethod()==Method.GET)return;
//        _entityBuilder.addBinaryBody(key, file, ContentType.create("image/jpeg"), file.getName());
//    }
//
//    private void buildMultipartEntity() throws AuthFailureError {
//        if (getParams() != null) {
//            for (Map.Entry<String, String> entry : getParams().entrySet()) {
//                _entityBuilder.addTextBody(entry.getKey(), entry.getValue());
//            }
//        }
//    }


    protected void checkCache(){
        //first check the volley cache
        JSONObject respCacheJ= QueueManager.getInstance().loadFromCacheJ(super.getUrl());
        if(respCacheJ!=null && respCacheJ.length()>0){
            try {
                deliverCacheResponse(parseResponse(respCacheJ.toString()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {
            //then check the atutil cache
            respCacheJ = FileUtil.getInstance().getCachedJson(QueueManager.getInstance().getContext(), super.getUrl());
            if (respCacheJ != null && respCacheJ.length() > 0) {
                try {
                    deliverCacheResponse(parseResponse(respCacheJ.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void deliverCacheResponse(T response){
        if(mListener instanceof ReachRequestListener) {
            ((ReachRequestListener)mListener).onCacheResponse(response);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            saveHardCache(jsonString);
            T ret=parseResponse(jsonString);
            return Response.success(ret, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }
    }

    protected T parseResponse(String jsonString) throws UnsupportedEncodingException {
        T ret=new Gson().fromJson(jsonString,classOfT);
        return ret;
    }

    private void saveHardCache(String jsonString){
        try {
            FileUtil.getInstance().returnOrUpdateCache(QueueManager.getInstance().getContext()
                    , new JSONObject(jsonString), super.getUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public String getUrl(){
        if(_params!=null && getMethod()==Method.GET) {
            StringBuilder sb = new StringBuilder(super.getUrl());
            sb.append("?");
            for (Map.Entry<String, String> entry : _params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value != null && !value.isEmpty()) {
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                    sb.append("&");
                }
            }
            return sb.toString();
        }

        return super.getUrl();
    }

    /**
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public byte[] getPostBody() throws AuthFailureError {
        return getBody();
    }

    @Override
    public String getBodyContentType() {
        if(getMethod()==Method.POST){
            return _httpEntity.getContentType().getValue();
        }
        return PROTOCOL_CONTENT_TYPE;
    }

//    @Override
//    public byte[] getBody() throws AuthFailureError {
//        if(getMethod()!=Method.POST) {
//            try {
//                return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
//            } catch (UnsupportedEncodingException uee) {
//                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
//                        mRequestBody, PROTOCOL_CHARSET);
//                return null;
//            }
//        }
//        else{
//            //BODY for POST requests
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            try {
//                _entityBuilder.build().writeTo(bos);
//            }
//            catch (IOException e){
//                VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
//            }
//
//            return bos.toByteArray();
//        }
//    }

    @Override
    public Map<String, String> getParams()  throws AuthFailureError {
        if(getMethod()==Method.GET){
            return super.getParams();
        }
        else {
            return _params != null ? _params : super.getParams();
        }
    }

    public void setParams(Map<String,String> params){
        _params = params;

//        if (getMethod()==Method.POST){
//            _entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//            try {
//                _entityBuilder.setCharset(CharsetUtils.get("UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            _entityBuilder.setLaxMode().setBoundary("xx");
//
//            try {
//                buildMultipartEntity();
//            } catch (Exception authFailureError) {
//                authFailureError.printStackTrace();
//            }
//            _httpEntity=_entityBuilder.build();
//        }
    }

//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        if(_headers==null)
//            _headers = new HashMap<String, String>();
//        try {
//            attachAuthToken(_headers);
//        } catch (GetAuthTokenException e) {
//            e.printStackTrace();
//        } catch (AppSecurityException e) {
//            e.printStackTrace();
//        }
//
//        return _headers;//!=null?_headers:super.getHeaders();
//    }

}