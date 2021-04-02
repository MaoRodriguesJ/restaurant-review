import { AgmCoreModule } from '@agm/core'
import { MatGoogleMapsAutocompleteModule } from '@angular-material-extensions/google-maps-autocomplete'
import { CommonModule } from '@angular/common'
import { NgModule } from '@angular/core'
import { ReactiveFormsModule } from '@angular/forms'
import { MaterialModule } from '@app/material/material.module'
import { StarRatingModule } from 'angular-star-rating'
import { ChangePasswordComponent } from './change-password/change-password.component'
import { DateFieldComponent } from './date-field/date-field.component'
import { EditUserComponent } from './edit-user/edit-user.component'
import { RestaurantComponent } from './restaurant/restaurant.component'
import { ReviewComponent } from './review/review.component'
import { UserComponent } from './user/user.component'

@NgModule({
  declarations: [
    UserComponent,
    RestaurantComponent,
    DateFieldComponent,
    EditUserComponent,
    ReviewComponent,
    ChangePasswordComponent,
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    StarRatingModule.forRoot(),
    MatGoogleMapsAutocompleteModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyAteKPAa7pdGD0I9OoIh6RLH3kQ2KZuydY',
      libraries: ['places'],
    }),
  ],
  exports: [
    MaterialModule,
    ReactiveFormsModule,
    UserComponent,
    RestaurantComponent,
    DateFieldComponent,
    EditUserComponent,
    ReviewComponent,
    ChangePasswordComponent,
  ],
})
export class ComponentsModule {}
