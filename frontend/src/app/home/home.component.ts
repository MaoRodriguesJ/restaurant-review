import { Component, OnInit } from '@angular/core'
import { Router } from '@angular/router'
import { UserContextService } from '../core/service/user-context.service'

@Component({
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {
  constructor(private userContext: UserContextService, private router: Router) {}

  ngOnInit(): void {}

  isRestaurantOwner() {
    return this.userContext.hasRole('ROLE_OWNER')
  }

  isAdmin() {
    return this.userContext.hasRole('ROLE_ADMIN')
  }

  registerNewRestaurant() {
    this.router.navigate(['create-restaurant'])
  }

  searchUser() {
    this.router.navigate(['admin/search-user'])
  }
}
