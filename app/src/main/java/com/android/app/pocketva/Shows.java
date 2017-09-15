package com.android.app.pocketva;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */

public class Shows implements Comparable<Shows>{

    @SerializedName("id")
    private int id;
    @SerializedName("title_romaji")
    private String titleRomaji;
    @SerializedName("title_english")
    private String titleEnglish;
    @SerializedName("title_japanese")
    private String titleJapanese;
    @SerializedName("type")
    private String type;
    @SerializedName("start_date_fuzzy")
    private int startDateFuzzy;
    @SerializedName("end_date_fuzzy")
    private int endDateFuzzy;
    @SerializedName("season")
    private Object season;
    @SerializedName("series_type")
    private String seriesType;
    @SerializedName("synonyms")
    private List<Object> synonyms = null;
    @SerializedName("genres")
    private List<String> genres = null;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("average_score")
    private float averageScore;
    @SerializedName("popularity")
    private int popularity;
    @SerializedName("updated_at")
    private int updatedAt;
    @SerializedName("hashtag")
    private Object hashtag;
    @SerializedName("image_url_sml")
    private String imageUrlSml;
    @SerializedName("image_url_med")
    private String imageUrlMed;
    @SerializedName("image_url_lge")
    private String imageUrlLge;
    @SerializedName("image_url_banner")
    private Object imageUrlBanner;
    @SerializedName("total_episodes")
    private int totalEpisodes;
    @SerializedName("airing_status")
    private String airingStatus;
    @SerializedName("role")
    private int role;
    @SerializedName("characters")
    private List<Characters> characters = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleRomaji() {
        return titleRomaji;
    }

    public void setTitleRomaji(String titleRomaji) {
        this.titleRomaji = titleRomaji;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    public String getTitleJapanese() {
        return titleJapanese;
    }

    public void setTitleJapanese(String titleJapanese) {
        this.titleJapanese = titleJapanese;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStartDateFuzzy() {
        return startDateFuzzy;
    }

    public void setStartDateFuzzy(int startDateFuzzy) {
        this.startDateFuzzy = startDateFuzzy;
    }

    public int getEndDateFuzzy() {
        return endDateFuzzy;
    }

    public void setEndDateFuzzy(int endDateFuzzy) {
        this.endDateFuzzy = endDateFuzzy;
    }

    public Object getSeason() {
        return season;
    }

    public void setSeason(Object season) {
        this.season = season;
    }

    public String getSeriesType() {
        return seriesType;
    }

    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

    public List<Object> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<Object> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(int updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getHashtag() {
        return hashtag;
    }

    public void setHashtag(Object hashtag) {
        this.hashtag = hashtag;
    }

    public String getImageUrlSml() {
        return imageUrlSml;
    }

    public void setImageUrlSml(String imageUrlSml) {
        this.imageUrlSml = imageUrlSml;
    }

    public String getImageUrlMed() {
        return imageUrlMed;
    }

    public void setImageUrlMed(String imageUrlMed) {
        this.imageUrlMed = imageUrlMed;
    }

    public String getImageUrlLge() {
        return imageUrlLge;
    }

    public void setImageUrlLge(String imageUrlLge) {
        this.imageUrlLge = imageUrlLge;
    }

    public Object getImageUrlBanner() {
        return imageUrlBanner;
    }

    public void setImageUrlBanner(Object imageUrlBanner) {
        this.imageUrlBanner = imageUrlBanner;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    public String getAiringStatus() {
        return airingStatus;
    }

    public void setAiringStatus(String airingStatus) {
        this.airingStatus = airingStatus;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public List<Characters> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Characters> characters) {
        this.characters = characters;
    }


    @Override
    public int compareTo(Shows shows) {
        return this.getTitleRomaji().compareTo(shows.getTitleRomaji());
    }
}
