package net.cuiwei.xiangle.bean;

import java.util.ArrayList;

/**
 * 用于Publish界面，fragment向activity传值
 */
public class Publish {
    public int type;
    public String content;
    public String image;
    public String video;
    public ArrayList<String> images;
    public int topic_id;

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Publish{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                ", images=" + images +
                '}';
    }
}
