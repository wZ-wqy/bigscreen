package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;
import java.util.Calendar;


//展示软件部服务满意度
public class SoftwareService {

    public static JSONObject softwareSelect(){
        Connection con = SatisfyService.getconnect();
        JSONObject js = new JSONObject();
        JSONArray ja = new JSONArray();
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("select * from softwarecount");
            ResultSet results  = pps.executeQuery();
            while(results.next()){
                JSONObject jso = new JSONObject();
                jso.accumulate("month",results.getString("month"));
                jso.accumulate("frequency",results.getString("frequency"));
                jso.accumulate("satisfaction",results.getString("satisfaction"));
                jso.accumulate("ordinary",results.getString("ordinary"));
                jso.accumulate("noSatisfaction",results.getString("noSatisfaction"));
                jso.accumulate("opinion",results.getString("opinion"));
                ja.add(jso);
            }
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        js.accumulate("data",ja);
        return js;
    }


    public static JSONObject monthSelect(){
        Connection con = SatisfyService.getconnect();
        JSONObject softTable = new JSONObject();
        JSONArray  thead = new JSONArray();
        JSONArray  tbody = new JSONArray();
        try
        {

            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("SELECT * FROM softwarecount where `month` = ?");
            pps.setString(1,sytime());

            ResultSet results  = pps.executeQuery();
            results.next();
            JSONObject dans = new JSONObject();
            JSONObject manyd = new JSONObject();
            JSONObject yijian = new JSONObject();
            JSONObject tb = new JSONObject();
            int frequency = results.getInt("frequency");
            double satisfaction = results.getInt("satisfaction");
            int myd = (int)(satisfaction/frequency*100);
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
            datas[0] = String.valueOf(frequency);
            datas[1] = String.valueOf(myd)+"%";
            datas[2] = results.getString("opinion");
            tb.accumulate("key", "0");
            tb.accumulate("datas",datas);
            tbody.add(tb);
            softTable.accumulate("thead",thead);
            softTable.accumulate("tbody",tbody);
            System.out.println("softTable::"+softTable);
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return softTable;
    }

    public static int softOpinionCount(){
        Connection con = SatisfyService.getconnect();
        int i = 0;
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("SELECT opinion FROM softwarecount");
            ResultSet results  = pps.executeQuery();

            while(results.next()){
                i += results.getInt("opinion");
            }
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

    public static JSONArray software(){
        Connection con = SatisfyService.getconnect();
        JSONArray ja = new JSONArray();
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("SELECT * FROM softwareyj");
            ResultSet results  = pps.executeQuery();
            while(results.next()){
                JSONObject jso = new JSONObject();
                jso.accumulate("title",results.getString("cusName"));
                jso.accumulate("text",results.getString("opinion"));
                ja.add(jso);
            }
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }



    public static String sytime(){

        Calendar calendar=Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if(month == 0){
            month = 12;
            year --;
        }
        String date = year+"-"+month;

        return date;
    }




}
