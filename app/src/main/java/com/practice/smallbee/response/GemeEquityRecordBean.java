package com.practice.smallbee.response;

import java.io.Serializable;
import java.util.List;

public class GemeEquityRecordBean implements Serializable {
    private String name;
    private String count;
    private List<RecordBean> recordList;
    private boolean hasMore;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<RecordBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<RecordBean> recordList) {
        this.recordList = recordList;
    }

    public static class RecordBean implements Serializable{
        private String name;
        private String validityPeriodStart;
        private String validityPeriodEnd;
        private String validityPeriodDuration;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValidityPeriodEnd() {
            return validityPeriodEnd;
        }

        public void setValidityPeriodEnd(String validityPeriodEnd) {
            this.validityPeriodEnd = validityPeriodEnd;
        }

        public String getValidityPeriodDuration() {
            return validityPeriodDuration;
        }

        public void setValidityPeriodDuration(String validityPeriodDuration) {
            this.validityPeriodDuration = validityPeriodDuration;
        }

        public String getValidityPeriodStart() {
            return validityPeriodStart;
        }

        public void setValidityPeriodStart(String validityPeriodStart) {
            this.validityPeriodStart = validityPeriodStart;
        }
    }
}
