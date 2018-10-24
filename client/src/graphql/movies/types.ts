type UserMovie = {
    title: string,
    posterLink: string
}

export type UserMoviesResponse = {
    userMovies: UserMovie[]
}

export type IdInputProps = {
    id: number
}