import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup } from '@angular/forms'
import { ActivatedRoute, Router } from '@angular/router'
import { AdminService } from '@app/admin.service'
import { UserModel } from '@app/models'
import { SnackbarService } from '@core/snackbar/snackbar.service'
import { Observable } from 'rxjs'

@Component({
  templateUrl: './user-edit.component.html',
})
export class UserEditComponent implements OnInit {
  userForm: FormGroup
  userId: number
  initialValues: Observable<UserModel>

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private snackBar: SnackbarService,
    private router: Router,
    private activeRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activeRoute.params.subscribe(async (params) => {
      this.userId = params.id
      this.initialValues = this.adminService.getUser(this.userId)
    })
    this.buildForm()
  }

  private buildForm() {
    this.userForm = this.fb.group({
      user: null,
    })
  }

  onSubmit = () => {
    if (this.userForm.valid) {
      this.adminService.editUser(this.userId, this.user.getRawValue() as UserModel).subscribe(
        (_) => {
          this.router.navigate(['/admin/search-user'])
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

  get user() {
    return this.userForm.controls.user as FormGroup
  }
}
