import { Component, VERSION, OnInit, ViewChild } from '@angular/core';
import { ZXingScannerComponent } from '@zxing/ngx-scanner';
import { Result } from '@zxing/library';
import { MatTableDataSource, MatDialog, MatDialogConfig } from '@angular/material';
import { FormBuilder, FormControl, FormGroup, Validators, FormArray } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';

import { BagService } from '../services/bag.service';
import { BagRequest } from '../shared/models/bag-request.model';
import { BaggageDialogComponent } from '../baggage-dialog/baggage-dialog.component';
import { ContainerDialogComponent } from '../container-dialog/container-dialog.component';


@Component({
  selector: 'app-bag-scan',
  templateUrl: './bag-scan.component.html',
  styleUrls: ['./bag-scan.component.scss']
})
export class BagScanComponent implements OnInit {

      baggageMessage = 'BPM Generated';
      repeatScan = false;
      errors = '';

      //ngVersion = VERSION.full;
  
      @ViewChild('scanner', {static: true})
      scanner: ZXingScannerComponent;
  
      hasCameras = false;
      hasPermission: boolean;
      qrResultString: string;
      scanCompleted = false;
      deviceOpen = true;
  
      availableDevices: MediaDeviceInfo[];
      selectedDevice: MediaDeviceInfo;

      constructor(private datePipe: DatePipe, private route: ActivatedRoute, 
        private router: Router, private dialog: MatDialog, private bagService: BagService) {
    
         }
  
      ngOnInit(): void {
  
          this.scanner.camerasFound.subscribe((devices: MediaDeviceInfo[]) => {
              this.hasCameras = true;

              console.log('Devices: ', devices);
              this.availableDevices = devices;
  
              for (const device of devices) {
                this.selectedDevice = device;
                break;
            }
              
  
              // selects the devices's back camera by default
              // for (const device of devices) {
              //     if (/back|rear|environment/gi.test(device.label)) {
              //         this.scanner.changeDevice(device);
              //         this.selectedDevice = device;
              //         break;
              //     }
              // }
          });

          this.scanner.camerasNotFound.subscribe((devices: MediaDeviceInfo[]) => {
              console.error('An error has occurred when trying to enumerate your video-stream-enabled devices.');
          });
  
          this.scanner.permissionResponse.subscribe((answer: boolean) => {
            this.hasPermission = answer;
          });
  
      }
  
      handleQrCodeResult(resultString: string) {
          console.log('Result: ', resultString);
          this.qrResultString = resultString;
  
          this.scanner.enable = false;
          this.hasCameras = false;
          this.deviceOpen = false;
          
          let device = {deviceId: '', label: '', groupId: ''} as MediaDeviceInfo;
          this.selectedDevice = device;
          this.availableDevices = [];

          this.scanCompleted = true;
          this.repeatScan = false;
      }
  
      // onDeviceSelectChange(selectedValue: string) {
      //     console.log('Selection changed: ', selectedValue);
      //     this.selectedDevice = this.scanner.getDeviceById(selectedValue);
      // }

      onBagSort() {

        this.bagService.sortBag(this.constructBagRequest()).subscribe((response: any) => {
          //store response in bagResponses array
          this.baggageMessage = response.message;
          this.openDialog(this.baggageMessage);
          this.repeatScan = true;
        },
        (error) => {
          console.log("Errors:Error : " + JSON.stringify(error.error));
          this.errors = error.error.message;
        });

      }

      onBagScreen() {

        this.bagService.screenBag(this.constructBagRequest()).subscribe((response: any) => {
          //store response in bagResponses array
          this.baggageMessage = response.message;
          this.openDialog(this.baggageMessage);
          this.repeatScan = true;
        },
        (error) => {
          console.log("Errors:Error : " + JSON.stringify(error.error));
          this.errors = error.error.message;
        });
      }

      onBagLoad() {

        let bagReq = this.constructBagRequest();

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
              this.repeatScan = true;
            },
            (error) => {
              console.log("Errors:Error : " + JSON.stringify(error.error));
              this.errors = error.error.message;
            });
          }
        });

      }      

      onBagUnload() {

        this.bagService.unloadBag(this.constructBagRequest()).subscribe((response: any) => {
          //store response in bagResponses array
          this.baggageMessage = response.message;
          this.openDialog(this.baggageMessage);
          this.repeatScan = true;
        },
        (error) => {
          console.log("Errors:Error : " + JSON.stringify(error.error));
          this.errors = error.error.message;
        });

      }

      openDialog(data) {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.data = data;
        dialogConfig.width = '500px';
        dialogConfig.height = '400px';
        const dialogRef = this.dialog.open(BaggageDialogComponent, dialogConfig);
      }


      constructBagRequest() : BagRequest {

        this.errors = '';
        this.baggageMessage = '';

        let scanData = this.qrResultString;

        if (scanData !== undefined  && scanData !== null) {
          let splittedScanData = scanData.split('#');
          if (splittedScanData !== undefined && splittedScanData !== null) {
            let scanBagTagID = splittedScanData[0].split('=');
            let scanDateOfTravel = splittedScanData[1].split('=');

            const bagTagID = scanBagTagID[1];
            const travelDate = scanDateOfTravel[1];
            console.log('Bag Tag ID : ' + bagTagID + " Travel date : " + travelDate);
        
            //const formattedTravelDate = this.datePipe.transform(travelDate, 'ddMMMyyyy');
            //console.log('bagTagId : ' + bagTagID + ' formatted travel date : ' + formattedTravelDate);
    
            let bagReq = {} as BagRequest;
            //set bagTagID
            bagReq.bagTagID = bagTagID;
            //set dateOfTravel
            bagReq.dateOfTravel = travelDate;
            //set loggedInAgent
            console.log('Login Res : ' + localStorage.getItem('currentUser'));
            const loginRes: any = JSON.parse(localStorage.getItem('currentUser'));
            //let loginRes: any = localStorage.getItem('currentUser');
            bagReq.loggedInAgent = loginRes.userName;
      
            //set agentLocation
            bagReq.agentLocation = loginRes.agentLocation;
    
            return bagReq;
          }
        }
      }

      onBagScanAgain() {
        this.router.navigate(['login', 'scan']);
      }

      onCancel() {
        this.router.navigate(['login', 'scan']);
      }

}
