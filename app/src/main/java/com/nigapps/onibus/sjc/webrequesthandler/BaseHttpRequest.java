package com.nigapps.onibus.sjc.webrequesthandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.nigapps.onibus.sjc.entities.BaseObject;

public class BaseHttpRequest {

    public static int REQUEST_BUS = 0;
    public static int VERIFY_SITE = 1;
    private int type;
    ProgressDialog progressDialog;
    int responseCode;
    IAsyncCallback callback;
    Response.Listener<String> stringResponseListener;
    private IHTMLParser htmlParser;
    private String url;
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            StringWriter errors = new StringWriter();
            error.printStackTrace(new PrintWriter(errors));
            dismissProgressDialog();
            callback.onError(error.toString());
        }
    };
    private Context context;

    public BaseHttpRequest(Activity localActivity,
                           String url, int type) {

        context = localActivity;
        this.url = url;
        this.type = type;
        setListeners();

    }

    public IHTMLParser getHtmlParser() {
        return htmlParser;
    }

    public void setHtmlParser(IHTMLParser htmlParser) {
        this.htmlParser = htmlParser;
    }


    public IAsyncCallback getCallback() {
        return callback;
    }

    public Context getContext() {
        return context;
    }

    private void setListeners() {
        stringResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HTMLParseAsyncTask task = new HTMLParseAsyncTask();
                task.setCurrentRequest(BaseHttpRequest.this);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
            }
        };
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void execute(IAsyncCallback callback, RequestQueue requestQueue) {
        this.callback = callback;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        String message = type == REQUEST_BUS ? "Aguarde por favor..." : "Atualizando horários.\nPor favor aguarde.";
        progressDialog.setMessage(message);
        dismissProgressDialog();
        progressDialog.show();
        addToRequestQueue(requestQueue, getStringRequest());
    }

    public <X> void addToRequestQueue(RequestQueue requestQueue, Request<X> req) {
        requestQueue.add(req);
    }

    Request<String> getStringRequest() {
        return new StringRequest(Request.Method.GET, url, stringResponseListener, errorListener);
    }

    public static class HTMLParseAsyncTask extends AsyncTask<String, Void, Void> {

        private BaseObject baseObject;
        private BaseHttpRequest currentRequest;

        public BaseHttpRequest getCurrentRequest() {
            return currentRequest;
        }

        public void setCurrentRequest(BaseHttpRequest currentRequest) {
            this.currentRequest = currentRequest;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            if (getCurrentRequest().getHtmlParser() != null) {
                baseObject = getCurrentRequest().getHtmlParser().parseHTML(params[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            getCurrentRequest().dismissProgressDialog();
            try {
                WebResponse webResponse = new WebResponse(baseObject);
                getCurrentRequest().getCallback().onComplete(webResponse);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}