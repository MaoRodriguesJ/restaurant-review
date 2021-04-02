import { Location } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup } from '@angular/forms'
import { ActivatedRoute } from '@angular/router'
import { AdminService } from '@app/admin.service'
import { ReviewInputModel, ReviewModel } from '@app/models'
import { SnackbarService } from '@core/snackbar/snackbar.service'
import { Observable } from 'rxjs'

@Component({
  selector: 'app-review-edit',
  templateUrl: './review-edit.component.html',
})
export class ReviewEditComponent implements OnInit {
  reviewForm: FormGroup
  reviewId: number
  initialValues: Observable<ReviewModel>

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private snackBar: SnackbarService,
    private location: Location,
    private activeRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activeRoute.params.subscribe(async (params) => {
      this.reviewId = params.id
      this.initialValues = this.adminService.getReview(this.reviewId)
    })
    this.buildForm()
  }

  private buildForm() {
    this.reviewForm = this.fb.group({
      review: null,
    })
  }

  onSubmit = () => {
    if (this.reviewForm.valid) {
      this.adminService.editReview(this.reviewId, this.review.getRawValue() as ReviewInputModel).subscribe((_) => {
        this.location.back()
        this.snackBar.openSnackBar('Review edited!', 'Dismiss')
      })
    }
    this.reviewForm.markAllAsTouched()
  }

  get review() {
    return this.reviewForm.controls.review as FormGroup
  }
}
