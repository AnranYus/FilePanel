package ltd.nanoda.file.model;

public class File {
    String name;
    String type;
    String size;


    String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File(String name, String type, String size, String date) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public File(String name, String type, String date) {
        this.name = name;
        this.type = type;
        this.date = date;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
