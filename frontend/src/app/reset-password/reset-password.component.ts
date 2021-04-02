import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { ActivatedRoute, Router } from '@angular/router'
import { UserService } from '@app/account/user.service'
import { ResetPasswordModel } from '@app/models'
import { CustomValidators } from '@app/validation/custom-validators'
import { CONFIRM_PASSWORD, PASSWORD, REQUIRED } from '@app/validation/validation-messages'
import { SnackbarService } from '@core/snackbar/snackbar.service'

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
})
export class ResetPasswordComponent implements OnInit {
  resetForm: FormGroup
  hide = true
  hideConfirm = true
  token: string
  email: string

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private snackBar: SnackbarService,
    private router: Router,
    private activeRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activeRoute.params.subscribe((params) => {
      this.token = params.token
      this.email = params.email
      this.userService.validResetToken(this.token, this.email).subscribe((data) => {
        if (!data) {
          this.router.navigate(['/login'])
          this.snackBar.openSnackBar('Invalid or expired token!', 'Dismiss')
        }
      })
    })
    this.buildForm()
  }

  private buildForm() {
    this.resetForm = this.fb.group(
      {
        password: [
          '',
          [
            Validators.required,
            CustomValidators.patternValidator(/\d/, { hasNumber: true }),
            CustomValidators.patternValidator(/[a-zA-Z]/, { hasLetter: true }),
          ],
        ],
        confirmPassword: ['', Validators.required],
      },
      {
        validators: Validators.compose([CustomValidators.passwordMatchValidator]),
      }
    )
  }

  onSubmit = () => {
    if (this.resetForm.valid) {
      this.userService
        .resetPassword(this.resetForm.getRawValue() as ResetPasswordModel, this.token, this.email)
        .subscribe((_) => {
          this.router.navigate(['/login'])
          this.snackBar.openSnackBar('Password changed!', 'Dismiss')
        })
    }
    this.resetForm.markAllAsTouched()
  }

  onHide = () => {
    this.hide = !this.hide
  }

  onHideConfirm = () => {
    this.hideConfirm = !this.hideConfirm
  }

  get password() {
    return this.resetForm.controls.password
  }

  get confirmPassword() {
    return this.resetForm.controls.confirmPassword
  }

  hasPasswordError() {
    return this.password && this.password.touched && this.password.errors
  }

  getPasswordErrorMessage() {
    if (this.password.hasError('required')) {
      return REQUIRED
    }

    return this.password.hasError('hasNumber') || this.password.hasError('hasLetter') ? PASSWORD : ''
  }

  hasConfirmPasswordError() {
    return this.confirmPassword && this.confirmPassword.touched && this.confirmPassword.errors
  }

  getConfirmPasswordErrorMessage() {
    if (this.confirmPassword.hasError('required')) {
      return REQUIRED
    }

    return this.confirmPassword.hasError('NoPassswordMatch') ? CONFIRM_PASSWORD : ''
  }
}
