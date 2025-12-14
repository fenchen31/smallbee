package com.practice.smallbee.response;

import java.io.Serializable;
import java.util.List;

public class UseRecodeBean implements Serializable {
    private int remainTime;
    private List<UseRecodeItemBean> useRecodeItem;

    public void setUseRecodeItem(List<UseRecodeItemBean> useRecodeItem) {
        this.useRecodeItem = useRecodeItem;
    }

    public static class UseRecodeItemBean {
        private String iconUrl;
        private long playTime;
        private String gameName;
        private String useTime;

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public long getPlayTime() {
            return playTime;
        }

        public void setPlayTime(long playTime) {
            this.playTime = playTime;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getUseTime() {
            return useTime;
        }

        public void setUseTime(String useTime) {
            this.useTime = useTime;
        }
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public List<UseRecodeItemBean> getUseRecodeItem() {
        return useRecodeItem;
    }

}
