package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Calendar;


//查询服务满意度运维周统计
public class WeekCountService {


    public  static JSONObject satisfaction() {
        JSONObject techTable = new JSONObject();
        JSONArray  thead = new JSONArray();
        JSONArray  tbody = new JSONArray();
        JSONObject dans = new JSONObject();
        JSONObject manyd = new JSONObject();
        JSONObject yijian = new JSONObject();
        JSONObject tb = new JSONObject();
        String startTime = String.valueOf(getTimesWeekmorning());
        String endTime = String.valueOf(getTimesWeeknight());
        System.out.println("starttime"+startTime);
        System.out.println("endtime"+endTime);
        int weelcou = weekCount(startTime,endTime);
        double satisfy = satisfy(startTime,endTime);
        double co = (satisfy/weelcou)*100;
        String bTo = co+"%";
        dans.accumulate("title","服务单数");
        dans.accumulate("dataIndex","datas[0]");
        manyd.accumulate("title","满意度");
        manyd.accumulate("dataIndex","datas[1]");
        yijian.accumulate("title","客户意见");
        yijian.accumulate("dataIndex","datas[2]");
        thead.add(dans);
        thead.add(manyd);
        thead.add(yijian);
        String[] datas = new String[3];
        datas[0] = String.valueOf(weelcou);
        datas[1] = bTo;
        datas[2] = String.valueOf(cusOpinion(startTime,endTime).size());
        tb.accumulate("key", "0");
        tb.accumulate("datas",datas);
        tbody.add(tb);
        techTable.accumulate("thead",thead);
        techTable.accumulate("tbody",tbody);
        System.out.println("techTable::"+techTable);
        return techTable;
    }

    public static int weekCount(String startTime,String endTime){
        Connection con = SatisfyService.getconnect();
        int closeCount = 0;

        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT COUNT(task.taskNo) FROM `task` join taskattribute on task.taskNo = taskattribute.taskNo where task.templateName = '服务单' and  (task.state = '已完成' or task.state = '已关闭') and task.completeTime BETWEEN ? and  ?");
            ppst.setString(1,startTime);
            ppst.setString(2,endTime);
            ResultSet result = ppst.executeQuery();
            result.next();
            closeCount =result.getInt(1);
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return closeCount;

    }
    public static double satisfy(String startTime,String endTime){
        Connection con = SatisfyService.getconnect();
        double satisfyCount = 0;
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT COUNT(task.taskNo) FROM `task` join taskattribute on task.taskNo = taskattribute.taskNo where task.templateName = '服务单' and  (task.state = '已完成' or task.state = '已关闭') and task.completeTime BETWEEN ? and ?");
            ppst.setString(1,startTime);
            ppst.setString(2,endTime);
            ResultSet result = ppst.executeQuery();
            result.next();
            satisfyCount =result.getInt(1);
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return satisfyCount;

    }
    public static JSONArray cusOpinion(String startTime, String endTime){
        Connection con = SatisfyService.getconnect();
        JSONArray cusName = new JSONArray();
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("select task.cusName,taskattribute.field_Kp3R9ntn6wD0YFsS from task join taskattribute on task.taskNo = taskattribute.taskNo where task.state = '已关闭' and taskattribute.field_Kp3R9ntn6wD0YFsS != '' and task.closeTime BETWEEN ? and ?");
            ppst.setString(1,startTime);
            ppst.setString(2,endTime);
            ResultSet result = ppst.executeQuery();

            while (result.next()){
                JSONObject cusn = new JSONObject();
                cusn.accumulate("title",result.getString("task.cusName"));
                cusn.accumulate("text",result.getString("taskattribute.field_Kp3R9ntn6wD0YFsS"));
                cusName.add(cusn);
            }
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cusName;

    }
    public static int taskOpinion(){
        Connection con = SatisfyService.getconnect();
        int i = 0;
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("SELECT COUNT(taskattribute.taskNo) FROM taskattribute where taskattribute.field_Kp3R9ntn6wD0YFsS != ''");
            ResultSet results  = pps.executeQuery();
            results.next();
            i = results.getInt("COUNT(taskattribute.taskNo)");
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    //获得本周一0点时间
    public static int getTimesWeekmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return (int) (cal.getTimeInMillis()/1000-(7 * 24 * 60 * 60));
    }
    //获得本周日24点时间
    public static int getTimesWeeknight(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return (int) ((cal.getTime().getTime())/1000);
    }



}
