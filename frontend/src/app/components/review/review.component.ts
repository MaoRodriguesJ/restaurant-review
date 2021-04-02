import { Component, Input, OnInit } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { ReviewInputModel } from '@app/models'
import { CustomValidators } from '@app/validation/custom-validators'
import { BEFORE_EQUAL_TODAY, REQUIRED } from '@app/validation/validation-messages'

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
})
export class ReviewComponent implements OnInit {
  @Input() formGroup: FormGroup
  @Input() controlPath: string
  @Input() initialValues: ReviewInputModel
  @Input() restaurantId: number

  reviewForm: FormGroup

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.buildForm()
    this.initForm()
  }

  private buildForm() {
    this.reviewForm = this.fb.group({
      restaurantId: [this.initialValues?.restaurantId || this.restaurantId],
      rate: [this.initialValues?.rate || 0],
      comment: [this.initialValues?.comment],
      visitDate: [
        this.initialValues?.visitDate,
        [Validators.required, CustomValidators.beforeEqualToday({ invalidDate: true })],
      ],
    })
  }

  private initForm() {
    this.formGroup.setControl(this.controlPath, this.reviewForm)
  }

  get rate() {
    return this.reviewForm.controls.rate
  }

  get comment() {
    return this.reviewForm.controls.comment
  }

  get visitDate() {
    return this.reviewForm.controls.visitDate
  }

  hasVisitDateError() {
    return this.visitDate && this.visitDate.touched && this.visitDate.errors
  }

  getVisitDateMessage() {
    if (this.visitDate.hasError('required')) {
      return REQUIRED
    }

    return this.visitDate.hasError('invalidDate') ? BEFORE_EQUAL_TODAY : ''
  }
}
