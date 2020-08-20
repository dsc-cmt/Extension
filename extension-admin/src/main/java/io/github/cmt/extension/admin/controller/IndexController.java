package io.github.cmt.extension.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author tuzhenxian
 * @date 20-8-3
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Model model){
        return "index";
    }

    @GetMapping("/appList")
    public String appList(Model model){
        return "appList";
    }

    @GetMapping("/newApp")
    public String newApp(Model model){
        return "newApp";
    }

    @GetMapping("/app")
    public String app(Model model,String appName){
        model.addAttribute("appName",appName);
        return "app";
    }

    @GetMapping("/newSpi")
    public String newSpi(Model model,String appName){
        model.addAttribute("appName",appName);
        return "newSpi";
    }
}
