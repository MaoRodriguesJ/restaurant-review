import { AgmCoreModule } from '@agm/core'
import { MatGoogleMapsAutocompleteModule } from '@angular-material-extensions/google-maps-autocomplete'
import { NgModule } from '@angular/core'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { BrowserModule } from '@angular/platform-browser'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { DialogModule } from '@core/modal/dialog.module'
import { StarRatingModule } from 'angular-star-rating'
import { AccountEditComponent } from './account-edit/account-edit.component'
import { AccountModule } from './account/account.module'
import { AppRoutingModule } from './app-routing.module'
import { AppComponent } from './app.component'
import { ComponentsModule } from './components/components.module'
import { CoreModule } from './core/core.module'
import { CreateRestaurantComponent } from './create-restaurant/create-restaurant.component'
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component'
import { HomeComponent } from './home/home.component'
import { LayoutModule } from './layout/layout.module'
import { MaterialModule } from './material/material.module'
import { PendingRepliesComponent } from './pending-replies/pending-replies.component'
import { RatingComponent } from './rating/rating.component'
import { ResetPasswordComponent } from './reset-password/reset-password.component'
import { RestaurantDetailComponent } from './restaurant-detail/restaurant-detail.component'
import { RestaurantListComponent } from './restaurant-list/restaurant-list.component'
import { ReviewCardComponent } from './review-card/review-card.component'

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    RestaurantListComponent,
    CreateRestaurantComponent,
    RatingComponent,
    ReviewCardComponent,
    PendingRepliesComponent,
    RestaurantDetailComponent,
    AccountEditComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    CoreModule,
    ReactiveFormsModule,
    FormsModule,
    LayoutModule,
    AccountModule,
    ComponentsModule,
    StarRatingModule.forRoot(),
    DialogModule,
    MatGoogleMapsAutocompleteModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyAteKPAa7pdGD0I9OoIh6RLH3kQ2KZuydY',
      libraries: ['places'],
    }),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
