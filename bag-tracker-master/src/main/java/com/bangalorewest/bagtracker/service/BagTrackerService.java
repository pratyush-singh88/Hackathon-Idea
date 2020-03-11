package com.bangalorewest.bagtracker.service;

import java.util.List;
import java.util.Map;

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

public interface BagTrackerService {

	ConfirmMessageGeneration generateBSM(PaxItinerary paxItinerary) throws BagTagUpdateFailedException;

	ConfirmLoginStatusDTO checkAndLogin(LoginDTO login) throws AuthenticationFailureException;

	ConfirmMessageGeneration generateLoadBPM(LoadUnloadBag loadUnloadBag) throws InvalidBagTagException;

	ConfirmMessageGeneration generateUnloadBPM(LoadUnloadBag loadUnloadRequest) throws InvalidBagTagException;
	
	ConfirmMessageGeneration generateSortingBPM(LoadUnloadBag loadUnloadBag) throws InvalidBagTagException;
	
	ConfirmMessageGeneration generateScreeningBPM(LoadUnloadBag loadUnloadBag) throws InvalidBagTagException;

	BagStatusResponse fetchLatestBagStatus(String bagTagID, String dateOfTravel) throws InvalidBagTagException;

	Map<String, List<BagHistoryDTO>> fetchBagHistory(String bagTagID, String dateOfTravel) throws InvalidBagTagException;


}
