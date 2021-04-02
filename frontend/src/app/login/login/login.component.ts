import { Component, OnInit } from '@angular/core'
import { FormBuilder, Validators } from '@angular/forms'
import { Router } from '@angular/router'
import { LoginModel } from '@app/models'
import { SnackbarService } from '@core/snackbar/snackbar.service'
import { LoginService } from '../login.service'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit {
  loginForm = this.fb.group({
    email: ['', Validators.required],
    password: ['', Validators.required],
  })
  hide = true

  constructor(
    private fb: FormBuilder,
    private loginService: LoginService,
    private snackBar: SnackbarService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.loginService.ifLogged()) {
      this.loginService.navigateToHome()
    }
  }

  onSubmit = () => {
    if (this.loginForm.valid) {
      this.loginService.createSession(this.loginForm.getRawValue() as LoginModel).subscribe(
        (_) => {
          this.loginService.navigateToHome()
        },
        (err) => {
          if (err?.status.toString().startsWith(4)) {
            this.snackBar.openSnackBar('Incorrect username or password', 'Dismiss')
          }
        }
      )
    }
    this.loginForm.markAllAsTouched()
  }

  onHide = () => {
    this.hide = !this.hide
  }

  onSignUp = () => {
    this.router.navigate(['/create'])
  }

  get email() {
    return this.loginForm.controls.email
  }

  get password() {
    return this.loginForm.controls.password
  }

  hasEmailError() {
    return this.email && this.email.touched && this.email.errors
  }

  getEmailErrorMessage() {
    return 'You must enter a value'
  }

  hasPasswordError() {
    return this.password && this.password.touched && this.password.errors
  }

  getPasswordErrorMessage() {
    return 'You must enter a value'
  }
}
