import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { LayoutModule } from '@angular/cdk/layout';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ZXingScannerComponent } from '@zxing/ngx-scanner';
import { NgxQRCodeModule } from 'ngx-qrcode2';

import { HeaderComponent } from './header/header.component';
import { AngularMaterialModule } from './angular-material.module';
import { LoginComponent } from './login/login.component';
import { CheckinComponent } from './checkin/checkin.component';
import { LoadUnloadComponent } from './load-unload/load-unload.component';
import { BaggageDialogComponent } from './baggage-dialog/baggage-dialog.component';
import { FlightDelayComponent } from './flight-delay/flight-delay.component';
import { ContainerDialogComponent } from './container-dialog/container-dialog.component';
import { BagScanComponent } from './bag-scan/bag-scan.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    CheckinComponent,
    LoadUnloadComponent,
    BaggageDialogComponent,
    FlightDelayComponent,
    ContainerDialogComponent,
    BagScanComponent,
    ZXingScannerComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    AngularMaterialModule,
    HttpClientModule,
    FlexLayoutModule,
    NgxQRCodeModule
  ],
  entryComponents: [
    BaggageDialogComponent,
    ContainerDialogComponent
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
