import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper'
import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup } from '@angular/forms'
import { MatStepper } from '@angular/material/stepper'
import { Router } from '@angular/router'
import { RestaurantOwnerInputModel } from '@app/models'
import { SnackbarService } from '@core/snackbar/snackbar.service'
import { UserService } from '../user.service'

@Component({
  templateUrl: './create-owner.component.html',
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: { showError: true },
    },
  ],
})
export class CreateOwnerComponent implements OnInit {
  form: FormGroup

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
    this.form = this.fb.group({
      user: null,
      restaurant: null,
    })
  }

  onNext(stepper: MatStepper) {
    this.user.markAllAsTouched()
    stepper.next()
  }

  onSubmit = () => {
    if (this.form.valid) {
      this.userService.createRestaurantOwner(this.form.getRawValue() as RestaurantOwnerInputModel).subscribe(
        (_) => {
          this.router.navigate(['/login'])
          this.snackBar.openSnackBar('Restaurant created!', 'Dismiss')
        },
        (err) => {
          if (err?.status === 422) {
            this.snackBar.openSnackBar('Email already in use', 'Dismiss')
          }
        }
      )
    }
    this.user.markAllAsTouched()
    this.restaurant.markAllAsTouched()
  }

  get user() {
    return this.form.controls.user as FormGroup
  }

  get restaurant() {
    return this.form.controls.restaurant as FormGroup
  }
}
