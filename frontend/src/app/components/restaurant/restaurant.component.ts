import { Appearance, Location } from '@angular-material-extensions/google-maps-autocomplete'
import { Component, Input, OnInit } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { PartialDefaultOption, RestaurantViewModel } from '@app/models'
import { REQUIRED, REQUIRED_SELECT } from '@app/validation/validation-messages'
import { createDefaultSelectFieldProvider, SelectFieldProvider } from '../select-field-provider'
import PlaceResult = google.maps.places.PlaceResult

export const TypesOfFood: PartialDefaultOption[] = [
  { id: 1, description: 'Fast food' },
  { id: 2, description: 'Thai' },
  { id: 3, description: 'Chinese' },
  { id: 4, description: 'Pizza' },
  { id: 5, description: 'Hamburger' },
  { id: 6, description: 'Japanese' },
  { id: 7, description: 'Brazilian' },
  { id: 8, description: 'Italian' },
  { id: 9, description: 'Seafood' },
  { id: 10, description: 'Desert' },
]

@Component({
  selector: 'app-restaurant',
  templateUrl: './restaurant.component.html',
})
export class RestaurantComponent implements OnInit {
  @Input() formGroup: FormGroup
  @Input() controlPath: string
  @Input() initialValues: RestaurantViewModel
  appearance = Appearance

  selectProvider: SelectFieldProvider<PartialDefaultOption>
  restaurantForm: FormGroup

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.buildForm()
    this.initForm()
  }

  private buildForm() {
    this.restaurantForm = this.fb.group({
      name: [this.initialValues?.name, Validators.required],
      description: [this.initialValues?.description, Validators.required],
      typeOfFood: [
        TypesOfFood.find((type) => type.description === this.initialValues?.typeOfFood),
        Validators.required,
      ],
      address: [this.initialValues?.address, Validators.required],
      latitude: [null],
      longitude: [null],
    })
    this.selectProvider = createDefaultSelectFieldProvider<PartialDefaultOption>(TypesOfFood)
  }

  private initForm() {
    this.formGroup.setControl(this.controlPath, this.restaurantForm)
  }

  onAutocompleteSelected(result: PlaceResult) {
    this.address.setValue(result.formatted_address)
  }

  onLocationSelected(location: Location) {
    this.latitude.setValue(location.latitude)
    this.longitude.setValue(location.longitude)
  }

  get name() {
    return this.restaurantForm.controls.name
  }

  get description() {
    return this.restaurantForm.controls.description
  }

  get typeOfFood() {
    return this.restaurantForm.controls.typeOfFood
  }

  get address() {
    return this.restaurantForm.controls.address
  }

  get latitude() {
    return this.restaurantForm.controls.latitude
  }

  get longitude() {
    return this.restaurantForm.controls.longitude
  }

  hasNameError() {
    return this.name && this.name.touched && this.name.errors
  }

  getNameErrorMessage() {
    return REQUIRED
  }

  hasDescriptionError() {
    return this.description && this.description.touched && this.description.errors
  }

  getDescriptionErrorMessage() {
    return REQUIRED
  }

  hasTypeOfFoodError() {
    return this.typeOfFood && this.typeOfFood.touched && this.typeOfFood.errors
  }

  getTypeOfFoodErrorMessage() {
    return REQUIRED_SELECT
  }

  hasAddressError() {
    return this.address && this.address.touched && this.address.errors
  }

  getAddressErrorMessage() {
    return REQUIRED
  }
}
