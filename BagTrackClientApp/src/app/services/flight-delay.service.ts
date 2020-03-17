import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { throwError, Observable } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

import { FlightDelayRequest } from '../shared/models/flight-delay-request.model';
import { FlightDelay } from '../shared/models/flight-delay.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class FlightDelayService {

        //private apiurl = 'http://localhost:8082';
        private apiurl = environment.flightDelayEndpoint;
    
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
    
        searchFlight(flightDelayRequest: FlightDelayRequest) {
            console.log('Search delay flight request : ' + JSON.stringify(flightDelayRequest));
            return this.httpClient.post(this.apiurl + '/api/flightdelay/predictions', 
                JSON.stringify(flightDelayRequest), this.httpOptions);
        }

        notifyPassenger(flightDelay: FlightDelay) {
            console.log('Delay flight response : ' + JSON.stringify(flightDelay));
            return this.httpClient.post(this.apiurl + '/api/flightdelay/sendNotification', 
                JSON.stringify(flightDelay), this.httpOptions);
        }

}