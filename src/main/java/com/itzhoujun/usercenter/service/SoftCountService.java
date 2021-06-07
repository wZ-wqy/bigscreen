package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;


//查询软件部服务满意度饼图
public class SoftCountService {


    public  static JSONArray satisfactionCount() {
        JSONArray softChart = new JSONArray();
        double cou = satisfaction()+ordinary()+noSatisfaction();
        int satisfact = (int) (satisfaction()/cou*100);
        int yiban = (int)(ordinary()/cou*100);
        int nosatisfact = (int)(noSatisfaction()/cou*100);
        JSONObject satisfactjs = new JSONObject();
        JSONObject yibanjs = new JSONObject();
        JSONObject nosatisfactjs = new JSONObject();
        satisfactjs.accumulate("item","满意");
        satisfactjs.accumulate("percent",satisfact);
        yibanjs.accumulate("item","一般");
        yibanjs.accumulate("percent",yiban);
        nosatisfactjs.accumulate("item","不满意");
        nosatisfactjs.accumulate("percent",nosatisfact);
        softChart.add(satisfactjs);
        softChart.add(yibanjs);
        softChart.add(nosatisfactjs);
        return softChart;
    }



    public static int satisfaction(){
        Connection con = SatisfyService.getconnect();
        int satisfyCount = 0;
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT satisfaction FROM softwarecount");
            ResultSet result = ppst.executeQuery();
            while(result.next()){
                satisfyCount += result.getInt(1);
            }
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
    public static int ordinary(){
        Connection con = SatisfyService.getconnect();
        int yibanCount = 0;
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT ordinary FROM softwarecount");
            ResultSet result = ppst.executeQuery();
            while(result.next()){
                yibanCount =result.getInt(1);
            }
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return yibanCount;

    }
    public static int noSatisfaction(){
        Connection con = SatisfyService.getconnect();
        int noSatisfyCount = 0;
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT noSatisfaction FROM softwarecount");
            ResultSet result = ppst.executeQuery();
            while(result.next()){
                noSatisfyCount =result.getInt(1);
            }
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noSatisfyCount;

    }

    public static int opinion(){
        Connection con = SatisfyService.getconnect();
        int opinionCount = 0;
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT opinion FROM softwarecount");
            ResultSet result = ppst.executeQuery();
            while(result.next()){
                opinionCount =result.getInt(1);
            }
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return opinionCount;

    }

}
