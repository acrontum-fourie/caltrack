package com.acrontum.caltrack.rest;

import com.acrontum.caltrack.service.MicrosoftGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingsController {

    @Autowired
    private MicrosoftGraph microsoftGraph;

    @GetMapping(path = "test")
    public String test() {
        return microsoftGraph.getCalendarEvents("", "").blockFirst();
    }
}
