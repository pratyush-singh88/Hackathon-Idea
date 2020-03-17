import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { throwError, Observable } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

import { Login } from '../classes/login';
import { ConfirmLogin } from '../classes/confirm-login';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class LoginService {

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

    login(login: Login): Observable<ConfirmLogin> {  
        //debugger;  
       console.log(JSON.stringify(login));
       const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
       return this.httpClient.post<ConfirmLogin>(this.apiurl + '/api/bagtracker/login', login, httpOptions); 
    }

    logout() {
        localStorage.removeItem('currentUser');
    }

}