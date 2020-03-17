import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatDialog, MatDialogConfig } from '@angular/material';
import { FormBuilder, FormControl, FormGroup, Validators, FormArray } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { MatAccordion } from '@angular/material';

import { BaggageDialogComponent } from '../baggage-dialog/baggage-dialog.component';
import { ContainerDialogComponent } from '../container-dialog/container-dialog.component';
import { Booking } from '../shared/models/booking.model';
import { BagResponse } from '../shared/models/bag-response.model';
import { ConfirmBagHistoryResponse } from '../shared/models/bag-response.model';
import { BagRequest } from '../shared/models/bag-request.model';
import { BagService } from '../services/bag.service';
import { BagStatusResponse } from '../shared/models/bag-status-response.model';
import { BPMStatusResponse } from '../shared/models/bpm-status-response.model';

@Component({
  selector: 'app-load-unload',
  templateUrl: './load-unload.component.html',
  styleUrls: ['./load-unload.component.scss']
})
export class LoadUnloadComponent implements OnInit {

  colspan = 2;
  loadUnloadBagForm: FormGroup;
  action = '';
  cancelRoute = '';
  baggageMessage = 'BPM Generated.with love';
  cancelButtonVisibility = false;
  bagStatusResponse: BagStatusResponse;
  history = false;
  //bagResponses: any = [];
  containerForLoadBag = false;
  bagStatusFound = false;
  errors = '';
  bagResMainPanel: BagResponse;
  uldContainer = '';
  confirmBagHistoryResponse: any = [];
  airportsPanel: any = [];

  panelOpenState = false;
  accordionList: any;
  @ViewChild('accordion', {static:true}) accordion: MatAccordion

  dataSourceBagResponse: MatTableDataSource<BagResponse>;
  dataSourceBagResponseColumn: string[] = ['status', 'timestamp', 'from', 'arrivalFlight', 
    'departureFlight', 'connectionFlight', 'loadFlight']

  constructor(private datePipe: DatePipe, private route: ActivatedRoute, 
    private router: Router, private dialog: MatDialog, private bagService: BagService) { 
      this.dataSourceBagResponse = new MatTableDataSource<BagResponse>();
    }

  ngOnInit() {
    //this.bagResponses = [];
    let date = new Date();
    let latest_date = this.datePipe.transform(date, 'yyyy-MM-dd');
    this.loadUnloadBagForm = new FormGroup({
      bagTagId: new FormControl('', Validators.required),
      travelDate: new FormControl(latest_date, Validators.required),
      container: new FormControl()
    });

    this.route.paramMap.subscribe(params => {
      const action = params.get('action');
      if (action === 'load') {
        console.log('load');
        this.colspan = 3;
        this.action = 'Load Bag';
        this.cancelRoute = 'load';
        this.cancelButtonVisibility = true;
        this.containerForLoadBag = true;
        this.errors = '';
        this.loadUnloadBagForm.patchValue({
          bagTagId: ''
        });
      }
      else if (action === 'unload') {
        console.log('unload');
        this.action = 'UnLoad Bag';
        this.cancelRoute = 'unload';
        this.cancelButtonVisibility = true;
        this.containerForLoadBag = false;
        this.errors = '';
        this.loadUnloadBagForm.patchValue({
          bagTagId: ''
        });
      }
      else if (action === 'status') {
        console.log('status');
        this.history = false;
        this.action = 'Bag Status';
        this.cancelButtonVisibility = false;
        this.containerForLoadBag = false;
        this.errors = '';
        this.loadUnloadBagForm.patchValue({
          bagTagId: ''
        });
      }
      else if (action === 'history') {
        console.log('history');
        this.history = false;
        this.bagStatusResponse = {} as BagStatusResponse;
        this.action = 'Bag History';
        this.cancelButtonVisibility = false;
        this.containerForLoadBag = false;
        this.bagStatusFound = false;
        this.errors = '';
        this.loadUnloadBagForm.patchValue({
          bagTagId: ''
        });
      }
    });
  }

