package com.tfg.jaset.TFG_Staff_Plannit.Utilidades;

import org.springframework.context.ConfigurableApplicationContext;

public class SpringContextUtil {
    private static ConfigurableApplicationContext context;;

    public  static  void setContext(ConfigurableApplicationContext applicationContext){
        context=applicationContext;
    }
    public static  ConfigurableApplicationContext getContext(){
        return context;
    }

}
