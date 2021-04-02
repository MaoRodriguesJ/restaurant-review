import { Component, Input, OnInit } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { CustomValidators } from '@app/validation/custom-validators'
import { CONFIRM_EMAIL, CONFIRM_PASSWORD, EMAIL, PASSWORD, REQUIRED } from '@app/validation/validation-messages'

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
})
export class UserComponent implements OnInit {
  @Input() formGroup: FormGroup
  @Input() controlPath: string

  userForm: FormGroup
  hide = true
  hideConfirm = true

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.buildForm()
    this.initForm()
  }

  private buildForm() {
    this.userForm = this.fb.group(
      {
        name: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        confirmEmail: ['', Validators.required],
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
        validators: Validators.compose([CustomValidators.emailMatchValidator, CustomValidators.passwordMatchValidator]),
      }
    )
  }

  private initForm() {
    this.formGroup.setControl(this.controlPath, this.userForm)
  }

  onHide = () => {
    this.hide = !this.hide
  }

  onHideConfirm = () => {
    this.hideConfirm = !this.hideConfirm
  }

  get name() {
    return this.userForm.controls.name
  }

  get email() {
    return this.userForm.controls.email
  }

  get confirmEmail() {
    return this.userForm.controls.confirmEmail
  }

  get password() {
    return this.userForm.controls.password
  }

  get confirmPassword() {
    return this.userForm.controls.confirmPassword
  }

  hasNameError() {
    return this.name && this.name.touched && this.name.errors
  }

  getNameErrorMessage() {
    return REQUIRED
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

  hasConfirmEmailError() {
    return this.confirmEmail && this.confirmEmail.touched && this.confirmEmail.errors
  }

  getConfirmEmailErrorMessage() {
    if (this.confirmEmail.hasError('required')) {
      return REQUIRED
    }

    return this.confirmEmail.hasError('NoEmailMatch') ? CONFIRM_EMAIL : ''
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
