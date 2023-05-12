//package com.hxx.sbConsole.module.flexmark;
//
//import com.vladsch.flexmark.html.HtmlRenderer;
//import com.vladsch.flexmark.parser.Parser;
//import com.vladsch.flexmark.util.ast.Document;
//import com.vladsch.flexmark.util.data.MutableDataSet;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.function.Consumer;
//
///**
// * @Author: huoxuxu
// * @Description:
// * @Date: 2023-05-11 10:25:06
// **/
//public class MarkdownUtil {
//    private static final MutableDataSet OPTIONS = new MutableDataSet(
//            PegdownOptionsAdapter.flexmarkOptions(
//                    true,
//                    // 所有的特性
//                    Extensions.ALL,
//                    // 自定义的 Link Target 扩展
//                    LinkTargetExtensions.create()
//            ))
//            .set(HtmlRenderer.SOFT_BREAK, "<br/>");
//
//    // 解析器
//    private static final Parser PARSER = Parser.builder(OPTIONS)
//            .build();
//
//    // 渲染器
//    private static final HtmlRenderer htmlRender = HtmlRenderer.builder(OPTIONS)
//            .build();
//
//    /**
//     * 渲染 Markdown
//     *
//     * @param markdown              文档
//     * @param consumer 用于动态改变 LinkTargetAttributeProvider的配置参数
//     * @return html
//     */
//    public static String renderHtml(String markdown, Consumer<Document> consumer) {
//        if (StringUtils.isEmpty(markdown)) {
//            return "";
//        }
//        Document document = PARSER.parse(markdown);
//        if (consumer != null) {
//            consumer.accept(document);
//        }
//        return htmlRender.render(document);
//    }
//}
