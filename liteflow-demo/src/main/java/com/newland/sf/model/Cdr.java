package com.newland.sf.model;

import com.yomahub.liteflow.model.base.Event;

/**
 * @author WRP
 * @since 2023/3/28
 */
public class Cdr extends Event {

    private String data;

    private String chachongData;

    private String yaosuqiuquData;

    private String pijiaData;

    private String koukuanData;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getChachongData() {
        return chachongData;
    }

    public void setChachongData(String chachongData) {
        this.chachongData = chachongData;
    }

    public String getYaosuqiuquData() {
        return yaosuqiuquData;
    }

    public void setYaosuqiuquData(String yaosuqiuquData) {
        this.yaosuqiuquData = yaosuqiuquData;
    }

    public String getPijiaData() {
        return pijiaData;
    }

    public void setPijiaData(String pijiaData) {
        this.pijiaData = pijiaData;
    }

    public String getKoukuanData() {
        return koukuanData;
    }

    public void setKoukuanData(String koukuanData) {
        this.koukuanData = koukuanData;
    }
}
