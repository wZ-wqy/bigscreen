package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


import static com.itzhoujun.usercenter.service.PersonalCount.person;


//个人工单总数对比
public class PersonCounts {

    public static JSONObject selectPerCount(Long kstime, Long jstime,Long sykstime, Long syjstime){
        Connection con = PersonStaffService.getconnect();
        JSONObject jso = new JSONObject();
        JSONArray ja = new JSONArray();
        try
        {
            Statement statement = con.createStatement();
            JSONObject js = PersonalCount.personal(kstime,jstime);
            JSONArray jsa = js.getJSONArray("chartData");
            PreparedStatement pps=con.prepareStatement("SELECT task.executorName,count( task.executorName )FROM task JOIN staff ON staff.executorName = task.executorName JOIN bumenfenzu ON staff.fenzuid = bumenfenzu.id WHERE bumenfenzu.fenzuName != '系统集成部'  and task.templateName ='服务单' and task.completeTime BETWEEN ? AND ?  GROUP BY bumenfenzu.id,staff.id ORDER BY bumenfenzu.id,staff.id");
            Long starttime = sykstime;
            Long endtime = syjstime;
            pps.setLong(1,starttime);
            pps.setLong(2,endtime);
            ResultSet results  = pps.executeQuery();
            String[] emp = person();
            String[] th = {"序号","驻点区域","姓名","上月合计（单）","本月合计（单）","同比上个月"};
            JSONObject tableData = new JSONObject();

            String shangyue = "上月合计（单）";
            String benyue = "本月合计（单）";
            String[] syue = new String[emp.length];
            String[] byue = new String[emp.length];
            JSONArray tdj = new JSONArray();
            syue[0] = shangyue;
            byue[0] = benyue;
            String[] heji = new String[4];
            heji[0] = "合计";
            int lmouth = 0;
            int tmouth = 0;
            int bih = 0;
            for(int i = 0;i<emp.length-1;i++) {
                String[] td = new String[6];
                JSONObject jb = new JSONObject();
                if (results.next()) {
                    String emplotte = jsa.getJSONObject(i).get("employee").toString();
                    String executorname = results.getString("task.executorName");
                    if (emplotte.equals(executorname)) {
                        jb.accumulate("employee", emplotte);
                        jb.accumulate("lMonth", results.getInt("count( task.executorName )"));
                        jb.accumulate("tMonth", jsa.getJSONObject(i).get("counts"));
                        syue[i + 1] = results.getString("count( task.executorName )");
                        byue[i + 1] = jsa.getJSONObject(i).get("counts").toString();
                        lmouth += results.getInt("count( task.executorName )");
                        tmouth += Integer.valueOf(jsa.getJSONObject(i).get("counts").toString());
                        td[0] = String.valueOf(i+1);
                        td[1] = selectZuming(results.getString("task.executorName"));
                        td[2] = results.getString("task.executorName");
                        td[3] = results.getString("count( task.executorName )");
                        td[4] = jsa.getJSONObject(i).get("counts").toString();
                        String er = "0";
                        if(Integer.valueOf(td[3])<Integer.valueOf(td[4])){
                            er = "1";
                        }else if(Integer.valueOf(td[3])>Integer.valueOf(td[4])){
                            er = "-1";
                        }
                        td[5] = er;
                        bih += Integer.valueOf(td[5]);
                    } else {
                        jb.accumulate("employee", jsa.getJSONObject(i).get("employee"));
                        jb.accumulate("lMonth", 0);
                        jb.accumulate("tMonth", jsa.getJSONObject(i).get("counts"));
                        syue[i + 1] = "0";
                        byue[i + 1] = jsa.getJSONObject(i).get("counts").toString();
                        td[0] = String.valueOf(i+1);
                        td[1] = selectZuming(jsa.getJSONObject(i).get("employee").toString());
                        td[2] = jsa.getJSONObject(i).get("employee").toString();
                        td[3] = "0";
                        td[4] = "0";
                        String er = "0";
                        if(Integer.valueOf(td[3])<Integer.valueOf(td[4])){
                            er = "1";
                        }else if(Integer.valueOf(td[3])>Integer.valueOf(td[4])){
                            er = "-1";
                        }
                        td[5] = er;
                        bih += Integer.valueOf(td[5]);
                        results.previous();
                    }
                    ja.add(jb);
                    tdj.add(td);
                }
                else{
                    jb.accumulate("employee", jsa.getJSONObject(i).get("employee"));
                    jb.accumulate("lMonth", 0);
                    jb.accumulate("tMonth", jsa.getJSONObject(i).get("counts"));
                    syue[i + 1] = "0";
                    byue[i + 1] = jsa.getJSONObject(i).get("counts").toString();
                    td[0] = String.valueOf(i+1);
                    td[1] = selectZuming(jsa.getJSONObject(i).get("employee").toString());
                    td[2] = jsa.getJSONObject(i).get("employee").toString();
                    td[3] = "0";
                    td[4] = "0";
                    String er = "0";
                    if(Integer.valueOf(td[3])<Integer.valueOf(td[4])){
                        er = "1";
                    }else if(Integer.valueOf(td[3])>Integer.valueOf(td[4])){
                        er = "-1";
                    }
                    td[5] = er;
                    bih += Integer.valueOf(td[5]);
                    ja.add(jb);
                    tdj.add(td);
                }
            }
            heji[1] = String.valueOf(lmouth);
            heji[2] = String.valueOf(tmouth);
            heji[3] = "0";
            if(lmouth>tmouth){
                heji[3] = "-1";
            }else if(lmouth<tmouth){
                heji[3] = "1";
            }
            tdj.add(heji);
            tableData.accumulate("th",th);
            tableData.accumulate("td",tdj);
            jso.accumulate("chartData",ja);
            jso.accumulate("tableData",tableData);
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jso ;
    }



    public static Long sykstime(){
        Long mouthone = 0L;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar=Calendar.getInstance();

        calendar.add(Calendar.MONTH, -1);

        calendar.set(Calendar.DAY_OF_MONTH, 1);

        try {
           String time=format.format(calendar.getTime());
            Date date = format.parse(time);
            mouthone =date.getTime();
            //mouthone = Long.valueOf(format.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //日期转时间戳（毫秒）

        return mouthone;
    }

    public static Long syendtime(){
        Long mouthlast = 0L;
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar=Calendar.getInstance();

        int month=calendar.get(Calendar.MONTH);

        calendar.set(Calendar.MONTH, month-1);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        try {
            String time=sf.format(calendar.getTime());
            Date date = sf.parse(time);
            //日期转时间戳（毫秒）
            mouthlast=date.getTime();
            //mouthlast = Long.valueOf(sf.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mouthlast;
    }
    public static String selectZuming(String name) {
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



}
