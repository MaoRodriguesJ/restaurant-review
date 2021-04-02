import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core'
import { FormControl } from '@angular/forms'
import { Router } from '@angular/router'
import { AdminService } from '@app/admin.service'
import { ReviewModel } from '@app/models'
import { PendingRepliesService } from '@app/pending-replies/pending-replies.service'
import { UserContextService } from '@core/service/user-context.service'
import { SnackbarService } from '@core/snackbar/snackbar.service'

@Component({
  selector: 'app-review-card',
  templateUrl: './review-card.component.html',
  styleUrls: ['./review-card.component.scss'],
})
export class ReviewCardComponent implements OnInit {
  @Input() review: ReviewModel
  @Output() refreshReviews: EventEmitter<boolean> = new EventEmitter()

  reply = new FormControl('')
  replied = false
  editingReply = false

  constructor(
    private userContext: UserContextService,
    private snackBar: SnackbarService,
    private replyService: PendingRepliesService,
    private adminService: AdminService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  isRestaurantOwner() {
    return this.userContext.hasRole('ROLE_OWNER')
  }

  isAdmin() {
    return this.userContext.hasRole('ROLE_ADMIN')
  }

  onReply(id: number) {
    this.replyService.reply(this.reply.value, id).subscribe((_) => {
      this.replied = true
      this.snackBar.openSnackBar('Replied', 'Dismiss')
    })
  }

  onEdit(id: number) {
    this.router.navigate([`/admin/reviews/${id}/edit`])
  }

  onDelete(id: number) {
    this.adminService.deleteReview(id).subscribe((_) => {
      this.snackBar.openSnackBar('Review deleted', 'Dismiss')
      this.refreshReviews.emit()
    })
  }

  onEditReply(id: number) {
    if (this.editingReply) {
      this.adminService.editReply(id, this.reply.value).subscribe((_) => {
        this.snackBar.openSnackBar('Reply edited!', 'Dismiss')
      })
      this.review.reply = this.reply.value
      this.editingReply = false
    } else {
      this.reply.setValue(this.review.reply)
      this.editingReply = true
    }
  }

  onDeleteReply(id: number) {
    this.adminService.deleteReply(id).subscribe((_) => {
      this.snackBar.openSnackBar('Reply deleted', 'Dismiss')
      this.refreshReviews.emit()
    })
  }
}
