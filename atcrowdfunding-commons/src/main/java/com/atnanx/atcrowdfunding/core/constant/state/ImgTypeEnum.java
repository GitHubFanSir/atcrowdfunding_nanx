package com.atnanx.atcrowdfunding.core.constant.state;

public enum ImgTypeEnum {

    HEADER_IMG((byte)0,"头图"),
    DETAIL_IMG((byte)1,"详情图");

    private byte code;//给数据库保存
    private String msg;//给人看的

    private ImgTypeEnum(byte code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
