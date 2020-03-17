import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators, FormArray } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { MatTableDataSource, MatDialog, MatDialogConfig } from '@angular/material';

import { FlightDelay } from '../shared/models/flight-delay.model';
import { FlightDelayRequest } from '../shared/models/flight-delay-request.model';
import { FlightDelayService } from '../services/flight-delay.service';

@Component({
  selector: 'app-flight-delay',
  templateUrl: './flight-delay.component.html',
  styleUrls: ['./flight-delay.component.scss']
})
export class FlightDelayComponent implements OnInit {

  flightDelayForm: FormGroup;
  searchResult = false;
  message = '';
  errors = '';

  dataSourceFlightDelayResponse: MatTableDataSource<FlightDelay>;
  dataSourceFlightDelayColumn: string[] = ['airline', 'flight', 'flightDate', 'predictionDelay', 'notify']

  constructor(private datePipe: DatePipe, private router: Router, 
    private flightDelayService: FlightDelayService) {
    this.dataSourceFlightDelayResponse = new MatTableDataSource<FlightDelay>();
   }

  ngOnInit() {

    let date = new Date();
    let latest_date = this.datePipe.transform(date, 'yyyy-MM-dd');
    this.flightDelayForm = new FormGroup({
      airline: new FormControl('', Validators.required),
      flightDate: new FormControl(latest_date, Validators.required),
      city: new FormControl()
    });
  }

  onFlightDelaySearch(flightDelayFormValue) {
    console.log('Flight delay form submit entry');
    this.searchResult = true;
    let flightDelayResponses: any = [];
    this.message = '';
    this.errors = '';

    let fltDelayRequest = {} as FlightDelayRequest;
    fltDelayRequest.airline = flightDelayFormValue.airline;
    fltDelayRequest.flightDate = flightDelayFormValue.flightDate;
    fltDelayRequest.originCity = flightDelayFormValue.city;

    //comment this after service integration
    // let fltDelayResponse = {} as FlightDelay;
    // fltDelayResponse.airline = 'AI';
    // fltDelayResponse.flightNumber = '101';
    // fltDelayResponse.flightDate = '03/03/2020';
    // fltDelayResponse.predictedDepartureDelay = '04.11';

    this.flightDelayService.searchFlight(fltDelayRequest).subscribe((fltDelayRes: any) => {

      fltDelayRes.forEach(fltDelay => {
        let fltDelayResponse = {} as FlightDelay;
        fltDelayResponse.airline = fltDelay.airline;
        fltDelayResponse.flightNumber = fltDelay.flightNumber;
        fltDelayResponse.flightDate = fltDelay.flightDate;
        fltDelayResponse.predictedDepartureDelay = fltDelay.predictedDepartureDelay;
        flightDelayResponses.push(fltDelayResponse);
      });
      this.dataSourceFlightDelayResponse.data = flightDelayResponses;
    }, 
    (error) => {
      this.errors = error.error.message;
    });
  }

  onPassengerNotification(flightDelayReqToNotify: any) {
    this.flightDelayService.notifyPassenger(flightDelayReqToNotify).subscribe((notificationRes: any) => {
      console.log('Notification response : ' + notificationRes.message);
      this.message = notificationRes.message;
      this.errors = notificationRes.failed;
    }, 
    (error) => {
      this.errors = error.error.message;
    });
  }

  onCancel() {
    this.router.navigate(['login', 'delay']);
  }

}
