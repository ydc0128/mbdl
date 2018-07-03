package com.example.administrator.ydxcgld.Bean;

/**
 * Created by Administrator on 2017/9/16.
 */

public class Daili {

    private int	AG_ID;
    private	String AG_Name;	//代理商姓名
    private	int AG_Sex;	//代理商性别0是女，1是男
    private	String AG_Pwd;	//代理商密码
    private	String AG_Company;	//代理商公司名
    private	String AG_Province;	//代理省（如果代理省填写，没代理整个省为空
    private String AG_City;	//代理市（如果代理市填写，没代理整个市为空）
    private	String AG_County;	//代理县（如果代理县填写，没代理为空）
    private	int AG_IdentityType;	//证件类型（1居民身份证、2护照、3军官证、4驾驶证、5港澳回乡证台胞证）
    private	String AG_IdentityCard;	//证件号码
    private	String AG_Tel;	//手机号
    private	String AG_CreatTime;	//注册时间
    private	String AG_UpdateTime;	//修改时间
    private	int AG_IsLock;	//锁定账户,0表示正常，1锁定
    private	int AG_Isonline;	//是否在线,1在线，0下线，默认0
    private	String AG_IMEI;	//校验码
    private	String AG_IdentityID;	//保存验证码
    private	String AG_BankName;	//开户行名称
    private	String AG_BankCard;	//开户行卡号
    private	String AG_BankAdress;	//开户行地址50个字符
    private	String AG_Image;	//代理商照片
    private	String AG_IdentityCardImage1;	//身份证正面拍照
    private	String AG_IdentityCardImage2;	//身份证背面拍照
    private	String AG_IdentityCardImage3;	//手持身份证拍照
    private	int AG_LSID;	//临时列，当注册时候，写入一个临时ID
    private	String AG_Remark;	//100	0	 	 	 	是

    public Daili() {
    }

    @Override
    public String toString() {
        return "Daili{" +
                "AG_BankAdress='" + AG_BankAdress + '\'' +
                ", AG_ID=" + AG_ID +
                ", AG_Name='" + AG_Name + '\'' +
                ", AG_Sex=" + AG_Sex +
                ", AG_Pwd='" + AG_Pwd + '\'' +
                ", AG_Company='" + AG_Company + '\'' +
                ", AG_Province='" + AG_Province + '\'' +
                ", AG_City='" + AG_City + '\'' +
                ", AG_County='" + AG_County + '\'' +
                ", AG_IdentityType=" + AG_IdentityType +
                ", AG_IdentityCard='" + AG_IdentityCard + '\'' +
                ", AG_Tel='" + AG_Tel + '\'' +
                ", AG_CreatTime='" + AG_CreatTime + '\'' +
                ", AG_UpdateTime='" + AG_UpdateTime + '\'' +
                ", AG_IsLock=" + AG_IsLock +
                ", AG_Isonline=" + AG_Isonline +
                ", AG_IMEI='" + AG_IMEI + '\'' +
                ", AG_IdentityID='" + AG_IdentityID + '\'' +
                ", AG_BankName='" + AG_BankName + '\'' +
                ", AG_BankCard='" + AG_BankCard + '\'' +
                ", AG_Image='" + AG_Image + '\'' +
                ", AG_IdentityCardImage1='" + AG_IdentityCardImage1 + '\'' +
                ", AG_IdentityCardImage2='" + AG_IdentityCardImage2 + '\'' +
                ", AG_IdentityCardImage3='" + AG_IdentityCardImage3 + '\'' +
                ", AG_LSID=" + AG_LSID +
                ", AG_Remark='" + AG_Remark + '\'' +
                '}';
    }

    public String getAG_BankAdress() {
        return AG_BankAdress;
    }

    public void setAG_BankAdress(String AG_BankAdress) {
        this.AG_BankAdress = AG_BankAdress;
    }

    public String getAG_BankCard() {
        return AG_BankCard;
    }

    public void setAG_BankCard(String AG_BankCard) {
        this.AG_BankCard = AG_BankCard;
    }

    public String getAG_BankName() {
        return AG_BankName;
    }

    public void setAG_BankName(String AG_BankName) {
        this.AG_BankName = AG_BankName;
    }

    public String getAG_City() {
        return AG_City;
    }

