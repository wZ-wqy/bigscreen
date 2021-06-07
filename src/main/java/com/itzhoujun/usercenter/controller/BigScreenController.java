package com.itzhoujun.usercenter.controller;


import com.itzhoujun.usercenter.service.SatisfyService;
import com.itzhoujun.usercenter.service.SoftCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.itzhoujun.usercenter.service.SoftwareService.*;
import static com.itzhoujun.usercenter.service.TemplateNameService.selectTemplateService;
import static com.itzhoujun.usercenter.service.WeekCountService.satisfaction;
import static com.itzhoujun.usercenter.service.WeekCountService.taskOpinion;
import static com.itzhoujun.usercenter.service.WeekOpinionService.weekOpinionSelect;


@RestController
@RequestMapping("/BigScreen")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j

public class  BigScreenController {


    @GetMapping(value = "/Select" , produces="application/json;charset=UTF-8")
    public JSONObject select(HttpServletRequest req, HttpServletResponse resp) {
        JSONObject ds = new JSONObject();
        JSONObject chartData = new JSONObject();
        JSONObject tableData = new JSONObject();
        JSONObject worksheetTable = new JSONObject();
        JSONObject commentData = new JSONObject();
        JSONArray commentCounts = new JSONArray();
        chartData.accumulate("techChart",SatisfyService.satisfaction());
        chartData.accumulate("softChart",SoftCountService.satisfactionCount());
        tableData.accumulate("techTable",satisfaction());
        tableData.accumulate("softTable",monthSelect());
        worksheetTable.accumulate("worksheetTable",selectTemplateService());
        commentData.accumulate("techComment",weekOpinionSelect());
        commentData.accumulate("softComment",software());
        commentCounts.add(taskOpinion());
        commentCounts.add(softOpinionCount());
        ds.accumulate("chartData",chartData);
        ds.accumulate("tableData",tableData);
        ds.accumulate("worksheetTable",worksheetTable);
        ds.accumulate("commentData",commentData);
        ds.accumulate("commentCounts",commentCounts);
        System.out.println("ds+++"+ds);
        return ds;
    }




























}
