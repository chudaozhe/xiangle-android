package net.cuiwei.xiangle.bean;

public class UserFollow {
    private long user_id;
    private long to_user_id;
    private String nickname;
    private String avatar;
    private String quotes;

    private int topic_id;
    private String name;
    private String image;
    private int sum;

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
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

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(long to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQuotes() {
        return quotes;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    @Override
    public String toString() {
        return "UserFollow{" +
                "user_id=" + user_id +
                ", to_user_id=" + to_user_id +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", quotes='" + quotes + '\'' +
                ", topic_id=" + topic_id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", sum=" + sum +
                '}';
    }
}
