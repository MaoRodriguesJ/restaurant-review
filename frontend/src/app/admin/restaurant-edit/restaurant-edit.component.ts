import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup } from '@angular/forms'
import { ActivatedRoute, Router } from '@angular/router'
import { AdminService } from '@app/admin.service'
import { RestaurantInputModel, RestaurantViewModel } from '@app/models'
import { RestaurantDetailService } from '@app/restaurant-detail/restaurant-detail.service'
import { SnackbarService } from '@core/snackbar/snackbar.service'
import { Observable } from 'rxjs'

@Component({
  selector: 'app-restaurant-edit',
  templateUrl: './restaurant-edit.component.html',
})
export class RestaurantEditComponent implements OnInit {
  restaurantForm: FormGroup
  restaurantId: number
  initialValues: Observable<RestaurantViewModel>

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private restaurantService: RestaurantDetailService,
    private snackBar: SnackbarService,
    private router: Router,
    private activeRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activeRoute.params.subscribe(async (params) => {
      this.restaurantId = params.id
      this.initialValues = this.restaurantService.getRestaurant(this.restaurantId)
    })
    this.buildForm()
  }

  private buildForm() {
    this.restaurantForm = this.fb.group({
      restaurant: null,
    })
  }

  onSubmit = () => {
    if (this.restaurantForm.valid) {
      this.adminService
        .editRestaurant(this.restaurantId, this.restaurant.getRawValue() as RestaurantInputModel)
        .subscribe((_) => {
          this.router.navigate([`restaurants/${this.restaurantId}`])
          this.snackBar.openSnackBar('Restaurant edited!', 'Dismiss')
        })
    }
    this.restaurantForm.markAllAsTouched()
  }

  get restaurant() {
    return this.restaurantForm.controls.restaurant as FormGroup
  }
}
