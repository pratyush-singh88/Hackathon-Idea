import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { throwError, Observable } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

import { Booking } from '../shared/models/booking.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class BookingService {

    //private apiurl = 'http://localhost:8081';
    private apiurl = environment.mintoServiceEndpoint;
    
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

    getBookings(dateOfFirstSegment: string): Observable<Booking> {  
        return this.httpClient.get<Booking>(this.apiurl + '/api/bookings/' + dateOfFirstSegment);
    }
}