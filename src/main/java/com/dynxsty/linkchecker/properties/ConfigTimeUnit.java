package com.dynxsty.linkchecker.properties;

import java.util.concurrent.TimeUnit;

public class ConfigTimeUnit extends ConfigElement {
    private String value;

    public ConfigTimeUnit(String entryname, TimeUnit defaultvalue) {
        super(entryname);
        if(this.isRegisteredInConfig()){
            try{
                this.value = this.load();
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
    public TimeUnit getValue(){ return TimeUnit.valueOf(this.value); }

    public void setValue(TimeUnit value){
        this.value = String.valueOf(value);
        try{
            this.save(String.valueOf(value));
        }catch (Exception e){
            System.out.println("Failed whilst saving: " + entryname);
            e.printStackTrace();
        }
    }


}
