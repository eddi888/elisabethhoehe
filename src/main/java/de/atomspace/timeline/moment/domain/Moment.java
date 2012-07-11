package de.atomspace.timeline.moment.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.Attachment;
import org.ektorp.support.CouchDbDocument;

public class Moment extends CouchDbDocument {
    private static final long serialVersionUID = 1L;

    private String description;

    private String licence;

    private String author;

    private boolean published;

    private List<Integer> years = new ArrayList<Integer>();

    private int year;

    private String visibleKey;

    @Override
    public void addInlineAttachment(Attachment a) {
        super.addInlineAttachment(a);
    }

    @JsonIgnore
    public String getAttachmentIdPreview() {
        Set<String> keys = getAttachments().keySet();
        for (String key : keys) {
            if (key.indexOf("preview") >= 0) {
                return key;
            }
        }
        return null;
    }

    @JsonIgnore
    public String getAttachmentId() {
        Set<String> keys = getAttachments().keySet();
        for (String key : keys) {
            if (key.indexOf("preview") < 0) {
                return key;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public String getVisibleKey() {
        return visibleKey;
    }

    public void setVisibleKey(String visibleKey) {
        this.visibleKey = visibleKey;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
