import { BPMStatusResponse } from './bpm-status-response.model';

export class BagStatusResponse {
    // BSM = BAG CHECKEDIN, BPM LOAD = BAG LOADED ONTO CONTAINER
	// BPM OFF = BAG OFFLOADED, HLD = BAG LOADED ONTO AIRCRAFT
    status: string;
    flight: string; // AI101
    date: string //.F
    airportCode: string;
    firstName: string;
    lastName: string;

    bpmStatusResponse: BPMStatusResponse;
}