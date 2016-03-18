package uk.ac.ncl.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Date;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ResearchData")
public class Data implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1161244783401940420L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "LASTVISITDATE")
    private Date lastVisit;

    @Column(name = "PUBLICATIONDATE")
    private Date publish;

    @Column(name = "ACCESSKEY")
    private String key;

    @Column(name = "SIZE")
    private Long size;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> rawData;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> tool;


    public Data(String name, String owner, Long size,
                Date publish, Date lastVisit, String key, HashSet<String> rawData, HashSet<String> tool) {
        super();
        this.name = name;
        this.owner = owner;
        this.publish = publish;
        this.lastVisit = lastVisit;
        this.size = size;
        this.key = key;
        this.rawData = rawData;
        this.tool = tool;
    }

    public Data() {

    }

    public Date getPublish() {
        return publish;
    }


    public void setPublish(Date publish) {
        this.publish = publish;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getRawData() {
        return rawData;
    }

    public void setRawData(Set<String> rawData) {
        this.rawData = rawData;
    }

    public Set<String> getTool() {
        return tool;
    }

    public void setTool(Set<String> tool) {
        this.tool = tool;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getkey() {
        return key;
    }

    public void setkey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "uk.ac.ncl.model.Data{" +
                "serialVersionUID=" + serialVersionUID +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", lastVisit=" + lastVisit +
                ", publish=" + publish +
                ", key='" + key + '\'' +
                ", size=" + size +
                ", rawData=" + rawData +
                ", tool=" + tool +
                '}';
    }
}
