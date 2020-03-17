import { Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatDialog, MatDialogConfig } from '@angular/material';
import { FormBuilder, FormControl, FormGroup, Validators, FormArray } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';

import { Booking } from '../shared/models/booking.model';
import { Flight } from '../shared/models/flight.model';
import { Passenger } from '../shared/models/passenger.model';
import { CheckinService } from '../services/checkin.service';
import { BaggageDialogComponent } from '../baggage-dialog/baggage-dialog.component';
import { ContainerDialogComponent } from '../container-dialog/container-dialog.component';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-checkin',
  templateUrl: './checkin.component.html',
  styleUrls: ['./checkin.component.scss']
})
export class CheckinComponent implements OnInit {

  // Number of cards to be generated with column and rows to be covered  
  // cards = [  
  //   { title: 'Card 1', cols: 2, rows: 1 },  
  //   { title: 'Card 2', cols: 1, rows: 1 },  
  //   { title: 'Card 3', cols: 1, rows: 2 },  
  //   { title: 'Card 4', cols: 1, rows: 1 }  
  // ];

  jsonBookings = [
    {
      'email': 'abc.gmail.com',
      'flight': 'AI101-10JAN2020-BLR-BOM#MH102-10JAN2020-BOM-HYD#LF402-11JAN2020-HYD-LHR',
      'dateOfFirstSegment': '10JAN2020',
      'firstName': 'ABC',
      'lastName': 'Singh',
      'loggedInAgent': 'QWERT'
    },
    {
      'email': 'xyz.gmail.com',
      'flight': 'AI101-10JAN2020-BLR-BOM#MH102-10JAN2020-BOM-HYD#LF402-11JAN2020-HYD-LHR',
      'dateOfFirstSegment': '10JAN2020',
      'firstName': 'XYZ',
      'lastName': 'Singh',
      'loggedInAgent': 'QWERT'
    }
  ]

  bookings: any = [];
  flights: any = [];
  passengers: any = [];

  dataSourceFlights: MatTableDataSource<Flight>;
  dataSourceFlightColumn: string[] = ['flight', 'flightDate', 'airportForSegment'];

  dataSourcePassenger: MatTableDataSource<Passenger>;
  dataSourcePassengerColumn: string[] = ['email', 'firstName', 'lastName', 'numberOfBags']
  passengerForm: FormGroup;

  message = '';
  error = '';
  baggageMessage: any  = [];
  selectedPaxEmail = '';
  errors = '';

  bagQRCode : string;
  title = 'generate-qrcode';
  elementType: 'url' | 'canvas' | 'img' = 'url';
  displayBagQRLink = false;
  href: string;
  
  constructor(private checkinService: CheckinService, private dialog: MatDialog,
    private bookingService: BookingService, private datePipe: DatePipe,
    private router: Router) {
    this.dataSourceFlights = new MatTableDataSource<Flight>();
    this.dataSourcePassenger = new MatTableDataSource<Passenger>();
   }

  ngOnInit() {

    this.passengerForm = new FormGroup({
      //firstName: new FormControl('', Validators.required),
      //lastName: new FormControl('', Validators.required),
      numberOfBags: new FormControl('')
    });

    // let flightDetals = this.jsonBookings[0].flight;
    // let flightsArray = flightDetals.split("#");
    // console.log("Flights data : " + flightsArray);
    // for(var index in flightsArray) {
    //   let flightString = flightsArray[index];
    //   let flightArray = flightString.split("-");

    //   let flt = {} as Flight;
    //   flt.flightNumber = flightArray[0];
    //   flt.departureDate = flightArray[1];
    //   flt.departureStation = flightArray[2];
    //   flt.arrivalStation = flightArray[3];

    //   console.log("Flight data : " 
    //     + flt.flightNumber, flt.departureDate, flt.departureStation, flt.arrivalStation);

    //   this.flights.push(flt);

    //   this.dataSourceFlights.data = this.flights;
    // }

    // //passenger data
    // let pax = {} as Passenger;
    // pax.email = this.jsonBookings[0].email;
    // pax.firstName = this.jsonBookings[0].firstName;
    // pax.lastName = this.jsonBookings[0].lastName;

    // this.passengers.push(pax);

    // let pax1 = {} as Passenger;
    // pax1.email = this.jsonBookings[1].email;
    // pax1.firstName = this.jsonBookings[1].firstName;
    // pax1.lastName = this.jsonBookings[1].lastName;

    // this.passengers.push(pax1);

    // this.dataSourcePassenger.data = this.passengers;

    //service call
    let date = new Date();
    let latest_date = this.datePipe.transform(date, 'ddMMMyyyy');

    this.bookingService.getBookings(latest_date).subscribe((bookingRes: any) => {

      this.bookings = bookingRes;

      //set flight details
      let flightDetals = bookingRes[0].flight;
      let flightsArray = flightDetals.split("#");
      console.log("Flights data : " + flightsArray);
      for(var index in flightsArray) {
        let flightString = flightsArray[index];
        let flightArray = flightString.split("-");
  
        let flt = {} as Flight;
        flt.flightNumber = flightArray[0];
        flt.departureDate = flightArray[1];
        flt.departureStation = flightArray[2];
        flt.arrivalStation = flightArray[3];
  
        console.log("Flight data : " 
          + flt.flightNumber, flt.departureDate, flt.departureStation, flt.arrivalStation);
  
        this.flights.push(flt);
      }
      this.dataSourceFlights.data = this.flights;

      //set passenger details
      bookingRes.forEach(booking => {
        let pax = {} as Passenger;
        pax.email = booking.email;
        pax.firstName = booking.firstName;
        pax.lastName = booking.lastName;
    
        this.passengers.push(pax);
      });
      this.dataSourcePassenger.data = this.passengers;
    });
  }

