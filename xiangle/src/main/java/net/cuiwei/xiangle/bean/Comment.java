package net.cuiwei.xiangle.bean;

public class Comment {
    private long id;
    private long pid;
    private long joke_id;
    private long user_id;
    private String content;
    private String joke_content;
    private long create_time;
    private String nickname;
    private String avatar;
    private String quotes;
    private String image;
    private String images;
    private String video;
    private String keywords;

    public static Comment create(String avatar, String content, String nickname){
        Comment comment1=new Comment();
        comment1.setAvatar(avatar);
        comment1.setContent(content);
        comment1.setNickname(nickname);
        return comment1;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getJoke_id() {
        return joke_id;
    }

    public void setJoke_id(long joke_id) {
        this.joke_id = joke_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
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

    public String getJoke_content() {
        return joke_content;
    }

    public void setJoke_content(String joke_content) {
        this.joke_content = joke_content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
