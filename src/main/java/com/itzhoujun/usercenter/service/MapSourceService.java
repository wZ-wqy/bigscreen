package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;
import java.util.Calendar;


//大屏地图部分
public class MapSourceService {

    public static JSONArray selectMapService(){
        Connection con = SatisfyService.getconnect();
        JSONArray mapSource = new JSONArray();
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("select t.adLatitude,t.adLongitude,t.adDist,t.templateName,t.taskNo,t.cusName,t.executorName,count(t.cusName)\n" +
                    "from ( \n" +
                    "SELECT\n" +
                    "customeraddress.adLongitude,\n" +
                    "customeraddress.adLatitude,\n" +
                    "task.cusName,\n" +
                    "task.executorName,\n" +
                    "customeraddress.adDist,\n" +
                    "task.templateName,\n" +
                    "task.taskNo,\n" +
                    "task.completeTime \n" +
                    "FROM\n" +
                    "task\n" +
                    "JOIN customeraddress ON task.cusNo = customeraddress.cusNo \n" +
                    "WHERE\n" +
                    "( task.state = '已完成' OR task.state = '已结算' ) \n" +
                    "AND task.completeTime BETWEEN ? \n" +
                    "AND ? \n" +
                    "GROUP BY\n" +
                    "task.taskNo \n" +
                    "ORDER BY\n" +
                    "task.cusName\n" +
                    ") t \n" +
                    "GROUP BY t.cusName,t.executorName");
            Long statime = getTodayZeroPointTimestamps()/1000;
            Long endtime = Calendar.getInstance().getTimeInMillis()/1000;
            pps.setLong(1,statime);
            pps.setLong(2,endtime);
            ResultSet results  = pps.executeQuery();
            while(results.next()){
                JSONObject jso = new JSONObject();
                String[] jwd = new String[2];
                jwd[0] = results.getString("t.adLongitude");
                jwd[1] = results.getString("t.adLatitude");
                jso.accumulate("coords",jwd);
                jso.accumulate("title",results.getString("t.cusName"));
                jso.accumulate("employee",results.getString("t.executorName"));
                jso.accumulate("zone",results.getString("t.adDist"));
                jso.accumulate("type",results.getString("t.templateName"));
                jso.accumulate("counts",results.getString("count(t.cusName)"));
                mapSource.add(jso);
            }
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mapSource;
    }


    public static Long getTodayZeroPointTimestamps(){
        Long currentTimestamps=System.currentTimeMillis();
        Long oneDayTimestamps= Long.valueOf(60*60*24*1000);
        return currentTimestamps-(currentTimestamps+60*60*8*1000)%oneDayTimestamps;
    }



}
