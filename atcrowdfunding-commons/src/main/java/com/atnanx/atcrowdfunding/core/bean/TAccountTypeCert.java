package com.atnanx.atcrowdfunding.core.bean;

public class TAccountTypeCert {
    private Integer id;

    private String accttype;

    private Integer certid;

    public TAccountTypeCert(Integer id, String accttype, Integer certid) {
        this.id = id;
        this.accttype = accttype;
        this.certid = certid;
    }

    public TAccountTypeCert() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccttype() {
        return accttype;
    }

    public void setAccttype(String accttype) {
        this.accttype = accttype == null ? null : accttype.trim();
    }

    public Integer getCertid() {
        return certid;
    }

    public void setCertid(Integer certid) {
        this.certid = certid;
    }
}