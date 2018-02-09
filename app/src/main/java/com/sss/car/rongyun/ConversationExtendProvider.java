package com.sss.car.rongyun;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.plugin.CombineLocationPlugin;
import io.rong.imkit.plugin.DefaultLocationPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;

/**
 * Created by leilei on 2017/6/10.
 */

public class ConversationExtendProvider extends DefaultExtensionModule {
    private List<IExtensionModule> moduleList;
    private List<IPluginModule> pluginModules;
    IExtensionModule defaultModule;
    private ImagePlugin imagePlugin;
    private FilePlugin filePlugin;
    private CombineLocationPlugin combineLocationPlugin;
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
//        List<IPluginModule> pluginModules =  super.getPluginModules(conversationType);
        pluginModules=new ArrayList<>();
        imagePlugin=new ImagePlugin();
        pluginModules.add(imagePlugin);


//        Class cls;
//        try {
//            cls = Class.forName("com.amap.api.netlocation.AMapNetworkLocationClient");
//            if(cls != null) {
//                CombineLocationPlugin constructor = new CombineLocationPlugin();
//                DefaultLocationPlugin recognizer = new DefaultLocationPlugin();
//                if(conversationType.equals(Conversation.ConversationType.PRIVATE)) {
//                    pluginModules.add(constructor);
//                } else {
//                    pluginModules.add(recognizer);
//                }
//            }
//        } catch (Exception var10) {
//            Log.e("bg_circular_bead_dialog", "Not include AMap");
//            var10.printStackTrace();
//        }


//        filePlugin=new FilePlugin();
//        pluginModules.add(filePlugin);


//        combineLocationPlugin=  new CombineLocationPlugin();
//        pluginModules.add(combineLocationPlugin);
        return pluginModules;
    }

    @Override
    public void onDisconnect() {
        super.onDisconnect();
        if (pluginModules!=null){
            pluginModules.clear();
        }
        pluginModules=null;
        if (moduleList!=null){
            moduleList.clear();
        }
        moduleList=null;
        defaultModule=null;
        imagePlugin=null;
        combineLocationPlugin=null;
        Runtime.getRuntime().gc();
    }

    public void setMyExtensionModule() {
        moduleList = RongExtensionManager.getInstance().getExtensionModules();
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                defaultModule=null;
                RongExtensionManager.getInstance().registerExtensionModule(this);
            }
        }

    }
}
