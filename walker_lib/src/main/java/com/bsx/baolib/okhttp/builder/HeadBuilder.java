package com.bsx.baolib.okhttp.builder;


import com.bsx.baolib.okhttp.OkHttpUtils;
import com.bsx.baolib.okhttp.request.OtherRequest;
import com.bsx.baolib.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
