package com.atnanx.atcrowdfunding.core.bean;

public class TMessage {
    private Integer id;

    private Integer memberid;

    private String content;

    private String senddate;

    public TMessage(Integer id, Integer memberid, String content, String senddate) {
        this.id = id;
        this.memberid = memberid;
        this.content = content;
        this.senddate = senddate;
    }

    public TMessage() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getSenddate() {
        return senddate;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate == null ? null : senddate.trim();
    }
}