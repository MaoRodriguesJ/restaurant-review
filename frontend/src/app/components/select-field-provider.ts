import { PartialDefaultOption } from '@app/models'

export interface SelectFieldProvider<T, V = T> {
  options: T[]
  value(option: T): V
  description(option: T): string
  compareWith(value: V, otherValue: V): boolean
}

export function createDefaultSelectFieldProvider<T extends PartialDefaultOption>(
  options: T[]
): SelectFieldProvider<T, T> {
  return ({
    options,
    value: (option: T) => option,
    description: (option: T) => option?.description,
    compareWith: (op1: T, op2: T) => op1 && op2 && op1.id === op2.id,
  } as unknown) as SelectFieldProvider<T, T>
}
