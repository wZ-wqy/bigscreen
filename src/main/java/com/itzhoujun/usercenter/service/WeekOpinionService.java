package com.itzhoujun.usercenter.service;


import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;

import static com.itzhoujun.usercenter.service.WeekCountService.getTimesWeekmorning;
import static com.itzhoujun.usercenter.service.WeekCountService.getTimesWeeknight;

public class WeekOpinionService {

    public static JSONArray weekOpinionSelect(){
        Connection con = SatisfyService.getconnect();
        JSONArray ja = new JSONArray();
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("SELECT task.id,task.cusName,taskattribute.field_Kp3R9ntn6wD0YFsS FROM task join taskattribute on task.taskNo = taskattribute.taskNo where taskattribute.field_Kp3R9ntn6wD0YFsS != ''  and task.completeTime BETWEEN ? and ? GROUP BY task.id");
            pps.setLong(1,getTimesWeekmorning());
            pps.setLong(2,getTimesWeeknight());
            ResultSet results  = pps.executeQuery();
            while(results.next()){
                JSONObject jso = new JSONObject();
                jso.accumulate("cusName",results.getString("task.cusName"));
                jso.accumulate("opinion",results.getString("taskattribute.field_Kp3R9ntn6wD0YFsS"));
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





}
