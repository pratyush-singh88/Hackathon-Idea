import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { throwError, Observable } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

import { Booking } from '../shared/models/booking.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CheckinService {

    //private apiurl = 'http://localhost:8080';
    private apiurl = environment.bagtrackServiceEndpoint;
    headers = new HttpHeaders().set('Content-Type', 'application/json')
        .set('Accept', 'application/json');
    httpOptions = {
        headers: this.headers
    };

    constructor(private httpClient: HttpClient) {
    }

    private handleError(error: any) {
        console.log(error);
        return throwError(error);
    }

    checkinPassenger(booking: Booking) {
        console.log('Booking details for checkin : ' + JSON.stringify(booking));
        const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
        return this.httpClient.post(this.apiurl + '/api/bagtracker/checkin', JSON.stringify(booking), 
                    httpOptions);
    }
}