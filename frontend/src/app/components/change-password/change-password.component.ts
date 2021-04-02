import { Component, Input, OnInit } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { CustomValidators } from '@app/validation/custom-validators'
import { CONFIRM_PASSWORD, PASSWORD, REQUIRED } from '@app/validation/validation-messages'

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
})
export class ChangePasswordComponent implements OnInit {
  @Input() formGroup: FormGroup
  @Input() controlPath: string

  passwordForm: FormGroup
  hide = true
  hideConfirm = true
  hideCurrent = true

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.buildForm()
    this.initForm()
  }

  private buildForm() {
    this.passwordForm = this.fb.group(
      {
        oldPassword: [''],
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

  private initForm() {
    this.formGroup.setControl(this.controlPath, this.passwordForm)
  }

  onHide = () => {
    this.hide = !this.hide
  }

  onHideConfirm = () => {
    this.hideConfirm = !this.hideConfirm
  }

  onHideCurrent = () => {
    this.hideCurrent = !this.hideCurrent
  }

  get oldPassword() {
    return this.passwordForm.controls.oldPassword
  }

  get password() {
    return this.passwordForm.controls.password
  }

  get confirmPassword() {
    return this.passwordForm.controls.confirmPassword
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
