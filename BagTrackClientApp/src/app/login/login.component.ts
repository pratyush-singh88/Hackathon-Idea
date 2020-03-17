import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

import { Login } from '../classes/login';
import { LoginService } from '../services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public loginForm: FormGroup;
  user = '';
  action = '';
  errors: any;
  location = false;

  constructor(private route: ActivatedRoute, private router: Router, 
    private loginService: LoginService) { }

  ngOnInit() {

    this.loginForm = new FormGroup({
      userName: new FormControl('', [Validators.required, Validators.maxLength(30)]),
      userPassword: new FormControl('', [Validators.required]),
      agentLocation: new FormControl()
    });

    this.route.paramMap.subscribe(params => {

      const loginRes: any = JSON.parse(localStorage.getItem('currentUser'));
      //console.log('Login : ' + JSON.stringify(loginRes));
      const action = params.get('action');
      if (action === 'checkin') {
        this.user = 'Agent';
        this.action = 'Checkin Bag';
        this.location = false;
        if (loginRes !== null) {
          let userRole = loginRes.userRole;
          if (userRole === 'CHECKIN_AGENT') {
            this.router.navigate(['checkin']);
          }
        }
      }
      else if (action === 'load') {
        this.user = 'Ground Handler';
        this.action = 'Load Bag';
        this.location = true;
        if (loginRes !== null) {
          let userRole = loginRes.userRole;
          if (userRole === 'GROUND_HANDLER') {
            this.router.navigate(['bag', 'load']);
          }
        }
      }
      else if (action === 'scan') {
        this.user = 'Ground Handler';
        this.action = 'Scan Bag';
        this.location = true;
        if (loginRes !== null) {
          let userRole = loginRes.userRole;
          if (userRole === 'GROUND_HANDLER') {
            this.router.navigate(['scan']);
          }
        }
      }
      else if (action === 'delay') {
        this.user = 'Agent/Ground Handler';
        this.action = 'Flight Delay';
        this.location = false;
        if (loginRes !== null) {
          let userRole = loginRes.userRole;
          if (userRole === 'GROUND_HANDLER' 
            || userRole === 'CHECKIN_AGENT') {
            this.router.navigate(['flight-delay']);
          }
        }
      }
    });

    // logout the person when he opens the app for the first time
    //this.loginService.logout();
  }

  onLoginUserFormSubmit($event: any) {
    console.log("Entry onFormSubmit login component");
    if (this.loginForm.invalid) {
      return;
    }
    else {
      //login using email and password
      let loginForm: Login = this.loginForm.value;
      if (this.user === 'Agent') {
        loginForm.userRole = 'CHECKIN_AGENT';
      }
      else if (this.user === 'Ground Handler') {
        loginForm.userRole = 'GROUND_HANDLER';
      }
      //uncomment after service integration
      this.loginUser(loginForm);

      //comment after service integration
      // if (this.action === 'Checkin Bag') {
      //   this.router.navigate(['checkin']);
      // }
      // else if (this.action === 'Load Bag') {
      //   this.router.navigate(['bag', 'load']);
      // }
      // else if (this.action === 'UnLoad Bag') {
      //   this.router.navigate(['bag', 'unload']);
      // }
    }
  }

  loginUser(login: Login) {
    this.loginService.login(login).subscribe(    
      (logRes: any) => {

        console.log("Login Successfull");
        console.log('Login response : ' + JSON.stringify(logRes));
        localStorage.setItem('currentUser', JSON.stringify(logRes));

        if (this.action === 'Checkin Bag') {
          this.router.navigate(['checkin']);
        }
        else if (this.action === 'Load Bag') {
          this.router.navigate(['bag', 'load']);
        }
        else if (this.action === 'Scan Bag') {
          this.router.navigate(['scan']);
        }
        else if (this.action === 'Flight Delay') {
          this.router.navigate(['flight-delay']);
        }
        //this.errors = "Not able to login. Please check the login details";
      },
      (error) => {
        console.log("Errors:Error : " + JSON.stringify(error.error));
        this.errors = error.error.message;
      });
  }

}
