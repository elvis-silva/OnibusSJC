package com.nigapps.onibus.sjc.webrequesthandler;

import com.nigapps.onibus.sjc.entities.BaseObject;

public interface IHTMLParser {
    BaseObject parseHTML(String htmlToParse);
}