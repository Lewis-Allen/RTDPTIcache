package com.lewisallen.rtdptiCache.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showTitle() {
        return "title";
    }

    @RequestMapping(value = "/dashboard", method=RequestMethod.GET)
    public String showDashboard(@RequestParam(value="template", required = false) String template,
                                @RequestParam(value="code[]", required = false) String[] codes,
                                @RequestParam(value="crs[]", required = false) String[] crs,
                                Model model
    ){
        // Check if no transport codes were provided.
        if(codes == null && crs == null){
            throw new RuntimeException("No transport codes provided. Provide codes as a parameter with code[]={code} or crs[]={crs}");
        }
        else
        {
            // Else we add codes to Model.
            if(codes != null)
                model.addAttribute("codes", codes);

            if(crs != null)
                model.addAttribute("crs", crs);
        }

        // Check if a template was provided.
        if(template == null)
            template = "default";


        return template;
    }
}
