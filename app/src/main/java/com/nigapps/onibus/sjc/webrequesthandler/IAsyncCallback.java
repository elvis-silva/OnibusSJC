package com.nigapps.onibus.sjc.webrequesthandler;

public interface IAsyncCallback {
    void onComplete(WebResponse responseContent);
    void onError(String errorData);
}