    public void setAG_City(String AG_City) {
        this.AG_City = AG_City;
    }

    public String getAG_Company() {
        return AG_Company;
    }

    public void setAG_Company(String AG_Company) {
        this.AG_Company = AG_Company;
    }

    public String getAG_County() {
        return AG_County;
    }

    public void setAG_County(String AG_County) {
        this.AG_County = AG_County;
    }

    public String getAG_CreatTime() {
        return AG_CreatTime;
    }

    public void setAG_CreatTime(String AG_CreatTime) {
        this.AG_CreatTime = AG_CreatTime;
    }

    public int getAG_ID() {
        return AG_ID;
    }

    public void setAG_ID(int AG_ID) {
        this.AG_ID = AG_ID;
    }

    public String getAG_IdentityCard() {
        return AG_IdentityCard;
    }

    public void setAG_IdentityCard(String AG_IdentityCard) {
        this.AG_IdentityCard = AG_IdentityCard;
    }

    public String getAG_IdentityCardImage1() {
        return AG_IdentityCardImage1;
    }

    public void setAG_IdentityCardImage1(String AG_IdentityCardImage1) {
        this.AG_IdentityCardImage1 = AG_IdentityCardImage1;
    }

    public String getAG_IdentityCardImage2() {
        return AG_IdentityCardImage2;
    }

    public void setAG_IdentityCardImage2(String AG_IdentityCardImage2) {
        this.AG_IdentityCardImage2 = AG_IdentityCardImage2;
    }

    public String getAG_IdentityCardImage3() {
        return AG_IdentityCardImage3;
    }

    public void setAG_IdentityCardImage3(String AG_IdentityCardImage3) {
        this.AG_IdentityCardImage3 = AG_IdentityCardImage3;
    }

    public String getAG_IdentityID() {
        return AG_IdentityID;
    }

    public void setAG_IdentityID(String AG_IdentityID) {
        this.AG_IdentityID = AG_IdentityID;
    }

    public int getAG_IdentityType() {
        return AG_IdentityType;
    }

    public void setAG_IdentityType(int AG_IdentityType) {
        this.AG_IdentityType = AG_IdentityType;
    }

    public String getAG_Image() {
        return AG_Image;
    }

    public void setAG_Image(String AG_Image) {
        this.AG_Image = AG_Image;
    }

    public String getAG_IMEI() {
        return AG_IMEI;
    }

    public void setAG_IMEI(String AG_IMEI) {
        this.AG_IMEI = AG_IMEI;
    }

    public int getAG_IsLock() {
        return AG_IsLock;
    }

    public void setAG_IsLock(int AG_IsLock) {
        this.AG_IsLock = AG_IsLock;
    }

    public int getAG_Isonline() {
        return AG_Isonline;
    }

    public void setAG_Isonline(int AG_Isonline) {
        this.AG_Isonline = AG_Isonline;
    }

    public int getAG_LSID() {
        return AG_LSID;
    }

    public void setAG_LSID(int AG_LSID) {
        this.AG_LSID = AG_LSID;
    }

    public String getAG_Name() {
        return AG_Name;
    }

    public void setAG_Name(String AG_Name) {
        this.AG_Name = AG_Name;
    }

    public String getAG_Province() {
        return AG_Province;
    }

    public void setAG_Province(String AG_Province) {
        this.AG_Province = AG_Province;
    }

    public String getAG_Pwd() {
        return AG_Pwd;
    }

    public void setAG_Pwd(String AG_Pwd) {
        this.AG_Pwd = AG_Pwd;
    }

    public String getAG_Remark() {
        return AG_Remark;
    }

    public void setAG_Remark(String AG_Remark) {
        this.AG_Remark = AG_Remark;
    }

    public int getAG_Sex() {
        return AG_Sex;
    }

    public void setAG_Sex(int AG_Sex) {
        this.AG_Sex = AG_Sex;
    }

    public String getAG_Tel() {
        return AG_Tel;
    }

    public void setAG_Tel(String AG_Tel) {
        this.AG_Tel = AG_Tel;
    }

    public String getAG_UpdateTime() {
        return AG_UpdateTime;
    }

    public void setAG_UpdateTime(String AG_UpdateTime) {
        this.AG_UpdateTime = AG_UpdateTime;
    }
}
