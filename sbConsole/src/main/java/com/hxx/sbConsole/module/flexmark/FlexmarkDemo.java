//package com.hxx.sbConsole.module.flexmark;
//
//import com.hxx.sbcommon.common.io.FileUtil;
//import com.vladsch.flexmark.html.HtmlRenderer;
//import com.vladsch.flexmark.parser.Parser;
//import com.vladsch.flexmark.util.ast.Document;
//import com.vladsch.flexmark.util.ast.Node;
//import com.vladsch.flexmark.util.data.MutableDataSet;
//import org.springframework.util.ResourceUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//
///**
// * @Author: huoxuxu
// * @Description:
// * @Date: 2023-05-11 10:13:13
// **/
//public class FlexmarkDemo {
//    public static void main(String[] args) {
//        try{
//            File mdFile= ResourceUtils.getFile("classpath:demo/Markdown示例.md");
//            case0(mdFile);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        System.out.println("ok");
//    }
//
//    static void case0(File mdFile) throws IOException {
//        MutableDataSet options = new MutableDataSet();
//
//        // uncomment to set optional extensions
//        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
//
//        // uncomment to convert soft-breaks to hard breaks
//        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
//
//        Parser parser = Parser.builder(options).build();
//        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
//
//        // You can re-use parser and renderer instances
//        String mdTxt = FileUtil.readAllTxt(mdFile, StandardCharsets.UTF_8);
//        Node document = parser.parse(mdTxt);
//        String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
//        System.out.println(html);
//    }
//
////    static void case1(){
////        String markdown = "[测试1](http://www.itlangzi.com/test1 '测试1') [测试2](/test2 '测试2') [测试3](https://www.google.com/test3 '测试3')";
////
////        System.out.println(MarkdownUtil.renderHtml(markdown, doc -> {
////            doc.set(LinkTargetExtensions.LINK_TARGET, new LinkTargetProperties(
////                    // 需要过滤的域名
////                    Arrays.asList("www.itlangzi.com","www.baidu.com"),
////                    // target 属性的值
////                    "_target",
////                    // 排除相对路径
////                    true
////            ));
////        }));
////    }
//
//    /**
//     * 将入参Markdown文档转换为html文档
//     */
//    public static String mdToHtmlForApiDoc(String md) {
//        // 按需添加扩展
//        MutableDataSet options = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(
////                        // 自定义扩展，为<pre>标签添加line-numbers的class，用于prism库代码左侧行号展示
////                        CodePreLineNumbersExtension.create(),
////                        AutolinkExtension.create(),
////                        EmojiExtension.create(),
////                        StrikethroughExtension.create(),
////                        TaskListExtension.create(),
////                        TablesExtension.create(),
////                        TocExtension.create()
//                ))
////                // set GitHub table parsing options
////                .set(TablesExtension.WITH_CAPTION, false)
////                .set(TablesExtension.COLUMN_SPANS, false)
////                .set(TablesExtension.MIN_HEADER_ROWS, 1)
////                .set(TablesExtension.MAX_HEADER_ROWS, 1)
////                .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
////                .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
////                .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
////                // setup emoji shortcut options
////                // uncomment and change to your image directory for emoji images if you have it setup
//////                .set(EmojiExtension.ROOT_IMAGE_PATH, emojiInstallDirectory())
////                .set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB)
////                .set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.IMAGE_ONLY)
//                ;
//        return mdToHtml(md, options);
//    }
//    private static String mdToHtml(String md, MutableDataSet options) {
//        // uncomment to convert soft-breaks to hard breaks
////        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
//        Parser parser = Parser.builder(options).build();
//        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
//        Document document = parser.parse(md);
//        return renderer.render(document);
//    }
//
//}
