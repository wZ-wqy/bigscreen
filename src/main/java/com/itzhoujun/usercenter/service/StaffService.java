package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StaffService {

    public static JSONObject selectStaffService(){
        Connection con = PersonStaffService.getconnect();
        JSONObject js = new JSONObject();
        JSONArray ja = new JSONArray();
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("SELECT staff.ecnid,staff.executorName,staff.fenzuid,bumenfenzu.fenzuName from staff JOIN bumenfenzu on staff.fenzuid = bumenfenzu.id where staff.state!='已删除' and bumenfenzu.fenzuName != '系统集成部' order by staff.id");
            ResultSet results  = pps.executeQuery();
            while(results.next()){
                JSONObject jso = new JSONObject();
                jso.accumulate("ecnid",results.getString("staff.ecnid"));
                jso.accumulate("username",results.getString("staff.executorName"));
                jso.accumulate("fenzuid",results.getString("staff.fenzuid"));
                jso.accumulate("fenzu",results.getString("bumenfenzu.fenzuName"));
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




}
