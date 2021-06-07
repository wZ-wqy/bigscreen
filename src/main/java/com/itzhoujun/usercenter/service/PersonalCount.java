package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;


//个人工单总数统计
public class PersonalCount {

    public static JSONObject personal(Long kstime, Long jstime){
        Connection con = PersonStaffService.getconnect();
        JSONObject jsonp = new JSONObject();
        JSONArray personal = new JSONArray();
        JSONArray tda = new JSONArray();
        JSONObject tableData = new JSONObject();
        try{
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT bumenfenzu.fenzuName,staff.executorName,count( task.executorName )FROM task JOIN staff ON staff.executorName = task.executorName JOIN bumenfenzu ON staff.fenzuid = bumenfenzu.id WHERE bumenfenzu.fenzuName != '系统集成部'  and task.templateName ='服务单' and task.completeTime BETWEEN ? and ?  GROUP BY bumenfenzu.id,staff.id ORDER BY bumenfenzu.id,staff.id");
            ppst.setLong(1,kstime);
            ppst.setLong(2,jstime);
            ResultSet result  = ppst.executeQuery();
            String[] per = person();
            String[] th = {"序号","驻点区域","姓名","合计"};
            int conuts = 0;
            for(int i =1;i<per.length;i++) {
                String[] td = new String[4];
                td[0] = String.valueOf(i);
                JSONObject person= new JSONObject();
                if(result.next()){
                    if(per[i]==result.getString("staff.executorName")||per[i].equals(result.getString("staff.executorName"))){
                        person.accumulate("employee",result.getString("staff.executorName"));
                        person.accumulate("counts",result.getInt("count( task.executorName )"));
                        td[1] = result.getString("bumenfenzu.fenzuName");
                        td[2] = result.getString("staff.executorName");
                        td[3] = result.getString("count( task.executorName )");
                        conuts += Integer.valueOf(td[3]);
                    }else {
                        person.accumulate("employee",per[i]);
                        person.accumulate("counts",0);
                        td[1] = zuming(per[i]);
                        td[2] = per[i];
                        td[3] = "0";
                        conuts += Integer.valueOf(td[3]);
                        result.previous();
                    }
                }else {
                    person.accumulate("employee",per[i]);
                    person.accumulate("counts",0);
                    td[1] = zuming(per[i]);
                    td[2] = per[i];
                    td[3] = "0";
                    conuts += Integer.valueOf(td[3]);
                }
                personal.add(person);
                tda.add(td);

            }
            String[] coun = {"合计",String.valueOf(conuts)};
            tda.add(coun);
            tableData.accumulate("th",th);
            tableData.accumulate("td",tda);
            jsonp.accumulate("chartData",personal);
            jsonp.accumulate("tableData",tableData);
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonp;

    }

    public static String zuming(String name) {
        String na = "";
        try{
            Connection con = PersonStaffService.getconnect();
            Statement statement = con.createStatement();
            PreparedStatement pst=con.prepareStatement("select bumenfenzu.fenzuName from staff join bumenfenzu on staff.fenzuid = bumenfenzu.id where staff.executorName = ?");
            pst.setString(1,name);
            ResultSet resultset  = pst.executeQuery();
            resultset.next();
            na = resultset.getString("bumenfenzu.fenzuName");
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return na;
    }
    public static String[] person() {
        Connection con = PersonStaffService.getconnect();
        String [] s = new String[0];
        try{
            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("select staff.executorName from staff join bumenfenzu on staff.fenzuid = bumenfenzu.id where bumenfenzu.fenzuname != '系统集成部'  ORDER BY bumenfenzu.id,staff.id ");
            ResultSet result  = pps.executeQuery();
            result.last();
            int i = result.getRow();
            String [] person = new String[i+1];
            result.beforeFirst();
            person[0] = "";
            int j = 1;
            while (result.next()){
                person[j] = result.getString(1);
                j++;
            }
            return person;
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return s;
    }


}
