package com.puzek.data.bean.postgresql;

import com.puzek.data.utils.FormatClassInfo;

import java.io.Serializable;

/**
 * 和巡检车相对应的照片实体类
 * Created by chen_wp on 2019-08-05.
 */
public class ParkSpaceImages implements Serializable {

    private long id;
    private long parentId; // 与 messageData 关联
    private String patrolCarNumber; // 巡检车车牌号
    private String batchNumber; // 停车信息表中的批次
    private String parkNumber; // 车位号
    private String image; // 图片
    private String imagePost; // 摄像头(前中后)
    private String time; // 照片名称中自带的时间戳
    private String frequency; // 记录是第一次还是第二次分析时存储的
    private String sendStatus; // 记录是否已经成功接到图片

    public ParkSpaceImages() {
    }

    public ParkSpaceImages(long id, long parentId, String patrolCarNumber, String batchNumber, String parkNumber, String image, String imagePost, String time, String frequency, String sendStatus) {
        this.id = id;
        this.parentId = parentId;
        this.patrolCarNumber = patrolCarNumber;
        this.batchNumber = batchNumber;
        this.parkNumber = parkNumber;
        this.image = image;
        this.imagePost = imagePost;
        this.time = time;
        this.frequency = frequency;
        this.sendStatus = sendStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getPatrolCarNumber() {
        return patrolCarNumber;
    }

    public void setPatrolCarNumber(String patrolCarNumber) {
        this.patrolCarNumber = patrolCarNumber;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImagePost() {
        return imagePost;
    }

    public void setImagePost(String imagePost) {
        this.imagePost = imagePost;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getParkNumber() {
        return parkNumber;
    }

    public void setParkNumber(String parkNumber) {
        this.parkNumber = parkNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return FormatClassInfo.format(this);
    }

}
