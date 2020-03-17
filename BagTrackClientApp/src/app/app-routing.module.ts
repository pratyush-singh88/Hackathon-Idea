import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { CheckinComponent } from './checkin/checkin.component';
import { LoadUnloadComponent } from './load-unload/load-unload.component';
import { FlightDelayComponent } from './flight-delay/flight-delay.component';
import { BagScanComponent } from './bag-scan/bag-scan.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login/checkin' },
  { path: 'login/:action', component: LoginComponent },
  { path: 'checkin', component: CheckinComponent },
  { path: 'bag/:action', component: LoadUnloadComponent },
  { path: 'flight-delay', component: FlightDelayComponent },
  { path: 'scan', component: BagScanComponent },

  {path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
