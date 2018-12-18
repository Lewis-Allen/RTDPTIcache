package com.lewisallen.rtdptiCache.api;

import com.lewisallen.rtdptiCache.caches.SIRICache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StopController {

    @RequestMapping(value="api/stop", method=RequestMethod.GET)
    public String stops(@RequestParam(value="code", required=true) String[] codes){
        return SIRICache.getSiriJson(codes);
    }
}
