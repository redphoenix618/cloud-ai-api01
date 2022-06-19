package com.xyy.data;

public class XFResultOutData {

    // 句子相对于本音频的起始时间，单位为ms
    private String bg;
    // 句子相对于本音频的终止时间，单位为ms
    private String ed;
    // 句子内容
    private String onebest;
    // 说话人编号，从1开始，未开启说话人分离时speaker都为0
    private String speaker;

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public String getEd() {
        return ed;
    }

    public void setEd(String ed) {
        this.ed = ed;
    }

    public String getOnebest() {
        return onebest;
    }

    public void setOnebest(String onebest) {
        this.onebest = onebest;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }
}
