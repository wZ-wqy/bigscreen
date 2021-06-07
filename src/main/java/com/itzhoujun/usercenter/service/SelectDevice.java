package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;




//驻点设备类型单数统计
public class SelectDevice {

    //数据拼接
    public static JSONObject selectDevice(Long kstime, Long jstime)
    {
        JSONArray res = new JSONArray();
        JSONObject jso = new JSONObject();
        JSONArray td = new JSONArray();
        JSONObject tableData = new JSONObject();
        Connection con = PersonStaffService.getconnect();
        try
        {
            Statement statement = con.createStatement();
//            执行查询语句
            PreparedStatement pst=con.prepareStatement("SELECT eqtype.sbtype,bumenfenzu.fenzuName,COUNT(task.executorName) AS shu FROM task JOIN staff ON staff.executorName = task.executorName JOIN bumenfenzu ON bumenfenzu.id=staff.fenzuid JOIN taskproducts ON task.taskNo = taskproducts.taskNo JOIN equipment ON equipment.serialNumber = taskproducts.serialNumber JOIN eqtype ON equipment.sbtypeid = eqtype.id WHERE bumenfenzu.fenzuName != '系统集成部' and task.templateName ='服务单' and task.completeTime BETWEEN ? AND ? GROUP BY eqtype.id,bumenfenzu.id ORDER BY eqtype.id,bumenfenzu.id");
            pst.setLong(1,kstime);
            pst.setLong(2,jstime);
            ResultSet resultSet  = pst.executeQuery();
            String[] zu = selectFenZu();
            String[] sb = selectEqu();
            for(int j =1;j<sb.length;j++) {
                for (int i = 1; i < zu.length; i++) {
                    if (resultSet.next()) {
                        if (sb[j].equals(resultSet.getString("eqtype.sbtype"))) {
                            JSONObject jsb = new JSONObject();
                            if (zu[i].equals(resultSet.getString("bumenfenzu.fenzuName")) || zu[i] == resultSet.getString("bumenfenzu.fenzuName")) {
                                jsb.accumulate("device", resultSet.getString("eqtype.sbtype"));
                                jsb.accumulate("group", zu[i]);
                                jsb.accumulate("counts", resultSet.getInt(3));
                            } else {
                                jsb.accumulate("device", sb[j]);
                                jsb.accumulate("group", zu[i]);
                                jsb.accumulate("counts", 0);
                                resultSet.previous();
                            }
                            res.add(jsb);
                        } else {
                                JSONObject jsbp = new JSONObject();
                                jsbp.accumulate("device", sb[j]);
                                jsbp.accumulate("group", zu[i]);
                                jsbp.accumulate("counts", 0);
                                resultSet.previous();
                                res.add(jsbp);

                        }
                    }
                }
            }
            String[] thqian =selectEqu();
            String[] th = new String[thqian.length+2];
            th[0] = "序号";
            th[1] = "驻点区域";
            for(int q = 2; q<thqian.length+1;q++){
                th[q] = thqian[q-1];
            }
            th[th.length-1] = "合计";

            tableData.accumulate("th",th);
            tableData.accumulate("td",selectTd(kstime,jstime));
            jso.accumulate("chartData",res);
            jso.accumulate("tableData",tableData);
            con.close();
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jso;


    }
//查询设备类型
    public static String[] selectEqu(){
        Connection con = PersonStaffService.getconnect();
        String[] strss = new String[0];
        try
        {
            Statement statement = con.createStatement();

            PreparedStatement ppst=con.prepareStatement("SELECT eqtype.sbtype FROM eqtype GROUP BY eqtype.id");
            ResultSet result  = ppst.executeQuery();
            result.last(); //结果集指针知道最后一行数据
            int n = result.getRow();
            String[] strs = new String[n+1];
            result.beforeFirst();//将结果集指针指回到开始位置，这样才能通过while获取rs中的数据
            strs[0] = "";
            int i = 1;
            while(result.next()) {
                strs[i] = result.getString(1);
                i++;
            }
            return strs;
        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return strss;
    }

//查询统计数据
    public static JSONArray selectTd(Long kstime, Long jstime){
        Connection con = PersonStaffService.getconnect();
        JSONArray ja = new JSONArray();
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("SELECT t.fenzuName,sum( IF ( t.sbtype = '电脑设备', shu, 0 ) ) AS '电脑设备合计',sum( IF ( t.sbtype = '打印设备', shu, 0 ) ) AS '打印设备合计',sum( IF ( t.sbtype = '网络设备', shu, 0 ) ) AS '网络设备合计',sum( IF ( t.sbtype = '监控设备', shu, 0 ) ) AS '监控设备合计',sum( IF ( t.sbtype = '电话设备', shu, 0 ) ) AS '电话设备合计',sum( IF ( t.sbtype = '会议设备', shu, 0 ) ) AS '会议设备合计',sum( IF ( t.sbtype = '家电设备', shu, 0 ) ) AS '家电设备合计',sum( IF ( t.sbtype = '数码类', shu, 0 ) ) AS '数码类合计',sum( IF ( t.sbtype = '医院设备', shu, 0 ) ) AS '医院设备合计',sum( IF ( t.sbtype = '其他类', shu, 0 ) ) AS '其他类合计' FROM(SELECT eqtype.sbtype,bumenfenzu.fenzuName,bumenfenzu.id,COUNT(task.executorName) AS shu FROM task  JOIN staff ON staff.executorName = task.executorName JOIN bumenfenzu ON bumenfenzu.id=staff.fenzuid JOIN taskproducts ON task.taskNo = taskproducts.taskNo JOIN equipment ON equipment.serialNumber = taskproducts.serialNumber JOIN eqtype ON equipment.sbtypeid = eqtype.id WHERE bumenfenzu.fenzuName != '系统集成部' and task.templateName ='服务单' and task.completeTime BETWEEN ? AND ? GROUP BY eqtype.id,bumenfenzu.id ORDER BY bumenfenzu.id) t GROUP BY t.fenzuName ORDER BY t.id");
            ppst.setLong(1,kstime);
            ppst.setLong(2,jstime);
            ResultSet result  = ppst.executeQuery();
            String[] sb = new String[selectEqu().length+2];
            String[] fenz = selectFenZu();
            for (int i = 1;i<fenz.length;i++) {
                int counts = 0;
                sb[0] = String.valueOf(i);
                if (result.next()) {
                        if (fenz[i] == result.getString("t.fenzuName") || fenz[i].equals(result.getString("t.fenzuName"))) {

                            for (int j = 1; j < sb.length-1; j++) {
                                sb[j] = result.getString(j);

                            }
                            for (int t = 2; t < sb.length-1;t++) {
                                counts += result.getInt(t);
                            }
                        } else {
                            sb[1] = fenz[i];
                            for(int h =2;h<sb.length;h++ ){
                                sb[h] = "0";
                            }
                            counts = 0;
                            result.previous();
                        }
                } else {
                    sb[1] = fenz[i];
                    for(int y =2;y<sb.length;y++ ){
                         sb[y] = "0";
                    }
                    counts = 0;
                }
                sb[sb.length-1] = String.valueOf(counts);
                ja.add(sb);
            }
            String[] heji = new String[selectEqu().length+1];
            heji[0] = "合计";
            for(int w =1;w<selectEqu().length+1;w++){
                int er = 0;
                for(int zu = 0 ;zu<ja.size();zu++){
                    String st =ja.getString(zu);
                    String[] a = st.split(",");
                    a[0] = a[0].substring(1);
                    for(int u = 0;u<a.length;u++){
                        a[u] =  a[u].replace("\"", "");
                        a[u] =  a[u].replace("]", "");
                    }
                    er += Integer.parseInt(a[w+1]);
                    JSONArray jas = new JSONArray();
                    jas.add(a);
                }
                heji[w] = String.valueOf(er);
            }
            ja.add(heji);




        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public static String[] selectFenZu(){
        Connection con = PersonStaffService.getconnect();
        String[] strss = new String[0];
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("select fenzuName from bumenfenzu where fenzuName!='系统集成部'");
            ResultSet result  = ppst.executeQuery();
            result.last(); //结果集指针知道最后一行数据
            int n = result.getRow();
            String[] fenzu = new String[n+1];
            fenzu[0] = "";
            result.beforeFirst();//将结果集指针指回到开始位置，这样才能通过while获取rs中的数据
            int i = 1;
            while(result.next()) {
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
