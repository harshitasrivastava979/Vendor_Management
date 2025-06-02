package com.vendormanagement.vendor_management_system.dto;

public class VendorRequestDto {
    private String name;
    private String address;
    private String contact;
    private String gstTanCin;
    private String bankAcc;
    private String ifsc;
    private Boolean neftEnabled;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getGstTanCin() { return gstTanCin; }
    public void setGstTanCin(String gstTanCin) { this.gstTanCin = gstTanCin; }

    public String getBankAcc() { return bankAcc; }
    public void setBankAcc(String bankAcc) { this.bankAcc = bankAcc; }

    public String getIfsc() { return ifsc; }
    public void setIfsc(String ifsc) { this.ifsc = ifsc; }

    public Boolean getNeftEnabled() { return neftEnabled; }
    public void setNeftEnabled(Boolean neftEnabled) { this.neftEnabled = neftEnabled; }
}
