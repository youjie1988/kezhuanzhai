package com.huaheng.mobilewms.bean;

public class Materialforecast {

    private String materialCode;
    private String materialName;
    private String specification;
    private int type;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    @Override
    public String toString() {
        return "Materialforecast{" +
                "materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", specification='" + specification + '\'' +
                ", type=" + type +
                '}';
    }
}
