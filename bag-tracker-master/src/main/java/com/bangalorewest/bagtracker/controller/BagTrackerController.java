/**
 * 
 */
package com.bangalorewest.bagtracker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bangalorewest.bagtracker.dto.BagHistoryDTO;
import com.bangalorewest.bagtracker.dto.BagStatusResponse;
import com.bangalorewest.bagtracker.dto.ConfirmLoginStatusDTO;
import com.bangalorewest.bagtracker.dto.ConfirmMessageGeneration;
import com.bangalorewest.bagtracker.dto.LoadUnloadBag;
import com.bangalorewest.bagtracker.dto.LoginDTO;
import com.bangalorewest.bagtracker.dto.PaxItinerary;
import com.bangalorewest.bagtracker.exception.AuthenticationFailureException;
import com.bangalorewest.bagtracker.exception.BagTagUpdateFailedException;
import com.bangalorewest.bagtracker.exception.InvalidBagTagException;
import com.bangalorewest.bagtracker.service.BagTrackerService;

/**
 * @author sudhanshu.singh
 *
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class BagTrackerController {


	@Autowired
	BagTrackerService bagTrackerService;

	@PostMapping("/api/bagtracker/checkin")
	public ResponseEntity<ConfirmMessageGeneration> generateMessageOnCheckin(@RequestBody PaxItinerary paxItinerary)
			throws BagTagUpdateFailedException {
		return new ResponseEntity<ConfirmMessageGeneration>(bagTrackerService.generateBSM(paxItinerary), HttpStatus.OK);
	}

	@PostMapping("/api/bagtracker/load")
	public ResponseEntity<ConfirmMessageGeneration> generateLoadBPM(@RequestBody LoadUnloadBag loadUnloadRequest)
			throws InvalidBagTagException {
		return new ResponseEntity<ConfirmMessageGeneration>(bagTrackerService.generateLoadBPM(loadUnloadRequest),
				HttpStatus.OK);
	}

	@PostMapping("/api/bagtracker/unload")
	public ResponseEntity<ConfirmMessageGeneration> generateUnloadBPM(@RequestBody LoadUnloadBag loadUnloadRequest)
			throws InvalidBagTagException {
		return new ResponseEntity<ConfirmMessageGeneration>(bagTrackerService.generateUnloadBPM(loadUnloadRequest),
				HttpStatus.OK);
	}
	
	@PostMapping("/api/bagtracker/sort")
	public ResponseEntity<ConfirmMessageGeneration> generateSortingBPM(@RequestBody LoadUnloadBag loadUnloadRequest)
			throws InvalidBagTagException {
		return new ResponseEntity<ConfirmMessageGeneration>(bagTrackerService.generateSortingBPM(loadUnloadRequest),
				HttpStatus.OK);
	}
	
	@PostMapping("/api/bagtracker/screen")
	public ResponseEntity<ConfirmMessageGeneration> generateScreeningBPM(@RequestBody LoadUnloadBag loadUnloadRequest)
			throws InvalidBagTagException {
		return new ResponseEntity<ConfirmMessageGeneration>(bagTrackerService.generateScreeningBPM(loadUnloadRequest),
				HttpStatus.OK);
	}

	@GetMapping("/api/bagtracker/status/{bagTagID}")
	public ResponseEntity<BagStatusResponse> retrieveBagStatus(@PathVariable("bagTagID") String bagTagID,
			@RequestParam String messageDate) throws InvalidBagTagException {
		return new ResponseEntity<BagStatusResponse>(bagTrackerService.fetchLatestBagStatus(bagTagID, messageDate),
				HttpStatus.OK);
	}

	@GetMapping("/api/bagtracker/history/{bagTagID}/{dateOfTravel}")
	public ResponseEntity<Map<String, List<BagHistoryDTO>>> retrieveBagHistory(@PathVariable("bagTagID") String bagTagID,
			@PathVariable("dateOfTravel") String dateOfTravel) throws InvalidBagTagException {
		return new ResponseEntity<Map<String, List<BagHistoryDTO>>>(bagTrackerService.fetchBagHistory(bagTagID, dateOfTravel),
				HttpStatus.OK);
	}

	@PostMapping("/api/bagtracker/login")
	public ResponseEntity<ConfirmLoginStatusDTO> login(@RequestBody LoginDTO login)
			throws AuthenticationFailureException {
		ConfirmLoginStatusDTO loginStatus = bagTrackerService.checkAndLogin(login);
		return new ResponseEntity<ConfirmLoginStatusDTO>(loginStatus, HttpStatus.OK);

	}

}
