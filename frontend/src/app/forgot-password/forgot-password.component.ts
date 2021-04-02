import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { Router } from '@angular/router'
import { UserService } from '@app/account/user.service'
import { EMAIL, REQUIRED } from '@app/validation/validation-messages'
import { SnackbarService } from '@core/snackbar/snackbar.service'

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss'],
})
export class ForgotPasswordComponent implements OnInit {
  userForm: FormGroup
  resetPasswordMail = false

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private snackBar: SnackbarService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.buildForm()
  }

  onSubmit = () => {
    if (this.userForm.valid) {
      this.resetPasswordMail = true
      this.userService.forgotPassword(this.email.value).subscribe(
        (_) => {
          this.resetPasswordMail = false
          this.router.navigate(['/login'])
          this.snackBar.openSnackBar('Reset email sent!', 'Dismiss')
        },
        (err) => {
          if (err?.status === 422) {
            this.resetPasswordMail = false
            this.snackBar.openSnackBar('Email not found', 'Dismiss')
          }
        }
      )
    }
    this.userForm.markAllAsTouched()
  }

  private buildForm() {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    })
  }

  get email() {
    return this.userForm.controls.email
  }

  hasEmailError() {
    return this.email && this.email.touched && this.email.errors
  }

  getEmailErrorMessage() {
    if (this.email.hasError('required')) {
      return REQUIRED
    }

    return this.email.hasError('email') ? EMAIL : ''
  }
}
