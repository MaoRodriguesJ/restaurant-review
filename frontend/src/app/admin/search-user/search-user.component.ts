import { Component } from '@angular/core'
import { FormControl } from '@angular/forms'
import { Router } from '@angular/router'
import { UserService } from '@app/account/user.service'
import { AdminService } from '@app/admin.service'
import { UserModel } from '@app/models'
import { SnackbarService } from '@core/snackbar/snackbar.service'

@Component({
  templateUrl: './search-user.component.html',
})
export class SearchUserComponent {
  user: UserModel
  email = new FormControl('')
  resetPasswordMail = false

  constructor(
    private adminService: AdminService,
    private snackBar: SnackbarService,
    private router: Router,
    private userService: UserService
  ) {}

  onSearch() {
    this.adminService.searchUser(this.email.value).subscribe((data) => {
      this.user = data
    })
  }

  onResetPassword(email: string) {
    this.resetPasswordMail = true
    this.userService.forgotPassword(email).subscribe(
      (_) => {
        this.resetPasswordMail = false
        this.snackBar.openSnackBar('Reset password email sent!', 'Dismiss')
      },
      (_) => {
        this.resetPasswordMail = false
      }
    )
  }

  onEdit(id: number) {
    this.router.navigate([`admin/users/${id}/edit`])
  }

  onDelete(id: number) {
    this.adminService.deleteUser(id).subscribe((_) => {
      this.snackBar.openSnackBar('User deleted', 'Dismiss')
      this.clear()
    })
  }

  private clear() {
    this.user = null
    this.email.reset()
  }
}
