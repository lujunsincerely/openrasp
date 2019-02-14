package com.baidu.openrasp.detector;

import com.baidu.openrasp.HookHandler;
import com.baidu.openrasp.tool.model.ApplicationModel;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.security.ProtectionDomain;

/**
 * Created by tyy on 19-2-13.
 */
public class JBossEPADetector extends ServerDetector {


    @Override
    public boolean isClassMatched(String className) {
        return "org/jboss/modules/Main".equals(className);
    }

    @Override
    public void handleServerInfo(ClassLoader classLoader, ProtectionDomain domain) {
        String serverVersion = "";
        try {
            Method getPackageMethod = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
            getPackageMethod.setAccessible(true);
            Package jbossBootPackage = (Package) getPackageMethod.invoke(classLoader, "org.jboss.modules");
            serverVersion = jbossBootPackage.getSpecificationVersion();
        } catch (Throwable t) {
            logDetectError("handle jboss epa startup failed", t);
        } finally {
            ApplicationModel.initServerInfo("jboss epa", serverVersion);
        }
    }

}