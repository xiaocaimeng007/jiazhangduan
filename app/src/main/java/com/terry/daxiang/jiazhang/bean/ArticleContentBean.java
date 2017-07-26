package com.terry.daxiang.jiazhang.bean;

import java.util.ArrayList;

/**
 * 文章内容
 * Created by chen_fulei on 2016/11/11.
 */

public class ArticleContentBean {

    private int id;//: 114,
    private int channel_id;//: 17,
    private int category_id;//: 68,
    private String call_index;//: "",
    private String title;//: "投诉",
    private String link_url;//: "",
    private String img_url;//: "/upload/201610/18/201610181317442458.png",
    private String shareurl;
    private String seo_title;//: "",
    private String seo_keywords;//: "",
    private String seo_description;//: "",
    private String zhaiyao;//: "为什么宝宝回家之后说饿",
    private String content;//: "",
    private int sort_id;//: 99,
    private int click;//: 0,
    private int status;//: 0,
    private int is_msg;//: 1,
    private int is_top;//: 0,
    private int is_red;//: 0,
    private int is_hot;//: 0,
    private int is_slide;//: 0,
    private int is_sys;//: 1,
    private int is_shoucang ;//: 0,
    private int is_dianzan ;//: 0,
    private int shoucangcount;//: 0,
    private int dianzancount;//: 0,
    private String user_name;//: "admin",
    private String add_time;//: "/Date(1476767865000)/",
    private String update_time;//: null,
    private Fields fields;
    private ArrayList<Albums> albums;
    private ArrayList<Attach> attach;
    private ArrayList<Group_price> group_price;
    private ArrayList<Comment> comment;
    private ArrayList<Article_zan> article_zan;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCall_index() {
        return call_index;
    }

