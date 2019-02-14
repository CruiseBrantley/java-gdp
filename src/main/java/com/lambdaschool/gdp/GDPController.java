package com.lambdaschool.gdp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class GDPController
{
    private final GDPRepository gdprepos;
    private final RabbitTemplate rt;

    public GDPController(GDPRepository gdprepos, RabbitTemplate rt)
    {
        this.gdprepos = gdprepos;
        this.rt = rt;
    }

    @GetMapping("/names")
    public List<GDP> all()
    {
        List<GDP> gdpList = gdprepos.findAll();
        gdpList.sort((a1, a2) -> a1.getCountry().compareToIgnoreCase(a2.getCountry()));
        return gdpList;
    }

    @GetMapping("/economy")
    public List<GDP> economy()
    {
        List<GDP> gdpList = gdprepos.findAll();
        gdpList.sort((a2, a1) -> a1.getGdp().compareTo(a2.getGdp()));
        return gdpList;
    }

    @GetMapping("/total")
    public ObjectNode sumGDP()
    {
        List<GDP> gdps = gdprepos.findAll();

        Long total = 0L;
        for(GDP g : gdps)
        {
            total = total + g.getGdp();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode totalGDP = mapper.createObjectNode();
        totalGDP.put("id", 0);
        totalGDP.put("country", total);

        return totalGDP;
    }

    @GetMapping("/gdp/{country}")
    public GDP findOne(@PathVariable String country)
    {
        GDPLog message = new GDPLog("Checked " + country + " GDP.");
        rt.convertAndSend(GdpApplication.QUEUE_NAME, message.toString());
        log.info("Sent message to queue.");

        List<GDP> gdpList = gdprepos.findAll();
        for(GDP item : gdpList)
        {
            if(item.getCountry().equalsIgnoreCase(country))
                return item;
        }
        return null;
    }

    @PostMapping("/gdp")
    public List<GDP> newGDP(@RequestBody List<GDP> newGDP)
    {
        return gdprepos.saveAll(newGDP);
    }
}
