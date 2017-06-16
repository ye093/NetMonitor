package com.pingan.aop.gradle
/**
 * class description here
 * @author yejy
 * @version 1.0.0
 * @since 2016-05-05
 */
class AspectjExtension {

    List<String> includeJarFilter = new ArrayList<String>()
    List<String> excludeJarFilter = new ArrayList<String>()
    List<String> ajcArgs=new ArrayList<>();

    public AspectjExtension includeJarFilter(String...filters) {
        if (filters != null) {
            includeJarFilter.addAll(filters)
        }

        return this
    }

    public AspectjExtension excludeJarFilter(String...filters) {
        if (filters != null) {
            excludeJarFilter.addAll(filters)
        }

        return this
    }
    public AspectjExtension ajcArgs(String...ajcArgs) {
        if (ajcArgs != null) {
            this.ajcArgs.addAll(ajcArgs)
        }
        return this
    }
}