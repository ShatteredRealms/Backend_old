package com.shatteredrealmsonline.controllers.all;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/v1/")
public class PingController
{
    @GetMapping(name="/ping")
    public @ResponseBody
    String pingHealthCheck()
    {
        return "ok";
    }
}
