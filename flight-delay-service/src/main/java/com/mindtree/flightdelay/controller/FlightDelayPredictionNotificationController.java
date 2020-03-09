package com.mindtree.flightdelay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.flightdelay.dto.DelayedFlightDetails;
import com.mindtree.flightdelay.dto.DelayedFlightDetailsRequest;
import com.mindtree.flightdelay.dto.NotificationResponseDTO;
import com.mindtree.flightdelay.service.FlightDelayPredictionService;


/**
 * 
 * @author M1021548
 *
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class FlightDelayPredictionNotificationController {

	@Autowired
	FlightDelayPredictionService flightDelayPredictionService;

	@PostMapping("/flightdelay/predictions")
	public List<DelayedFlightDetails> getPredictedDelayForFlightSchedules(@RequestBody DelayedFlightDetailsRequest request) {
		return flightDelayPredictionService.getDelayInfoForScheduledFlights(request);
	}
	
	@PostMapping("/flightdelay/sendNotification")
	public NotificationResponseDTO sendNotification(@RequestBody DelayedFlightDetails delayedFlightDetails) {
		return flightDelayPredictionService.sendNotification(delayedFlightDetails);
	}

}
