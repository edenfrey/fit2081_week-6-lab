package com.example.mymovieapp;

public class MovieClass {

    String movieTitle,movieYear,movieGenre,movieKeywords,movieCountry;
    double movieCost;
    public MovieClass(String _movieTitle,String _movieYear,String _movieGenre,String _movieCost,String _movieKeywords,String _movieCountry){
        movieTitle = _movieTitle;
        movieYear = _movieYear;
        movieGenre = _movieGenre;
        movieKeywords = _movieKeywords;
        movieCountry = _movieCountry;
        movieCost = Double.parseDouble(_movieCost);
    }
    public String getMovieTitle(){
        return movieTitle;
    }

    public double getMovieCost() {
        return movieCost;
    }

    public String getMovieCountry() {
        return movieCountry;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public String getMovieKeywords() {
        return movieKeywords;
    }

    public String getMovieYear() {
        return movieYear;
    }
}