  openDialog(data) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = data;
    dialogConfig.width = '500px';
    dialogConfig.height = '400px';
    const dialogRef = this.dialog.open(BaggageDialogComponent, dialogConfig);
  }

  onSubmit(loadUnloadBagFormValue: any, operationType: string) {

    this.errors = '';
    this.baggageMessage = '';
    this.history = false;
    this.airportsPanel = [];
    this.bagResMainPanel = {} as BagResponse;
    this.confirmBagHistoryResponse = [];

    const bagTagID = loadUnloadBagFormValue.bagTagId;
    const travelDate = loadUnloadBagFormValue.travelDate;
    console.log('Bag Tag ID : ' + bagTagID + " Travel date : " + travelDate);

    const formattedTravelDate = this.datePipe.transform(travelDate, 'ddMMMyyyy');
    console.log('bagTagId : ' + bagTagID + ' formatted travel date : ' + formattedTravelDate);

    if (operationType === 'sorting') {

      let bagReq = {} as BagRequest;
      //set bagTagID
      bagReq.bagTagID = bagTagID;
      //set dateOfTravel
      bagReq.dateOfTravel = formattedTravelDate;
      //set loggedInAgent
      console.log('Login Res : ' + localStorage.getItem('currentUser'));
      const loginRes: any = JSON.parse(localStorage.getItem('currentUser'));
      //let loginRes: any = localStorage.getItem('currentUser');
      bagReq.loggedInAgent = loginRes.userName;

      //set agentLocation
      bagReq.agentLocation = loginRes.agentLocation;

      this.bagService.sortBag(bagReq).subscribe((response: any) => {
        //store response in bagResponses array
        this.baggageMessage = response.message;
        this.openDialog(this.baggageMessage);
      },
      (error) => {
        console.log("Errors:Error : " + JSON.stringify(error.error));
        this.errors = error.error.message;
      });

    }
    else if (operationType === 'screening') {

      let bagReq = {} as BagRequest;
      //set bagTagID
      bagReq.bagTagID = bagTagID;
      //set dateOfTravel
      bagReq.dateOfTravel = formattedTravelDate;
      //set loggedInAgent
      console.log('Login Res : ' + localStorage.getItem('currentUser'));
      const loginRes: any = JSON.parse(localStorage.getItem('currentUser'));
      //let loginRes: any = localStorage.getItem('currentUser');
      bagReq.loggedInAgent = loginRes.userName;

      //set agentLocation
      bagReq.agentLocation = loginRes.agentLocation;

      this.bagService.screenBag(bagReq).subscribe((response: any) => {
        //store response in bagResponses array
        this.baggageMessage = response.message;
        this.openDialog(this.baggageMessage);
      },
      (error) => {
        console.log("Errors:Error : " + JSON.stringify(error.error));
        this.errors = error.error.message;
      });
      
    }
    else if (operationType === 'load') {
      let bagReq = {} as BagRequest;
      //set bagTagID
      bagReq.bagTagID = bagTagID;
      //set dateOfTravel
      bagReq.dateOfTravel = formattedTravelDate;
      //set loggedInAgent
      console.log('Login Res : ' + localStorage.getItem('currentUser'));
      const loginRes: any = JSON.parse(localStorage.getItem('currentUser'));
      bagReq.loggedInAgent = loginRes.userName;

      //set agentLocation
      bagReq.agentLocation = loginRes.agentLocation;

      //this.openContainerDialog('Conatiner data');

      const dialogConfig = new MatDialogConfig();
      dialogConfig.data = 'Container data';
      dialogConfig.width = '400px';
      dialogConfig.height = '250px';
      const dialogRef = this.dialog.open(ContainerDialogComponent, dialogConfig);
  
      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed', result);
        bagReq.uldContainer = result.data;

        if (result.data !== 'Cancel') {
          this.bagService.loadBag(bagReq).subscribe((response: any) => {
            //store response in bagResponses array
            this.baggageMessage = response.message;
            this.openDialog(this.baggageMessage);
          },
          (error) => {
            console.log("Errors:Error : " + JSON.stringify(error.error));
            this.errors = error.error.message;
          });
        }
      });

      //set uldContainer
      //const uldContainer = loadUnloadForm.container;
      //bagReq.uldContainer = this.uldContainer;
    }
    else if (operationType === 'unload') {
      let bagReq = {} as BagRequest;
      //set bagTagID
      bagReq.bagTagID = bagTagID;
      //set dateOfTravel
      bagReq.dateOfTravel = formattedTravelDate;
      //set loggedInAgent
      console.log('Login Res : ' + localStorage.getItem('currentUser'));
      const loginRes: any = JSON.parse(localStorage.getItem('currentUser'));
      //let loginRes: any = localStorage.getItem('currentUser');
      bagReq.loggedInAgent = loginRes.userName;

      //set agentLocation
      bagReq.agentLocation = loginRes.agentLocation;

      this.bagService.unloadBag(bagReq).subscribe((response: any) => {
        //store response in bagResponses array
        this.baggageMessage = response.message;
        this.openDialog(this.baggageMessage);
      },
      (error) => {
        console.log("Errors:Error : " + JSON.stringify(error.error));
        this.errors = error.error.message;
      });
    }

    else if (operationType === 'history') {

      this.bagService.getBagHistory(bagTagID, formattedTravelDate).subscribe((response: any) => {
        //store response in bagResponses array
        //let bagResponses: any = [];
        //console.log('Airport : ' + response[0]);
        for (var airport in response) {
          console.log(airport, response[airport]);
          this.airportsPanel.push(airport);

          let bagResponses: any = [];
          let confirmHistoryRes = {} as ConfirmBagHistoryResponse;
          
          response[airport].forEach((bagHistoryResponse) => {
              let bagResonse = {} as BagResponse;

              bagResonse.airportCode = airport;
              bagResonse.status = bagHistoryResponse.status;
              bagResonse.timestamp = bagHistoryResponse.timestamp;
              bagResonse.from = bagHistoryResponse.from;
              bagResonse.departureFlight = bagHistoryResponse.departureFlight;
              bagResonse.loadFlight = bagHistoryResponse.loadFlight;
              bagResonse.connectionFlight = bagHistoryResponse.connectionFlight;
              bagResponses.push(bagResonse);
          });

          confirmHistoryRes.airportCode = airport;
          confirmHistoryRes.bagResponses = bagResponses;

          this.confirmBagHistoryResponse.push(confirmHistoryRes);

          this.bagResMainPanel.bagTagID = response[airport][0].bagTagID;
          this.bagResMainPanel.firstName = response[airport][0].firstName;
          this.bagResMainPanel.lastName = response[airport][0].lastName;
          this.bagResMainPanel.numberOfCheckedInBags = response[airport][0].numberOfCheckedInBags;
          this.bagResMainPanel.departureFlight = response[airport][0].departureFlight;

        }
          //this.dataSourceBagResponse.data = bagResponses;
          this.history = true;
      },
      (error) => {
        console.log("Errors:Error : " + JSON.stringify(error.error));
        this.errors = error.error.message;
      });
    }
  }

  onCancel() {
    if (this.cancelRoute === 'load') {
      this.router.navigate(['login', 'load']);
    }
    else if (this.cancelRoute === 'unload') {
      this.router.navigate(['login', 'unload']);
    }
  }

  beforePanelClosed(panel){
    panel.isExpanded = false;
    console.log("Panel going to close!");
  }
  beforePanelOpened(airportCodePanel) {
    console.log("Airport Panel going to  open! ");
    console.log(airportCodePanel);
    //airportCodePanel.isExpanded = true;
    this.confirmBagHistoryResponse.forEach((confirmBagHistory: any) => {
      if (confirmBagHistory.airportCode === airportCodePanel) {
        this.dataSourceBagResponse.data = confirmBagHistory.bagResponses;
      }
    })
  }
 
  afterPanelClosed(){
    console.log("Panel closed!");
  }
  afterPanelOpened(){
    console.log("Panel opened!");
  }
 
 
  closeAllPanels(){
    this.accordion.closeAll();
  }
  openAllPanels(){
    this.accordion.openAll();
  }

}
