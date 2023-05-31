package ibf2022.batch2.csf.backend.models;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

public class Archive {
    private String bundleId;
    private Long date;
    private String title;
    private String name;
    private String comments;
    private List<String> urls;

    public String getBundleId() {
        return bundleId;
    }
    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
    public Long getDate() {
        return date;
    }
    public void setDate(Long date) {
        this.date = date;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public List<String> getUrls() {
        return urls;
    }
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public static Archive create(Document document) {
        Archive archive = new Archive();
        archive.setBundleId(document.getString("bundleId"));
        archive.setDate(document.getLong("date"));
        archive.setTitle(document.getString("title"));
        archive.setName(document.getString("name"));
        archive.setComments(document.getString("comments"));
        archive.setUrls(document.get("urls", List.class));
        return archive;
    }

    public JsonObjectBuilder toJsonObjectBuilder() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (String s: this.getUrls()) {
            jsonArrayBuilder.add(s);
        }
        return Json.createObjectBuilder()
                    .add("bundleId", this.getBundleId())
                    .add("date", (new Date(this.getDate())).toString())
                    .add("title", this.getTitle())
                    .add("name", this.getName())
                    .add("comments", this.getComments())
                    .add("urls", jsonArrayBuilder);
    }
}
