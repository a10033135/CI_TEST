package com.evan.ci_test.Common;

public class PhotoDetail {

    private String id;
    private String title;
    private String thumbnailUrl;

    public PhotoDetail(String id, String title, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
