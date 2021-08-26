package com.hxx.sbweb.model;

public class ApiCommonParam {
    private String appKey;//发放给用户的Key
    private String version;//接口版本
    private String ownerCompanyCode;//数据所属公司编码
    private String operateCompanyCode;//操作者所属公司编码
    private String sign;//签名
    private Long timestamps;//时间戳
    private Object reserver;//扩展字段

    public ApiCommonParam() {}

    public String getAppKey() {
        return appKey;
    }
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getOwnerCompanyCode() {
        return ownerCompanyCode;
    }
    public void setOwnerCompanyCode(String ownerCompanyCode) {
        this.ownerCompanyCode = ownerCompanyCode;
    }
    public String getOperateCompanyCode() {
        return operateCompanyCode;
    }
    public void setOperateCompanyCode(String operateCompanyCode) {
        this.operateCompanyCode = operateCompanyCode;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public Object getReserver() {
        return reserver;
    }
    public void setReserver(Object reserver) {
        this.reserver = reserver;
    }
    public Long getTimestamps() {
        return timestamps;
    }
    public void setTimestamps(Long timestamps) {
        this.timestamps = timestamps;
    }


}
