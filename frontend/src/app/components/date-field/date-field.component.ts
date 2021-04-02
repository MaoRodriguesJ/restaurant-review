import { Component, Input, OnInit, Optional, Self } from '@angular/core'
import { AbstractControl, ControlValueAccessor, NgControl } from '@angular/forms'
import { MatDatepickerInputEvent } from '@angular/material/datepicker'
import * as moment from 'moment'

const DATE_FORMAT = 'YYYY-MM-DD'

@Component({
  selector: 'app-date-field',
  templateUrl: './date-field.component.html',
})
export class DateFieldComponent implements OnInit, ControlValueAccessor {
  @Input()
  placeholder = 'dd/mm/aaaa'

  @Input()
  label: ''

  @Input()
  hint: ''

  @Input()
  hasError: boolean

  @Input()
  errorMessage: string

  control: AbstractControl

  @Input()
  _value: string

  onChange = (_: any) => {}
  onTouch = (_?: any) => {}

  constructor(@Self() @Optional() public ngControl: NgControl) {
    if (this.ngControl) {
      this.ngControl.valueAccessor = this
    }
  }

  get value() {
    return moment(this._value, DATE_FORMAT)
  }

  set value(val) {
    this._value = moment(val).format(DATE_FORMAT)
    this.onChange(this._value)
  }

  onDateInput(event: MatDatepickerInputEvent<moment.Moment>) {
    this.value = moment(event.value, DATE_FORMAT, true)
    this.onTouch()
  }

  writeValue(value: any) {
    if (value) {
      this.value = moment(value, DATE_FORMAT, true)
    } else {
      this._value = value
    }
  }

  registerOnChange(fn: (_: any) => void) {
    this.onChange = fn
  }

  registerOnTouched(fn: (_?: any) => void) {
    this.onTouch = fn
  }

  ngOnInit(): void {
    this.placeholder = this.placeholder || 'dd/mm/aaaa'
    this.control = this.ngControl?.control
  }

  onBlur() {
    const controlValue = this.control.value
    if (this._value === 'Invalid date' || !controlValue) {
      this.control.setValue('')
    }
    this.onTouch()
  }
}
