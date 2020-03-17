import { Component, OnInit, Optional, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-baggage-dialog',
  templateUrl: './baggage-dialog.component.html',
  styleUrls: ['./baggage-dialog.component.scss']
})
export class BaggageDialogComponent implements OnInit {

  localDialogData: any = [];
  localFormattedDialogData = '';

  constructor(public dialogRef: MatDialogRef<BaggageDialogComponent>, 
    @Optional() @Inject(MAT_DIALOG_DATA) public data: any) {
    console.log('Dialog data: ' + data);
    data.forEach(dialogData => {
      console.log('Dialog data: ' + dialogData);
      this.localDialogData.push(dialogData);
    });
    //this.localDialogData = data;

    this.localDialogData.forEach((dialogDataForAlter: any) => {
      let messageArray = dialogDataForAlter.split("\n");
      console.log('messageArray: ' + messageArray);
      for(var index in messageArray) {
        let messageElement = messageArray[index];
        this.localFormattedDialogData = this.localFormattedDialogData + messageElement.trim() + '\n';
        console.log('localFormattedDialogData: ' + this.localFormattedDialogData);
      }
    });
   }

  ngOnInit() {
  }

  closeDialog() {
    this.dialogRef.close( {event:'Cancel'} );
  }

}
