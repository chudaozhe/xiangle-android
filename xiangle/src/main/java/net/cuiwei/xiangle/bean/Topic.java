package net.cuiwei.xiangle.bean;

import java.io.Serializable;

public class Topic implements Serializable {
    public int id;
    public String name;
    public String image;
    public int sum;
    public int is_follow;

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public Topic setData(Topic field) {
        Topic topic1 = new Topic();
        topic1.setId(field.getId());
        topic1.setImage(field.getImage());
        topic1.setName(field.getName());
        topic1.setSum(field.getSum());
        return topic1;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", sum=" + sum +
                ", is_follow=" + is_follow +
                '}';
    }
}
