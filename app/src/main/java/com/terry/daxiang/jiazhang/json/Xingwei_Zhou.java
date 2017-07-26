package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */

public class Xingwei_Zhou {

    /**
     * name : 2016年7月  第一周
     * text : 这道题我无论怎么算也算不出来。这下我的心像是被封锁在了北极的大冰山里一样，我想放弃了：这些题是尖子生才有可能做出来的，而我，却从来都没当过尖子生，我怎么有可能做出这种难题呢。想着想着，心里还真难受，我竟趴在桌子上哭了起来。突然，我想起了爸爸对我说的诘。
     一道亮光划过心头，“对，男儿有泪不轻弹，不可以在困难面前倒下。”想到这儿，我立刻擦干眼泪，重拾信心，准备与“敌人”血拼了。
     我拿出学习电脑查相关题型，仔细听了讲解后，才知道，原来这道题要用分数除法。经过我不断地对比、计算，我终于把这道题解开了。我不禁为自己战胜困难欢呼了起来。
     */

    private List<Xingwei_ZhouBean> data;

    public List<Xingwei_ZhouBean> getData() {
        return data;
    }

    public void setData(List<Xingwei_ZhouBean> data) {
        this.data = data;
    }

    public static class Xingwei_ZhouBean {
        private String name;
        private String text;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
