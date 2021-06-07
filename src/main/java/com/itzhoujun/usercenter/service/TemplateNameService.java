package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;

public class TemplateNameService {


    public static JSONObject selectTemplateService(){
        Connection con = SatisfyService.getconnect();
        JSONObject worksheetTable = new JSONObject();
        JSONObject fwjs = new JSONObject();
        JSONObject fxjs = new JSONObject();
        JSONArray  tbody = new JSONArray();
        JSONArray  thead = new JSONArray();
        String[] zu = selectFenZu();
        zu[0] = "类型";
        String[] fwcounts = new String[selectFenZu().length];
        String[] fxcounts = new String[selectFenZu().length];
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("SELECT bumenfenzu.fenzuName,count(task.taskNo) FROM task join staff on task.executorName = staff.executorName join bumenfenzu on staff.fenzuid = bumenfenzu.id where task.templateName = '服务单' and task.state != '已关闭' and fenzuName!='系统集成部' GROUP BY bumenfenzu.id ORDER BY bumenfenzu.id ");
            ResultSet results  = pps.executeQuery();
            fwcounts[0] = "服务单";
            fxcounts[0] = "返修单";
            for( int i =1;i<zu.length;i++){
                results.next();
                if(zu[i].equals(results.getString(1))){
                    fwcounts[i] = results.getString("count(task.taskNo)");
                }else {
                    fwcounts[i] = "0";
                    results.previous();
                }

            }
            PreparedStatement ppss=con.prepareStatement("SELECT bumenfenzu.fenzuName,count(task.taskNo) FROM task join staff on task.executorName = staff.executorName join bumenfenzu on staff.fenzuid = bumenfenzu.id where task.templateName = '返修单' and task.state != '已关闭' and fenzuName!='系统集成部' GROUP BY bumenfenzu.id ORDER BY bumenfenzu.id ");
            ResultSet result  = ppss.executeQuery();
            for( int h =1;h<zu.length;h++){
                result.next();
                if(zu[h].equals(result.getString(1))){
                    fxcounts[h] = result.getString("count(task.taskNo)");
                }else {
                    fxcounts[h] = "0";
                    result.previous();
                }

            }
            for(int j=0;j<zu.length;j++){
                JSONObject js = new JSONObject();
                js.accumulate("title",zu[j]);
                js.accumulate("dataIndex","datas["+j+"]");
                thead.add(js);
            }

            fwjs.accumulate("key","0");
            fwjs.accumulate("datas",fwcounts);
            fxjs.accumulate("key","1");
            fxjs.accumulate("datas",fxcounts);
            tbody.add(fwjs);
            tbody.add(fxjs);
            worksheetTable.accumulate("thead",thead);
            worksheetTable.accumulate("tbody",tbody);
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return worksheetTable;
    }


    public static String[] selectFenZu() {
        Connection con = SatisfyService.getconnect();
        String[] strss = new String[0];
        try {
            Statement statement = con.createStatement();
            PreparedStatement ppst = con.prepareStatement("select fenzuName from bumenfenzu where fenzuName!='系统集成部'");
            ResultSet result = ppst.executeQuery();
            result.last(); //结果集指针知道最后一行数据
            int n = result.getRow();
            String[] fenzu = new String[n + 1];
            fenzu[0] = "";
            result.beforeFirst();//将结果集指针指回到开始位置，这样才能通过while获取rs中的数据
            int i = 1;
            while (result.next()) {
                fenzu[i] = result.getString(1);
                i++;
            }
            return fenzu;
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return strss;
    }


















}
