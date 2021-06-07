package com.itzhoujun.usercenter.service;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.sql.*;

import static com.itzhoujun.usercenter.service.SelectDevice.selectFenZu;


//查询个人设备类型单数统计

public class PersonStaffService {

    private static final String url = "jdbc:mysql://localhost:3306/shouhoubao?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&autoReconnect=true";
    private static final String user = "root";
    private static final String password = "123456";
    private static Connection con;
    static Connection getconnect()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return con;
    }
    public static JSONObject selectsql(Long kstime, Long jstime)//插入数据进入数据库中
    {
        JSONArray res = new JSONArray();
        Connection con = PersonStaffService.getconnect();
        JSONObject jso = new JSONObject();
        JSONObject tabled = new JSONObject();
        try
        {
            Statement statement = con.createStatement();
//            执行查询语句
            PreparedStatement pst=con.prepareStatement("SELECT et.sbtype,t.executorName,COUNT( t.executorName ) FROM  task t JOIN taskproducts td ON t.taskNo = td.taskNo JOIN staff s ON s.executorName = t.executorName  JOIN bumenfenzu b on b.id = s.fenzuid JOIN equipment e ON e.serialNumber = td.serialNumber JOIN eqtype et ON e.sbtypeid = et.id WHERE b.fenzuName !='系统集成部' and t.completeTime  BETWEEN ? AND ? and t.templateName ='服务单'  GROUP BY et.id,b.id,s.id ORDER BY et.id,b.id,s.id");
            pst.setLong(1,kstime);
            pst.setLong(2,jstime);
            ResultSet resultSet  = pst.executeQuery();
            String[] person = fenzrenm();
            String[] shebei = SelectDevice.selectEqu();
            JSONArray ja =  selectss(kstime,jstime);
            for(int j =1;j<shebei.length;j++) {
                for (int i = 1; i < person.length; i++) {
                    if (resultSet.next()) {
                        if (shebei[j].equals(resultSet.getString("et.sbtype"))) {
                            JSONObject jsb = new JSONObject();
                            if (person[i].equals(resultSet.getString("t.executorName")) || person[i] == resultSet.getString("t.executorName")) {
                                jsb.accumulate("device", resultSet.getString("et.sbtype"));
                                jsb.accumulate("employee", person[i]);
                                jsb.accumulate("counts", resultSet.getInt(3));
                            } else {
                                jsb.accumulate("device", shebei[j]);
                                jsb.accumulate("employee", person[i]);
                                jsb.accumulate("counts", 0);
                                resultSet.previous();
                            }
                            res.add(jsb);
                        } else {
                            JSONObject jsbp = new JSONObject();
                            jsbp.accumulate("device", shebei[j]);
                            jsbp.accumulate("employee", person[i]);
                            jsbp.accumulate("counts", 0);
                            resultSet.previous();
                            res.add(jsbp);
                        }
                    }
                }
            }
            String[] eqtype = new String[shebei.length+3];
            eqtype[0] = "序号";
            eqtype[1] = "驻点区域";
            eqtype[2] = "姓名";
            for(int eq = 1 ;eq<shebei.length;eq++){
                eqtype[eq+2] = shebei[eq];
            }
            eqtype[eqtype.length-1] = "合计";
            tabled.accumulate("th",eqtype);
            tabled.accumulate("td",selectss(kstime,jstime));
            jso.accumulate("chartData",res);
            jso.accumulate("tableData",tabled);
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

    public static JSONArray selectss(Long kstime, Long jstime){
        Connection con = PersonStaffService.getconnect();
        JSONArray ja = new JSONArray();
        try
        {
            Statement statement = con.createStatement();
            PreparedStatement ppst=con.prepareStatement("(select  " +
                    "t.fenzuName as 'aa',  " +
                    "t.executorName,  " +
                    "t.sbtype,  " +
                    "t.fenzuid,  " +
                    "sum( IF ( t.sbtype = '电脑设备', shu, 0 ) ) AS '电脑设备合计',  " +
                    "sum( IF ( t.sbtype = '打印设备', shu, 0 ) ) AS '打印设备合计',  " +
                    "sum( IF ( t.sbtype = '网络设备', shu, 0 ) ) AS '网络设备合计',  " +
                    "sum( IF ( t.sbtype = '监控设备', shu, 0 ) ) AS '监控设备合计',  " +
                    "sum( IF ( t.sbtype = '家电设备', shu, 0 ) ) AS '家电设备合计',  " +
                    "sum( IF ( t.sbtype = '电话设备', shu, 0 ) ) AS '电话设备合计',  " +
                    "sum( IF ( t.sbtype = '会议设备', shu, 0 ) ) AS '会议设备合计',  " +
                    "sum( IF ( t.sbtype = '数码类', shu, 0 ) ) AS '数码类合计',  " +
                    "sum( IF ( t.sbtype = '医院设备', shu, 0 ) ) AS '医院设备合计',  " +
                    "sum( IF ( t.sbtype = '其他类', shu, 0 ) ) AS '其他类合计',  " +
                    "sum(t.shu)  " +
                    "from (  " +
                    "SELECT  " +
                    "bumenfenzu.fenzuName,  " +
                    "bumenfenzu.id as fenzuid,  " +
                    "staff.executorName,  " +
                    "eqtype.sbtype,  " +
                    "COUNT( task.executorName ) AS shu  " +
                    "FROM  " +
                    "task  " +
                    "JOIN staff ON staff.executorName = task.executorName  " +
                    "JOIN bumenfenzu ON bumenfenzu.id = staff.fenzuid  " +
                    "JOIN taskproducts ON task.taskNo = taskproducts.taskNo  " +
                    "JOIN equipment ON equipment.serialNumber = taskproducts.serialNumber  " +
                    "JOIN eqtype ON equipment.sbtypeid = eqtype.id  " +
                    "WHERE  " +
                    "bumenfenzu.fenzuName != '系统集成部'  " +
                    "and task.templateName ='服务单' " +
                    "AND  " +
                    "task.completeTime BETWEEN ? AND ? " +
                    "GROUP BY  " +
                    "bumenfenzu.id,  " +
                    "staff.id,  " +
                    "eqtype.id  " +
                    ") t  " +
                    "GROUP BY  " +
                    "t.executorName ORDER BY fenzuid asc)  " +
                    "union  " +
                    "SELECT  " +
                    "if(b.executorName=1,0,'null'),  " +
                    "if(b.sbtype=1,0,'null'),  " +
                    "if(b.fenzuName=1,0,'null'),  " +
                    "b.fenzuid,  " +
                    "sum(b.`电脑设备合计`), sum(b.`打印设备合计`), sum(b.`网络设备合计`), sum(b.`监控设备合计`), sum(b.`家电设备合计`), sum(b.`电话设备合计`), sum(b.`会议设备合计`), sum(b.`数码类合计`), sum(b.`医院设备合计`),  " +
                    "sum(b.`其他类合计`), sum(b.shu)  " +
                    "from  " +
                    "(  " +
                    "select  " +
                    "a.executorName,  " +
                    "a.sbtype,  " +
                    "a.fenzuName,  " +
                    "a.fenzuid,  " +
                    "SUM(a.shu) as shu,  " +
                    "sum( IF ( a.sbtype = '电脑设备', shu, 0 ) ) AS '电脑设备合计',  " +
                    "sum( IF ( a.sbtype = '打印设备', shu, 0 ) ) AS '打印设备合计',  " +
                    "sum( IF ( a.sbtype = '网络设备', shu, 0 ) )AS '网络设备合计',  " +
                    "sum( IF ( a.sbtype = '监控设备', shu, 0 ) ) AS '监控设备合计',  " +
                    "sum( IF ( a.sbtype = '家电设备', shu, 0 ) ) AS '家电设备合计',  " +
                    "sum( IF ( a.sbtype = '电话设备', shu, 0 ) ) AS '电话设备合计',  " +
                    "sum( IF ( a.sbtype = '会议设备', shu, 0 ) ) AS '会议设备合计',  " +
                    "sum( IF ( a.sbtype = '数码类', shu, 0 ) ) AS '数码类合计',  " +
                    "sum( IF ( a.sbtype = '医院设备', shu, 0 ) ) AS '医院设备合计',  " +
                    "sum( IF ( a.sbtype = '其他类', shu, 0 ) ) AS '其他类合计'  " +
                    "from (  " +
                    "SELECT  " +
                    "staff.executorName,  " +
                    "eqtype.sbtype,  " +
                    "bumenfenzu.fenzuName,  " +
                    "bumenfenzu.id as fenzuid,  " +
                    "COUNT( task.executorName ) AS shu  " +
                    "FROM  " +
                    "task  " +
                    "JOIN staff ON staff.executorName = task.executorName  " +
                    "JOIN bumenfenzu ON bumenfenzu.id = staff.fenzuid  " +
                    "JOIN taskproducts ON task.taskNo = taskproducts.taskNo  " +
                    "JOIN equipment ON equipment.serialNumber = taskproducts.serialNumber  " +
                    "JOIN eqtype ON equipment.sbtypeid = eqtype.id  " +
                    "WHERE  " +
                    "bumenfenzu.fenzuName != '系统集成部'  " +
                    "and task.templateName ='服务单' " +
                    "AND  " +
                    "task.completeTime BETWEEN ? AND ? " +
                    "GROUP BY  " +
                    "eqtype.id,  " +
                    "bumenfenzu.id  " +
                    ") a  " +
                    ") as b ORDER by fenzuid");
            ppst.setLong(1,kstime);
            ppst.setLong(2,jstime);
            ppst.setLong(3,kstime);
            ppst.setLong(4,jstime);
            ResultSet result = ppst.executeQuery();
            String[] shebei = SelectDevice.selectEqu();
            String[] person = fenzrenm();
            String[] zu = selectFenZu();
            int y = 1;
            String[] end = new String[shebei.length+1];
            end[0] = "合计";
            while(result.next()) {
                String[] stri = new String[shebei.length + 3];
                if (!result.getString(1).equals("null")) {
                    for (int i = 1; i < stri.length+2; i++) {
                        stri[0] = String.valueOf(y);
                        if (i != 3 || i != 4) {
                            if(i>4) {
                                stri[i-2] = result.getString(i);
                            }else {
                                stri[i] = result.getString(i);
                            }
                        } else {
                            continue;
                        }
                    }
                } else {
                    for (int j = 1; j < stri.length+2; j++) {
                        if (j > 4) {
                                end[j-4] = result.getString(j);
                        } else {
                            continue;
                        }
                    }
                    continue;
                }
                ja.add(stri);
                y++;
            }
            ja.add(end);

        } catch (JsonIOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ja;
    }

    public static String[] fenzrenm() {
        Connection con = PersonStaffService.getconnect();
        String [] s = new String[0];
        try{
            Statement statement = con.createStatement();
            PreparedStatement pps=con.prepareStatement("select staff.executorName from staff join bumenfenzu on staff.fenzuid=bumenfenzu.id where bumenfenzu.fenzuName !='系统集成部' ORDER BY bumenfenzu.id,staff.id");
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