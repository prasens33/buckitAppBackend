package edu.cmu.sv.app17.helpers;

public class APPListResponse {
    public boolean success;
    public Object content;
    public APPListResponseMetaData metadata;
    public APPListResponse(Object content, long count) {
        this.success = true;
        this.content = content;
        this.metadata = new APPListResponseMetaData(count);
    }

    public APPListResponse() {
        this.success = true;
    }

    private class APPListResponseMetaData {
        public long count;
        public APPListResponseMetaData(long count) {

            this.count = count;
        }
    }
}
