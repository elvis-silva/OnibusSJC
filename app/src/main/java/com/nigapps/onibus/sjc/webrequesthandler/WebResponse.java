package com.nigapps.onibus.sjc.webrequesthandler;

import com.nigapps.onibus.sjc.entities.BaseObject;

public class WebResponse {

    public BaseObject getTypedObject() {
        return baseObject;
    }

    private BaseObject baseObject;

    public WebResponse(BaseObject currentBaseObject) {
        baseObject = currentBaseObject;
    }


}