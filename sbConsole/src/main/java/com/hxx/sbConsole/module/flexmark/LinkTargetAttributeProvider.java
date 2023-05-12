//package com.hxx.sbConsole.module.flexmark;
//
//import com.vladsch.flexmark.ast.Link;
//import com.vladsch.flexmark.html.AttributeProvider;
//import com.vladsch.flexmark.html.AttributeProviderFactory;
//import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
//import com.vladsch.flexmark.html.renderer.AttributablePart;
//import com.vladsch.flexmark.html.renderer.LinkResolverContext;
//import com.vladsch.flexmark.util.ast.Node;
//import com.vladsch.flexmark.util.data.DataHolder;
//import com.vladsch.flexmark.util.html.Attribute;
//import com.vladsch.flexmark.util.html.Attributes;
//import com.vladsch.flexmark.util.html.MutableAttributes;
//import org.apache.commons.lang3.StringUtils;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.regex.Pattern;
//
///**
// * @Author: huoxuxu
// * @Description:
// * @Date: 2023-05-11 10:28:05
// **/
//public class LinkTargetAttributeProvider implements AttributeProvider {
//    // 用于获取配置的数据
//    private final DataHolder dataHolder;
//    // 绝对路径正则匹配
//    private final Pattern pattern = Pattern.compile("^[a-zA-z]+://[^\\s]*");
//
//    public LinkTargetAttributeProvider(DataHolder dataHolder) {
//        this.dataHolder = dataHolder;
//    }
//
//    @Override
//    public void setAttributes(@NotNull Node node, @NotNull AttributablePart part, @NotNull MutableAttributes attributes) {
//        // 只处理 Link
//        if (node instanceof Link && part == AttributablePart.LINK) {
//
//            // 获取 href 标签
//            Attribute hrefAttr = attributes.get("href");
//            if (hrefAttr == null) {
//                return;
//            }
//            // 值也不能为空
//            String href = hrefAttr.getValue();
//            if (StringUtils.isEmpty(href)) {
//                return;
//            }
//
//            // 获取配置参数
//            // 注意此处不能直接使用 Spring Boot 的依赖注入
//            // 但可以使用ApplicatonContext.getBean的形式获取
//            LinkTargetProperties dataKey = FlexmarkExtensions.LINK_TARGET.get(this.dataHolder);
//
//            // 判断是否是绝对路径
//            if (!pattern.matcher(href)
//                    .matches()) {
//                if (dataKey.isRelativeExclude()) {
//                    // 如果是相对路径，则排除
//                    return;
//                }
//            } else {
//
//                // 获取域名/host
//                Optional<String> host = ServletUtil.getHost(href);
//                if (host.isEmpty()) {
//                    return;
//                }
//                List<String> excludes = dataKey.getExcludes();
//                if (excludes != null && !excludes.isEmpty()) {
//
//                    // 如果包含当前的host则排除
//                    if (excludes.contains(host.get())) {
//                        return;
//                    }
//                }
//            }
//            String target = dataKey.getTarget();
//            if (StringUtils.isEmpty(target)) {
//                target = "";
//            }
//            // 设置target 属性
//            attributes.replaceValue("target", target);
//        }
//    }
//
//    static AttributeProviderFactory factory() {
//        return new IndependentAttributeProviderFactory() {
//            @Override
//            public @NotNull AttributeProvider apply(@NotNull LinkResolverContext linkResolverContext) {
//                // 在此处获取dataHolder
//                return new LinkTargetAttributeProvider(linkResolverContext.getOptions());
//            }
//        };
//    }
//}