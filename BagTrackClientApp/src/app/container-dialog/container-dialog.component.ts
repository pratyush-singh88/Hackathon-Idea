import { Component, OnInit, Optional, Inject, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { NgxQRCodeModule } from 'ngx-qrcode2';

@Component({
  selector: 'app-container-dialog',
  templateUrl: './container-dialog.component.html',
  styleUrls: ['./container-dialog.component.scss']
})
export class ContainerDialogComponent implements OnInit {

  localDialogData = '';
  container = '';
  
  dialogOpenForQRCode = false;
  href: string;

  constructor(public dialogRef: MatDialogRef<ContainerDialogComponent>, 
    @Optional() @Inject(MAT_DIALOG_DATA) public data: any) { 
      this.localDialogData = data;
      if (this.localDialogData !== 'Container data') {
        this.dialogOpenForQRCode = true;
      }
    }

  ngOnInit() {
  }

  doAction() {
    this.dialogRef.close({event:'close', data: this.container});
  }

  doQRCodeDownload() {
    
    this.href = document.getElementsByTagName('img')[1].src;
    console.log(this.href);

    this.dialogRef.close( {event:'Cancel'} );
  }

  closeDialog() {
    this.dialogRef.close( {event:'Cancel'} );
  }

  cancelDialog() {
    this.dialogRef.close( {event:'Cancel', data: 'Cancel'} );
  }

}
