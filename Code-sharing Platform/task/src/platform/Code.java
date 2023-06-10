package platform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
public class Code {
    @GeneratedValue
    @JsonIgnore
    @Id
    private UUID id;

    @GeneratedValue
    @JsonIgnore
    private Long longId;
    private String code;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;
    private long time;
    private long views;
    @JsonIgnore
    private LocalDateTime endDate;

    @JsonIgnore
    private boolean expired;
    @JsonIgnore
    private boolean expiresByViews;
    @JsonIgnore
    private boolean expiresByTime;

    public Code(String code, LocalDateTime date, long views, long time) {
        this.code = code;
        this.date = date;
        this.views = views;
        this.time = time;
        this.endDate = this.date.plusSeconds(time);
        this.expired = false;
        this.expiresByViews = views > 0;
        this.expiresByTime = time > 0;
    }

    public Code() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @JsonIgnore
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return date.format(formatter);
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isExpiresByViews() {
        return expiresByViews;
    }

    public void setExpiresByViews(boolean expiresByViews) {
        this.expiresByViews = expiresByViews;
    }

    public boolean isExpiresByTime() {
        return expiresByTime;
    }

    public void setExpiresByTime(boolean expiresByTime) {
        if (expiresByTime) {
            this.endDate = this.date.plusSeconds(time);
        }
        this.expiresByTime = expiresByTime;
    }

    public Long getLongId() {
        return longId;
    }

    public void setLongId(Long longId) {
        this.longId = longId;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