  onBagAddition(numberOfBags, email) {
    this.message = '';
    this.error = '';
    console.log('No of bags : ' + numberOfBags);
    if (numberOfBags > 0 && numberOfBags <= 2) {
      for (let pax of  this.passengers) {
        if (pax.email === email) {
          let passenger = pax;
          passenger.numberOfBags = numberOfBags;
          this.message = 'Baggage added for the passenger';
        }
      }
    }
    else {
      this.errors = 'Maximum allowed bag is two';
    }
  }

  checkinPassenger(passengerFormValue) {
    console.log('Checkin passenger in-progress');
    //need to comment
    //this.openDialog(this.baggageMessage);
    this.baggageMessage = [];
    this.bagQRCode = '';
    this.message = '';
    ///document.clear();

    this.bookings.forEach(booking => {
      if (booking.email === this.selectedPaxEmail) {

        let book = {} as Booking;
        //set loggedInUser
        console.log('Login Res : ' + localStorage.getItem('currentUser'));
        const loginRes: any = JSON.parse(localStorage.getItem('currentUser'));
        //let loginRes: any = localStorage.getItem('currentUser');
        book.loggedInAgent = loginRes.userName;

        //set flight
        book.flight = booking.flight;
        //set firstName
        book.firstName = booking.firstName;
        //set lastName
        book.lastName = booking.lastName;
        //set emailID
        book.emailID = booking.email;
        //set dateOfTravel
        let dateOfTravel = this.datePipe.transform(booking.dateOfFirstSegment, 'ddMMMyyyy');
        book.dateOfTravel = dateOfTravel;

        //set numberOfCheckedInBag
        this.passengers.forEach(pax => {
          if (pax.email === this.selectedPaxEmail) {
            book.numberOfCheckedInBags = pax.numberOfBags;
          }
        })

        //need to uncomment and set loggedInUserName
        this.checkinService.checkinPassenger(book).subscribe((response: any) => {
          response.message.forEach(bsmMessage => {
            this.baggageMessage.push(bsmMessage);
            //this.baggageMessage = response.message;
          });
          this.openDialog(this.baggageMessage);

          this.bagQRCode = 'bagTagId=' + response.bagTagID + '#dateOfTravel=' + response.bagDate;
          console.log('QR data : ' + this.bagQRCode);
          this.message = 'Passenger checked-in and baggage message generated';
          this.displayBagQRLink = true;
        },
        (error) => {
          console.log("Errors:Error : " + JSON.stringify(error.error));
          this.errors = error.error.message;
        });
      }
    });
  }

  openDialog(data) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = data;
    dialogConfig.width = '400px';
    dialogConfig.height = '400px';
    const dialogRef = this.dialog.open(BaggageDialogComponent, dialogConfig);

    //this.router.navigate(['login', 'checkin']);
  }

  getSelectedPassenger(event, email, index) {
    this.selectedPaxEmail = email;
    console.log('Selected email : ' + this.selectedPaxEmail);
  }

  generateBaggageQRCode() {

    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = this.bagQRCode;
    dialogConfig.width = '400px';
    dialogConfig.height = '300px';
    const dialogRef = this.dialog.open(ContainerDialogComponent, dialogConfig);

    this.displayBagQRLink = false;

    this.router.navigate(['login', 'checkin']);

  }

}
