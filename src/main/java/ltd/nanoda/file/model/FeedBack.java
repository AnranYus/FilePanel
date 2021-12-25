package ltd.nanoda.file.model;

public class FeedBack {
    int code;
    String type;
    String content;

    public FeedBack(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public FeedBack(int code, String type, String content) {
        this.code = code;
        this.type = type;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
