package com.example.mukhammadazizkhon.locationhelper.Model;

/**
 * Created by Mukha on 9/27/2018.
 */

public class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;

   public Weather(){
   }

   public int getID(){
       return id;
   }
   public void setId(int id){
       this.id = id;
   }


    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
