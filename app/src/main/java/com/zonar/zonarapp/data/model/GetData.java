package com.zonar.zonarapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetData {

    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Gender")
    @Expose
    private String Gender;
    @SerializedName("Birthday")
    @Expose
    private String Birthday;
    @SerializedName("E_mail")
    @Expose
    private String E_mail;
    @SerializedName("data1")
    @Expose
    private String data1;

    public String getName(){
        return name;
    }

    public String getGender(){
        return Gender;
    }

    public String getBirthday(){
        return Birthday;
    }

    public String getE_mail(){
        return E_mail;
    }

    public String getData1(){
        return data1;
    }

    @Override
    public String toString() {
        return "Get{" +
                "pk='" + pk + '\'' +
                "name='" + name + '\'' +
                "Gender='" + Gender + '\'' +
                "Birthday='" + Birthday + '\'' +
                "E_mail='" + E_mail + '\'' +
                "data1='" + data1 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetData user_data = (GetData) o;

        if (name != null ? !name.equals(user_data.name) : user_data.name != null) return false;
        if (Gender != null ? !Gender.equals(user_data.Gender) : user_data.Gender != null) return false;
        if (Birthday != null ? !Birthday.equals(user_data.Birthday) : user_data.Birthday != null) return false;
        if (data1 != null ? !data1.equals(user_data.data1) : user_data.data1 != null) return false;
        if (E_mail != null ? !E_mail.equals(user_data.E_mail) : user_data.E_mail != null) return false;
        return pk != null ? pk.equals(user_data.pk) : user_data.pk == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (Birthday != null ? Birthday.hashCode() : 0);
        result = 31 * result + (Gender != null ? Gender.hashCode() : 0);
        result = 31 * result + (E_mail != null ? E_mail.hashCode() : 0);
        result = 31 * result + (data1 != null ? data1.hashCode() : 0);
        result = 31 * result + (pk != null ? pk.hashCode() : 0);
        return result;
    }
}