    public void setCall_index(String call_index) {
        this.call_index = call_index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public String getSeo_title() {
        return seo_title;
    }

    public void setSeo_title(String seo_title) {
        this.seo_title = seo_title;
    }

    public String getSeo_keywords() {
        return seo_keywords;
    }

    public void setSeo_keywords(String seo_keywords) {
        this.seo_keywords = seo_keywords;
    }

    public String getSeo_description() {
        return seo_description;
    }

    public void setSeo_description(String seo_description) {
        this.seo_description = seo_description;
    }

    public String getZhaiyao() {
        return zhaiyao;
    }

    public void setZhaiyao(String zhaiyao) {
        this.zhaiyao = zhaiyao;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort_id() {
        return sort_id;
    }

    public void setSort_id(int sort_id) {
        this.sort_id = sort_id;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_msg() {
        return is_msg;
    }

    public void setIs_msg(int is_msg) {
        this.is_msg = is_msg;
    }

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    public int getIs_red() {
        return is_red;
    }

    public void setIs_red(int is_red) {
        this.is_red = is_red;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public int getIs_slide() {
        return is_slide;
    }

    public void setIs_slide(int is_slide) {
        this.is_slide = is_slide;
    }

    public int getIs_sys() {
        return is_sys;
    }

    public void setIs_sys(int is_sys) {
        this.is_sys = is_sys;
    }

    public int getIs_shoucang() {
        return is_shoucang;
    }

    public void setIs_shoucang(int is_shoucang) {
        this.is_shoucang = is_shoucang;
    }

    public int getIs_dianzan() {
        return is_dianzan;
    }

    public void setIs_dianzan(int is_dianzan) {
        this.is_dianzan = is_dianzan;
    }

    public int getShoucangcount() {
        return shoucangcount;
    }

    public void setShoucangcount(int shoucangcount) {
        this.shoucangcount = shoucangcount;
    }

    public int getDianzancount() {
        return dianzancount;
    }

    public void setDianzancount(int dianzancount) {
        this.dianzancount = dianzancount;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public ArrayList<Albums> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Albums> albums) {
        this.albums = albums;
    }

    public ArrayList<Attach> getAttach() {
        return attach;
    }

    public void setAttach(ArrayList<Attach> attach) {
        this.attach = attach;
    }

    public ArrayList<Group_price> getGroup_price() {
        return group_price;
    }

    public void setGroup_price(ArrayList<Group_price> group_price) {
        this.group_price = group_price;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comment> comment) {
        this.comment = comment;
    }

    public ArrayList<Article_zan> getArticle_zan() {
        return article_zan;
    }

    public void setArticle_zan(ArrayList<Article_zan> article_zan) {
        this.article_zan = article_zan;
    }

    public static class Fields{
        private String AuthorID;//: "0",
        private String grade_name;///: "大一班",
        private String author;//: "小红妈妈"
        private String video_src;
        private String source;

        public String getAuthorID() {
            return AuthorID;
        }

        public void setAuthorID(String authorID) {
            AuthorID = authorID;
        }

        public String getGrade_name() {
            return grade_name;
        }

        public void setGrade_name(String grade_name) {
            this.grade_name = grade_name;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getVideo_src() {
            return video_src;
        }

        public void setVideo_src(String video_src) {
            this.video_src = video_src;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

    public static class Albums{
        private int id;//: 93,
        private int article_id;//: 114,
        private String thumb_path;//: "/upload/201610/18/201610181317505739.png",
        private String original_path;//: "/upload/201610/18/201610181317505739.png",
        private String remark;//: "",
        private String add_time;//: "/Date(1476767913000)/"

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getArticle_id() {
            return article_id;
        }

        public void setArticle_id(int article_id) {
            this.article_id = article_id;
        }

        public String getThumb_path() {
            return thumb_path;
        }

        public void setThumb_path(String thumb_path) {
            this.thumb_path = thumb_path;
        }

        public String getOriginal_path() {
            return original_path;
        }

        public void setOriginal_path(String original_path) {
            this.original_path = original_path;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }

    public static class Attach{
        private int id;//: 18,
        private int article_id;//: 113,
        private String file_name;//: "VID_20160616_123403.mp4",
        private String file_path;//: "/upload/201610/17/201610172155129018.mp4",
        private int file_size;//: 18084,
        private String file_ext;//: "mp4",
        private int down_num;//: 0,
        private int point;//: 0,
        private String add_time;//: "/Date(1476712543000)/"

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getArticle_id() {
            return article_id;
        }

        public void setArticle_id(int article_id) {
            this.article_id = article_id;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public String getFile_path() {
            return file_path;
        }

        public void setFile_path(String file_path) {
            this.file_path = file_path;
        }

        public int getFile_size() {
            return file_size;
        }

        public void setFile_size(int file_size) {
            this.file_size = file_size;
        }

        public String getFile_ext() {
            return file_ext;
        }

        public void setFile_ext(String file_ext) {
            this.file_ext = file_ext;
        }

        public int getDown_num() {
            return down_num;
        }

        public void setDown_num(int down_num) {
            this.down_num = down_num;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }

    public static class Group_price{

    }

    public static class Comment{
        private int id;//: 40,
        private int channel_id;//: 17,
        private int article_id;//: 114,
        private int parent_id;//: 0,
        private int user_id;//: 2,
        private String user_name;//: "王园长",
        private String user_ip;//: "127.0.0.1",
        private String content;//: "回复王园长:回复王园长:就这样哈",
        private int is_lock;//: 0,
        private String add_time;//: "/Date(1478242981000)/",
        private int is_reply;//: 0,
        private String reply_content;//: "",
        private String reply_time;//: null

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        public int getArticle_id() {
            return article_id;
        }

        public void setArticle_id(int article_id) {
            this.article_id = article_id;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_ip() {
            return user_ip;
        }

        public void setUser_ip(String user_ip) {
            this.user_ip = user_ip;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIs_lock() {
            return is_lock;
        }

        public void setIs_lock(int is_lock) {
            this.is_lock = is_lock;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public int getIs_reply() {
            return is_reply;
        }

        public void setIs_reply(int is_reply) {
            this.is_reply = is_reply;
        }

        public String getReply_content() {
            return reply_content;
        }

        public void setReply_content(String reply_content) {
            this.reply_content = reply_content;
        }

        public String getReply_time() {
            return reply_time;
        }

        public void setReply_time(String reply_time) {
            this.reply_time = reply_time;
        }
    }

    public static class Article_zan{
        int id;//: 135,
        int cid;//: 113,
        String img_url;//: "/upload/201610/15/201610150120391087.png",
        int uid;//: 17

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
