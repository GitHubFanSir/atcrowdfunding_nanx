package com.atnanx.atcrowdfunding.core.bean;

public class TReturn {
    private Integer id;
    //项目ID
    private Integer projectid;
    //0 - 实物回报， 1 虚拟物品回报
    private String type;
    //支持金额
    private Integer supportmoney;
    //回报内容
    private String content;
    //0 为不限回报数量
    private Integer count;
    //单笔限购
    private Integer signalpurchase;
    //限购数量
    private Integer purchase;
    //运费
    private Integer freight;
    //0 - 不开发票， 1 - 开发票
    private String invoice;
    //回报时间,众筹成功后多少天进行回报
    private Integer rtndate;

    private Integer supporter;

    private Integer sales;


    public TReturn(Integer id, Integer projectid, String type, Integer supportmoney, String content, Integer count, Integer signalpurchase, Integer purchase, Integer supporter, Integer sales, Integer freight, String invoice, Integer rtndate) {
        this.id = id;
        this.projectid = projectid;
        this.type = type;
        this.supportmoney = supportmoney;
        this.content = content;
        this.count = count;
        this.signalpurchase = signalpurchase;
        this.purchase = purchase;
        this.supporter = supporter;
        this.sales = sales;
        this.freight = freight;
        this.invoice = invoice;
        this.rtndate = rtndate;
    }

    public TReturn() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getSupportmoney() {
        return supportmoney;
    }

    public void setSupportmoney(Integer supportmoney) {
        this.supportmoney = supportmoney;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSignalpurchase() {
        return signalpurchase;
    }

    public void setSignalpurchase(Integer signalpurchase) {
        this.signalpurchase = signalpurchase;
    }

    public Integer getPurchase() {
        return purchase;
    }

    public void setPurchase(Integer purchase) {
        this.purchase = purchase;
    }

    public Integer getSupporter() {
        return supporter;
    }

    public void setSupporter(Integer supporter) {
        this.supporter = supporter;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getFreight() {
        return freight;
    }

    public void setFreight(Integer freight) {
        this.freight = freight;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice == null ? null : invoice.trim();
    }

    public Integer getRtndate() {
        return rtndate;
    }

    public void setRtndate(Integer rtndate) {
        this.rtndate = rtndate;
    }
}