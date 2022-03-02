package com.flyhub.ideaMS.dao.module;

public enum SystemModule {

    /**
     * This is the enum name for the api user controller class
     */
    MERCHANT_MODULE(SystemModuleNames.MERCHANT_CONTROLLER),

    /**
     * This is the enum name for the access control controller class
     */
    APP_SECURITY_MODULE(SystemModuleNames.APP_SECURITY_CONTROLLER),
    /**
     * this enum name represents the Application Config class
     */
    APP_CONFIG_MODULE(SystemModuleNames.APP_CONFIG_CONTROLLER);

    private final String name;

    private SystemModule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
