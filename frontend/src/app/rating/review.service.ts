import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { ReviewInputModel } from '@app/models'
import { take } from 'rxjs/operators'
import { environment } from 'src/environments/environment'

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  baseUrl = environment.baseUrl

  constructor(private http: HttpClient) {}

  public createReview(form: ReviewInputModel) {
    return this.http.post<number>(this.baseUrl + '/api/reviews', form).pipe(take(1))
  }
}
