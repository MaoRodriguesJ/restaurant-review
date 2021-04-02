import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { ReviewListPayloadModel } from '@app/models'
import { take } from 'rxjs/operators'
import { environment } from 'src/environments/environment'

@Injectable({
  providedIn: 'root',
})
export class PendingRepliesService {
  baseUrl = environment.baseUrl

  constructor(private http: HttpClient) {}

  public getReviewsPendingReply(restaurantId: number, pageNumber: number, pageSize: number) {
    return this.http
      .get<ReviewListPayloadModel>(
        this.baseUrl + `/api/restaurants/${restaurantId}/pendingReplies/?size=${pageSize}&page=${pageNumber}`
      )
      .pipe(take(1))
  }

  public reply(form: string, reviewId: number) {
    return this.http.post<number>(this.baseUrl + `/api/reviews/${reviewId}/replies`, form).pipe(take(1))
  }
}
