package com.dynxsty.linkchecker.properties;

import java.util.ArrayList;

public class ConfigInt extends ConfigElement {
    private int value;

    public ConfigInt(String entryname, int defaultvalue) {
        super(entryname);
        if(this.isRegisteredInConfig()){
            try{
                this.value = Integer.parseInt(this.load());
            }catch (Exception e){
                System.out.println("Failed whilst loading: " + entryname);
                e.printStackTrace();
            }
        }else{
            try{
                this.save(String.valueOf(defaultvalue));
            }catch (Exception e){
                System.out.println("Failed whilst saving: " + entryname);
                e.printStackTrace();
            }
        }
    }



    public int getValue(){
        return this.value;
    }

    public void setValue(int value){
        this.value = value;
        try{
            this.save(String.valueOf(value));
        }catch (Exception e){
            System.out.println("Failed whilst saving: " + entryname);
            e.printStackTrace();
        }
    }

}
