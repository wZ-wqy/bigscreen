package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.sql.*;

import static com.itzhoujun.usercenter.service.SelectDevice.selectFenZu;


//驻点人均单数统计
public class SelectPerCapita {

//汇总数据拼接
    public static JSONObject perCapita(Long kstime, Long jstime,int day) {

        JSONArray countSta = new JSONArray();
        JSONArray fzcount = new JSONArray();
        JSONArray chadata = new JSONArray();
        JSONArray td = new JSONArray();
        JSONArray zong = new JSONArray();
        JSONObject tbdata = new JSONObject();
        JSONObject data = new JSONObject();

        countSta = countSta(kstime, jstime);
        fzcount = fzcount();
        for (int i = 0; i < countSta.size(); i++) {
            JSONObject lb = new JSONObject();
            JSONObject js1 = countSta.getJSONObject(i);
            JSONObject js2 = fzcount.getJSONObject(i);
            double shu1 = js1.getInt("counts");
            double shu2 = js2.getInt("counts");
            if(shu1!=0){
                double renj = shu1 / shu2 / day;
                BigDecimal pinjun = new BigDecimal(renj);
                renj = pinjun.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                lb.accumulate("type", "人均每天服务单数（单）");
                lb.accumulate("group", js1.getString("group"));
                lb.accumulate("counts", renj);
            }else {
                lb.accumulate("type", "人均每天服务单数（单）");
                lb.accumulate("group", js1.getString("group"));
                lb.accumulate("counts", 0);
            }

            zong.add(lb);
        }
        for (int h = 0; h < countSta.size(); h++) {
            chadata.add(countSta.getJSONObject(h));
        }
        for (int h = 0; h < fzcount.size(); h++) {
            chadata.add(fzcount.getJSONObject(h));
        }
        for (int h = 0; h < zong.size(); h++) {
            chadata.add(zong.getJSONObject(h));
        }
        data.accumulate("chartData", chadata);
        String[] zongweihu = new String[zong.size() + 2];
        String[] fuwuren = new String[zong.size() + 2];
        String[] renjun = new String[zong.size() + 2];
        zongweihu[0] = "1";
        fuwuren[0] = "2";
        renjun[0] = "3";
        zongweihu[1] = "总维护单数（单）";
        fuwuren[1] = "服务人员总数（人）";
        renjun[1] = "人均每天服务单数（单）";
        for (int j = 0; j < countSta.size(); j++) {
            JSONObject js1 = countSta.getJSONObject(j);
            JSONObject js2 = fzcount.getJSONObject(j);
            JSONObject js3 = zong.getJSONObject(j);
            zongweihu[j+2] = js1.getString("counts");
            fuwuren[j+2] = js2.getString("counts");
            renjun[j+2] = js3.getString("counts");
        }
        td.add(zongweihu);
        td.add(fuwuren);
        td.add(renjun);
        String[] chazu = selectFenZu();
        String[] zu = new String[selectFenZu().length+1];
        zu[0] = "序号";
        zu[1] = "名称";
        for(int i =1;i<chazu.length;i++){
            zu[i+1] = chazu[i];
        }
        tbdata.accumulate("th", zu);
        tbdata.accumulate("td", td);
        data.accumulate("tableData", tbdata);
        return data;
    }

    //查询chartData部分数据
    public static JSONArray fzcount(){
        Connection con = PersonStaffService.getconnect();
        JSONArray lb = new JSONArray();
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement pp=con.prepareStatement("SELECT bumenfenzu.fenzuName,COUNT(staff.executorName) from staff JOIN bumenfenzu on staff.fenzuid=bumenfenzu.id WHERE bumenfenzu.fenzuName!='系统集成部'  GROUP BY bumenfenzu.id");
            ResultSet result  = pp.executeQuery();
            String[] zu = selectFenZu();
            for(int i =1;i<zu.length;i++) {
                JSONObject person = new JSONObject();
                if (result.next()) {
                    if (zu[i] == result.getString("bumenfenzu.fenzuName") || zu[i].equals(result.getString("bumenfenzu.fenzuName"))) {
                        person.accumulate("type","服务人员总数（人）");
                        person.accumulate("group", result.getString("bumenfenzu.fenzuName"));
                        person.accumulate("counts", result.getInt("COUNT(staff.executorName)"));
                    } else {
                        person.accumulate("type","服务人员总数（人）");
                        person.accumulate("group", zu[i]);
                        person.accumulate("counts", 0);
                        result.previous();
                    }
                } else {
                    person.accumulate("type","服务人员总数（人）");
                    person.accumulate("group", zu[i]);
                    person.accumulate("counts", 0);
                }
                lb.add(person);
            }
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lb;
    }

    //查询tableData部分数据
    public static JSONArray countSta(Long kstime, Long jstime){
        Connection con = PersonStaffService.getconnect();
        JSONArray ja = new JSONArray();
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("select bumenfenzu.fenzuName,COUNT(task.executorName) from task JOIN staff on task.executorName=staff.executorName join bumenfenzu on bumenfenzu.id=staff.fenzuid where bumenfenzu.fenzuName!='系统集成部'  and task.templateName ='服务单' and task.completeTime BETWEEN ? AND ? GROUP BY bumenfenzu.id ");
            ppst.setLong(1,kstime);
            ppst.setLong(2,jstime);
            ResultSet result  = ppst.executeQuery();
            String[] zu = selectFenZu();
            for(int i =1;i<zu.length;i++) {
                JSONObject person = new JSONObject();
                if (result.next()) {
                    if (zu[i] == result.getString("bumenfenzu.fenzuName") || zu[i].equals(result.getString("bumenfenzu.fenzuName"))) {
                        person.accumulate("type", "总维护单数（单）");
                        person.accumulate("group", result.getString("bumenfenzu.fenzuName"));
                        person.accumulate("counts", result.getInt("COUNT(task.executorName)"));
                    } else {
                        person.accumulate("type", "总维护单数（单）");
                        person.accumulate("group", zu[i]);
                        person.accumulate("counts", 0);
                        result.previous();
                    }
                } else {
                    person.accumulate("type", "总维护单数（单）");
                    person.accumulate("group", zu[i]);
                    person.accumulate("counts", 0);
                }
                ja.add(person);
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
