import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { LoginService } from '../services/login.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  private triggerPrediction = environment.triggerPredictionEndpoint;

  constructor(private loginService: LoginService, private router: Router) { }

  ngOnInit() {
  }

  onLogout() {
    this.loginService.logout();
    this.router.navigate(['login', 'checkin']);
  }

  onTriggerPrediction() {
    window.open(this.triggerPrediction, "_blank");
  }

}
