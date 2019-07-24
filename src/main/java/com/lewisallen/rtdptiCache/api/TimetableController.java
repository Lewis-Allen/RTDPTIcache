package com.lewisallen.rtdptiCache.api;

import com.google.gson.Gson;
import com.lewisallen.rtdptiCache.logging.ResourceNotFoundException;
import com.lewisallen.rtdptiCache.models.Timetable;
import com.lewisallen.rtdptiCache.repositories.TimetableRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "timetable")
public class TimetableController {
    private static final int TIMETABLE_MAX_DEPARTURES = 9; // Maximum departures displayable on a timetable.

    // Paths of template files used in this controller.
    private static final String TIMETABLE_CREATE_TEMPLATE = "timetable/timetableCreate";
    private static final String TIMETABLE_LIST_TEMPLATE = "timetable/timetableList";
    private static final String TIMETABLE_SHOW_TEMPLATE = "timetable/timetable";

    private final TimetableRepository repository;

    public TimetableController(TimetableRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/{timetableId}")
    public String showTimetable(Model model, @PathVariable Long timetableId) {
        Optional<Timetable> result = repository.findById(timetableId);
        if (!result.isPresent())
            throw new ResourceNotFoundException();

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

        return TIMETABLE_SHOW_TEMPLATE;
    }

    @GetMapping(value = "create")
    public String showLoadTemplateScreen(Model model) {
        return TIMETABLE_CREATE_TEMPLATE;
    }

    @GetMapping
    public String showListOfTimetables(Model model) {
        List<Timetable> timetables = repository.findAllByOrderByLastUsedDateDesc();

        model.addAttribute("timetables", timetables);

        return TIMETABLE_LIST_TEMPLATE;
    }

    /**
     * @param file a csv containing data in the following format:
     *             Data in the form of:
     *             <p>
     *             BUS_STOP_NAME
     *             ARRIVAL_TIME,VEHICLE_NAME,DESTINATION
     *             ARRIVAL_TIME,VEHICLE_NAME,DESTINATION
     *             ...
     *             <p>
     *             e.g.
     *             <p>
     *             Brighton University North
     *             12:00,UB1,Old Steine
     *             13:00,UB1,Old Steine
     *             14:00,UB1,Grand Parade
     * @return Redirection to newly created timetable, or existing identical timetable in database.
     */
    @PostMapping(value = "")
    public RedirectView createNewTimetable(@RequestParam(value = "file") MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        String formData = new String(bytes);

        String[] lines = formData.split(System.lineSeparator());
        String stopName = lines[0];

        String data = Arrays.stream(lines).skip(1).collect(Collectors.joining(System.lineSeparator()));

        Optional<Timetable> existingTimetable = repository.findTimetableByData(data);
        if (existingTimetable.isPresent()) {
            return new RedirectView("timetable/" + existingTimetable.get().getId());
        }

        Timetable timetable = new Timetable(stopName, data);

        repository.saveAndFlush(timetable);

        return new RedirectView("timetable/" + timetable.getId());
    }

    /**
     * Parse departure data from csv form.
     * Data in the form of:
     * <p>
     * BUS_STOP_NAME
     * ARRIVAL_TIME,VEHICLE_NAME,DESTINATION
     * ARRIVAL_TIME,VEHICLE_NAME,DESTINATION
     * ...
     * <p>
     * e.g.
     * <p>
     * Brighton University North
     * 12:00,UB1,Old Steine
     * 13:00,UB1,Old Steine
     * 14:00,UB1,Grand Parade
     *
     * @param departureData data to be parsed.
     * @return JSONArray of departures
     */
    private JSONArray parseDepartureData(String departureData) {
        List<String> lines = Arrays.asList(departureData.split(System.lineSeparator()));

        // Get only departures in future.
        List<String> linesToDisplay = lines.stream()
                .filter(line -> LocalTime.parse(line.split(",")[0]).isAfter(LocalTime.now())).collect(Collectors.toList());

        JSONArray stops = new JSONArray();

        for (int i = 0; i < linesToDisplay.size() && i < TIMETABLE_MAX_DEPARTURES; i++) {
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
