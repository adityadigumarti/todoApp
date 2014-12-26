package com.mashable.assignment.domain;

import io.searchbox.annotations.JestId;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Todo Item.
 * 
 * @author Adi
 * 
 */
@XmlRootElement
public class TodoItem {

    @JestId
    private String id;
    private String title;
    private String body;
    private boolean done = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TodoItem other = (TodoItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TodoItem [id=" + id + ", title=" + title + ", body=" + body + ", done=" + done + "]";
    }

}
