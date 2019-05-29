package com.fast0n.rojadirectatvapp.Fragment.HomeFragment;

public class DataItems {

    private String name;
    private String time;
    private String url;
    private String url_status;

    DataItems(String name, String time, String url, String url_status) {
        this.name = name;
        this.time = time;
        this.url = url;
        this.url_status = url_status;

    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public String getUrl_status() {
        return url_status;
    }


}