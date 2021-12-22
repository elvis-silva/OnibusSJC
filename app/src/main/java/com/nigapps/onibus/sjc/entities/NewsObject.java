package com.nigapps.onibus.sjc.entities;

import com.squareup.picasso.RequestCreator;

/**
 * Created by elvis on 27/03/18.
 */

public class NewsObject {

    private String title, resume, source, imageUrl, sourceUrl;
    private RequestCreator requestCreator;

    public NewsObject(String title, String resume, String source, String sourceUrl, String imageUrl) {
        this.title = title;
        this.resume = resume;
        this.source = source;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getResume() {
        return resume;
    }

    public String getSource() {
        return source;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setRequestCreator(RequestCreator  requestCreator) {
        this.requestCreator = requestCreator;
    }

    public RequestCreator getRequestCreator() {
        return requestCreator;
    }
}
