package com.itzhoujun.usercenter.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.itzhoujun.usercenter.service.MapSourceService.selectMapService;

@RestController
@RequestMapping("/Map")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MapSourceController {

    @GetMapping(value = "/Select")
    public JSONArray select(HttpServletRequest req, HttpServletResponse resp) { return  selectMapService(); }




}
