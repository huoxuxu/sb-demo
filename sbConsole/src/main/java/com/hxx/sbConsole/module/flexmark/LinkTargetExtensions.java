//package com.hxx.sbConsole.module.flexmark;
//
//import com.vladsch.flexmark.html.HtmlRenderer;
//import com.vladsch.flexmark.util.data.DataKey;
//import com.vladsch.flexmark.util.data.MutableDataHolder;
//
//import javax.validation.constraints.NotNull;
//
///**
// * @Author: huoxuxu
// * @Description:
// * @Date: 2023-05-11 10:26:19
// **/
//public class LinkTargetExtensions implements HtmlRenderer.HtmlRendererExtension {
//    // 定义配置参数
//    // 并设置默认值
//    public static final DataKey<LinkTargetProperties> LINK_TARGET = new DataKey<>("LINK_TARGET", new LinkTargetProperties());
//    @Override
//    public void rendererOptions(@NotNull MutableDataHolder mutableDataHolder) {
//
//    }
//
//    @Override
//    public void extend(HtmlRenderer.@NotNull Builder builder, @NotNull String s) {
//        builder.attributeProviderFactory(LinkTargetAttributeProvider.factory());
//    }
//
//    public static LinkTargetExtensions create() {
//        return new LinkTargetExtensions();
//    }
//}
//
