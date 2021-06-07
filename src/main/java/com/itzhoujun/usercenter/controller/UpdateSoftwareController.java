package com.itzhoujun.usercenter.controller;

import com.itzhoujun.usercenter.domain.softwarecount.Softwarecount;
import com.itzhoujun.usercenter.service.SoftwarecountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/updatesoftwareController")
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UpdateSoftwareController {

    private final SoftwarecountService softwarecountService;
    private final Softwarecount softwarecount;
    @PostMapping(value = "/software")
    public int software(@RequestParam(value = "month" ,defaultValue="1" ) String month, @RequestParam(value = "frequency",defaultValue="1" ) String frequency, @RequestParam(value = "satisfaction",defaultValue="1" ) String satisfaction , @RequestParam(value = "ordinary" ,defaultValue="1" ) String ordinary,@RequestParam(value = "nosatisfaction",defaultValue="1"  ) String nosatisfaction,@RequestParam(value = "opinion",defaultValue="0" ) String opinion, @RequestParam(value = "check_type",defaultValue="1" ) String check_type) throws Exception {
        System.out.println("开始修改staff");
        softwarecount.setMonth(month);
        softwarecount.setFrequency(Integer.parseInt(frequency));
        softwarecount.setSatisfaction(Integer.parseInt(satisfaction));
        softwarecount.setOrdinary(Integer.parseInt(ordinary));
        softwarecount.setNosatisfaction(Integer.parseInt(nosatisfaction));
        softwarecount.setOpinion(Integer.parseInt(opinion));
        int count = 0;
        if(check_type.equals("update")){
            count = softwarecountService.softwareUpdate(softwarecount);
        }else if(check_type.equals("delete")){
            count = softwarecountService.softwareDelete(softwarecount);
        }else if(check_type.equals("insert")){
            count = softwarecountService.softwareInsert(softwarecount);
        }

        return count;
    }

}
