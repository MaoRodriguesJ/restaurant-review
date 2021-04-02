import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup } from '@angular/forms'
import { MatDialogConfig } from '@angular/material/dialog'
import { ActivatedRoute, Router } from '@angular/router'
import { ReviewInputModel } from '@app/models'
import { DialogService } from '@core/modal/dialog.service'
import { SnackbarService } from '@core/snackbar/snackbar.service'
import { ReviewService } from './review.service'

@Component({
  templateUrl: './rating.component.html',
})
export class RatingComponent implements OnInit {
  reviewForm: FormGroup
  restaurantId: number

  constructor(
    private fb: FormBuilder,
    private snackBar: SnackbarService,
    private router: Router,
    private activeRoute: ActivatedRoute,
    private reviewService: ReviewService,
    private dialogService: DialogService
  ) {}

  ngOnInit(): void {
    this.activeRoute.params.subscribe((params) => {
      this.restaurantId = params.id
      this.buildForm()
    })
  }

  private buildForm() {
    this.reviewForm = this.fb.group({
      review: null,
    })
  }

  onSubmit = () => {
    if (this.reviewForm.valid) {
      if (this.rate.value === 0) {
        const dialogRef = this.dialogService.openConfirmDialog(this.dialogConfig())
        dialogRef.afterClosed().subscribe((hasConfirmed) => {
          if (hasConfirmed) {
            this.createReview()
          }
        })
      } else {
        this.createReview()
      }
    }
    this.reviewForm.markAllAsTouched()
  }

  private createReview() {
    this.reviewService.createReview(this.review.getRawValue() as ReviewInputModel).subscribe((_) => {
      this.router.navigate(['home'])
      this.snackBar.openSnackBar('Review created!', 'Dismiss')
    })
  }

  dialogConfig() {
    const dialogConfig = new MatDialogConfig()
    dialogConfig.data = {
      title: '0 start rating',
      message: 'Are you sure you want to give a 0 star rating to this visit?',
      primaryOption: 'YES',
      secondaryOption: 'NO',
    }
    dialogConfig.width = '17.5em'
    return dialogConfig
  }

  get review() {
    return this.reviewForm.controls.review as FormGroup
  }

  get rate() {
    return this.review.controls.rate
  }
}
