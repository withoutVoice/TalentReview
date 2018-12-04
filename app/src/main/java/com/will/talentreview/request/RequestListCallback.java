package com.will.talentreview.request;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.ResponseCallback;
import com.will.talentreview.constant.Config;
import com.will.talentreview.entity.ListBean;
import com.will.talentreview.entity.RequestResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * @author chenwei
 * @time 2018-11-28
 */

public abstract class RequestListCallback<T> extends ResponseCallback<String, ResponseBody> {

    private Gson gson;
    private JsonParser jsonParser;

    public RequestListCallback() {
        gson = new Gson();
        jsonParser = new JsonParser();
    }

    @Override
    public String onHandleResponse(ResponseBody response) throws Exception {
        String responseString = new String(response.bytes());
        return responseString;
    }

    @Override
    public void onNext(Object tag, Call call, String response) {
//        Class<T> entityClass=null;
//        Class<?> child=null;
        Class<?> parent = getActualTypeArgument(getClass());
//        if(parent==null){
//            onRequestFinished(getRequestResult(response, String.class));
//        }else {
//            child=getActualTypeArgument(parent);
//        }
//        if(child!=null){
//            entityClass= (Class<T>) child;
//        }else {
//
//        }
//        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        onRequestFinished(getRequestResult(response, parent));
    }

    /*
     * 获取泛型类Class对象，不是泛型类则返回null
	 */
    public Class<?> getActualTypeArgument(Class<?> clazz) {
        Class<?> entitiClass = null;
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                entitiClass = (Class<?>) actualTypeArguments[0];
            }
        }

        return entitiClass;
    }

    @Override
    public void onError(Object tag, Throwable e) {
        RequestResult requestResult = new RequestResult();
        requestResult.setSuccess(false);
        requestResult.setMsg(e.getMessage());
        onRequestFinished(requestResult);
    }

    @Override
    public void onCancel(Object tag, Throwable e) {
        RequestResult requestResult = new RequestResult();
        requestResult.setSuccess(false);
        requestResult.setMsg("请求取消");
        onRequestFinished(requestResult);
    }

    public abstract void onRequestFinished(RequestResult<T> requestResult);

    /**
     * 业务数据转换
     *
     * @param json 业务数据
     * @param cla  数据类型
     * @return requestResult
     */
    private <T> RequestResult getRequestResult(String json, Class<T> cla) {
        RequestResult requestResult = new RequestResult();
        // Json的解析类对象
        try {
            JSONObject jsonObject = new JSONObject(json);
            requestResult.setMsg(jsonObject.optString(RequestResult.MSG, ""));
            requestResult.setCode(jsonObject.optInt(RequestResult.CODE, -1));
            requestResult.setSuccess(requestResult.getCode() == 0);
            if (!requestResult.isSuccess()) {
                if (requestResult.getCode() == Config.TOKEN_ERROR_CODE) {
                    requestResult.setMsg("登陆信息已失效，请重新登录");
                }
                return requestResult;
            }
            String dataStr = jsonObject.optString(RequestResult.DATA);
            if (cla == String.class) {
                requestResult.setData(dataStr);
            } else {
                Object listArray = new JSONTokener(dataStr).nextValue();
                if (listArray instanceof JSONArray) {

                    List<T> dataList = fromJsonToList(dataStr, cla);
                    requestResult.setDatas(dataList);
                } else if (listArray instanceof JSONObject) {
                    String listStr = ((JSONObject) listArray).optString("list");
                    if (((JSONObject) listArray).has("list")) {
                        JSONObject data = new JSONObject(dataStr);
                        ListBean listBean = new ListBean();
                        listBean.setCurrPage(data.optInt("", 0));
                        listBean.setPageSize(data.optInt("", 0));
                        listBean.setTotalCount(data.optInt("", 0));
                        listBean.setTotalPage(data.optInt("", 0));
                        Object listArray2 = new JSONTokener(listStr).nextValue();
                        if (!TextUtils.isEmpty(listStr) && listArray2 instanceof JSONArray) {
                            List<T> list = fromJsonToList(listStr, cla);
                            listBean.setRecords(list);
                        }
                        requestResult.setData(listBean);
                    } else {
                        T data = gson.fromJson(dataStr, cla);
                        requestResult.setData(data);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestResult;
    }

    /**
     * 将json格式的字符串转换为list
     *
     * @param json json
     * @param cla  Class
     * @param <T>  泛型
     * @return 结果
     */
    public <T> List<T> fromJsonToList(String json, Class<T> cla) {
        if (TextUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        JsonArray jsonArrays = jsonParser.parse(json).getAsJsonArray();
        List<T> resultList = new ArrayList<>();
        // 加强for循环遍历JsonArray
        for (JsonElement jsonArray : jsonArrays) {
            T data = gson.fromJson(jsonArray, cla);
            if (null != data) {
                resultList.add(data);
            }
        }
        return resultList;
    }
}
