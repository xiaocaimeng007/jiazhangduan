package com.terry.daxiang.jiazhang.bean;

import java.util.ArrayList;

/**
 * Created by chen_fulei on 2016/12/21.
 */

public class ChildActionDayBean {
    private String Childid;//: "8",
    private String Childname;//: "小青",
    private String Childavator;//: "/upload/201611/18/201611181252142767.jpg",
    private ArrayList<Actiontypes> Actiontypes;

    public String getChildid() {
        return Childid;
    }

    public void setChildid(String childid) {
        Childid = childid;
    }

    public String getChildname() {
        return Childname;
    }

    public void setChildname(String childname) {
        Childname = childname;
    }

    public String getChildavator() {
        return Childavator;
    }

    public void setChildavator(String childavator) {
        Childavator = childavator;
    }

    public ArrayList<ChildActionDayBean.Actiontypes> getActiontypes() {
        return Actiontypes;
    }

    public void setActiontypes(ArrayList<ChildActionDayBean.Actiontypes> actiontypes) {
        Actiontypes = actiontypes;
    }

    public static class Actiontypes{
        private int Id ;//: 95,
        private String Name;//: "能力",
        private String Itemcount;//: 2,
        private int Percent;
        private ArrayList<Items> Items;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getPercent() {
            return Percent;
        }

        public void setPercent(int percent) {
            Percent = percent;
        }

        public String getItemcount() {
            return Itemcount;
        }

        public void setItemcount(String itemcount) {
            Itemcount = itemcount;
        }

        public ArrayList<Actiontypes.Items> getItems() {
            return Items;
        }

        public void setItems(ArrayList<Actiontypes.Items> items) {
            Items = items;
        }

        public static class Items{
            private int Id;//: 114,
            private String Name;//: "自我表现能力",
            private int Contentcount;//: 3,
            private int Parentid;//: 95,
            private String Parentname;//: "能力"
            private ArrayList<Itemcontents> Itemcontents;

            public int getId() {
                return Id;
            }

            public void setId(int id) {
                Id = id;
            }

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            public int getContentcount() {
                return Contentcount;
            }

            public void setContentcount(int contentcount) {
                Contentcount = contentcount;
            }

            public int getParentid() {
                return Parentid;
            }

            public void setParentid(int parentid) {
                Parentid = parentid;
            }

            public String getParentname() {
                return Parentname;
            }

            public void setParentname(String parentname) {
                Parentname = parentname;
            }

            public ArrayList<Items.Itemcontents> getItemcontents() {
                return Itemcontents;
            }

            public void setItemcontents(ArrayList<Items.Itemcontents> itemcontents) {
                Itemcontents = itemcontents;
            }

            public static class Itemcontents{
                private int Id;//: 558,
                private String Title;//: "幼儿自我意识的建立与认知，渴望认同与被关注，开始有自信或胆怯的表现。",
                private String Date;//: "2016.12.12"

                public int getId() {
                    return Id;
                }

                public void setId(int id) {
                    Id = id;
                }

                public String getTitle() {
                    return Title;
                }

                public void setTitle(String title) {
                    Title = title;
                }

                public String getDate() {
                    return Date;
                }

                public void setDate(String date) {
                    Date = date;
                }
            }
        }
    }
}
