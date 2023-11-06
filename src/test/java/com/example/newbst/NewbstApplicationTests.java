package com.example.newbst;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.example.newbst.dto.ReturnImg;
import com.example.newbst.dto.ReturnImgUrl;
import com.example.newbst.pojo.Post;
import com.example.newbst.utils.HtmlFilter;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
class NewbstApplicationTests {

//    @Test
//    void testGetLocation() throws Exception {
//        String hostAddress = InetAddress.getLocalHost().getHostAddress();
//        String cityCodeByIp = MapUtils.getCityCodeByIp(hostAddress);
//        System.out.println(cityCodeByIp);
//    }

    @Test
    void addContent() throws IOException {
        String path = this.getClass().getClassLoader().getResource("static").getPath();
        File file = new File(path, "student.text");
        if (!file.exists()) {
            file.createNewFile();
            file.setWritable(true);
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        bufferedWriter.append("xuuuuuuuuuxxxxxxxxxxxxxxxxxx");
        bufferedWriter.close();
    }

    @Test
    void getImgName() {
        String body = "<p>An <b>example</b> link.</p><img src=\"http://192.168.42.169/post/uploadpic/llricjn0.png\"><img src=\"http://192.168.42.169/post/uploadpic/llrid1wa.png\">";
        Document doc = Jsoup.parse(body);
        List<String> imgs = doc.select("img").eachAttr("src").stream()
                .map(s -> s.split("/")).map(arr -> arr[arr.length - 1]).collect(Collectors.toList());
        System.out.println(imgs);
    }

    @Test
    void filterContent() {
        String body = "<p>An <b>example</b> link.</p><img src='http://192.168.42.169/post/uploadpic/llrf81r0.png'/>";
        System.out.println(HtmlFilter.create().clean(body));
    }

    @Test
    public void testEhcache() {
        Long number = new Long("15");
        BigDecimal square = BigDecimal.valueOf(number)
                .multiply(BigDecimal.valueOf(number));
    }
    @Test
    void testRandom() {
        System.out.println((int)(Math.random()*1000000));
    }

    @Test
    void deleteImg() {
        List<String> pictures = new LinkedList<>();
        pictures.add("989-xxxxxxxxxx1");
        pictures.add("989-xxxxxxxxxx2");
        pictures.add("989-xxxxxxxxxx3");
        pictures.add("989-xxxxxxxxxx4");
        pictures.add("989-xxx");
        pictures.add("988-xxxxxxxxxx");
        System.out.println("此时picture是：" + pictures);

        List<String> imgs = new LinkedList<>();
        imgs.add("989-xxxxxxxxxx1");
        imgs.add("989-xxxxxxxxxx2");
        imgs.add("989-xxxxxxxxxx3");
        imgs.add("989-xxxxxxxxxx4");
        System.out.println("最后上传时的文章图片是：" + imgs);
        imgs = imgs.stream().map(s -> s.split("-")[1]).collect(Collectors.toList());

        // 得到这篇文章所有上传过的文件
        List<String> Uploaded = pictures.stream().filter(s -> s.split("-")[0].equals("989")).map(s -> s.split("-")[1])
                .collect(Collectors.toList());
        System.out.println("这篇文章上传过的所有图片：" + Uploaded);


        for (String pic : Uploaded){
            if (!imgs.contains(pic)) {
                // 删除图片
                System.out.println("被从本地删除的图片：" + pic);
            }
            pictures.remove("989" + "-" + pic);
            System.out.println("此时picture是：" + pictures);
        }
    }

    @Test
    void out() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        ReturnImg returnImg = new ReturnImg(0, new ReturnImgUrl(localHost.getHostAddress()
                + "/post/uploadpic/" + "xxxxxx.png"));
        System.out.println(JSONUtil.toJsonStr(returnImg));

    }


    @Test
    void createInnerClass() {
        returnJson returnJson = new returnJson(1, "xxxxx");
        System.out.println(JSONUtil.toJsonStr(returnJson));
    }

    public static class returnJson {
        Integer errno;
        NewbstApplicationTests.data data;
        public returnJson(Integer errno, String url) {
            this.errno = errno;
            this.data = new NewbstApplicationTests.data(url);
        }
    }

    public static class data {
        String url;
        public data(String url) {
            this.url = url;
        }
    }

