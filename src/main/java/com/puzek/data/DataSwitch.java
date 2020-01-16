package com.puzek.data;

import com.puzek.data.bean.postgresql.*;
import com.puzek.data.utils.IdWorker;
import com.puzek.data.utils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSwitch {

    private static Logger LOG = LoggerFactory.getLogger(DataSwitch.class);

    private static Connection mysqlServerCon;
    private static Connection postSqlCon;

    private static IdWorker idWorker = new IdWorker();

    public static void main(String[] args) {
        if (!checkConnection()) {
            return;
        }
        System.out.println("连接成功。。。");

        Statement statement = null;
        try {
//            mysqlServerCon.setAutoCommit(false); // TODO 开启事务
            statement = postSqlCon.createStatement();

            handleMessageData(statement);

            handleParkingSpaceImages(statement);

            handleWorking(statement);

            handleModifyResults(statement);

            handleBatchNumber(statement);

//            mysqlServerCon.commit(); // TODO 提交事务
        } catch (Exception e) {
            e.printStackTrace();
//            try {
//                mysqlServerCon.rollback(); // TODO 回滚事务
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
        } finally {
            closeConnect(statement);
        }

    }

    private static void closeConnect(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (mysqlServerCon != null) {
            try {
                mysqlServerCon.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (postSqlCon != null) {
            try {
                postSqlCon.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleMessageData(Statement statement) {
        int numPage = 0;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            while (true) {
                String selectMessageDataSql = "SELECT\n" +
                        "  m.id                 as id,\n" +
                        "  m.message_id         as message_id,\n" +
                        "  m.batch_number       as batch_number,\n" +
                        "  m.min_batch_number   as min_batch_number,\n" +
                        "  m.patrol_car_id      as patrol_car_id,\n" +
                        "  m.patrol_car_number  as patrol_car_number,\n" +
                        "  m.area_number        as area_number,\n" +
                        "  m.area_name          as area_name,\n" +
                        "  m.park_number        as park_number,\n" +
                        "  m.park_status        as park_status,\n" +
                        "  m.car_number         as car_number,\n" +
                        "  m.photograph_time    as photograph_time,\n" +
                        "  m.push_status        as push_status,\n" +
                        "  m.push_time          as push_time,\n" +
                        "  m.send_status        as send_status,\n" +
                        "  m.type               as type,\n" +
                        "  m.record_time        as record_time,\n" +
                        "  m.new_car_number     as new_car_number,\n" +
                        "  m.panorama           as panorama,\n" +
                        "  m.distribution       as distribution,\n" +
                        "  m.update_time        as update_time,\n" +
                        "  m.distribution_times as distribution_times,\n" +
                        "  m.one_user_id        as one_user_id,\n" +
                        "  m.two_user_id        as two_user_id,\n" +
                        "  m.three_user_id      as three_user_id,\n" +
                        "  m.tag                as tag,\n" +
                        "  m.status             as status\n" +
                        "FROM cloud.message_data AS m\n" +
                        "OFFSET " + numPage + " * 100\n" +
                        "LIMIT 100";

                resultSet = statement.executeQuery(selectMessageDataSql);

                List<MessageData> messageDataList = new ArrayList<>();

                while (resultSet.next()) {//遍历结果集
                    int id = resultSet.getInt("id");
                    String messageId = resultSet.getString("message_id");
                    String batchNumber = resultSet.getString("batch_number");
                    String minBatchNumber = resultSet.getString("min_batch_number");
                    String patrolCarId = resultSet.getString("patrol_car_id");
                    String patrolCarNumber = resultSet.getString("patrol_car_number");
                    String areaNumber = resultSet.getString("area_number");
                    String areaName = resultSet.getString("area_name");
                    String parkNumber = resultSet.getString("park_number");
                    String parkStatus = resultSet.getString("park_status");
                    String carNumber = resultSet.getString("car_number");
                    String photographTime = resultSet.getString("photograph_time");
                    String pushStatus = resultSet.getString("push_status");
                    String pushTime = resultSet.getString("push_time");
                    String sendStatus = resultSet.getString("send_status");
                    String type = resultSet.getString("type");
                    String recordTime = resultSet.getString("record_time");
                    String newCarNumber = resultSet.getString("new_car_number");
                    String panorama = resultSet.getString("panorama");
                    String distribution = resultSet.getString("distribution");
                    String updateTime = resultSet.getString("update_time");
                    int distributionTimes = resultSet.getInt("distribution_times");
                    int oneUserId = resultSet.getInt("one_user_id");
                    int twoUserId = resultSet.getInt("two_user_id");
                    int threeUserId = resultSet.getInt("three_user_id");
                    String tag = resultSet.getString("tag");
                    String status = resultSet.getString("status");
                    MessageData messageData = new MessageData(id, messageId, batchNumber, minBatchNumber, patrolCarId, patrolCarNumber, areaNumber, areaName, parkNumber, parkStatus, carNumber, photographTime, pushStatus, pushTime, sendStatus, type, recordTime, newCarNumber, panorama, distribution, updateTime, distributionTimes, oneUserId, twoUserId, threeUserId, tag, status);
                    messageDataList.add(messageData);
                }

                System.out.println("messageDataList.size() = " + messageDataList.size());
                if (messageDataList == null || messageDataList.size() == 0) {
                    break;
                }

                String insertMessageDataSql = "INSERT INTO message_data (id, message_id, batch_number, min_batch_number, patrol_car_id, patrol_car_number, area_number, area_name, park_number, park_status, car_number, photograph_time, push_status, push_time, send_status, type, record_time, new_car_number, panorama, distribution, update_time, distribution_times, one_user_id, two_user_id, three_user_id, tag, status)\n" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                preparedStatement = mysqlServerCon.prepareStatement(insertMessageDataSql);
                for (MessageData m : messageDataList) {
                    preparedStatement.setInt(1, m.getId());
                    preparedStatement.setString(2, m.getMessageId());
                    preparedStatement.setString(3, m.getBatchNumber());
                    preparedStatement.setString(4, m.getMinBatchNumber());
                    preparedStatement.setString(5, m.getPatrolCarId());
                    preparedStatement.setString(6, m.getParkNumber());
                    preparedStatement.setString(7, m.getAreaNumber());
                    preparedStatement.setString(8, m.getAreaName());
                    preparedStatement.setString(9, m.getParkNumber());
                    preparedStatement.setString(10, m.getParkStatus());
                    preparedStatement.setString(11, m.getCarNumber());
                    preparedStatement.setString(12, m.getPhotographTime());
                    preparedStatement.setString(13, m.getPushStatus());
                    preparedStatement.setString(14, m.getPushTime());
                    preparedStatement.setString(15, m.getSendStatus());
                    preparedStatement.setString(16, m.getType());
                    preparedStatement.setString(17, m.getRecordTime());
                    preparedStatement.setString(18, m.getNewCarNumber());
                    preparedStatement.setString(19, m.getPanorama());
                    preparedStatement.setString(20, m.getDistribution());
                    preparedStatement.setString(21, m.getUpdateTime());
                    preparedStatement.setInt(22, m.getDistributionTimes());
                    preparedStatement.setInt(23, m.getOneUserId());
                    preparedStatement.setInt(24, m.getTwoUserId());
                    preparedStatement.setInt(25, m.getThreeUserId());
                    preparedStatement.setString(26, m.getTag());
                    preparedStatement.setString(27, m.getStatus());

                    try {
                        preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.info(e.toString());
                    }
                }
                System.out.println("插入messageData " + messageDataList.size() + " 条成功...");
                numPage++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
        }

    }

    private static void handleParkingSpaceImages(Statement statement) {
        int numPage = 0;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            while (true) {
                String selectImagesSql = "SELECT\n" +
                        "  (SELECT m.id\n" +
                        "   FROM cloud.message_data AS m\n" +
                        "   WHERE m.batch_number = i.batch_number\n" +
                        "         AND m.park_number = i.park_number\n" +
                        "   LIMIT 1)           as id,\n" +
                        "  i.patrol_car_number as patrol_car_number,\n" +
                        "  i.batch_number      as batch_number,\n" +
                        "  i.park_number       as park_number,\n" +
                        "  i.image             as image,\n" +
                        "  i.image_post        as image_post,\n" +
                        "  i.time              as time,\n" +
                        "  i.frequency         as frequency,\n" +
                        "  i.send_status       as send_status\n" +
                        "FROM cloud.parking_space_images AS i\n" +
                        "OFFSET " + numPage + " * 100\n" +
                        "LIMIT 100";

                resultSet = statement.executeQuery(selectImagesSql);

                List<ParkSpaceImages> parkSpaceImagesList = new ArrayList<>();

                while (resultSet.next()) {//遍历结果集
                    long parentId = resultSet.getLong("id");
                    String patrolCarNumber = resultSet.getString("patrol_car_number");
                    String batchNumber = resultSet.getString("batch_number");
                    String parkNumber = resultSet.getString("park_number");
                    String image = resultSet.getString("image");
                    String imagePost = resultSet.getString("image_post");
                    String time = resultSet.getString("time");
                    String frequency = resultSet.getString("frequency");
                    String sendStatus = resultSet.getString("send_status");

                    Thread.sleep(1);
                    long id = idWorker.nextId();
                    if (parentId == 0) {
                        parentId = 1;
                    }
                    ParkSpaceImages images = new ParkSpaceImages(id, parentId, patrolCarNumber, batchNumber, parkNumber, image, imagePost, time, frequency, sendStatus);
                    parkSpaceImagesList.add(images);
                }

                System.out.println("parkSpaceImagesList.size() = " + parkSpaceImagesList.size());
                if (parkSpaceImagesList == null || parkSpaceImagesList.size() == 0) {
                    break;
                }

                String insertImagesSql = "INSERT INTO parking_space_images (id, parent_id, patrol_car_number, batch_number, park_number, image, image_post, time, frequency, send_status)\n" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = mysqlServerCon.prepareStatement(insertImagesSql);
                for (ParkSpaceImages images : parkSpaceImagesList) {
                    preparedStatement.setLong(1, images.getId());
                    preparedStatement.setLong(2, images.getParentId());
                    preparedStatement.setString(3, images.getPatrolCarNumber());
                    preparedStatement.setString(4, images.getBatchNumber());
                    preparedStatement.setString(5, images.getParkNumber());
                    preparedStatement.setString(6, images.getImage());
                    preparedStatement.setString(7, images.getImagePost());
                    preparedStatement.setString(8, images.getTime());
                    preparedStatement.setString(9, images.getFrequency());
                    preparedStatement.setString(10, images.getSendStatus());

                    try {
                        preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.info(e.toString());
                    }
                }
                System.out.println("插入 images " + parkSpaceImagesList.size() + " 条成功...");
                numPage++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
        }

    }

    private static void handleWorking(Statement statement) {
        int numPage = 0;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            while (true) {
                String selectWorkingSql = "SELECT\n" +
                        "  (SELECT m.id\n" +
                        "   FROM cloud.message_data AS m\n" +
                        "   WHERE m.message_id = w.message_id\n" +
                        "   LIMIT 1)             as parent_id,\n" +
                        "  w.message_id          as message_id,\n" +
                        "  w.user_id             as user_id,\n" +
                        "  w.allocate_time       as allocate_time,\n" +
                        "  w.allocate_car_number as allocate_car_number,\n" +
                        "  w.audit_car_number    as audit_car_number,\n" +
                        "  w.audit_time          as audit_time,\n" +
                        "  w.feedback_result     as feedback_result,\n" +
                        "  w.feedback_time       as feedback_time,\n" +
                        "  w.times               as times\n" +
                        "FROM cloud.working AS w\n" +
                        "OFFSET " + numPage + " * 100\n" +
                        "LIMIT 100";

                resultSet = statement.executeQuery(selectWorkingSql);

                List<Working> workingList = new ArrayList<>();

                while (resultSet.next()) {//遍历结果集
                    long parentId = resultSet.getLong("parent_id");
                    String messageId = resultSet.getString("message_id");
                    int userId = resultSet.getInt("user_id");
                    String allocateTime = resultSet.getString("allocate_time");
                    String allocateCarNumber = resultSet.getString("allocate_car_number");
                    String auditCarNumber = resultSet.getString("audit_car_number");
                    String auditTime = resultSet.getString("audit_time");
                    String feedbackResult = resultSet.getString("feedback_result");
                    String feedbackTime = resultSet.getString("feedback_time");
                    int times = resultSet.getInt("times");

                    Thread.sleep(1);
                    long id = idWorker.nextId();
                    if (parentId == 0) {
                        parentId = 1;
                    }
                    Working working = new Working(id, parentId, messageId, userId, allocateTime, allocateCarNumber, auditCarNumber, auditTime, feedbackResult, feedbackTime, times);
                    workingList.add(working);
                }

                System.out.println("workingList.size() = " + workingList.size());
                if (workingList == null || workingList.size() == 0) {
                    break;
                }

                String insertWorkingSql = "INSERT INTO working (id, parent_id, message_id, user_id, allocate_time, allocate_car_number, audit_car_number, audit_time, feedback_result, feedback_time, times)\n" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = mysqlServerCon.prepareStatement(insertWorkingSql);
                for (Working w : workingList) {
                    preparedStatement.setLong(1, w.getId());
                    preparedStatement.setLong(2, w.getParentId());
                    preparedStatement.setString(3, w.getMessageId());
                    preparedStatement.setInt(4, w.getUserId());
                    preparedStatement.setString(5, w.getAllocateTime());
                    preparedStatement.setString(6, w.getAllocateCarNumber());
                    preparedStatement.setString(7, w.getAuditCarNumber());
                    preparedStatement.setString(8, w.getAuditTime());
                    preparedStatement.setString(9, w.getFeedbackResult());
                    preparedStatement.setString(10, w.getFeedbackTime());
                    preparedStatement.setInt(11, w.getTimes());

                    try {
                        preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.info(e.toString());
                    }
                }
                System.out.println("插入 working " + workingList.size() + " 条成功...");
                numPage++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
        }

    }

    private static void handleModifyResults(Statement statement) {
        int numPage = 0;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            while (true) {
                String selectModifyResultsSql = "SELECT\n" +
                        "  m.id                AS id,\n" +
                        "  m.message_id        AS message_id,\n" +
                        "  m.patrol_car_number AS patrol_car_number,\n" +
                        "  m.batch_number      AS batch_number,\n" +
                        "  m.park_number       AS park_number,\n" +
                        "  m.car_number        AS car_number,\n" +
                        "  m.user_id           AS user_id,\n" +
                        "  m.user_name         AS user_name,\n" +
                        "  m.audit_car_number  AS audit_car_number,\n" +
                        "  m.audit_time        AS audit_time\n" +
                        "FROM cloud.modify_results AS m\n" +
                        "OFFSET " + numPage + " * 100\n" +
                        "LIMIT 100";

                resultSet = statement.executeQuery(selectModifyResultsSql);

                List<ModifyResults> modifyResultsList = new ArrayList<>();

                while (resultSet.next()) {//遍历结果集
                    long id = resultSet.getLong("id");
                    String messageId = resultSet.getString("message_id");
                    String patrolCarNumber = resultSet.getString("patrol_car_number");
                    String batchNumber = resultSet.getString("batch_number");
                    String parkNumber = resultSet.getString("park_number");
                    String carNumber = resultSet.getString("car_number");
                    int userId = resultSet.getInt("user_id");
                    String userName = resultSet.getString("user_name");
                    String auditCarNumber = resultSet.getString("audit_car_number");
                    String auditTime = resultSet.getString("audit_time");

                    ModifyResults modifyResults = new ModifyResults(id, messageId, patrolCarNumber, batchNumber, parkNumber, carNumber, userId, userName, auditCarNumber, auditTime);
                    modifyResultsList.add(modifyResults);
                }

                System.out.println("modifyResultsList.size() = " + modifyResultsList.size());
                if (modifyResultsList == null || modifyResultsList.size() == 0) {
                    break;
                }

                String insertModifyResultsSql = "INSERT INTO modify_results (id, message_id, patrol_car_number, batch_number, park_number, car_number, user_id, user_name, audit_car_number, audit_time)\n" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = mysqlServerCon.prepareStatement(insertModifyResultsSql);
                for (ModifyResults m : modifyResultsList) {
                    preparedStatement.setLong(1, m.getId());
                    preparedStatement.setString(2, m.getMessageId());
                    preparedStatement.setString(3, m.getPatrolCarNumber());
                    preparedStatement.setString(4, m.getBatchNumber());
                    preparedStatement.setString(5, m.getParkNumber());
                    preparedStatement.setString(6, m.getCarNumber());
                    preparedStatement.setInt(7, m.getUserId());
                    preparedStatement.setString(8, m.getUserName());
                    preparedStatement.setString(9, m.getAuditCarNumber());
                    preparedStatement.setString(10, m.getAuditTime());

                    try {
                        preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.info(e.toString());
                    }
                }
                System.out.println("插入 ModifyResults " + modifyResultsList.size() + " 条成功...");
                numPage++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
        }

    }

    private static void handleBatchNumber(Statement statement) {
        int numPage = 0;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            while (true) {
                String selectBatchNumberSql = "SELECT\n" +
                        "  b.id                as id,\n" +
                        "  b.patrol_car_number as patrol_car_number,\n" +
                        "  b.number            as number,\n" +
                        "  b.time              as time\n" +
                        "FROM cloud.batch_number AS b\n" +
                        "OFFSET " + numPage + " * 100\n" +
                        "LIMIT 100";

                resultSet = statement.executeQuery(selectBatchNumberSql);

                List<BatchNumber> batchNumberList = new ArrayList<>();

                while (resultSet.next()) {//遍历结果集
                    long id = resultSet.getLong("id");
                    String patrolCarNumber = resultSet.getString("patrol_car_number");
                    String number = resultSet.getString("number");
                    String time = resultSet.getString("time");

                    BatchNumber batchNumber = new BatchNumber(id, patrolCarNumber, number, time);
                    batchNumberList.add(batchNumber);
                }

                System.out.println("batchNumberList.size() = " + batchNumberList.size());
                if (batchNumberList == null || batchNumberList.size() == 0) {
                    break;
                }

                String insertBatchNumberSql = "INSERT INTO batch_number (id, patrol_car_number, number, time) VALUES (?, ?, ?, ?)";
                preparedStatement = mysqlServerCon.prepareStatement(insertBatchNumberSql);
                for (BatchNumber b : batchNumberList) {
                    preparedStatement.setLong(1, b.getId());
                    preparedStatement.setString(2, b.getPatrolCarNumber());
                    preparedStatement.setString(3, b.getNumber());
                    preparedStatement.setString(4, b.getTime());

                    try {
                        preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.info(e.toString());
                    }
                }
                System.out.println("插入 BatchNumber " + batchNumberList.size() + " 条成功...");
                numPage++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
        }

    }

    private static void close(ResultSet resultSet, PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Connection getConnection(String driverName, String url, String userName, String pwd, Connection connect) {
        Connection connection = null;
        try {
            Class.forName(driverName);
            System.out.println(driverName + " 数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (connect == null || (connect != null && connect.isClosed())) {
                connection = DriverManager.getConnection(url, userName, pwd);
                System.out.println(driverName + " 数据库连接成功");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static boolean checkConnection() {
        LOG.info("检查 mysql 数据库连接...");

        String key = "sql.driver";
        String filePath = "config/application.properties";
        String driverName = PropertyUtils.getPropertyValue(key, filePath);
        LOG.info("sql.driver = " + driverName);

        key = "sql.url";
        String url = PropertyUtils.getPropertyValue(key, filePath);
        LOG.info("sql.url = " + url);

        key = "sql.username";
        String userName = PropertyUtils.getPropertyValue(key, filePath);
        LOG.info("sql.username = " + userName);

        key = "sql.password";
        String pwd = PropertyUtils.getPropertyValue(key, filePath);
        LOG.info("sql.password = " + pwd);

        mysqlServerCon = getConnection(driverName, url, userName, pwd, mysqlServerCon);
        LOG.info("mysqlServerCon = " + mysqlServerCon);
        try {
            if (mysqlServerCon == null || (mysqlServerCon != null && mysqlServerCon.isClosed())) {
                System.out.println("连接mysql数据库失败");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("连接mysql数据库异常... " + e.toString());
            return false;
        }

        LOG.info("检查 postgres 数据库连接...");
        key = "postsql.driver";
        driverName = PropertyUtils.getPropertyValue(key, filePath);
        LOG.info("postsql.driver = " + driverName);

        key = "postsql.url";
        url = PropertyUtils.getPropertyValue(key, filePath);
        LOG.info("postsql.url = " + url);

        key = "postsql.username";
        userName = PropertyUtils.getPropertyValue(key, filePath);
        LOG.info("postsql.username = " + userName);

        key = "postsql.password";
        pwd = PropertyUtils.getPropertyValue(key, filePath);
        LOG.info("postsql.password = " + pwd);

        postSqlCon = getConnection(driverName, url, userName, pwd, postSqlCon);
        LOG.info("postSqlCon = " + postSqlCon);
        try {
            if (postSqlCon == null || (postSqlCon != null && postSqlCon.isClosed())) {
                System.out.println("连接postSql数据库失败, postSqlCon = " + postSqlCon);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info("连接postSql数据库异常... " + e.toString());
            return false;
        }
        return true;
    }

}
