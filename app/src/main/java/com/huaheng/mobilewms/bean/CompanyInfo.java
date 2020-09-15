package com.huaheng.mobilewms.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author Enzo Cotter
 * @date 2019/12/15
 */
public class CompanyInfo implements Parcelable {

    private int companyId;
    private String companyCode;
    private String companyName;

    public CompanyInfo(int companyId, String companyCode, String companyName) {
        this.companyId = companyId;
        this.companyCode = companyCode;
        this.companyName = companyName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "CompanyInfo{" +
                "companyId=" + companyId +
                ", companyCode='" + companyCode + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(companyName);
        parcel.writeString(companyCode);
        parcel.writeInt(companyId);
    }

    public static final Creator<CompanyInfo> CREATOR
            = new Creator<CompanyInfo>() {
        @Override
        public CompanyInfo createFromParcel(Parcel in) {
            return new CompanyInfo(in);
        }
        @Override
        public CompanyInfo[] newArray(int size) {
            return new CompanyInfo[size];
        }
    };

    private CompanyInfo(Parcel in) {
        companyName = in.readString();
        companyCode = in.readString();
        companyId = in.readInt();
    }
}
