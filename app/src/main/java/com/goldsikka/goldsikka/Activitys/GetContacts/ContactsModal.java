package com.goldsikka.goldsikka.Activitys.GetContacts;

public class ContactsModal {
    String phoneNumber,ContacytName;
    int contactimg;
    private boolean isChecked = false;
  public   ContactsModal(String phoneNumber,String name){
      this.ContacytName = name;
      this.phoneNumber = phoneNumber;
  }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getContacytName() {
        return ContacytName;
    }

    public void setContacytName(String contacytName) {
        ContacytName = contacytName;
    }

    public int getContactimg() {
        return contactimg;
    }

    public void setContactimg(int contactimg) {
        this.contactimg = contactimg;
    }
}
