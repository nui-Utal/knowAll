package com.example.newbst.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.util.List;

/**
 * created by Xu on 2023/8/26 12:01.
 */
public class HtmlFilter {

    /**
     * 默认使用relaxed()
     * 允许的标签: a, b, blockquote, br, caption, cite, code, col, colgroup, dd, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q, small, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul。结果不包含标签rel=nofollow ，如果需要可以手动添加。
     */
    private Whitelist whiteList;


    /**
     * 配置过滤化参数,不对代码进行格式化
     */
    private Document.OutputSettings outputSettings;


    private HtmlFilter() {
    }

    /**
     * 静态创建HtmlFilter方法
     * @param whiteList 白名单标签
     * @param pretty 是否格式化
     * @return HtmlFilter
     */
    public static HtmlFilter create(Whitelist whiteList, boolean pretty) {
        HtmlFilter filter = new HtmlFilter();
        if (whiteList == null) {
            filter.whiteList = Whitelist.relaxed();
        }
        filter.outputSettings = new Document.OutputSettings().prettyPrint(pretty);
        return filter;
    }

    /**
     * 静态创建HtmlFilter方法
     * @return HtmlFilter
     */
    public static HtmlFilter create() {
        return create(null, false);
    }

    /**
     * 静态创建HtmlFilter方法
     * @param whiteList 白名单标签
     * @return HtmlFilter
     */
    public static HtmlFilter create(Whitelist whiteList) {
        return create(whiteList, false);
    }

    /**
     * 静态创建HtmlFilter方法
     * @param excludeTags 例外的特定标签
     * @param includeTags 需要过滤的特定标签
     * @param pretty   是否格式化
     * @return HtmlFilter
     */
    public static HtmlFilter create( List<String> excludeTags,List<String> includeTags, boolean pretty) {
        HtmlFilter filter = create(null, pretty);
        //要过滤的标签
        if (includeTags != null && !includeTags.isEmpty()) {
            String[] tags = (String[]) includeTags.toArray(new String[0]);
            filter.whiteList.removeTags(tags);
        }
        //例外标签
        if (excludeTags != null && !excludeTags.isEmpty()) {
            String[] tags = (String[]) excludeTags.toArray(new String[0]);
            filter.whiteList.addTags(tags);
        }
        return filter;
    }


    /**
     * 静态创建HtmlFilter方法
     * @param excludeTags 例外的特定标签
     * @param includeTags 需要过滤的特定标签
     * @return HtmlFilter
     */
    public static HtmlFilter create(List<String> excludeTags, List<String> includeTags) {
        return create( includeTags, excludeTags, false );
    }

    /**
     * @param content 需要过滤内容
     * @return 过滤后的String
     */
    public String clean(String content) {
        return Jsoup.clean(content, "", this.whiteList, this.outputSettings);

    }

}

