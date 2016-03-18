package uk.ac.ncl.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Tool")
public class Tool implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5379987700799924492L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "SIZE")
    private Long size;

    @Column(name = "UPLOADDATE")
    private Date uploadDate;

    @Column(name = "ACCESSKEY")
    private String key;


    public Tool() {

    }

    public Tool(String name, String owner, Long size, Date uploadDate, String key) {
        super();
        this.owner = owner;
        this.size = size;
        this.name = name;
        this.uploadDate = uploadDate;
        this.key = key;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return name;
    }

}
