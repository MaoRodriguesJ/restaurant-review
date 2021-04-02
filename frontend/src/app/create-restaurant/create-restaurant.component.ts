import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup } from '@angular/forms'
import { Router } from '@angular/router'
import { RestaurantInputModel } from '@app/models'
import { SnackbarService } from '@core/snackbar/snackbar.service'
import { RestaurantService } from './restaurant.service'

@Component({
  templateUrl: './create-restaurant.component.html',
})
export class CreateRestaurantComponent implements OnInit {
  restaurantForm: FormGroup

  constructor(
    private fb: FormBuilder,
    private snackBar: SnackbarService,
    private router: Router,
    private restaurantService: RestaurantService
  ) {}

  ngOnInit(): void {
    this.buildForm()
  }

  private buildForm() {
    this.restaurantForm = this.fb.group({
      restaurant: null,
    })
  }

  onSubmit = () => {
    if (this.restaurantForm.valid) {
      this.restaurantService.createRestaurant(this.restaurant.getRawValue() as RestaurantInputModel).subscribe((_) => {
        this.router.navigate(['home'])
        this.snackBar.openSnackBar('Restaurant created!', 'Dismiss')
      })
    }
    this.restaurantForm.markAllAsTouched()
  }

  get restaurant() {
    return this.restaurantForm.controls.restaurant as FormGroup
  }
}
