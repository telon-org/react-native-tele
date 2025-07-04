package one.telefon.tele;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeleModulePackage implements ReactPackage {

    public TeleModulePackage() {

    }

    @Override
    public List<NativeModule> createNativeModules(
            ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new TeleModule(reactContext));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
        /*
        return Arrays.<ViewManager>asList(
            new PjSipRemoteVideoViewManager(),
            new PjSipPreviewVideoViewManager()
        );*/
    }
}