    @Test
    void getImgLabel() {
        String html = "<p>An <b>example</b> link.</p><img src='http://192.168.139.1:8080/post/uploadpic/lllzv01i.png'/>" +
                "<img src='http://192.168.139.1:8081/post/uploadpic/lllzv01i.png'/>" +
                "<img src='http://192.168.139.1:8082/post/uploadpic/lllzv01i.png'/>" +
                "<img src='http://192.168.139.1:8083/post/uploadpic/lllzv01i.png'/>" +
                "<img src='http://192.168.139.1:8084/post/uploadpic/lllzv01i.png'/>";
        Document doc = Jsoup.parse(html);
        // 得到第一张图片
//        Element link = doc.select("img").get(0);
//        System.out.println(link);
        // 得到所有图片
//        List<String> strings = doc.select("img").eachAttr("src");
//        System.out.println(strings);
//
//        String linkHref = link.attr("src"); // "http://example.com/"
//        System.out.println(linkHref);

        Elements img1 = doc.select("img").empty();
        System.out.println(img1);
//        String linkOuterH = link.outerHtml();
//        System.out.println(linkOuterH);
//        String linkInnerH = link.html(); // "<b>example</b>"
    }

    @Test
    void testGetStringList() {
        List<Term> segment = HanLP.segment("你好，欢迎使用HanLP！");
        System.out.println(segment);
        List<String> collect = segment.stream().map(Term::toString)
                .filter(s -> !s.contains("/w")).map(s -> s.split("/")[0]).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    void testHanlp() {
        System.out.println("首次编译运行时，HanLP会自动构建词典缓存，请稍候……\n");
        // 第一次运行会有文件找不到的错误但不影响运行，缓存完成后就不会再有了
        System.out.println("标准分词：");
        System.out.println(HanLP.segment("你好，欢迎使用HanLP！"));
        System.out.println("\n");
//        NLPTokenizer 无法使用
        List<Term> termList = HanLP.segment("中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程");
        System.out.println("NLP分词：");
        System.out.println(termList);
        System.out.println("\n");
    }


    @Test
    void testLength() {
        System.out.println("结以际与".length());
    }

    @Test
    void testJsoup() {
        String html = "<p><b>这是</b><script>alert('恶意代码')</script><a href='http://example.com'>一个链接</a></p>";

        // 使用 Jsoup 进行过滤
        String cleanHtml = Jsoup.clean(html, Whitelist.basic());

        System.out.println(cleanHtml);
    }

    @Test
    void testConversion() {
        String json = "{\"updateTime\":1692364115000,\"title\":\"学校概况\",\"body\":\"湖南文理学院是一所由湖南省人民政府主办的全日制普通本科院校。学校位于国际湿地城市、国家卫生城市、中国优秀旅游城市、“桃花源里的城市”——湖南省常德市。\\n学校始于1958年创办的常德师范高等专科学校,1999年，原常德师范高等专科学校和原常德高等专科学校合并组建常德师范学院，升格为本科院校，2003年更为现名，先后汇集了湖南农学院常德分院、常德教育学院、常德市城乡建设职业技术学校、常德艺术学校各自学科专业优势，至今已有65年高等教育办学历史。学校2016年被确定为国家“产教融合工程应用型本科规划高校”，2017年被确定为新增硕士学位授予立项建设单位，2018年被确认为湖南省“双一流”高水平应用特色学院，2018年高水平通过了教育部本科教学工作审核评估，是“全国模范职工之家”“湖南省文明标兵单位”、湖南省“三全育人”综合改革试点高校、“省级园林式单位”“湖南省平安建设示范校”“湖南省平安高校”“湖南省现代大学制度建设先进高校”等。2020年起，学校调入湖南省本科一批录取高校。\\n学校校园环境优美,占地面积2000余亩,校舍建筑总面积68.33万余m2,教学用房面积29.75万余m2,实验室面积15.25万余m2。拥有充足的教学科研仪器设备和图书文献资料,目前,教学科研仪器设备总值3.48亿元,图书馆藏纸质图书253万多册，电子图书226万种（305万多册）。\\n学校学科门类齐全，涵盖文、理、工、农、史、法、经、管、教、艺、医十一大学科门类，现有18个教学院、1个独立学院，69个本科专业（含3个中外合作办学专业）。学校面向全国31个省（市、自治区）招生，现有在校全日制本科生26000余人。\\n学校师资力量雄厚，现有教职工1550人，其中，专任教师1205人，教授149人、博士468人。拥有国家杰青、国家社科重大项目首席专家、国家万人计划科技创业领军人才、教育部新世纪优秀人才支持计划人选、湖南省121创新人才工程人选等在内的国家级、省部级人才工程人选120余人次；拥有国家级教学团队1个、省级教学和科研团队8个，拥有国家教指委副主任委员1人。\\n学校教学改革不断深入，近年来，先后获得教育部“新文科”项目1项，省级“新文科”“新工科”“新农科”项目5项；获得省级教学成果奖23项，其中一等奖3项、二等奖6项；立项省教育厅教学改革项目280余项。\\n学校专业课程建设成效显著，现有汉语言文学、地理科学、学前教育、英语、体育教育5个国家一流本科专业建设点，音乐学、美术学2个国家改革试点专业，以及生物科学、材料科学与工程等24个省一流本科专业建设点。获批国家级教学团队1个、省级教学团队4个；建成国家精品课程和精品资源共享课程各1门、省级精品课程14门；立项国家一流本科课程5门、省级一流课程67门；立项省级课程思政教学研究示范中心1个、省级思政“金课”1门、省级课程思政示范课程2门；建设校级优质课程300余门。\\n学校教学科研平台基地充足，获批国家大学生文化素质教育基地、国家Linux技术培训与推广中心、国家级校企合作人才培养基地、全国气象科普教育基地、中国水产学会科普教育基地、水产博士后科研工作站等7个国家级教学和科研平台，拥有“洞庭湖生态经济区建设与发展”和“水产高效健康生产”2个省级“2011”协同创新中心、“水生动物重要疫病分子免疫技术”等7个省级重点实验室、“水产生物资源与环境生态”等4个省工程研究中心、“环洞庭湖区域发展”等8个省级社科研究基地，以及湖南省普通高校科技成果转化和技术转移基地、湖南省水生生物资源监测中心沅水澧水监测站、水产博士后科研流动站协作研发中心、知识产权中心、创新创业教育中心、省级示范实验室等其他省部级教学和科研平台近100个。\\n学校秉承“科研促教学”的宗旨，科研水平显著增强。近5年来，先后承担包括国家社科基金重大委托项目、科技部星火计划重点项目在内的国家级科研项目100余项；发表科研论文4000余篇，其中SCI、SSCI、EI、CSSCI、CSCD收录近1100篇；先后出版学术著作300余部，其中国家级出版社出版105部；获得省部级及以上各类科技成果奖28项，其中国际铂金奖1项、省部级一等奖2项，中国发明创业奖成果一等奖1项；获得授权发明专利400余件，实用新型与外观设计、软件制作权1000余件，制定标准12件。\\n学校毕业生就业工作富有特色，成绩突出。近年来，毕业生毕业去向落实率一直位居湖南省同类高校前列，学校先后被教育部授予“全国毕业生就业典型经验高校”“全国高等学校创业教育研究与实践先进单位”“全国深化创新创业教育改革特色典型经验高校”，并被湖南省教育厅授予“湖南省大学生就业创业示范校”称号，被省发改委授予“湖南省双创示范基地”称号，多次被评为“湖南省高校毕业生就业工作一把手工程优秀单位”。学校应用型人才培养改革经验《湖南文理学院：培养市场需求的应用型人才》入选国家教育体制改革领导小组主编的《教育体制改革简报》，上升为国家经验。\\n学校先后与美、英、德、澳、波兰、日本、韩国等20个国家和地区的72所大学建立合作与交流关系，开展师生交流、教学和科研合作，并分别与加拿大菲沙河谷大学、美国科罗拉多州立大学普韦布洛分校、韩国牧园大学举办3个本科专业的中外合作办学项目，项目数量在全省名列前茅。我校从2004年开始招收留学生，至今累计招收来自35个国家的400余名留学生。\\n学校坚持校地校企合作，充分发挥学科资源优势，服务“三高四新”美好蓝图，推动地方经济社会发展。遴选高水平博士作为“科创助理”派驻20个市直部门，服务政府决策；选派300多名教授博士组建29个专家团队，开启“教授博士沅澧行”活动，为200多家本土企业上门送技术、送服务，开创了高校服务地方新模式。先后与常德市各县市区人民政府签订战略合作协议，开展全方位深度合作。紧紧围绕机械制造、新能源新材料等当地支柱产业和新兴产业，先后与中车汽车集团等500多家企业开展广泛合作，先后接受委托项目1000余项，累计投入研发经费3亿余元，解决了企业多项技术难题。\\n2021年，学校召开了第四次党代会，确定了今后五年的“123456”发展思路，即，围绕一个目标：建设特色鲜明的国内一流应用型大学；立足两大战略：“人才强校”战略、“学科强校”战略；实现三大突破：硕士学位授予单位突破，国家级标志性成果突破，医学专业设置突破；坚持四大发展：“转型发展”“内涵发展”“特色发展”“开放发展”；实施五大工程：党建引领工程，治理体系与治理能力现代化工程，师生综合素质提高工程，社会服务能力提升工程，师生幸福感提质工程；建设六个文理：“示范文理”“活力文理”“一流文理”“卓越文理”“开放文理”和“幸福文理”。\\n经过六十五年的发展，学校凝练了“博学弘文、明理求真”的校训，形成了“尚善尚知尚行、爱国爱校爱人”的校风。目前，学校正朝着建设特色鲜明的国内一流应用型大学目标稳步前行。（2023年6月）\\n\",\"type\":\"结以际与\",\"createTime\":1692364115000,\"id\":603062273}";
        Post post = JSONUtil.toBean(json, Post.class);
        System.out.println(post.toString());
    }

    @Test
    void testCopyProperties() {
        Post edit = new Post();
        edit.setType("xxxxx");
        edit.setBody("xxxxx");
        Post ori = new Post();
        ori.setTitle("介绍");
        ori.setCreateTime(LocalDateTime.now());
        ori.setBody("湖南文理学院是一所由湖南省人民政府主办的全日制普通本科院校。学校位于国际湿地城市、国家卫生城市、中国优秀旅游城市、“桃花源里的城市”——湖南省常德市。");
        BeanUtil.copyProperties(ori, edit, getIgnorePropertyNames(ori, edit));

        System.out.println("ori：" + ori.toString() + "\nedit：" + edit.toString());
    }

    /**
     * 得到为空属性的集合
     * @param source
     * @return
     */
//    private String[] getIgnorePropertyNames(Object source, Object target) {
//        final BeanWrapper src = new BeanWrapperImpl(source);
//        final BeanWrapper tar = new BeanWrapperImpl(target);
//        java.beans.PropertyDescriptor[] srcPds = src.getPropertyDescriptors();
//        java.beans.PropertyDescriptor[] tarPds = tar.getPropertyDescriptors();
//
//        Set<String> ignoreName = new HashSet<>();
//        for (java.beans.PropertyDescriptor pd : srcPds) {
//            Object srcValue = src.getPropertyValue(pd.getName());
//            if (srcValue == null) ignoreName.add(pd.getName());
//        }
//        for (java.beans.PropertyDescriptor pd : tarPds) {
//            Object tarValue = tar.getPropertyValue(pd.getName());
//            if (tarValue != null) ignoreName.add(pd.getName());
//        }
//
//        String[] result = new String[ignoreName.size()];
//        return ignoreName.toArray(result);
//    }
    private String[] getIgnorePropertyNames(Object source, Object target) {
        final BeanWrapperImpl src = new BeanWrapperImpl(source);
        final BeanWrapperImpl tar = new BeanWrapperImpl(target);
        java.beans.PropertyDescriptor[] srcPds = src.getPropertyDescriptors();
        java.beans.PropertyDescriptor[] tarPds = tar.getPropertyDescriptors();

        Set<String> ignoreName = new HashSet<>();
        ignoreName.addAll(Arrays.stream(srcPds)
                .filter(pd -> src.getPropertyValue(pd.getName()) == null)
                .map(java.beans.PropertyDescriptor::getName)
                .collect(Collectors.toSet()));

        ignoreName.addAll(Arrays.stream(tarPds)
                .filter(pd -> tar.getPropertyValue(pd.getName()) != null)
                .map(java.beans.PropertyDescriptor::getName)
                .collect(Collectors.toSet()));

        return ignoreName.toArray(new String[0]);
    }

    @Test
    public void testTimestamp() {
        long timestamp = System.currentTimeMillis();
        String base36Code = Long.toString(timestamp, 36);

        System.out.println("时间戳：" + timestamp);
        System.out.println("36位进制代码：" + base36Code);
    }


}
