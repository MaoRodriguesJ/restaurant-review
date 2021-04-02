import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup } from '@angular/forms'
import { Router } from '@angular/router'
import { UserInputModel } from '@app/models'
import { SnackbarService } from '@core/snackbar/snackbar.service'
import { UserService } from '../user.service'

@Component({
  templateUrl: './create-user.component.html',
})
export class CreateUserComponent implements OnInit {
  userForm: FormGroup

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private snackBar: SnackbarService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.buildForm()
  }

  private buildForm() {
    this.userForm = this.fb.group({
      user: null,
    })
  }

  onSubmit = () => {
    if (this.userForm.valid) {
      this.userService.createUser(this.user.getRawValue() as UserInputModel).subscribe(
        (_) => {
          this.router.navigate(['/login'])
          this.snackBar.openSnackBar('User created!', 'Dismiss')
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
