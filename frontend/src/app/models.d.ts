export interface PartialDefaultOption {
  id: number
  description: string
}

export interface LoginModel {
  email: string
  password: string
}

export interface UserInputModel {
  name: string
  email: string
  password: string
}

export interface RestaurantInputModel {
  name: string
  description: string
  typeOfFood: PartialDefaultOption
  address: string
  latitude: number
  longitude: number
}

export interface RestaurantOwnerInputModel {
  user: UserInputModel
  restaurant: RestaurantInputModel
}

export interface RestaurantViewModel {
  id: number
  name: string
  description: string
  address: string
  typeOfFood: string
  avgRate: number
  pendingReplies: number
  latitude: number
  longitude: number
}

export interface RestaurantListPayloadModel {
  content: RestaurantViewModel[]
  totalElements: number
}

export interface ReviewInputModel {
  restaurantId: number
  rate: number
  comment: string
  visitDate: string
}

export interface UserModel {
  id: number
  username: string
  displayName: string
}

export interface PasswordModel {
  oldPassword: string
  newPassword: string
}

export interface ResetPasswordModel {
  password: string
}

export interface ReviewModel {
  id: number
  author: UserModel
  rate: number
  comment: string
  visitDate: string
  reply: string
  replyDate: string
}

export interface ReviewListPayloadModel {
  content: ReviewModel[]
  totalElements: number
}
