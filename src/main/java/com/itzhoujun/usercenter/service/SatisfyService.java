package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;


//运维服务部饼图
public class SatisfyService {

    private static final String url = "jdbc:mysql://localhost:3306/shouhoubao?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&autoReconnect=true";
    private static final String user = "root";
    private static final String password = "123456";
    private static Connection con;
    static Connection getconnect()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return con;
    }


    public  static JSONArray satisfaction() {
        JSONArray techChart = new JSONArray();

        double yiban =  Math.ceil(yiban()/taskCloseCount()*100);
        double nosatisfact = Math.ceil(noSatisfy()/taskCloseCount()*100);
        int satisfact = (int)(100-yiban-nosatisfact);
        JSONObject satisfactjs = new JSONObject();
        JSONObject yibanjs = new JSONObject();
        JSONObject nosatisfactjs = new JSONObject();
        satisfactjs.accumulate("item","满意");
        satisfactjs.accumulate("percent",satisfact);
        yibanjs.accumulate("item","一般");
        yibanjs.accumulate("percent",yiban);
        nosatisfactjs.accumulate("item","不满意");
        nosatisfactjs.accumulate("percent",nosatisfact);
        techChart.add(satisfactjs);
        techChart.add(yibanjs);
        techChart.add(nosatisfactjs);
        return techChart;
    }

    public static double taskCloseCount(){
        Connection con = getconnect();
        double closeCount = 0;
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT COUNT(task.taskNo) FROM `task` join taskattribute on task.taskNo = taskattribute.taskNo where task.templateName = '服务单' and state = '已关闭'");
            ResultSet result = ppst.executeQuery();
            result.next();
            closeCount = result.getInt(1);
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return closeCount;

    }
    public static int satisfy(){
        Connection con = getconnect();
        int satisfyCount = 0;
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT COUNT(task.taskNo) FROM `task` join taskattribute on task.taskNo = taskattribute.taskNo where task.templateName = '服务单' and state = '已关闭' and taskattribute.field_BKzX3eYzndTcLhkK='满意'");
            ResultSet result = ppst.executeQuery();
            result.next();
            satisfyCount =result.getInt(1);
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return satisfyCount;

    }
    public static int yiban(){
        Connection con = getconnect();
        int yibanCount = 0;
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT COUNT(task.taskNo) FROM `task` join taskattribute on task.taskNo = taskattribute.taskNo where task.templateName = '服务单' and state = '已关闭' and taskattribute.field_BKzX3eYzndTcLhkK='一般'");
            ResultSet result = ppst.executeQuery();
            result.next();
            yibanCount =result.getInt(1);
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return yibanCount;

    }
    public static int noSatisfy(){
        Connection con = getconnect();
        int noSatisfyCount = 0;
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT COUNT(task.taskNo) FROM `task` join taskattribute on task.taskNo = taskattribute.taskNo where task.templateName = '服务单' and state = '已关闭' and taskattribute.field_BKzX3eYzndTcLhkK='不满意'");
            ResultSet result = ppst.executeQuery();
            result.next();
            noSatisfyCount =result.getInt(1);
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noSatisfyCount;

    }

    public static int suggestions(){
        Connection con = getconnect();
        int suggestionsCount = 0;
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT count(taskattribute.taskNo) FROM `taskattribute` JOIN task on taskattribute.taskNo = task.taskNo where field_Kp3R9ntn6wD0YFsS !='' and task.state = '已关闭'");
            ResultSet result = ppst.executeQuery();
            result.next();
            suggestionsCount =result.getInt(1);
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suggestionsCount;

    }







}
