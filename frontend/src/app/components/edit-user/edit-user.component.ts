import { Component, Input, OnInit } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { UserModel } from '@app/models'
import { EMAIL, REQUIRED } from '@app/validation/validation-messages'

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
})
export class EditUserComponent implements OnInit {
  @Input() formGroup: FormGroup
  @Input() initialValues: UserModel
  @Input() controlPath: string

  userForm: FormGroup

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.buildForm()
    this.initForm()
  }

  private buildForm() {
    this.userForm = this.fb.group({
      name: [this.initialValues?.displayName, Validators.required],
      email: [this.initialValues?.username, [Validators.required, Validators.email]],
    })
  }

  private initForm() {
    this.formGroup.setControl(this.controlPath, this.userForm)
  }

  get name() {
    return this.userForm.controls.name
  }

  get email() {
    return this.userForm.controls.email
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
}
