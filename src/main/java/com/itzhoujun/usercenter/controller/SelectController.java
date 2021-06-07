package com.itzhoujun.usercenter.controller;



import com.itzhoujun.usercenter.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/selectController")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SelectController {


    @PostMapping(value = "/select")

    public JSONObject select(@RequestParam(value = "time[]" ) String[] time,@RequestParam(value = "chartType",defaultValue="1" ) String chartType,@RequestParam(value = "workingDay",defaultValue="1" ) String workingDay) {

        System.out.println("开始执行");
        Long kst = Long.parseLong(time[0].substring(0,time[0].length()-3));
        Long jst = Long.parseLong(time[1].substring(0,time[1].length()-3))+86400;
        Long sykst = 0l;
        Long syjst = 0l;
        if(time.length>3){
            sykst = Long.parseLong(time[2].substring(0,time[0].length()-3));
            syjst = Long.parseLong(time[3].substring(0,time[1].length()-3));
        }

        JSONObject gdl = new JSONObject();
        System.out.println(chartType);
        int chart = 0;
        int day = 0;
        if(chartType !=null && !chartType .equals("")){
            chart = Integer.parseInt( chartType );
            day = Integer.parseInt( workingDay );
        }
        if(chart == 1){
            gdl = PersonStaffService.selectsql(kst,jst);//查询个人设备类型单数统计
        }else if(chart==2){
            gdl = SelectDevice.selectDevice(kst,jst);//驻点设备类型单数统计
        }else if(chart==3){
            gdl = PersonalCount.personal(kst,jst);//个人工单总数统计
        }else if(chart==4){
            gdl = SelectPerCapita.perCapita(kst,jst,day);//驻点人均单数统计
        }else if(chart==5){
            gdl = PersonCounts.selectPerCount(kst,jst,sykst,syjst);//个人工单总数对比
        }
        System.out.println(gdl);
        return gdl;
    }






}

