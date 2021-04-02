import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup } from '@angular/forms'
import { Router } from '@angular/router'
import { AdminService } from '@app/admin.service'
import { PasswordModel, UserModel } from '@app/models'
import { UserContextService } from '@core/service/user-context.service'
import { SnackbarService } from '@core/snackbar/snackbar.service'
import { Observable } from 'rxjs'

@Component({
  templateUrl: './account-edit.component.html',
})
export class AccountEditComponent implements OnInit {
  userForm: FormGroup
  passwordForm: FormGroup
  userId: number
  initialValues: Observable<UserModel>

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private snackBar: SnackbarService,
    private router: Router,
    private userContext: UserContextService
  ) {}

  ngOnInit(): void {
    this.userId = this.userContext.userInfo.id
    this.initialValues = this.userContext.refresh()
    this.buildForm()
  }

  private buildForm() {
    this.userForm = this.fb.group({
      user: null,
    })
    this.passwordForm = this.fb.group({
      password: null,
    })
  }

  onSubmitUser = () => {
    if (this.userForm.valid) {
      this.adminService.editUser(this.userId, this.user.getRawValue() as UserModel).subscribe(
        (_) => {
          this.router.navigate(['/home'])
          this.snackBar.openSnackBar('User edited!', 'Dismiss')
        },
        (err) => {
          if (err?.status === 422) {
            this.snackBar.openSnackBar('Email already in use', 'Dismiss')
          }
        }
      )
    }
    this.userForm.markAllAsTouched()
  }

  onSubmitPassword = () => {
    if (this.passwordForm.valid) {
      this.adminService.changePassword(this.userId, this.password.getRawValue() as PasswordModel).subscribe(
        (_) => {
          this.router.navigate(['/home'])
          this.snackBar.openSnackBar('Password changed!', 'Dismiss')
        },
        (err) => {
          if (err?.status === 422) {
            this.snackBar.openSnackBar('Wrong password!', 'Dismiss')
          }
        }
      )
    }
    this.passwordForm.markAllAsTouched()
  }

  get user() {
    return this.userForm.controls.user as FormGroup
  }

  get password() {
    return this.passwordForm.controls.password as FormGroup
  }
}
