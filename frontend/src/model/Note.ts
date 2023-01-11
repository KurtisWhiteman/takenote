export class Note {
  id: number
  title: string
  description: string
  createdAt: string
  updatedAt: string
  is_deleted
  activeIndex!: boolean

  constructor (id: number, title: string, description: string, is_deleted: boolean) {
    this.id = id
    this.title = title
    this.description = description
    this.is_deleted = is_deleted
  }
}
