package com.weixin.vo;

import java.io.Serializable;

/**
 * 微信相应实体
 * @author 梁嘉贺
 * @date 2018年4月3日 下午2:14:59
 * @Description 微信相应实体
 */
public class ResponseInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 返回码
     */
    private String errcode;
    
    /**
     * 返回信息
     */
    private String errmsg;

    /**
     * 获取时间
     */
    private Long initialTimeStamp;
    
    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Long getInitialTimeStamp() {
        return initialTimeStamp;
    }

    public void setInitialTimeStamp(Long initialTimeStamp) {
        this.initialTimeStamp = initialTimeStamp;
    }
    
}
