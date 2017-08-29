package notyen.parkproject.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import notyen.parkproject.apidata.OpenDataList;
import notyen.parkproject.utils.Constants;
import notyen.parkproject.utils.Develop;
import notyen.parkproject.utils.JParser;


public class Api {
    static RequestQueue mQueue;
    static Context context;

    public static void instance(Context context) {
        Api.context = context;
        mQueue = Volley.newRequestQueue(context);
    }

    /**
     * 台北市公園OpenData
     *
     * @param onApiResponse
     * @param errorListener
     */
    public static void getParkOpenData(OnApiResponse<OpenDataList> onApiResponse, Response.ErrorListener errorListener) {
        get(Constants.openDataUrl, null, onApiResponse, errorListener, OpenDataList.class);
    }

    private static <O extends ApiData> void post(String url, final Map<String, String> params, final OnApiResponse<O> onApiResponse, final Response.ErrorListener errorListener, final Class<O> c) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Develop.e("API", "GGGGGG!!");
                if (onApiResponse != null) {
                    Develop.e("API", "GGGGGG22!!");
                    onApiResponse.onApiResonse(JParser.toObject(response, c));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Develop.e("onErrorResponse", error.getMessage());
                if (errorListener != null)
                    errorListener.onErrorResponse(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Develop.e("API", "AuthFailureError!!");
                Map<String, String> p;
                p = new HashMap<>();

                if (params != null)
                    p.putAll(params);
                return p;
            }
        };

        mQueue.add(stringRequest);
    }

    private static <O extends ApiData> void get(String url, final Map<String, String> params, final OnApiResponse<O> onApiResponse, final Response.ErrorListener errorListener, final Class<O> c) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Develop.e("onResponse", response);
                Develop.e("API", "GGGGGG!!");
                if (onApiResponse != null) {
                    Develop.e("API", "GGGGGG22!!");
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        String returnData = jsonObj.get("result").toString();
                        Develop.e("returnData", "returnData:" + returnData);

                        onApiResponse.onApiResonse(JParser.toObject(returnData, c));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Develop.e("onErrorResponse", error.getMessage());
                if (errorListener != null)
                    errorListener.onErrorResponse(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p;
                p = new HashMap<>();

                if (params != null)
                    p.putAll(params);
                return p;
            }
        };

        mQueue.add(stringRequest);
    }

    private static <O extends ApiData> void post(String url, final String contentType, final Map<String, String> params, final boolean userInfo, final OnApiResponse<O> onApiResponse, final Response.ErrorListener errorListener, final Class<O> c) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (onApiResponse != null)
                    onApiResponse.onApiResonse(JParser.toObject(response, c));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorListener != null)
                    errorListener.onErrorResponse(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p;
                p = new HashMap<>();

                if (params != null)
                    p.putAll(params);
                return p;
            }

            @Override
            public String getBodyContentType() {
                if (contentType != null)
                    return contentType;
                else
                    return super.getBodyContentType();
            }
        };

        mQueue.add(stringRequest);
    }

}
