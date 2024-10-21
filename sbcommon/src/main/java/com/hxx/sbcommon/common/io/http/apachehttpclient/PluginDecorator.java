package com.hxx.sbcommon.common.io.http.apachehttpclient;

public class PluginDecorator<T extends Class> implements Comparable{
    T plugin;
    int order;
    Object instance;

    public PluginDecorator(T plugin){
        this(plugin, 0);
    }

    public PluginDecorator(T plugin, int order){
        this.plugin = plugin;
        this.order = order;
    }

    public void setInstance(Object obj){
        this.instance = obj;
    }

    public Object getInstance(){
        return this.instance;
    }

    public T getPlugin() {
        return plugin;
    }

    public void setPlugin(T plugin) {
        this.plugin = plugin;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(Object o) {
        if (! (o instanceof PluginDecorator)) {
            throw new IllegalArgumentException("PluginDecorator can't compare with other type!");
        }
        PluginDecorator target = (PluginDecorator)o;
        if (this.order == target.order){
            return 0;
        }
        return this.order > target.order ? 1 : -1;
    }
}
