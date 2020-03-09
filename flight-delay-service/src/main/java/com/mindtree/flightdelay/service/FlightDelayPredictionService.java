package com.mindtree.flightdelay.service;

import java.util.List;

import com.mindtree.flightdelay.dto.DelayedFlightDetails;
import com.mindtree.flightdelay.dto.DelayedFlightDetailsRequest;
import com.mindtree.flightdelay.dto.NotificationResponseDTO;



public interface FlightDelayPredictionService {
	
	List<DelayedFlightDetails> getDelayInfoForScheduledFlights(DelayedFlightDetailsRequest request);
	
	NotificationResponseDTO sendNotification(DelayedFlightDetails delayedFlightDetails);

}
