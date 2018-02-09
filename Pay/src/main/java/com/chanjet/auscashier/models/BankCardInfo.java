package com.chanjet.auscashier.models;

/**
 * Created by Administrator on 2017/4/21.
 */

public class BankCardInfo {

    /**
     * bankName : 建设银行
     * bindingStatus : 1
     * bkAcctTp : 01
     * cardBegin : 621700
     * cardEnd : 4762
     * mobNo : 15*******62
     * modifyTime : 20170419143739
     */

    private String bankName;
    private String bindingStatus;
    private String bkAcctTp;
    private String cardBegin;
    private String cardEnd;
    private String mobNo;
    private String modifyTime;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBindingStatus() {
        return bindingStatus;
    }

    public void setBindingStatus(String bindingStatus) {
        this.bindingStatus = bindingStatus;
    }

    public String getBkAcctTp() {
        return bkAcctTp;
    }

    public void setBkAcctTp(String bkAcctTp) {
        this.bkAcctTp = bkAcctTp;
    }

    public String getCardBegin() {
        return cardBegin;
    }

    public void setCardBegin(String cardBegin) {
        this.cardBegin = cardBegin;
    }

    public String getCardEnd() {
        return cardEnd;
    }

    public void setCardEnd(String cardEnd) {
        this.cardEnd = cardEnd;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
