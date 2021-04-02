import { Component, HostListener, OnInit } from '@angular/core'
import { Router } from '@angular/router'
import { UserContextService } from '@core/service/user-context.service'

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  toolbarClass: string
  appTitle = 'RESTAURANT REVIEWS'

  private lastScrollTop = 0

  constructor(private userContext: UserContextService, private router: Router) {}

  ngOnInit(): void {
    this.toolbarClass = 'mat-toolbar smart-scroll'
  }

  @HostListener('window:scroll', [])
  onScroll(): void {
    const scrollTop = document.documentElement.scrollTop || document.body.scrollTop
    this.toolbarClass = 'mat-toolbar smart-scroll ' + (scrollTop < this.lastScrollTop ? 'scrolled-up' : 'scrolled-down')
    this.lastScrollTop = scrollTop
  }

  onActivate(event) {
    window.scroll(0, 0)
  }

  onLogout() {
    this.userContext.destroy()
  }

  onAccount() {
    this.router.navigate(['/account'])
  }
}
