package com.pain.blue.shopping.controller;

import com.pain.blue.rest.response.RestResponse;
import com.pain.blue.shopping.domain.dto.ActivityDTO;
import com.pain.blue.shopping.domain.dto.ActivityDestDTO;
import com.pain.blue.shopping.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController("/activities")
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("")
    public RestResponse save(@RequestBody ActivityDTO activityDTO) {
        activityService.save(activityDTO);
        return RestResponse.success();
    }

    @GetMapping("/description")
    public RestResponse description(@RequestParam String productId) {
        ActivityDestDTO description = activityService.description(productId);
        return RestResponse.success(description);
    }

    @PostMapping("/start")
    public RestResponse start(@RequestParam String productId) {
        activityService.start(productId);
        return RestResponse.success();
    }
}
