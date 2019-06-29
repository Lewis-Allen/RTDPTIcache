package com.lewisallen.rtdptiCache.api;

import com.google.gson.Gson;
import com.lewisallen.rtdptiCache.models.Timetable;
import com.lewisallen.rtdptiCache.repositories.TimetableRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "timetable")
public class TimetableController
{
    private final TimetableRepository repository;

    public TimetableController(TimetableRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(value = "/{timetableId}")
    public String showTimetable(Model model, @PathVariable Long timetableId)
    {
        Optional<Timetable> result = repository.findById(timetableId);

        if(result.isPresent())
        {
            Timetable timetable = result.get();

            JSONObject o = new JSONObject();
            o.put("stopName", timetable.getName());

            // Get departures
            String departures = timetable.getData();
            JSONArray stops = parseDepartureData(departures);

            o.put("visits", stops);
            JSONObject wrapper = new JSONObject();
            wrapper.put("payload", o);

            Gson gson = new Gson();
            Object departureInformation = gson.fromJson(wrapper.toString(), Object.class);
            model.addAttribute("departureInformation", departureInformation);

            timetable.setLastUsedDate(LocalDateTime.now());
            repository.saveAndFlush(timetable);

            return "dashboardTemplates/timetable";
        }
        else
        {
            return "uploadForm";
        }
    }

    @GetMapping(value = "create")
    public String showLoadTemplateScreen(Model model)
    {
        return "uploadForm";
    }

    @PostMapping(value = "")
    public RedirectView createNewTimetable(@RequestBody String formData)
    {
        String timetableString = formData.trim().split("=")[1];

        String[] lines = timetableString.split(System.lineSeparator());
        String stopName = lines[0];

        String data = Arrays.asList(lines).stream().skip(1).collect(Collectors.joining(System.lineSeparator()));

        Optional<Timetable> existingTimetable = repository.findTimetableByData(data);
        if(existingTimetable.isPresent())
        {
            return new RedirectView("timetable/" + existingTimetable.get().getId());
        }
        else
        {
            Timetable timetable = new Timetable(stopName, data);

            repository.saveAndFlush(timetable);

            return new RedirectView("timetable/" + timetable.getId());
        }
    }

    private JSONArray parseDepartureData(String departureData)
    {
        List<String> lines = Arrays.asList(departureData.split(System.lineSeparator()));

        // Get only departures in future.
        List<String> linesToDisplay = lines.stream()
                .filter(line -> LocalTime.parse(line.split(",")[0]).isAfter(LocalTime.now())).collect(Collectors.toList());

        JSONArray stops = new JSONArray();
        for(int i = 0; i < linesToDisplay.size() && i < 9; i++)
        {
            String[] visit = linesToDisplay.get(i).split(",");
            JSONObject stop = new JSONObject();
            stop.put("Departure", visit[0]);
            stop.put("Vehicle", visit[1]);
            stop.put("Destination", visit[2]);
            stops.put(stop);
        }

        return stops;
    }
}
