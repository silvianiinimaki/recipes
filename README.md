# Recipe -app

This is a recipe app where user can search new recipes from an API, save them, create their own recipes and edit saved recipes.
The app is implemented with Kotlin in Android Studio.

## About the project

I created this project in the spring of 2021 as my first whole Kotlin-app. I had so much fun designing the layout and the features.
Only regret I have is the API I used, because it didn't return enough details of the recipes and the user has to go to different websites to get more information.
I did add a button to the recipes, so it's as easy as possible to toggle between the source website and the app.

## Features

- Search recipes from api
    - Save them
- Make new recipes
    - Add ingredients and directions one by one and delete them
    - Add a picture
    - Add link to external website
- Edit recipes
- Delete recipes

| Main menu | Search recipes | Recipe details | Saved recipes  | New recipe | Edit recipe |
|------------|-------------|-------------|------------|-------------|-------------|
| ![mqpeDnmU](https://user-images.githubusercontent.com/60136781/137463022-cdd26af9-f06a-4d91-b3a0-16aef93ead9b.jpeg) | ![FvMcdwm0](https://user-images.githubusercontent.com/60136781/137463019-03c7a057-00d2-4c53-8ed3-d233a6eca91b.jpeg) | ![7JBWJdIE](https://user-images.githubusercontent.com/60136781/137463015-a421a212-69b8-482c-89e3-419fd3c55013.jpeg) | ![n-L0ESUI](https://user-images.githubusercontent.com/60136781/137463024-17437c55-0e9e-454e-8ba2-ff0924683c6e.jpeg) | ![CByHwOmw](https://user-images.githubusercontent.com/60136781/137463026-96ff3139-5b55-4cdf-8a9b-269b94b0ab7c.jpeg) |![39sUoKi4 (1)](https://user-images.githubusercontent.com/60136781/137464060-a8c95233-9b57-4827-886b-274aa5e7e3e0.jpeg) |

## Demo video

https://www.dropbox.com/s/h43n04ae5rfemfj/InShot_20211015_110201045.mp4?dl=0

## Built with

- Kotlin
- Android studio
- API: https://developer.edamam.com/edamam-recipe-api

## Getting started

### Prerequisites

- Android Studio
- Kotlin

### Installation

* Get a free API-key and id from https://developer.edamam.com/edamam-recipe-api
* Download project as zip
* Open in android studio
* Add your API key and id to the url in searchActivity.kt line: 111
