package com.pam.brewcraft;

import java.util.HashMap;

public class ItemModelList {
    private final HashMap<Integer, String> registrations = new HashMap<Integer, String>();

    private final String rootDirectory;

    public ItemModelList(String resourceRoot) {
        if (resourceRoot.charAt(resourceRoot.length() - 1) != '/') {
            throw new RuntimeException("Resource root path must be relative! (end with '/')");
        }

        this.rootDirectory = getResourcePath(resourceRoot);
    }

    public ItemModelList add(int meta, String path) {
        this.registrations.put(meta, this.rootDirectory != null ? this.rootDirectory + path : getResourcePath(path));

        return this;
    }

    HashMap<Integer, String> getRegistrations() {
        return this.registrations;
    }

    private static String getResourcePath(String resource) {
        return (Reference.MODID + ":") + resource;
    }

}

