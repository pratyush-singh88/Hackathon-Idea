import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { throwError, Observable } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

import { BagResponse } from '../shared/models/bag-response.model';
import { BagRequest } from '../shared/models/bag-request.model';
import { BagStatusResponse } from '../shared/models/bag-status-response.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class BagService {

    //private apiurl = 'http://localhost:8080';
    private apiurl = environment.bagtrackServiceEndpoint;

    headers = new HttpHeaders().set('Content-Type', 'application/json');
        
    httpOptions = {
        headers: this.headers
    };

    constructor(private httpClient: HttpClient) {
    }

    private handleError(error: any) {
        console.log(error);
        return throwError(error);
    }

    loadBag(bagRequest: BagRequest) {
        console.log('Load Bag Request : ' + JSON.stringify(bagRequest));
        return this.httpClient.post(this.apiurl + '/api/bagtracker/load', JSON.stringify(bagRequest),
        this.httpOptions);
    }

    unloadBag(bagRequest: BagRequest) {
        console.log('UnLoad Bag Request : ' + JSON.stringify(bagRequest));
        return this.httpClient.post(this.apiurl + '/api/bagtracker/unload', JSON.stringify(bagRequest),
        this.httpOptions);
    }

    sortBag(bagRequest: BagRequest) {
        console.log('Sorting Bag Request : ' + JSON.stringify(bagRequest));
        return this.httpClient.post(this.apiurl + '/api/bagtracker/sort', JSON.stringify(bagRequest),
        this.httpOptions);
    }

    screenBag(bagRequest: BagRequest) {
        console.log('Screening Bag Request : ' + JSON.stringify(bagRequest));
        return this.httpClient.post(this.apiurl + '/api/bagtracker/screen', JSON.stringify(bagRequest),
        this.httpOptions);
    }

    getBagStatus(bagTagID: string, dateOfTravel: string): Observable<BagStatusResponse> {  
        console.log('BagTagID & DateOfTravel : ' + bagTagID + " & " + dateOfTravel);
        return this.httpClient.get<BagStatusResponse>(this.apiurl + '/api/bagtracker/status/' + bagTagID
            + "?messageDate=" + dateOfTravel);
    }

    getBagHistory(bagTagID: string, dateOfTravel: string): Observable<BagResponse[]> {  
        return this.httpClient.get<BagResponse[]>(this.apiurl + '/api/bagtracker/history/' + bagTagID
            + "/" + dateOfTravel);
    }

}