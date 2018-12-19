package com.lewisallen.rtdptiCache.api;

import com.lewisallen.rtdptiCache.caches.SIRICache;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StopController {

    // To Do - Add logger

    @RequestMapping(value="api/stop", method=RequestMethod.POST)
    public ResponseEntity<String> stops(@RequestBody JSONObject json){
        try
        {
            JSONArray jsonCodeList = json.getJSONArray("codes");
            List<String> codes = new ArrayList<String>();
            for(int i = 0; i < jsonCodeList.length(); i++){
                codes.add(jsonCodeList.get(i).toString());
            }

            return new ResponseEntity(SIRICache.getSiriJson(codes.stream().toArray(String[]::new)),HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity(null,HttpStatus.BAD_REQUEST);
    }
}
